package com.conectatec.ui.auth;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.conectatec.R;
import com.conectatec.databinding.ActivityRegistroBinding;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity contenedor del flujo de registro.
 * Solo maneja el NavHostFragment — toda la lógica
 * vive en RegistroViewModel compartido entre los fragments.
 */
@AndroidEntryPoint
public class RegistroActivity extends AppCompatActivity {

    private ActivityRegistroBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navHostRegistro);

        if (navHost != null) {
            navController = navHost.getNavController();
        }
    }

    /**
     * Llamado desde RegistroExitosoFragment para ir a WaitingApproval.
     */
    public void irAEspera() {
        startActivity(new android.content.Intent(this, WaitingApprovalActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}