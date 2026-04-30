package com.conectatec.ui.admin.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.conectatec.databinding.FragmentAdminPerfilBinding;
import com.conectatec.ui.auth.LoginActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/**
 * Perfil del Administrador.
 * Muestra foto (iniciales como placeholder), info de cuenta y botón de cerrar sesión.
 */
public class AdminPerfilFragment extends Fragment {

    private FragmentAdminPerfilBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminPerfilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Los datos están hardcodeados en el XML (dummy).
        // TODO: cargar desde SessionManager cuando esté implementado.

        binding.btnLogoutAdmin.setOnClickListener(v -> confirmarCerrarSesion());
    }

    private void confirmarCerrarSesion() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Cerrar sesión")
                .setMessage("¿Deseas cerrar sesión?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Cerrar sesión", (d, w) -> cerrarSesion())
                .show();
    }

    private void cerrarSesion() {
        // TODO: SessionManager.clearSession()
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
