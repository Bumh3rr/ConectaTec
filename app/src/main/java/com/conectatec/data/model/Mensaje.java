package com.conectatec.data.model;

public class Mensaje {
    public final int id;
    public final String texto;
    public final String hora;
    public final boolean esMio;
    public final String autorNombre;
    public final String autorIniciales;
    public final boolean tieneAdjunto;

    public Mensaje(int id, String texto, String hora, boolean esMio,
                   String autorNombre, String autorIniciales, boolean tieneAdjunto) {
        this.id = id;
        this.texto = texto;
        this.hora = hora;
        this.esMio = esMio;
        this.autorNombre = autorNombre;
        this.autorIniciales = autorIniciales;
        this.tieneAdjunto = tieneAdjunto;
    }
}
