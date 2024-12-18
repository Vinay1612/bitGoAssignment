package model;

import java.util.concurrent.atomic.AtomicLong;

public class Notification {
    private static final AtomicLong counter = new AtomicLong(0);
    private Long id;
    private String email;
    private Double priceOfBitcoin;
    private String status;
    private Long tradingVolume;

    public Notification( String email, Double priceOfBitcoin, Long tradingVolume) {
        this.id = counter.incrementAndGet();
        this.email = email;
        this.priceOfBitcoin = priceOfBitcoin;
        this.tradingVolume = tradingVolume;
        this.status = "PENDING";
    }

    public Long getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    public Double getPriceOfBitcoin() {
        return priceOfBitcoin;
    }
    public String getStatus() {
        return status;
    }
    public Long getTradingVolume() {
        return tradingVolume;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPriceOfBitcoin(Double priceOfBitcoin) {
        this.priceOfBitcoin = priceOfBitcoin;
    }
    public void setStatus(String status) {

    }
    public void setTradingVolume(Long tradingVolume) {
        this.tradingVolume = tradingVolume;
    }

}
