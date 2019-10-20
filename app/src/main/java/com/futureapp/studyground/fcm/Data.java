package com.futureapp.studyground.fcm;

import java.util.HashMap;
import java.util.Map;

public class Data {

    String latitude,longitude,name,telefono;

    public Data(String latitude, String longitude, String name, String telefono) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.telefono = telefono;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }


}
