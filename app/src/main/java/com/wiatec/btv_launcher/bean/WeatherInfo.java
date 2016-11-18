package com.wiatec.btv_launcher.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by PX on 2016-11-15.
 */

public class WeatherInfo implements Parcelable {
    private String country;
    private String city;
    private String date;
    private String icon;
    private String weather;
    private String weatherDescription;
    private String temperature;
    private String humidity;
    private String pressure;
    private String maxTemperature;
    private String minTemperature;
    private String deg;
    private String spd;
    private String sunrise;
    private String sunset;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(String maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public String getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(String minTemperature) {
        this.minTemperature = minTemperature;
    }

    public String getDeg() {
        return deg;
    }

    public void setDeg(String deg) {
        this.deg = deg;
    }

    public String getSpd() {
        return spd;
    }

    public void setSpd(String spd) {
        this.spd = spd;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    @Override
    public String toString() {
        return "WeatherInfo{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", date='" + date + '\'' +
                ", icon='" + icon + '\'' +
                ", weather='" + weather + '\'' +
                ", weatherDescription='" + weatherDescription + '\'' +
                ", temperature='" + temperature + '\'' +
                ", humidity='" + humidity + '\'' +
                ", pressure='" + pressure + '\'' +
                ", maxTemperature='" + maxTemperature + '\'' +
                ", minTemperature='" + minTemperature + '\'' +
                ", deg='" + deg + '\'' +
                ", spd='" + spd + '\'' +
                ", sunrise='" + sunrise + '\'' +
                ", sunset='" + sunset + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.country);
        dest.writeString(this.city);
        dest.writeString(this.date);
        dest.writeString(this.icon);
        dest.writeString(this.weather);
        dest.writeString(this.weatherDescription);
        dest.writeString(this.temperature);
        dest.writeString(this.humidity);
        dest.writeString(this.pressure);
        dest.writeString(this.maxTemperature);
        dest.writeString(this.minTemperature);
        dest.writeString(this.deg);
        dest.writeString(this.spd);
        dest.writeString(this.sunrise);
        dest.writeString(this.sunset);
    }

    public WeatherInfo() {
    }

    protected WeatherInfo(Parcel in) {
        this.country = in.readString();
        this.city = in.readString();
        this.date = in.readString();
        this.icon = in.readString();
        this.weather = in.readString();
        this.weatherDescription = in.readString();
        this.temperature = in.readString();
        this.humidity = in.readString();
        this.pressure = in.readString();
        this.maxTemperature = in.readString();
        this.minTemperature = in.readString();
        this.deg = in.readString();
        this.spd = in.readString();
        this.sunrise = in.readString();
        this.sunset = in.readString();
    }

    public static final Parcelable.Creator<WeatherInfo> CREATOR = new Parcelable.Creator<WeatherInfo>() {
        @Override
        public WeatherInfo createFromParcel(Parcel source) {
            return new WeatherInfo(source);
        }

        @Override
        public WeatherInfo[] newArray(int size) {
            return new WeatherInfo[size];
        }
    };
}
