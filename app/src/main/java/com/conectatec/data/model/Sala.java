package com.conectatec.data.model;

public class Sala {
    public final int id;
    public final String nombre;
    public final String tipo;
    public final String avatarIniciales;
    public final String ultimoMensaje;
    public final String fechaUltimo;
    public final int noLeidos;

    public Sala(int id, String nombre, String tipo, String avatarIniciales,
                String ultimoMensaje, String fechaUltimo, int noLeidos) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.avatarIniciales = avatarIniciales;
        this.ultimoMensaje = ultimoMensaje;
        this.fechaUltimo = fechaUltimo;
        this.noLeidos = noLeidos;
    }
}
