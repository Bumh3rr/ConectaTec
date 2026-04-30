package com.conectatec.ui.admin.usuarios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.conectatec.databinding.BottomSheetAsignarRolBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * BottomSheet para seleccionar y confirmar el rol de un usuario.
 * El rol seleccionado se comunica al Fragment a través del listener.
 */
public class AsignarRolBottomSheet extends BottomSheetDialogFragment {

    public static final String TAG = "AsignarRolBottomSheet";

    public interface OnRolSeleccionadoListener {
        void onRolSeleccionado(String rol);
    }

    private BottomSheetAsignarRolBinding binding;
    private OnRolSeleccionadoListener listener;

    // Rol actualmente seleccionado en la UI
    private String rolSeleccionado = "PENDIENTE";

    public static AsignarRolBottomSheet newInstance(String rolActual) {
        AsignarRolBottomSheet sheet = new AsignarRolBottomSheet();
        Bundle args = new Bundle();
        args.putString("rolActual", rolActual);
        sheet.setArguments(args);
        return sheet;
    }

    public void setListener(OnRolSeleccionadoListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BottomSheetAsignarRolBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Preseleccionar rol actual si viene en los args
        if (getArguments() != null) {
            rolSeleccionado = getArguments().getString("rolActual", "PENDIENTE");
        }
        actualizarRadioButtons();
        setupListeners();
    }

    private void setupListeners() {
        binding.opcionPendiente.setOnClickListener(v  -> seleccionar("PENDIENTE"));
        binding.opcionAdmin.setOnClickListener(v      -> seleccionar("ADMINISTRADOR"));
        binding.opcionDocente.setOnClickListener(v    -> seleccionar("DOCENTE"));
        binding.opcionEstudiante.setOnClickListener(v -> seleccionar("ESTUDIANTE"));

        binding.btnConfirmarRol.setOnClickListener(v -> {
            if (listener != null) listener.onRolSeleccionado(rolSeleccionado);
            dismiss();
        });
    }

    private void seleccionar(String rol) {
        rolSeleccionado = rol;
        actualizarRadioButtons();
    }

    private void actualizarRadioButtons() {
        binding.rbPendiente.setChecked("PENDIENTE".equals(rolSeleccionado));
        binding.rbAdmin.setChecked("ADMINISTRADOR".equals(rolSeleccionado));
        binding.rbDocente.setChecked("DOCENTE".equals(rolSeleccionado));
        binding.rbEstudiante.setChecked("ESTUDIANTE".equals(rolSeleccionado));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
