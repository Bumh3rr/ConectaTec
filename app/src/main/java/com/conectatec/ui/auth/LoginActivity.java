package com.conectatec.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.conectatec.databinding.ActivityLoginBinding;
import com.conectatec.ui.admin.MainAdminActivity;
import com.conectatec.ui.docente.MainDocenteActivity;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Pantalla de inicio de sesión.
 * Conecta con LoginViewModel para autenticar al usuario vía JWT.
 */
@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sin status bar visible — pantalla limpia
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            int imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom;
            int navHeight = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            v.setPadding(0, 0, 0, Math.max(imeHeight, navHeight));
            return insets;
        });
        setupListeners();
    }

    private void setupListeners() {

        // TODO: viewModel.login(correo, contrasena) — provisional: selector de rol
        binding.btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, MainAdminActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        binding.btnLoginDocente.setOnClickListener(v -> {
            startActivity(new Intent(this, MainDocenteActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Link a pantalla de registro
        binding.tvIrRegistro.setOnClickListener(v -> {
            startActivity(new Intent(this, RegistroActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}