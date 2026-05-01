package com.conectatec.ui.docente.tareas;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.conectatec.R;
import com.conectatec.databinding.FragmentDocenteCrearTareaBinding;
import com.conectatec.databinding.ItemArchivoAdjuntoBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteCrearTareaFragment extends Fragment {

    private FragmentDocenteCrearTareaBinding binding;
    private int grupoId;
    private int bloqueId;

    private final List<Uri> archivosAdjuntos = new ArrayList<>();

    private final ActivityResultLauncher<String[]> pickerLauncher =
            registerForActivityResult(new ActivityResultContracts.OpenMultipleDocuments(),
                    uris -> {
                        if (uris != null && !uris.isEmpty()) {
                            for (Uri uri : uris) {
                                requireContext().getContentResolver()
                                        .takePersistableUriPermission(uri,
                                                Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                if (!archivosAdjuntos.contains(uri)) {
                                    archivosAdjuntos.add(uri);
                                }
                            }
                            renderArchivos();
                        }
                    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteCrearTareaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        grupoId  = getArguments() != null ? getArguments().getInt("grupoId",  1) : 1;
        bloqueId = getArguments() != null ? getArguments().getInt("bloqueId", 1) : 1;

        binding.btnVolverCrearTarea.setOnClickListener(v -> requireActivity().onBackPressed());
        binding.etFechaLimiteTareaNueva.setOnClickListener(v -> abrirDatePicker());
        binding.tilFechaLimiteTareaNueva.setEndIconOnClickListener(v -> abrirDatePicker());
        binding.zonaSubirArchivo.setOnClickListener(v ->
                pickerLauncher.launch(new String[]{"*/*"}));
        binding.btnPublicarTarea.setOnClickListener(v -> onPublicarClicked());
    }

    private void abrirDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(requireContext(),
                (picker, year, month, day) -> {
                    String fecha = String.format(Locale.getDefault(),
                            "%02d/%02d/%04d", day, month + 1, year);
                    binding.etFechaLimiteTareaNueva.setText(fecha);
                },
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void renderArchivos() {
        binding.containerArchivosAdjuntos.removeAllViews();

        if (archivosAdjuntos.isEmpty()) {
            binding.containerArchivosAdjuntos.setVisibility(View.GONE);
            return;
        }

        binding.containerArchivosAdjuntos.setVisibility(View.VISIBLE);
        LayoutInflater inflater = LayoutInflater.from(requireContext());

        for (Uri uri : new ArrayList<>(archivosAdjuntos)) {
            ItemArchivoAdjuntoBinding item =
                    ItemArchivoAdjuntoBinding.inflate(inflater, binding.containerArchivosAdjuntos, false);

            item.tvNombreArchivo.setText(resolverNombre(uri));
            item.tvTamanioArchivo.setText(resolverTamanio(uri));

            // Tap en la tarjeta → abrir el archivo
            item.getRoot().setOnClickListener(v -> abrirArchivo(uri));

            // Tap en X → eliminar
            item.btnEliminarArchivo.setOnClickListener(v -> {
                archivosAdjuntos.remove(uri);
                renderArchivos();
            });

            binding.containerArchivosAdjuntos.addView(item.getRoot());
        }
    }

    private String resolverNombre(Uri uri) {
        String nombre = null;
        try (Cursor c = requireContext().getContentResolver()
                .query(uri, null, null, null, null)) {
            if (c != null && c.moveToFirst()) {
                int idx = c.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (idx >= 0) nombre = c.getString(idx);
            }
        } catch (Exception ignored) {}
        return nombre != null ? nombre : uri.getLastPathSegment();
    }

    private String resolverTamanio(Uri uri) {
        long bytes = -1;
        try (Cursor c = requireContext().getContentResolver()
                .query(uri, null, null, null, null)) {
            if (c != null && c.moveToFirst()) {
                int idx = c.getColumnIndex(OpenableColumns.SIZE);
                if (idx >= 0 && !c.isNull(idx)) bytes = c.getLong(idx);
            }
        } catch (Exception ignored) {}

        if (bytes < 0)              return "· toca para abrir";
        if (bytes < 1_024)          return bytes + " B · toca para abrir";
        if (bytes < 1_048_576)      return String.format(Locale.getDefault(), "%.0f KB · toca para abrir", bytes / 1_024.0);
        return                             String.format(Locale.getDefault(), "%.1f MB · toca para abrir", bytes / 1_048_576.0);
    }

    private void abrirArchivo(Uri uri) {
        String mime = requireContext().getContentResolver().getType(uri);
        if (mime == null) mime = "*/*";
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setDataAndType(uri, mime)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Snackbar.make(binding.getRoot(),
                    "No hay app para abrir este archivo", Snackbar.LENGTH_SHORT).show();
        }
    }

    private String obtenerTipoSeleccionado() {
        int chipId = binding.chipGroupTipoTarea.getCheckedChipId();
        if (chipId == R.id.chipTipoTrabajo)  return "TRABAJO";
        if (chipId == R.id.chipTipoExamen)   return "EXAMEN";
        if (chipId == R.id.chipTipoProyecto) return "PROYECTO";
        return "TAREA";
    }

    private void onPublicarClicked() {
        String titulo = binding.etTituloTareaNueva.getText() != null
                ? binding.etTituloTareaNueva.getText().toString().trim() : "";
        String fecha  = binding.etFechaLimiteTareaNueva.getText() != null
                ? binding.etFechaLimiteTareaNueva.getText().toString().trim() : "";

        if (titulo.isEmpty()) {
            binding.tilTituloTareaNueva.setError("Requerido");
            return;
        }
        if (fecha.isEmpty()) {
            binding.tilFechaLimiteTareaNueva.setError("Selecciona una fecha límite");
            return;
        }
        binding.tilTituloTareaNueva.setError(null);
        binding.tilFechaLimiteTareaNueva.setError(null);

        String tipo = obtenerTipoSeleccionado();
        // TODO: llamar a TareaService.crearTarea(grupoId, bloqueId, titulo, tipo, fecha, archivosAdjuntos)
        //       luego NotificacionService.enviar(tipo=1)

        Snackbar.make(binding.getRoot(), "Tarea creada", Snackbar.LENGTH_SHORT).show();
        Navigation.findNavController(requireView()).popBackStack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
