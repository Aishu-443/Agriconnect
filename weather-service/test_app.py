"""
Unit tests for AgriConnect Weather Service.
All external Open-Meteo API calls are mocked using unittest.mock.patch.
"""

import pytest
from unittest.mock import patch, MagicMock
import requests
from app import app, WEATHER_CODES


@pytest.fixture
def client():
    """Flask test client fixture."""
    app.config['TESTING'] = True
    with app.test_client() as test_client:
        yield test_client


def test_health_endpoint(client):
    """
    Test GET /health returns 200 and json has status==healthy.
    """
    response = client.get('/health')
    assert response.status_code == 200
    data = response.get_json()
    assert data is not None
    assert data.get('status') == 'healthy'
    assert data.get('service') == 'AgriConnect Weather Service'
    assert data.get('version') == '1.0.0'


@patch('app.requests.get')
def test_current_weather_valid(mock_get, client):
    """
    Test GET /weather/current?latitude=17.69&longitude=83.22
    returns 200 with temperature, humidity, weather_description fields.
    """
    mock_resp = MagicMock()
    mock_resp.status_code = 200
    mock_resp.json.return_value = {
        "latitude": 17.69,
        "longitude": 83.22,
        "current": {
            "time": "2026-11-15T12:00",
            "temperature_2m": 29.5,
            "relative_humidity_2m": 72,
            "wind_speed_10m": 14.2,
            "weather_code": 1,
            "precipitation": 0.0
        }
    }
    mock_get.return_value = mock_resp

    response = client.get('/weather/current?latitude=17.69&longitude=83.22')
    assert response.status_code == 200

    data = response.get_json()
    assert data is not None
    assert 'temperature' in data
    assert 'humidity' in data
    assert 'weather_description' in data
    assert data['temperature'] == 29.5
    assert data['humidity'] == 72
    assert data['wind_speed'] == 14.2
    assert data['weather_code'] == 1
    assert data['weather_description'] == 'Mainly clear'
    assert data['precipitation'] == 0.0
    assert data['source'] == 'Open-Meteo'
    assert data['location_lat'] == 17.69
    assert data['location_lon'] == 83.22

    mock_get.assert_called_once()
    _, kwargs = mock_get.call_args
    assert kwargs['timeout'] == 10


def test_current_weather_missing_params(client):
    """
    GET /weather/current with no params -> 400.
    """
    response = client.get('/weather/current')
    assert response.status_code == 400
    data = response.get_json()
    assert 'error' in data

    response_lat = client.get('/weather/current?latitude=17.69')
    assert response_lat.status_code == 400

    response_lon = client.get('/weather/current?longitude=83.22')
    assert response_lon.status_code == 400


def test_current_weather_invalid_lat(client):
    """
    latitude=999 -> 400.
    """
    response = client.get('/weather/current?latitude=999&longitude=83.22')
    assert response.status_code == 400
    data = response.get_json()
    assert 'error' in data
    assert 'Latitude must be between -90.0 and 90.0' in data['error']


def test_current_weather_invalid_lon(client):
    """
    longitude=999 -> 400.
    """
    response = client.get('/weather/current?latitude=17.69&longitude=999')
    assert response.status_code == 400
    data = response.get_json()
    assert 'error' in data
    assert 'Longitude must be between -180.0 and 180.0' in data['error']


@patch('app.requests.get')
def test_forecast_valid(mock_get, client):
    """
    Mock response, assert 200 and 'forecast' key with list.
    """
    mock_resp = MagicMock()
    mock_resp.status_code = 200
    mock_resp.json.return_value = {
        "latitude": 17.69,
        "longitude": 83.22,
        "daily": {
            "time": [
                "2026-11-15", "2026-11-16", "2026-11-17",
                "2026-11-18", "2026-11-19", "2026-11-20", "2026-11-21"
            ],
            "temperature_2m_max": [31.0, 32.2, 30.8, 29.5, 30.0, 31.5, 32.0],
            "temperature_2m_min": [22.0, 23.1, 22.5, 21.8, 22.0, 22.5, 23.0],
            "weather_code": [0, 1, 2, 61, 80, 0, 1],
            "precipitation_sum": [0.0, 0.0, 0.5, 5.2, 2.1, 0.0, 0.0],
            "wind_speed_10m_max": [15.0, 14.5, 18.2, 22.0, 16.5, 12.0, 13.5]
        }
    }
    mock_get.return_value = mock_resp

    response = client.get('/weather/forecast?latitude=17.69&longitude=83.22')
    assert response.status_code == 200

    data = response.get_json()
    assert data is not None
    assert 'forecast' in data
    assert isinstance(data['forecast'], list)
    assert len(data['forecast']) == 7

    first_day = data['forecast'][0]
    assert first_day['date'] == "2026-11-15"
    assert first_day['temp_max'] == 31.0
    assert first_day['temp_min'] == 22.0
    assert first_day['weather_code'] == 0
    assert first_day['weather_description'] == "Clear sky"
    assert first_day['precipitation_sum'] == 0.0
    assert first_day['wind_speed_max'] == 15.0

    mock_get.assert_called_once()


def test_forecast_missing_params(client):
    """
    GET /weather/forecast without coordinates returns 400.
    """
    response = client.get('/weather/forecast')
    assert response.status_code == 400
    data = response.get_json()
    assert 'error' in data


@patch('app.requests.get')
def test_current_weather_timeout(mock_get, client):
    """
    Test graceful handling when requests.get raises Timeout (returns 503).
    """
    mock_get.side_effect = requests.exceptions.Timeout("Connection timed out")

    response = client.get('/weather/current?latitude=17.69&longitude=83.22')
    assert response.status_code == 503
    data = response.get_json()
    assert data == {"error": "Weather service temporarily unavailable"}


@patch('app.requests.get')
def test_current_weather_exception(mock_get, client):
    """
    Test graceful handling when requests.get raises RequestException (returns 503).
    """
    mock_get.side_effect = requests.exceptions.ConnectionError("Connection failed")

    response = client.get('/weather/current?latitude=17.69&longitude=83.22')
    assert response.status_code == 503
    data = response.get_json()
    assert data == {"error": "Weather service temporarily unavailable"}


@patch('app.requests.get')
def test_forecast_timeout(mock_get, client):
    """
    Test forecast endpoint handles API timeouts with 503.
    """
    mock_get.side_effect = requests.exceptions.Timeout("Timeout")

    response = client.get('/weather/forecast?latitude=17.69&longitude=83.22')
    assert response.status_code == 503
    data = response.get_json()
    assert data == {"error": "Weather service temporarily unavailable"}


@patch('app.requests.get')
def test_open_meteo_upstream_http_error(mock_get, client):
    """
    Test Open-Meteo returning non-200 HTTP response status (returns 503).
    """
    mock_resp = MagicMock()
    mock_resp.status_code = 500
    mock_resp.text = "Internal Server Error"
    mock_get.return_value = mock_resp

    response = client.get('/weather/current?latitude=17.69&longitude=83.22')
    assert response.status_code == 503
    data = response.get_json()
    assert data == {"error": "Weather service temporarily unavailable"}
