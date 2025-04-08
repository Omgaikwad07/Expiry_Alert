package com.example.expiry_alert;

public class Product {
    private String name;
    private String brand;
    private String expiryDate;
    private String barcode;
    private long addedOn; // Timestamp to track when the product was added
    private boolean isActive;

    // Required empty constructor for Firebase
    public Product() {}

    // Constructor
    public Product(String name, String brand, String expiryDate, String barcode, long addedOn, boolean isActive) {
        this.name = name;
        this.brand = brand;
        this.expiryDate = expiryDate;
        this.barcode = barcode;
        this.addedOn = addedOn;
        this.isActive = isActive;
    }

    // Getters and Setters
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public String getExpiryDate() { return expiryDate; }
    public String getBarcode() { return barcode; }
    public long getAddedOn() { return addedOn; }
    public boolean isActive() { return isActive; }
}
