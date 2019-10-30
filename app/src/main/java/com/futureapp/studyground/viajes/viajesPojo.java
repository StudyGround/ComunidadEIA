package com.futureapp.studyground.viajes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class viajesPojo {
    String origen,destino,telefono,precio,hora,nombre;

    ArrayList<String> data=new ArrayList<>();

    public viajesPojo() {

    }

    public viajesPojo(String origen, String destino, String telefono, String precio, String hora, String nombre) {
        this.origen = origen;
        this.destino = destino;
        this.telefono = telefono;
        this.precio = precio;
        this.hora = hora;
        this.nombre = nombre;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    @Override
    public String toString() {
        return "viajesPojo{" +
                "origen='" + origen + '\'' +
                ", destino='" + destino + '\'' +
                ", telefono='" + telefono + '\'' +
                ", precio='" + precio + '\'' +
                ", hora='" + hora + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}

