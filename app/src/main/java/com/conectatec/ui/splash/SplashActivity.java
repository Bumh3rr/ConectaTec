package com.conectatec.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.conectatec.databinding.ActivitySplashBinding;
import com.conectatec.ui.auth.LoginActivity;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Pantalla de carga inicial.
 * Aquí se verificará si el usuario ya tiene sesión activa (JWT guardado).
 * Si hay token válido → redirige al Main del rol correspondiente.
 * Si no hay token → redirige a LoginActivity.
 */
@AndroidEntryPoint
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY_MS = 2000;
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // TODO: verificar SessionManager
            // Si hay token → detectar rol → ir al Main correspondiente
            // Por ahora siempre va al Login
            irALogin();
        }, SPLASH_DELAY_MS);
    }

    private void irALogin() {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish(); // Splash no regresa al backstack
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}