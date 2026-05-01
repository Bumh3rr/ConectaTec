package com.conectatec.ui.docente.tareas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.conectatec.R;
import com.conectatec.data.model.Entrega;
import com.conectatec.databinding.FragmentDocenteCalificarEntregaBinding;
import com.conectatec.ui.common.UiState;
import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteCalificarEntregaFragment extends Fragment {

    private FragmentDocenteCalificarEntregaBinding binding;
    private DocenteCalificarEntregaViewModel viewModel;
    private int tareaId;
    private int alumnoId;
    private boolean yaCalificada = false;

    private static final String[][] ARCHIVOS_DUMMY = {
            { "tarea_entregable.pdf",   "1.2 MB", "application/pdf" },
            { "evidencia_practica.jpg", "580 KB", "image/jpeg"      }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteCalificarEntregaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tareaId  = getArguments() != null ? getArguments().getInt("tareaId",  1) : 1;
        alumnoId = getArguments() != null ? getArguments().getInt("alumnoId", 0) : 0;

        prepararAnimacion();
        binding.btnVolverCalificar.setOnClickListener(v -> requireActivity().onBackPressed());
        configurarArchivos();
        binding.btnGuardarCalificacion.setOnClickListener(v -> onGuardarClicked());

        viewModel = new ViewModelProvider(this).get(DocenteCalificarEntregaViewModel.class);
        observeViewModel();
        viewModel.cargarEntrega(tareaId, alumnoId);
    }

    private void observeViewModel() {
        viewModel.getState().observe(getViewLifecycleOwner(), state -> {
            binding.progressBarCalificar.setVisibility(
                    state instanceof UiState.Loading ? View.VISIBLE : View.GONE);
            if (state instanceof UiState.Success) {
                Entrega entrega = ((UiState.Success<Entrega>) state).data;
                poblarVistas(entrega);
            } else if (state instanceof UiState.Error) {
                Snackbar.make(binding.getRoot(),
                        ((UiState.Error<?>) state).mensaje, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void prepararAnimacion() {
        binding.cardAlumnoCalif.setAlpha(0f);
        binding.cardCalificacionEntrega.setAlpha(0f);
    }

    private void animarEntrada() {
        float translY = getResources().getDisplayMetrics().density * 32;
        View[] views = {binding.cardAlumnoCalif, binding.cardCalificacionEntrega};
        for (int i = 0; i < views.length; i++) {
            View card = views[i];
            card.setTranslationY(translY);
            card.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setStartDelay(i * 100L)
                    .setDuration(300)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        }
    }

    private void poblarVistas(Entrega entrega) {
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

        if (entrega.estado == Entrega.ESTADO_CALIFICADA && entrega.calificacion != null) {
            yaCalificada = true;
            binding.etCalificacionEntrega.setText(String.valueOf(entrega.calificacion));
            binding.btnGuardarCalificacion.setText(R.string.btn_actualizar_calificacion);
        }
        animarEntrada();
    }

    private void configurarArchivos() {
        // TODO: reemplazar por URIs firmadas del backend
        binding.cardEntregaArchivo1.setOnClickListener(v ->
                abrirArchivoDemo(ARCHIVOS_DUMMY[0][2]));
        binding.cardEntregaArchivo2.setOnClickListener(v ->
                abrirArchivoDemo(ARCHIVOS_DUMMY[1][2]));
    }

    private void abrirArchivoDemo(String mimeType) {
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setDataAndType(Uri.EMPTY, mimeType)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Snackbar.make(binding.getRoot(),
                    "Modo demo — archivo no disponible", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void aplicarChipEstado(Entrega e) {
        int drawable;
        String label;
        switch (e.estado) {
            case Entrega.ESTADO_ENTREGADA:
                drawable = R.drawable.bg_chip_estudiante; label = "ENTREGADA";    break;
            case Entrega.ESTADO_CALIFICADA:
                drawable = R.drawable.bg_chip_docente;    label = "CALIFICADA";   break;
            case Entrega.ESTADO_BORRADOR:
                drawable = R.drawable.bg_chip_pendiente;  label = "BORRADOR";     break;
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
            binding.etCalificacionEntrega.setError("Requerido");
            binding.etCalificacionEntrega.requestFocus();
            return;
        }
        try {
            int valor = Integer.parseInt(calif);
            if (valor < 0 || valor > 100) {
                binding.etCalificacionEntrega.setError("Debe ser entre 0 y 100");
                return;
            }
        } catch (NumberFormatException nfe) {
            binding.etCalificacionEntrega.setError("Número inválido");
            return;
        }

        // TODO: llamar a EntregaService.calificar(tareaId, alumnoId, valor)
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
