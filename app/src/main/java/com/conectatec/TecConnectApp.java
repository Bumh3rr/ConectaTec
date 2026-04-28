package com.conectatec;

import android.app.Application;
import dagger.hilt.android.HiltAndroidApp;

/**
 * Clase Application principal de TecConnect.
 * @HiltAndroidApp arranca el grafo de inyección de dependencias.
 * Registrar en AndroidManifest: android:name=".TecConnectApp"
 */
@HiltAndroidApp
public class TecConnectApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}