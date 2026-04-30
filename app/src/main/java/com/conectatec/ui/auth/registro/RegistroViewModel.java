package com.conectatec.ui.auth.registro;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

/**
 * ViewModel compartido entre todos los fragments del flujo de registro.
 * Mantiene los datos del usuario mientras navega entre pasos.
 */
public class RegistroViewModel extends ViewModel {

    // Paso 1 — datos del formulario
    private String nombre;
    private String correo;
    private String contrasena;

    // Paso 2 — foto de perfil
    private Uri fotoUri;

    // Getters y Setters
    public String getNombre()              { return nombre; }
    public void setNombre(String nombre)   { this.nombre = nombre; }

    public String getCorreo()              { return correo; }
    public void setCorreo(String correo)   { this.correo = correo; }

    public String getContrasena()                  { return contrasena; }
    public void setContrasena(String contrasena)   { this.contrasena = contrasena; }

    public Uri getFotoUri()            { return fotoUri; }
    public void setFotoUri(Uri uri)    { this.fotoUri = uri; }
}