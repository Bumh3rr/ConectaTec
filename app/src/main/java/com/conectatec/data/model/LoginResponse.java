package com.conectatec.data.model;

public class LoginResponse {
    public final String jwt;
    /** "ADMIN" | "DOCENTE" | "ESTUDIANTE" | "PENDIENTE" */
    public final String rol;
    public final String nombre;

    public LoginResponse(String jwt, String rol, String nombre) {
        this.jwt    = jwt;
        this.rol    = rol;
        this.nombre = nombre;
    }
}
