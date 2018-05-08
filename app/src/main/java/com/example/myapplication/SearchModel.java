package com.example.myapplication;

/**
 * Created by celestine on 18/04/2018.
 */

public class SearchModel {
    private String image,name,address,atmosphere,average_price,class_shop,hours,menu,phone,tags,website,lat,longitude;


    public SearchModel(String image, String name, String address, String atmosphere, String average_price, String class_shop, String hours, String menu, String phone, String tags, String website, String lat, String longitude) {
        this.image = image;
        this.name = name;
        this.address = address;
        this.atmosphere = atmosphere;
        this.average_price = average_price;
        this.class_shop = class_shop;
        this.hours = hours;
        this.menu = menu;
        this.phone = phone;
        this.tags = tags;
        this.website = website;
        this.lat = lat;
        this.longitude = longitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAtmosphere() {
        return atmosphere;
    }

    public void setAtmosphere(String atmosphere) {
        this.atmosphere = atmosphere;
    }

    public String getAverage_price() {
        return average_price;
    }

    public void setAverage_price(String average_price) {
        this.average_price = average_price;
    }

    public String getClass_shop() {
        return class_shop;
    }

    public void setClass_shop(String class_shop) {
        this.class_shop = class_shop;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
