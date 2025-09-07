# Weather Forecast App

A **JavaFX-based Weather Forecast Application** that fetches current weather data and displays it in a visually appealing card layout. The app dynamically changes the background color based on the weather condition and shows weather icons, temperature, and description.

---

## Features

- Fetch current weather by city name.
- Displays temperature, weather description, and an icon.
- Dynamic weather card background depending on condition:
  - Sunny → Yellow gradient
  - Cloudy → Grey gradient
  - Rain → Blue gradient
  - Snow → Light blue gradient
  - Default → Blue gradient
- User-friendly JavaFX interface with smooth layout.

---

## Screenshots

### Example UI
![Weather App Screenshot](./static/assets/sample_ui.png)  

*Replace with your own screenshots.*

---

## Prerequisites

- Java Development Kit (JDK 11 or above)
- Maven or Gradle (optional, for dependencies)
- OpenWeatherMap API key

---

## Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/weather-forecast-app.git
   cd weather-forecast-app
Add your OpenWeatherMap API key
Open WeatherForecastApp.java and replace:


private static final String API_KEY = "YOUR_API_KEY";
with your API key.

Run the Application
Using your IDE (IntelliJ, Eclipse) or terminal:


javac WeatherForecastApp.java
java WeatherForecastApp
How to Use
Enter the name of the city in the input field.

Click Get Weather.

The weather card will display:

City name

Weather icon

Temperature (°C)

Description

The background color of the card changes according to the weather condition.

Project Structure

WeatherForecastApp/
├─ WeatherForecastApp.java   ← Main JavaFX application
├─ static/
│   └─ assets/
│       └─ bot.webp          ← Placeholder image/logo
├─ README.md
Dependencies
JavaFX

Gson (for JSON parsing)

OpenWeatherMap API

Notes
Make sure you have an active internet connection for API calls.

The app currently supports only current weather. You can extend it to 5-day forecasts.

License
MIT License © 2025



---









Ask ChatGPT
