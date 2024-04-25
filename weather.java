import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class weather {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ask the user to enter the location
        System.out.print("Enter location (city or coordinates): ");
        String location = scanner.nextLine().trim();

        // API URL
        String apiUrl = "https://weatherapi-com.p.rapidapi.com/current.json?q=" + location;

        // API key
        String apiKey = "6733e02147msh4536cd67418a1abp13fe5ejsnd3e4c25e0b40";

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("X-RapidAPI-Key", apiKey)
                    .header("X-RapidAPI-Host", "weatherapi-com.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body();
                // write all taking attributes from api.
                double temperatureC = extractValue(responseBody, "\"temp_c\":");
                // double precipitation = extractValue(responseBody, "\"precip_mm\":");
                int humidity = (int) extractValue(responseBody, "\"humidity\":");
                double windSpeed = extractValue(responseBody, "\"wind_kph\":");

                // Display information
                System.out.println("Temperature: " + temperatureC + "°C");
                // System.out.println("Precipitation: " + precipitation + " mm");
                System.out.println("Humidity: " + humidity + "%");
                System.out.println("Wind: " + windSpeed + " km/h");
            } else {
                System.out.println("Failed to retrieve weather information. Response Code: " + response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static double extractValue(String responseBody, String key) {
        int index = responseBody.indexOf(key);
        if (index != -1) {
            int startIndex = index + key.length();
            int endIndex = responseBody.indexOf(',', startIndex);
            if (endIndex == -1) {
                endIndex = responseBody.indexOf('}', startIndex);
            }
            String valueString = responseBody.substring(startIndex, endIndex);
            return Double.parseDouble(valueString);
        }
        return Double.NaN; // Return NaN if key is not found
    }
}
