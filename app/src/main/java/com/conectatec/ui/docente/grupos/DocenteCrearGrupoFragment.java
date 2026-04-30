package com.conectatec.ui.docente.grupos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.conectatec.databinding.FragmentDocenteCrearGrupoBinding;
import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Formulario para crear un grupo nuevo.
 *
 * Flujo:
 *  1. El docente completa nombre, materia y descripción opcional.
 *  2. Al pulsar "Crear grupo" se validan los campos.
 *  3. Se genera un código de unión aleatorio y se muestra el QR.
 *  4. "Listo" regresa a la lista de grupos.
 */
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
        setupListeners();
    }

    private void setupListeners() {
        binding.btnBackCrearGrupo.setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());

        binding.btnCrearGrupoNuevo.setOnClickListener(v -> intentarCrearGrupo());

        binding.btnListoCrearGrupo.setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());
    }

    private void intentarCrearGrupo() {
        String nombre = getText(binding.etNombreGrupoNuevo);
        String materia = getText(binding.etMateriaGrupoNuevo);

        if (nombre.isEmpty()) {
            binding.tilNombreGrupoNuevo.setError("Campo requerido");
            return;
        }
        binding.tilNombreGrupoNuevo.setError(null);

        if (materia.isEmpty()) {
            binding.tilMateriaGrupoNuevo.setError("Campo requerido");
            return;
        }
        binding.tilMateriaGrupoNuevo.setError(null);

        // TODO: llamar a GrupoService.crearGrupo(nombre, materia, descripcion)
        String codigo = generarCodigo();
        mostrarExito(codigo);
    }

    private void mostrarExito(String codigo) {
        binding.btnCrearGrupoNuevo.setVisibility(View.GONE);
        binding.layoutQrExito.setVisibility(View.VISIBLE);
        binding.tvCodigoUnionNuevo.setText("TC-" + codigo);
        Snackbar.make(requireView(), "Grupo creado exitosamente", Snackbar.LENGTH_SHORT).show();
    }

    private String generarCodigo() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(CHARSET.charAt((int) (Math.random() * CHARSET.length())));
        }
        return sb.toString();
    }

    private String getText(com.google.android.material.textfield.TextInputEditText et) {
        CharSequence cs = et.getText();
        return cs != null ? cs.toString().trim() : "";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
