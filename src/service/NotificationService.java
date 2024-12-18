package service;

import model.Notification;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NotificationService {

    private final Map<Long, Notification> notifications = new ConcurrentHashMap<>();

    // Create a new notification
    public Notification createNotification(String email, Double priceOfBitcoin, Long tradingVolume) {
        Notification notification = new Notification(email, priceOfBitcoin, tradingVolume);
        notifications.put(notification.getId(), notification);
        return notification;
    }

    // Retrieve a notification by ID
    public Notification getNotification(Long id) {
        return notifications.get(id);
    }

    // List all notifications
    public List<Notification> listNotifications() {
        return new ArrayList<>(notifications.values());
    }

    // Delete a notification by ID
    public boolean deleteNotification(Long id) {
        return notifications.remove(id) != null;
    }

    // Mark a notification as sent
    public boolean sendNotification(Long id) {
        Notification notification = notifications.get(id);
        if (notification != null) {
            System.out.println("Sending notification to: " + notification.getEmail());
            notification.setStatus("SENT");
            return true;
        }
        return false;
    }
}