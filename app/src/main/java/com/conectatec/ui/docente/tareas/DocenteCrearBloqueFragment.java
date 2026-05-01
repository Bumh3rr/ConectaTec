package com.conectatec.ui.docente.tareas;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.conectatec.databinding.FragmentDocenteCrearBloqueBinding;
import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteCrearBloqueFragment extends Fragment {

    private FragmentDocenteCrearBloqueBinding binding;
    private int grupoId;
    private int siguienteNumero;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteCrearBloqueBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        grupoId = getArguments() != null ? getArguments().getInt("grupoId", 0) : 0;
        siguienteNumero = getArguments() != null ? getArguments().getInt("siguienteNumero", 1) : 1;

        renderPreview();
        setupLivePreview();
        setupListeners();
    }

    private void renderPreview() {
        binding.tvPreviewNumeroBloque.setText(String.valueOf(siguienteNumero));
        binding.tvNumeroAsignadoBloque.setText("Bloque " + siguienteNumero);
    }

    private void setupLivePreview() {
        binding.etNombreNuevoBloque.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String nombre = s.toString().trim();
                binding.tvPreviewNombreBloque.setText(
                        TextUtils.isEmpty(nombre) ? "Nombre del bloque" : nombre);
            }
        });
    }

    private void setupListeners() {
        binding.btnVolverCrearBloque.setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack());

        binding.btnGuardarBloque.setOnClickListener(v -> onGuardarClicked());
    }

    private void onGuardarClicked() {
        CharSequence raw = binding.etNombreNuevoBloque.getText();
        String nombre = (raw != null) ? raw.toString().trim() : "";

        if (TextUtils.isEmpty(nombre)) {
            binding.etNombreNuevoBloque.setError("El nombre es obligatorio");
            binding.etNombreNuevoBloque.requestFocus();
            return;
        }

        // TODO: llamar a TareasService.crearBloque(grupoId, siguienteNumero, nombre)

        Snackbar.make(binding.getRoot(),
                "Bloque \"" + nombre + "\" creado", Snackbar.LENGTH_SHORT).show();

        NavHostFragment.findNavController(this).popBackStack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
