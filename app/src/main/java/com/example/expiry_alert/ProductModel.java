//package com.example.expiry_alert;
//
//public class ProductModel {
//    private String name;
//    private String brand;
//    private String expiryDate;
//
//    // Required empty constructor for Firebase
//    public ProductModel() {
//    }
//
//    // Constructor with all fields
//    public ProductModel(String name, String brand, String expiryDate) {
//        this.name = name;
//        this.brand = brand;
//        this.expiryDate = expiryDate;
//    }
//
//    // Getters and Setters
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getBrand() {
//        return brand;
//    }
//
//    public void setBrand(String brand) {
//        this.brand = brand;
//    }
//
//    public String getExpiryDate() {
//        return expiryDate;
//    }
//
//    public void setExpiryDate(String expiryDate) {
//        this.expiryDate = expiryDate;
//    }
//}
package com.example.expiry_alert;

public class ProductModel {
    public String name;
    public String brand;
    public String expiryDate;

    public ProductModel() {
        // Required for Firebase
    }

    public ProductModel(String name, String brand, String expiryDate) {
        this.name = name;
        this.brand = brand;
        this.expiryDate = expiryDate;
    }
}
