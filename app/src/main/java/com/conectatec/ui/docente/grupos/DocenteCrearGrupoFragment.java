package com.conectatec.ui.docente.grupos;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.conectatec.databinding.FragmentDocenteCrearGrupoBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteCrearGrupoFragment extends Fragment {

    private static final String CHARSET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    private FragmentDocenteCrearGrupoBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteCrearGrupoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupLivePreview();
        setupListeners();
    }

    private void setupLivePreview() {
        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) { actualizarPreview(); }
        };
        binding.etNombreGrupoNuevo.addTextChangedListener(watcher);
        binding.etMateriaGrupoNuevo.addTextChangedListener(watcher);
    }

    private void actualizarPreview() {
        String nombre = getText(binding.etNombreGrupoNuevo);
        String materia = getText(binding.etMateriaGrupoNuevo);

        binding.tvPreviewInicialesGrupo.setText(iniciales(nombre));
        binding.tvPreviewNombreGrupo.setText(nombre.isEmpty() ? "Nombre del grupo" : nombre);
        binding.tvPreviewMateriaGrupo.setText(materia.isEmpty() ? "Materia" : materia);
    }

    private void setupListeners() {
        binding.btnBackCrearGrupo.setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());
        binding.btnCrearGrupoNuevo.setOnClickListener(v -> intentarCrearGrupo());
        binding.btnListoCrearGrupo.setOnClickListener(v ->
                Navigation.findNavController(requireView()).navigateUp());
    }

    private void intentarCrearGrupo() {
        String nombre = getText(binding.etNombreGrupoNuevo);
        String materia = getText(binding.etMateriaGrupoNuevo);

        if (nombre.isEmpty()) {
            binding.etNombreGrupoNuevo.setError("Campo requerido");
            binding.etNombreGrupoNuevo.requestFocus();
            return;
        }
        if (materia.isEmpty()) {
            binding.etMateriaGrupoNuevo.setError("Campo requerido");
            binding.etMateriaGrupoNuevo.requestFocus();
            return;
        }

        // TODO: llamar a GrupoService.crearGrupo(nombre, materia, descripcion)
        mostrarExito(nombre, generarCodigo());
    }

    private void mostrarExito(String nombre, String codigo) {
        binding.cardPreviewGrupo.setVisibility(View.GONE);
        binding.tvContextoCrearGrupo.setVisibility(View.GONE);
        binding.containerFormGrupoNuevo.setVisibility(View.GONE);
        binding.layoutExitoGrupoNuevo.setVisibility(View.VISIBLE);
        binding.tvNombreGrupoExito.setText(nombre);
        binding.tvCodigoUnionNuevo.setText("TC-" + codigo);
        binding.btnCrearGrupoNuevo.setVisibility(View.GONE);
        binding.btnListoCrearGrupo.setVisibility(View.VISIBLE);
        Snackbar.make(requireView(), "¡Grupo creado exitosamente!", Snackbar.LENGTH_SHORT).show();
    }

    private String iniciales(String nombre) {
        if (nombre.isEmpty()) return "?";
        String[] palabras = nombre.trim().split("\\s+");
        if (palabras.length >= 2) {
            return "" + Character.toUpperCase(palabras[0].charAt(0))
                      + Character.toUpperCase(palabras[1].charAt(0));
        }
        return String.valueOf(Character.toUpperCase(nombre.charAt(0)));
    }

    private String generarCodigo() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(CHARSET.charAt((int) (Math.random() * CHARSET.length())));
        }
        return sb.toString();
    }

    private String getText(TextInputEditText et) {
        CharSequence cs = et.getText();
        return cs != null ? cs.toString().trim() : "";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
