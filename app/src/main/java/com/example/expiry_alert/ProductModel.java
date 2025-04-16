package com.example.expiry_alert;

public class ProductModel {
    private String productId;
    private String name;
    private String brand;
    private String expiryDate;
    private String category;

    public ProductModel() {
        // Required for Firebase
    }

    public ProductModel(String productId, String name, String brand, String expiryDate, String category) {
        this.productId = productId;
        this.name = name;
        this.brand = brand;
        this.expiryDate = expiryDate;
        this.category = category;
    }

    // Getters and Setters
    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getCategory() {
        return category;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
