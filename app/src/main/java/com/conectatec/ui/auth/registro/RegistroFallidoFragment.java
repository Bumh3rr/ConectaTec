package com.conectatec.ui.auth.registro;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.conectatec.R;
import com.conectatec.databinding.FragmentRegistroFallidoBinding;
import com.conectatec.ui.auth.LoginActivity;

/**
 * Paso 4b — Registro fallido.
 * Muestra el motivo del error y ofrece dos opciones:
 * intentar de nuevo (regresa al paso 1) o volver al login.
 */
public class RegistroFallidoFragment extends Fragment {

    private FragmentRegistroFallidoBinding binding;

    // Claves para recibir el motivo del error desde el ViewModel o args
    public static final String ERROR_CORREO_DUPLICADO = "El correo ya está registrado. Intenta con otro.";
    public static final String ERROR_FOTO_INVALIDA    = "Tu foto no fue reconocida como un rostro humano. Intenta con otra imagen.";
    public static final String ERROR_RED              = "No se pudo conectar con el servidor. Verifica tu conexión e intenta de nuevo.";
    public static final String ERROR_GENERICO         = "Ocurrió un error inesperado. Por favor intenta de nuevo.";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRegistroFallidoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mostrarMotivoError();
        setupListeners();
    }

    /**
     * Muestra el motivo específico del error.
     * TODO: recibir el error desde RegistroViewModel cuando esté conectado al backend.
     */
    private void mostrarMotivoError() {
        // Por ahora muestra el error genérico
        // Cuando esté conectado:
        // String error = viewModel.getErrorRegistro();
        // binding.tvMotivoError.setText(error);
        binding.tvMotivoError.setText(ERROR_GENERICO);
    }

    private void setupListeners() {

        // Intentar de nuevo — regresa al paso 1 limpiando el backstack
        binding.btnIntentarDeNuevo.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_fallido_to_form)
        );

        // Volver al login — cierra la RegistroActivity
        binding.btnVolverLogin.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            requireActivity().finish();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}