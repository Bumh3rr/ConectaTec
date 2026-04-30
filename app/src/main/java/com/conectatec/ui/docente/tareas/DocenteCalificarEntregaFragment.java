package com.conectatec.ui.docente.tareas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.conectatec.R;
import com.conectatec.databinding.FragmentDocenteCalificarEntregaBinding;
import com.conectatec.ui.docente.tareas.adapter.EntregaDocenteAdapter;
import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteCalificarEntregaFragment extends Fragment {

    private FragmentDocenteCalificarEntregaBinding binding;
    private int tareaId;
    private int alumnoId;

    private boolean yaCalificada = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteCalificarEntregaBinding.inflate(
                inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tareaId  = getArguments() != null ? getArguments().getInt("tareaId", 1) : 1;
        alumnoId = getArguments() != null ? getArguments().getInt("alumnoId", 0) : 0;

        binding.btnVolverCalificar.setOnClickListener(v ->
                requireActivity().onBackPressed());

        cargarDatosAlumno();

        binding.btnGuardarCalificacion.setOnClickListener(v -> onGuardarClicked());
    }

    private void cargarDatosAlumno() {
        EntregaDocenteAdapter helper = new EntregaDocenteAdapter();
        helper.cargarParaTarea(tareaId);

        EntregaDocenteAdapter.EntregaDummyDocente entrega = null;
        for (EntregaDocenteAdapter.EntregaDummyDocente e :
                EntregaDocenteAdapter.obtenerListaActual(helper)) {
            if (e.alumnoId == alumnoId) {
                entrega = e;
                break;
            }
        }

        if (entrega == null) {
            Snackbar.make(binding.getRoot(),
                    "Entrega no encontrada", Snackbar.LENGTH_SHORT).show();
            return;
        }

        binding.tvNombreAlumnoCalif.setText(entrega.alumnoNombre);
        binding.tvInicialesAlumnoCalif.setText(entrega.alumnoIniciales);
        binding.tvCorreoAlumnoCalif.setText(
                entrega.alumnoNombre.toLowerCase().replace(" ", ".") + "@conectatec.mx");

        aplicarChipEstado(entrega);

        if (entrega.fechaEntrega != null) {
            binding.tvFechaEntregaCalif.setText("Entregada: " + entrega.fechaEntrega);
            binding.containerArchivoCalif.setVisibility(View.VISIBLE);
        } else {
            binding.tvFechaEntregaCalif.setText("Sin entrega registrada");
            binding.containerArchivoCalif.setVisibility(View.GONE);
        }

        if (entrega.estado == EntregaDocenteAdapter.ESTADO_CALIFICADA
                && entrega.calificacion != null) {
            yaCalificada = true;
            binding.etCalificacionEntrega.setText(String.valueOf(entrega.calificacion));
            binding.btnGuardarCalificacion.setText(R.string.btn_actualizar_calificacion);
        }
    }

    private void aplicarChipEstado(EntregaDocenteAdapter.EntregaDummyDocente e) {
        int drawable;
        String label;
        switch (e.estado) {
            case EntregaDocenteAdapter.ESTADO_ENTREGADA:
                drawable = R.drawable.bg_chip_estudiante; label = "ENTREGADA"; break;
            case EntregaDocenteAdapter.ESTADO_CALIFICADA:
                drawable = R.drawable.bg_chip_estudiante; label = "CALIFICADA"; break;
            case EntregaDocenteAdapter.ESTADO_BORRADOR:
                drawable = R.drawable.bg_chip_pendiente;  label = "BORRADOR";  break;
            default:
                drawable = R.drawable.bg_chip_admin;      label = "SIN ENTREGAR"; break;
        }
        binding.tvChipEstadoCalif.setBackgroundResource(drawable);
        binding.tvChipEstadoCalif.setText(label);
    }

    private void onGuardarClicked() {
        String calif = binding.etCalificacionEntrega.getText() != null
                ? binding.etCalificacionEntrega.getText().toString().trim() : "";
        if (calif.isEmpty()) {
            binding.tilCalificacionEntrega.setError("Requerido");
            return;
        }
        try {
            int valor = Integer.parseInt(calif);
            if (valor < 0 || valor > 100) {
                binding.tilCalificacionEntrega.setError("Debe estar entre 0 y 100");
                return;
            }
        } catch (NumberFormatException nfe) {
            binding.tilCalificacionEntrega.setError("Número inválido");
            return;
        }

        // TODO: llamar a EntregaService.calificar() — luego NotificacionService.enviar(tipo=3)

        Snackbar.make(binding.getRoot(),
                yaCalificada ? "Calificación actualizada" : "Calificación guardada",
                Snackbar.LENGTH_SHORT).show();
        Navigation.findNavController(requireView()).popBackStack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
