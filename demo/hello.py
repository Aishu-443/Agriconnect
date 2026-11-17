from flask import Flask
app = Flask(__name__)
@app.route('/')
def hello():
    return '<h1 style="color:green">Hello from AgriConnect Flask!</h1><p>Python Flask demo running!</p>'
if __name__ == '__main__':
    app.run(debug=True, port=5001)
