package com.example.david.centroestudios;

/**
 * Clase para el contenido de las cardview en Buscar escuela
 */
public class CentrosEstudios {
    private String nombre;
    private String direccion;
    private String telefono;
    private String localidad;
    private String longitud;
    private String latitud;

    public CentrosEstudios(String nombre, String direccion, String telefono, String localidad, String longitud, String latitud) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.localidad = localidad;
        this.longitud = longitud;
        this.latitud = latitud;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getLocalidad() {
        return localidad;
    }

    public String getLongitud() {
        return longitud;
    }

    public String getLatitud() {
        return latitud;
    }


}
