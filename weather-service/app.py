"""
AgriConnect Weather Service
Flask 3.x microservice providing real-time weather and 7-day agricultural forecasts
using the Open-Meteo API.
"""

import logging
import os
from flask import Flask, request, jsonify
from flask_cors import CORS
import requests

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s [%(levelname)s] %(name)s: %(message)s"
)
logger = logging.getLogger("weather-service")

app = Flask(__name__)
# Enable CORS for all routes and origins
CORS(app)

OPEN_METEO_BASE_URL = "https://api.open-meteo.com/v1/forecast"
REQUEST_TIMEOUT_SECONDS = 10

# WMO weather code map (complete dict)
WEATHER_CODES = {
    0: 'Clear sky',
    1: 'Mainly clear',
    2: 'Partly cloudy',
    3: 'Overcast',
    45: 'Foggy',
    48: 'Rime fog',
    51: 'Light drizzle',
    53: 'Moderate drizzle',
    55: 'Dense drizzle',
    61: 'Slight rain',
    63: 'Moderate rain',
    65: 'Heavy rain',
    71: 'Slight snow',
    73: 'Moderate snow',
    75: 'Heavy snow',
    80: 'Slight showers',
    81: 'Moderate showers',
    82: 'Violent showers',
    95: 'Thunderstorm',
    96: 'Thunderstorm with hail',
    99: 'Thunderstorm heavy hail'
}


def _validate_coordinates(lat_str, lon_str):
    """
    Validates latitude and longitude query parameters.
    Returns (lat_float, lon_float, error_response).
    """
    if lat_str is None or lon_str is None or lat_str.strip() == '' or lon_str.strip() == '':
        return None, None, (jsonify({"error": "Missing required query parameters: 'latitude' and 'longitude'"}), 400)

    try:
        lat = float(lat_str)
        lon = float(lon_str)
    except (ValueError, TypeError):
        return None, None, (jsonify({"error": "Invalid coordinates format: latitude and longitude must be valid floating point numbers."}), 400)

    if not (-90.0 <= lat <= 90.0):
        return None, None, (jsonify({"error": f"Invalid latitude: {lat}. Latitude must be between -90.0 and 90.0."}), 400)

    if not (-180.0 <= lon <= 180.0):
        return None, None, (jsonify({"error": f"Invalid longitude: {lon}. Longitude must be between -180.0 and 180.0."}), 400)

    return lat, lon, None


@app.route('/health', methods=['GET'])
def health():
    """
    Health check endpoint for liveness and readiness probes.
    """
    return jsonify({
        "status": "healthy",
        "service": "AgriConnect Weather Service",
        "version": "1.0.0"
    }), 200


@app.route('/weather/current', methods=['GET'])
def get_current_weather():
    """
    GET /weather/current?latitude=&longitude=
    Fetches real-time weather metrics for the given coordinates from Open-Meteo.
    """
    lat_param = request.args.get('latitude')
    lon_param = request.args.get('longitude')

    lat, lon, err = _validate_coordinates(lat_param, lon_param)
    if err:
        return err

    url = f"{OPEN_METEO_BASE_URL}?latitude={lat}&longitude={lon}&current=temperature_2m,relative_humidity_2m,wind_speed_10m,weather_code,precipitation&timezone=auto"

    try:
        logger.info(f"Fetching current weather for lat={lat}, lon={lon}")
        response = requests.get(url, timeout=REQUEST_TIMEOUT_SECONDS)
        if response.status_code != 200:
            logger.error(f"Open-Meteo API returned error status {response.status_code}: {response.text}")
            return jsonify({"error": "Weather service temporarily unavailable"}), 503
    except (requests.exceptions.RequestException, Exception) as e:
        logger.error(f"Open-Meteo API request failure or timeout: {e}")
        return jsonify({"error": "Weather service temporarily unavailable"}), 503

    try:
        data = response.json()
        current_data = data.get("current", {})
        weather_code = current_data.get("weather_code")
        weather_desc = WEATHER_CODES.get(
            weather_code,
            f"Unknown ({weather_code})" if weather_code is not None else "Unknown"
        )

        result = {
            "temperature": current_data.get("temperature_2m"),
            "humidity": current_data.get("relative_humidity_2m"),
            "wind_speed": current_data.get("wind_speed_10m"),
            "weather_code": weather_code,
            "weather_description": weather_desc,
            "precipitation": current_data.get("precipitation", 0.0),
            "source": "Open-Meteo",
            "location_lat": lat,
            "location_lon": lon,
            "latitude": lat,
            "longitude": lon,
            "temperature_2m": current_data.get("temperature_2m"),
            "relative_humidity_2m": current_data.get("relative_humidity_2m"),
            "wind_speed_10m": current_data.get("wind_speed_10m"),
            "time": current_data.get("time")
        }
        return jsonify(result), 200
    except Exception as e:
        logger.error(f"Error parsing weather data: {e}")
        return jsonify({"error": "Weather service temporarily unavailable"}), 503


@app.route('/weather/forecast', methods=['GET'])
def get_weather_forecast():
    """
    GET /weather/forecast?latitude=&longitude=
    Fetches 7-day daily forecast for the given coordinates from Open-Meteo.
    """
    lat_param = request.args.get('latitude')
    lon_param = request.args.get('longitude')

    lat, lon, err = _validate_coordinates(lat_param, lon_param)
    if err:
        return err

    url = f"{OPEN_METEO_BASE_URL}?latitude={lat}&longitude={lon}&daily=temperature_2m_max,temperature_2m_min,weather_code,precipitation_sum,wind_speed_10m_max&timezone=auto"

    try:
        logger.info(f"Fetching 7-day forecast for lat={lat}, lon={lon}")
        response = requests.get(url, timeout=REQUEST_TIMEOUT_SECONDS)
        if response.status_code != 200:
            logger.error(f"Open-Meteo API returned error status {response.status_code}: {response.text}")
            return jsonify({"error": "Weather service temporarily unavailable"}), 503
    except (requests.exceptions.RequestException, Exception) as e:
        logger.error(f"Open-Meteo API request failure or timeout: {e}")
        return jsonify({"error": "Weather service temporarily unavailable"}), 503

    try:
        data = response.json()
        daily = data.get("daily", {})
        dates = daily.get("time", [])
        max_temps = daily.get("temperature_2m_max", [])
        min_temps = daily.get("temperature_2m_min", [])
        codes = daily.get("weather_code", [])
        precips = daily.get("precipitation_sum", [])
        wind_speeds = daily.get("wind_speed_10m_max", [])

        forecast_list = []
        for i in range(len(dates)):
            w_code = codes[i] if i < len(codes) else None
            item = {
                "date": dates[i],
                "temp_max": max_temps[i] if i < len(max_temps) else None,
                "temp_min": min_temps[i] if i < len(min_temps) else None,
                "temperature_max": max_temps[i] if i < len(max_temps) else None,
                "temperature_min": min_temps[i] if i < len(min_temps) else None,
                "weather_code": w_code,
                "weather_description": WEATHER_CODES.get(
                    w_code,
                    f"Unknown ({w_code})" if w_code is not None else "Unknown"
                ),
                "precipitation_sum": precips[i] if i < len(precips) else 0.0,
                "wind_speed_max": wind_speeds[i] if i < len(wind_speeds) else None,
                "wind_speed_10m_max": wind_speeds[i] if i < len(wind_speeds) else None
            }
            forecast_list.append(item)

        result = {
            "forecast": forecast_list,
            "latitude": lat,
            "longitude": lon,
            "location_lat": lat,
            "location_lon": lon,
            "source": "Open-Meteo"
        }
        return jsonify(result), 200
    except Exception as e:
        logger.error(f"Error parsing forecast data: {e}")
        return jsonify({"error": "Weather service temporarily unavailable"}), 503


if __name__ == '__main__':
    port = int(os.environ.get('PORT', 5000))
    logger.info(f"Starting AgriConnect Weather Service on port {port}...")
    app.run(host='0.0.0.0', port=port, debug=False)
