package com.conectatec.data.model;

public class Miembro {
    public final int id;
    public final String nombre;
    public final String iniciales;
    public final String correo;
    public final String matricula;

    public Miembro(int id, String nombre, String iniciales, String correo, String matricula) {
        this.id = id;
        this.nombre = nombre;
        this.iniciales = iniciales;
        this.correo = correo;
        this.matricula = matricula;
    }
}
