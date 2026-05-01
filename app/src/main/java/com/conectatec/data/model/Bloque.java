package com.conectatec.data.model;

public class Bloque {
    public final int id;
    public final int numero;
    public final String nombre;
    public final int totalTareas;

    public Bloque(int id, int numero, String nombre, int totalTareas) {
        this.id = id;
        this.numero = numero;
        this.nombre = nombre;
        this.totalTareas = totalTareas;
    }
}
