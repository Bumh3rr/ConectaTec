package com.conectatec.ui.docente.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.conectatec.databinding.FragmentDocentePerfilBinding;
import com.conectatec.ui.auth.LoginActivity;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Perfil del Docente.
 *
 * Muestra:
 *  - Header de marca "TecConnect / Mi perfil / Docente".
 *  - Card hero con avatar de iniciales, nombre, correo y chip de rol.
 *  - Card "Información de cuenta" con ID empleado, fecha de registro,
 *    departamento y estado.
 *  - Card "Configuración" con 3 filas (Notificaciones / Tema / Idioma)
 *    como placeholders.
 *  - Botón outlined "Cerrar sesión" que vuelve a LoginActivity.
 */
@AndroidEntryPoint
public class DocentePerfilFragment extends Fragment {

    private FragmentDocentePerfilBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocentePerfilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupListeners();
    }

    private void setupListeners() {
        binding.rowNotificacionesDocente.setOnClickListener(v -> { /* placeholder */ });
        binding.rowTemaDocente.setOnClickListener(v -> { /* placeholder */ });
        binding.rowIdiomaDocente.setOnClickListener(v -> { /* placeholder */ });

        binding.btnCerrarSesionDocente.setOnClickListener(v -> cerrarSesion());
    }

    private void cerrarSesion() {
        // TODO: llamar a SessionService.cerrarSesion()
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().overridePendingTransition(
                android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
