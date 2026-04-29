package com.conectatec.ui.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.conectatec.R;
import com.conectatec.databinding.ActivityWaitingApprovalBinding;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Pantalla mostrada cuando el usuario se registró pero su rol
 * aún es PENDIENTE. Muestra sus datos y permite cerrar sesión.
 * El SplashActivity redirige aquí si el token existe pero rol == PENDIENTE.
 */
@AndroidEntryPoint
public class WaitingApprovalActivity extends AppCompatActivity {

    private ActivityWaitingApprovalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWaitingApprovalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cargarDatosUsuario();
        setupListeners();
    }

    /**
     * Carga nombre y correo del usuario desde el JWT guardado en sesión.
     * TODO: obtener desde SessionManager cuando esté implementado.
     */
    private void cargarDatosUsuario() {
        // TODO: SessionManager.getNombre() y SessionManager.getCorreo()
        binding.tvNombre.setText("—");
        binding.tvCorreo.setText("—");
    }

    private void setupListeners() {
        binding.btnLogout.setOnClickListener(v -> cerrarSesion());
    }

    private void cerrarSesion() {
        // TODO: SessionManager.clearSession()
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}