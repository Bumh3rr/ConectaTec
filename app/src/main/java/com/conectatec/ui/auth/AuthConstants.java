package com.conectatec.ui.auth;

public final class AuthConstants {

    /**
     * Dominio institucional permitido. Cambiar al dominio real de la institución.
     * Ejemplo: "@tec.mx", "@alumno.ipn.mx"
     */
    public static final String DOMINIO_INSTITUCIONAL = "@tudominio.edu.mx";

    private AuthConstants() {}

    /** Retorna true si el email termina con el dominio institucional. */
    public static boolean esDominioValido(String email) {
        if (email == null || email.isEmpty()) return false;
        return email.endsWith(DOMINIO_INSTITUCIONAL);
    }
}
