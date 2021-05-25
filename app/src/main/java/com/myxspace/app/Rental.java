package com.myxspace.app;

import java.util.List;

public class Rental {

    private String name;
    private float rating;
    private int duration;
    private int capacity;
    private int price;
    private String host;
    private String description;
    private String place;

    private String rentalImageUrl;
    private String hostPhotoUrl;

    private List<String> services;

    public Rental() {
    }

    public Rental(String name, float rating, int duration, int capacity, int price, String host, String description, String place, String rentalImageUrl, String hostPhotoUrl, List<String> services) {
        this.name = name;
        this.rating = rating;
        this.duration = duration;
        this.capacity = capacity;
        this.price = price;
        this.host = host;
        this.description = description;
        this.place = place;
        this.rentalImageUrl = rentalImageUrl;
        this.hostPhotoUrl = hostPhotoUrl;
        this.services = services;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getRentalImageUrl() {
        return rentalImageUrl;
    }

    public void setRentalImageUrl(String rentalImageUrl) {
        this.rentalImageUrl = rentalImageUrl;
    }

    public String getHostPhotoUrl() {
        return hostPhotoUrl;
    }

    public void setHostPhotoUrl(String hostPhotoUrl) {
        this.hostPhotoUrl = hostPhotoUrl;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }
}
