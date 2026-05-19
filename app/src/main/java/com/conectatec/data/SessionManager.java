package com.conectatec.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class SessionManager {

    private static final String PREFS_NAME = "tc_session";
    private static final String KEY_JWT    = "jwt";
    private static final String KEY_ROL    = "rol";
    private static final String KEY_NOMBRE = "nombre";

    private final SharedPreferences prefs;

    @Inject
    public SessionManager(@ApplicationContext Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            prefs = EncryptedSharedPreferences.create(
                    context,
                    PREFS_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            throw new RuntimeException("SessionManager: error al inicializar cifrado", e);
        }
    }

    /**
     * Constructor protegido sin argumentos. Solo para subclases en tests.
     * prefs = null — todos los métodos públicos hacen guard null.
     */
    protected SessionManager() {
        prefs = null;
    }

    public void guardarSesion(String jwt, String rol, String nombre) {
        if (prefs == null) return;
        prefs.edit()
                .putString(KEY_JWT, jwt)
                .putString(KEY_ROL, rol)
                .putString(KEY_NOMBRE, nombre)
                .apply();
    }

    /** @return JWT guardado, o null si no hay sesión activa. */
    public String getJwt()    { return prefs == null ? null : prefs.getString(KEY_JWT,    null); }

    /** @return Rol del usuario ("ADMIN", "DOCENTE", "ESTUDIANTE", "PENDIENTE"), o null. */
    public String getRol()    { return prefs == null ? null : prefs.getString(KEY_ROL,    null); }

    /** @return Nombre del usuario, o null. */
    public String getNombre() { return prefs == null ? null : prefs.getString(KEY_NOMBRE, null); }

    /** Elimina todos los datos de sesión (logout). */
    public void cerrarSesion() {
        if (prefs != null) prefs.edit().clear().apply();
    }
}
