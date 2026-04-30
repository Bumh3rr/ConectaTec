package com.conectatec.ui.admin.grupos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.navigation.Navigation;

import com.conectatec.R;
import com.conectatec.databinding.FragmentAdminGrupoDetalleBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

/**
 * Detalle de un grupo: info completa, miembros, tareas y avisos.
 * Botones para desactivar o eliminar el grupo.
 * Datos dummy para validación visual en el emulador.
 */
public class AdminGrupoDetalleFragment extends Fragment {

    private FragmentAdminGrupoDetalleBinding binding;
    private boolean grupoActivo = true;
    private int grupoId = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminGrupoDetalleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        grupoId = getArguments() != null ? getArguments().getInt("grupoId", 1) : 1;
        cargarDatosDummy();
        setupListeners();
    }

    private void cargarDatosDummy() {
        binding.tvNombreGrupoDetalle.setText("Ingeniería de Software – 6A");
        binding.tvMateriaDetalle.setText("Ingeniería de Software");
        binding.tvDocenteDetalle.setText("Prof. Carlos Bautista");
        binding.tvFechaCreacionDetalle.setText("01/02/2025");
        binding.tvTotalTareasGrupo.setText("7 " + getString(R.string.label_tareas));

        // Badge estado
        actualizarEstadoBadge();

        // Miembros dummy
        cargarMiembros();

        // Avisos dummy
        cargarAvisos();
    }

    private void cargarMiembros() {
        binding.containerMiembros.removeAllViews();
        String[] miembros = {
            "María González",
            "Jorge Hernández",
            "Ana Laura Méndez"
        };
        for (String m : miembros) {
            TextView tv = new TextView(requireContext());
            tv.setText("• " + m);
            tv.setTextColor(requireContext().getColor(R.color.colorOnSurfaceVariant));
            tv.setTextSize(13f);
            tv.setPadding(0, 0, 0, 8);
            binding.containerMiembros.addView(tv);
        }
    }

    private void cargarAvisos() {
        binding.containerAvisos.removeAllViews();
        String[] avisos = {
            "Entrega del proyecto final el 15/05",
            "Examen parcial el 30/04"
        };
        for (String a : avisos) {
            TextView tv = new TextView(requireContext());
            tv.setText("• " + a);
            tv.setTextColor(requireContext().getColor(R.color.colorOnSurfaceVariant));
            tv.setTextSize(13f);
            tv.setPadding(0, 0, 0, 8);
            binding.containerAvisos.addView(tv);
        }
    }

    private void actualizarEstadoBadge() {
        if (grupoActivo) {
            binding.tvEstadoGrupoDetalle.setText("ACTIVO");
            binding.tvEstadoGrupoDetalle.setBackgroundResource(R.drawable.bg_chip_estudiante);
        } else {
            binding.tvEstadoGrupoDetalle.setText("INACTIVO");
            binding.tvEstadoGrupoDetalle.setBackgroundResource(R.drawable.bg_chip_pendiente);
        }
    }

    private void setupListeners() {
        binding.btnVolverGrupo.setOnClickListener(v -> requireActivity().onBackPressed());

        binding.tvVerTodosMiembros.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putInt("grupoId", grupoId);
            args.putInt("actividadId", 0);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_grupo_detalle_to_miembros, args);
        });

        binding.btnDesactivarGrupo.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.msg_confirmar_titulo)
                    .setMessage(R.string.msg_confirmar_desactivar_grupo)
                    .setNegativeButton(R.string.btn_cancelar_dialog, null)
                    .setPositiveButton(R.string.btn_desactivar_grupo, (d, w) -> {
                        grupoActivo = !grupoActivo;
                        actualizarEstadoBadge();
                        Snackbar.make(binding.getRoot(),
                                grupoActivo ? "Grupo activado" : "Grupo desactivado",
                                Snackbar.LENGTH_SHORT).show();
                    })
                    .show();
        });

        binding.btnEliminarGrupo.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.msg_confirmar_titulo)
                    .setMessage(R.string.msg_confirmar_eliminar_grupo)
                    .setNegativeButton(R.string.btn_cancelar_dialog, null)
                    .setPositiveButton(R.string.btn_eliminar_grupo, (d, w) ->
                            Snackbar.make(binding.getRoot(), "Grupo eliminado",
                                    Snackbar.LENGTH_SHORT).show())
                    .show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
