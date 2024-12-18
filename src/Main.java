
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import service.NotificationService;
import model.Notification;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws IOException {
        NotificationService notificationService = new NotificationService();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/createNotification", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (!"POST".equals(exchange.getRequestMethod())) {
                    exchange.sendResponseHeaders(405, -1);
                    return;
                }

                String query = new String(exchange.getRequestBody().readAllBytes());
                Map<String, String> params = parseQuery(query);

                String email = params.get("email");
                Double priceOfBitcoin = Double.parseDouble(params.get("priceOfBitcoin"));
                Long tradingVolume = Long.parseLong(params.get("tradingVolume"));

                Notification notification = notificationService.createNotification(email, priceOfBitcoin, tradingVolume);
                String response = "Notification created with ID: " + notification.getId();

                exchange.sendResponseHeaders(200, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        });

        server.createContext("/sendNotification", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (!"POST".equals(exchange.getRequestMethod())) {
                    exchange.sendResponseHeaders(405, -1);
                    return;
                }

                String query = new String(exchange.getRequestBody().readAllBytes());
                Map<String, String> params = parseQuery(query);

                Long id = Long.parseLong(params.get("id"));
                boolean success = notificationService.sendNotification(id);

                String response = success ? "Notification sent successfully." : "Notification not found.";
                exchange.sendResponseHeaders(success ? 200 : 404, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        });

        server.createContext("/listNotifications", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (!"GET".equals(exchange.getRequestMethod())) {
                    exchange.sendResponseHeaders(405, -1);
                    return;
                }

                List<Notification> notifications = notificationService.listNotifications();
                StringBuilder responseBuilder = new StringBuilder();
                for (Notification notification : notifications) {
                    responseBuilder.append("ID: ").append(notification.getId())
                            .append(", Status: ").append(notification.getStatus())
                            .append(", Email: ").append(notification.getEmail())
                            .append("\n");
                }

                String response = responseBuilder.toString();
                exchange.sendResponseHeaders(200, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        });

        server.createContext("/deleteNotification", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (!"POST".equals(exchange.getRequestMethod())) {
                    exchange.sendResponseHeaders(405, -1);
                    return;
                }

                String query = new String(exchange.getRequestBody().readAllBytes());
                Map<String, String> params = parseQuery(query);

                Long id = Long.parseLong(params.get("id"));
                boolean success = notificationService.deleteNotification(id);

                String response = success ? "Notification deleted successfully." : "Notification not found.";
                exchange.sendResponseHeaders(success ? 200 : 404, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        });

        server.start();
        System.out.println("Server is running on port 8080...");
    }

    private static Map<String, String> parseQuery(String query) {
        Map<String, String> params = new HashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length > 1) {
                params.put(keyValue[0], keyValue[1]);
            }
        }
        return params;
    }
}