package WeatherApp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import com.google.gson.*;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecastApp extends Application {

    private static final String API_KEY = "Your Api key here"; // Replace with your OpenWeatherMap API key

    // Current weather UI elements
    private VBox weatherCard;
    private Label cityLabel;
    private Label tempLabel;
    private Label humidityLabel;
    private Label descLabel;
    private ImageView iconView;

    // 5-day forecast container
    private HBox forecastContainer;

    @Override
    public void start(Stage primaryStage) {
        // Input field
        TextField cityField = new TextField();
        cityField.setPromptText("Enter city");

        // Button
        Button getWeatherBtn = new Button("Get Weather");
        getWeatherBtn.setStyle("-fx-background-color: #4facfe; -fx-text-fill: white; -fx-background-radius: 20;");

        // Current weather card
        weatherCard = new VBox(10);
        weatherCard.setPadding(new Insets(20));
        weatherCard.setAlignment(Pos.CENTER);
        weatherCard.setStyle("-fx-background-color: linear-gradient(to bottom, #89f7fe, #66a6ff);" +
                "-fx-background-radius: 15;");

        cityLabel = new Label();
        cityLabel.setFont(Font.font(22));
        tempLabel = new Label();
        tempLabel.setFont(Font.font(18));
        humidityLabel = new Label();
        humidityLabel.setFont(Font.font(16));
        descLabel = new Label();
        descLabel.setFont(Font.font(16));

        iconView = new ImageView();
        iconView.setFitHeight(50);
        iconView.setFitWidth(50);

        weatherCard.getChildren().addAll(cityLabel, iconView, tempLabel, humidityLabel, descLabel);

        // 5-day forecast container
        forecastContainer = new HBox(10);
        forecastContainer.setAlignment(Pos.CENTER);
        forecastContainer.setPadding(new Insets(10));

        // Layout
        VBox root = new VBox(15, cityField, getWeatherBtn, weatherCard, forecastContainer);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #cfd9df, #e2ebf0);");

        // Button event
        getWeatherBtn.setOnAction(e -> {
            String city = cityField.getText().trim();
            if (!city.isEmpty()) {
                fetchWeather(city); // Current weather
                fetchFiveDayForecast(city); // 5-day forecast
            }
        });

        // Scene
        Scene scene = new Scene(root, 750, 650);
        primaryStage.setTitle("Weather Forecast App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Fetch current weather
    private void fetchWeather(String city) {
        try {
            String urlStr = "https://api.openweathermap.org/data/2.5/weather?q=" +
                    city + "&appid=" + API_KEY + "&units=metric";
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != 200) {
                showError("Error: " + conn.getResponseMessage());
                return;
            }

            JsonObject json = JsonParser.parseReader(new InputStreamReader(conn.getInputStream())).getAsJsonObject();
            conn.disconnect();

            String cityName = json.get("name").getAsString();
            double temp = json.getAsJsonObject("main").get("temp").getAsDouble();
            int humidity = json.getAsJsonObject("main").get("humidity").getAsInt();
            String description = json.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();
            String iconCode = json.getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();

            updateWeatherUI(cityName, temp, humidity, description, iconCode);

        } catch (Exception ex) {
            showError("Error: " + ex.getMessage());
        }
    }

    // Fetch 5-day forecast
    private void fetchFiveDayForecast(String city) {
        try {
            String urlStr = "https://api.openweathermap.org/data/2.5/forecast?q="
                    + city + "&appid=" + API_KEY + "&units=metric";
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != 200) {
                showError("Forecast Error: " + conn.getResponseMessage());
                return;
            }

            JsonObject json = JsonParser.parseReader(new InputStreamReader(conn.getInputStream())).getAsJsonObject();
            conn.disconnect();

            forecastContainer.getChildren().clear(); // Clear previous cards

            JsonArray list = json.getAsJsonArray("list");

            // Pick one forecast per day around 12:00 PM
            int daysAdded = 0;
            for (int i = 0; i < list.size(); i++) {
                JsonObject obj = list.get(i).getAsJsonObject();
                String dtTxt = obj.get("dt_txt").getAsString();
                if (dtTxt.contains("12:00:00")) {
                    double temp = obj.getAsJsonObject("main").get("temp").getAsDouble();
                    int humidity = obj.getAsJsonObject("main").get("humidity").getAsInt();
                    String desc = obj.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();
                    String icon = obj.getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();

                    forecastContainer.getChildren().add(createForecastCard(dtTxt.split(" ")[0], temp, humidity, desc, icon));

                    daysAdded++;
                    if (daysAdded == 5) break;
                }
            }

        } catch (Exception ex) {
            showError("Forecast Error: " + ex.getMessage());
        }
    }

    // Create a single forecast card
    private VBox createForecastCard(String date, double temp, int humidity, String desc, String iconCode) {
        Label dateLabel = new Label(date);
        dateLabel.setFont(Font.font(14));

        Label tempLabel = new Label(String.format("%.1f °C", temp));
        tempLabel.setFont(Font.font(14));

        Label humidityLabel = new Label("Humidity: " + humidity + "%");
        humidityLabel.setFont(Font.font(12));

        Label descLabel = new Label(desc);
        descLabel.setFont(Font.font(12));

        ImageView iconView = new ImageView(new Image("https://openweathermap.org/img/wn/" + iconCode + "@2x.png"));
        iconView.setFitHeight(40);
        iconView.setFitWidth(40);

        VBox card = new VBox(5, dateLabel, iconView, tempLabel, humidityLabel, descLabel);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(10));
        card.setStyle(getBackgroundStyle(desc));
        card.setPrefWidth(130);
        card.setSpacing(5);

        return card;
    }

    // Update UI for current weather
    private void updateWeatherUI(String city, double temperature, int humidity, String weatherDescription, String iconCode) {
        cityLabel.setText(city);
        tempLabel.setText("Temperature: " + String.format("%.2f", temperature) + " °C");
        humidityLabel.setText("Humidity: " + humidity + "%");
        descLabel.setText(weatherDescription);

        String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
        iconView.setImage(new Image(iconUrl));

        weatherCard.setStyle(getBackgroundStyle(weatherDescription));
    }

    // Show error
    private void showError(String msg) {
        cityLabel.setText("");
        tempLabel.setText("");
        humidityLabel.setText("");
        descLabel.setText(msg);
        iconView.setImage(null);

        weatherCard.setStyle("-fx-background-color: linear-gradient(to bottom, #ff758c, #ff7eb3);" +
                "-fx-background-radius: 15; -fx-padding: 20;");
    }

    // Dynamic background styling
    private String getBackgroundStyle(String condition) {
        condition = condition.toLowerCase();
        if (condition.contains("sun")) {
            return "-fx-background-color: linear-gradient(to bottom, #fceabb, #f8b500);" +
                    "-fx-background-radius: 15; -fx-padding: 20;";
        } else if (condition.contains("cloud")) {
            return "-fx-background-color: linear-gradient(to bottom, #bdc3c7, #2c3e50);" +
                    "-fx-background-radius: 15; -fx-padding: 20;";
        } else if (condition.contains("rain")) {
            return "-fx-background-color: linear-gradient(to bottom, #00c6ff, #0072ff);" +
                    "-fx-background-radius: 15; -fx-padding: 20;";
        } else if (condition.contains("snow")) {
            return "-fx-background-color: linear-gradient(to bottom, #e0eafc, #cfdef3);" +
                    "-fx-background-radius: 15; -fx-padding: 20;";
        } else {
            return "-fx-background-color: linear-gradient(to bottom, #89f7fe, #66a6ff);" +
                    "-fx-background-radius: 15; -fx-padding: 20;";
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
