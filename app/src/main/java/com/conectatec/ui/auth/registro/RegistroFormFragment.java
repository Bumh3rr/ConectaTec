package com.conectatec.ui.auth.registro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.conectatec.R;
import com.conectatec.databinding.FragmentRegistroFormBinding;

/**
 * Paso 1 del flujo de registro.
 * Recoge nombre, correo y contraseña. Valida localmente
 * y guarda en RegistroViewModel antes de navegar al paso 2.
 */
public class RegistroFormFragment extends Fragment {

    private FragmentRegistroFormBinding binding;
    private RegistroViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRegistroFormBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ViewModel compartido con el scope de la Activity
        viewModel = new ViewModelProvider(requireActivity()).get(RegistroViewModel.class);

        setupListeners();
    }

    private void setupListeners() {

        binding.btnSiguiente.setOnClickListener(v -> {
//            if (validarCampos()) {
//                // Guardar en ViewModel para usarlos en el paso final
//                viewModel.setNombre(binding.etNombreCompleto.getText().toString().trim());
//                viewModel.setCorreo(binding.etCorreo.getText().toString().trim());
//                viewModel.setContrasena(binding.etContrasena.getText().toString().trim());
//
//                // Navegar al paso 2 — foto
//                Navigation.findNavController(v)
//                        .navigate(R.id.action_form_to_foto);
//            }

            Navigation.findNavController(v)
                    .navigate(R.id.action_form_to_foto);
        });

        binding.tvIrLogin.setOnClickListener(v ->
                requireActivity().finish()
        );
    }

    private boolean validarCampos() {
        boolean valido = true;

        String nombre    = binding.etNombreCompleto.getText().toString().trim();
        String correo    = binding.etCorreo.getText().toString().trim();
        String contrasena = binding.etContrasena.getText().toString().trim();
        String confirmar  = binding.etConfirmarContrasena.getText().toString().trim();

        if (nombre.isEmpty()) {
            binding.tilNombreCompleto.setError("El nombre es obligatorio");
            valido = false;
        } else {
            binding.tilNombreCompleto.setError(null);
        }

        if (correo.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            binding.tilCorreo.setError("Ingresa un correo válido");
            valido = false;
        } else {
            binding.tilCorreo.setError(null);
        }

        if (contrasena.length() < 8) {
            binding.tilContrasena.setError("Mínimo 8 caracteres");
            valido = false;
        } else {
            binding.tilContrasena.setError(null);
        }

        if (!contrasena.equals(confirmar)) {
            binding.tilConfirmarContrasena.setError("Las contraseñas no coinciden");
            valido = false;
        } else {
            binding.tilConfirmarContrasena.setError(null);
        }

        return valido;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}