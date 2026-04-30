package com.conectatec.ui.docente.tareas;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.conectatec.databinding.FragmentDocenteCrearTareaBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteCrearTareaFragment extends Fragment {

    private FragmentDocenteCrearTareaBinding binding;
    private int grupoId;
    private int bloqueId;

    private static final String[] TIPOS = { "TAREA", "TRABAJO", "EXAMEN", "PROYECTO" };

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
        grupoId  = getArguments() != null ? getArguments().getInt("grupoId", 1) : 1;
        bloqueId = getArguments() != null ? getArguments().getInt("bloqueId", 1) : 1;

        binding.btnVolverCrearTarea.setOnClickListener(v ->
                requireActivity().onBackPressed());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, TIPOS);
        binding.spinnerTipoTareaNueva.setAdapter(adapter);

        binding.etFechaLimiteTareaNueva.setOnClickListener(v -> abrirDatePicker());
        binding.btnPublicarTarea.setOnClickListener(v -> onPublicarClicked());
    }

    private void abrirDatePicker() {
        Calendar c = Calendar.getInstance();
        DatePickerDialog dlg = new DatePickerDialog(requireContext(),
                (datePicker, year, month, day) -> {
                    String fecha = String.format(Locale.getDefault(),
                            "%02d/%02d/%04d", day, month + 1, year);
                    binding.etFechaLimiteTareaNueva.setText(fecha);
                },
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dlg.show();
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
            Snackbar.make(binding.getRoot(),
                    "Selecciona una fecha límite", Snackbar.LENGTH_SHORT).show();
            return;
        }

        // TODO: llamar a TareaService.crearTarea() — luego NotificacionService.enviar(tipo=1)

        Snackbar.make(binding.getRoot(),
                "Tarea creada", Snackbar.LENGTH_SHORT).show();
        Navigation.findNavController(requireView()).popBackStack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
