package com.dpaksoni.assignment.models;

import com.google.gson.annotations.SerializedName;

public class CountryCount {

    @SerializedName("Country")
    private String country;

    @SerializedName("CountryCode")
    private String countryCode;

    @SerializedName("Slug")
    private String slug;

    @SerializedName("NewConfirmed")
    private Integer newConfirmed;

    @SerializedName("TotalConfirmed")
    private Integer totalConfirmed;

    @SerializedName("NewDeaths")
    private Integer newDeaths;

    @SerializedName("TotalDeaths")
    private Integer totalDeaths;

    @SerializedName("NewRecovered")
    private Integer newRecovered;

    @SerializedName("TotalRecovered")
    private Integer totalRecovered;

    @SerializedName("Date")
    private String date;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getNewConfirmed() {
        return newConfirmed;
    }

    public void setNewConfirmed(Integer newConfirmed) {
        this.newConfirmed = newConfirmed;
    }

    public Integer getTotalConfirmed() {
        return totalConfirmed;
    }

    public void setTotalConfirmed(Integer totalConfirmed) {
        this.totalConfirmed = totalConfirmed;
    }

    public Integer getNewDeaths() {
        return newDeaths;
    }

    public void setNewDeaths(Integer newDeaths) {
        this.newDeaths = newDeaths;
    }

    public Integer getTotalDeaths() {
        return totalDeaths;
    }

    public void setTotalDeaths(Integer totalDeaths) {
        this.totalDeaths = totalDeaths;
    }

    public Integer getNewRecovered() {
        return newRecovered;
    }

    public void setNewRecovered(Integer newRecovered) {
        this.newRecovered = newRecovered;
    }

    public Integer getTotalRecovered() {
        return totalRecovered;
    }

    public void setTotalRecovered(Integer totalRecovered) {
        this.totalRecovered = totalRecovered;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
