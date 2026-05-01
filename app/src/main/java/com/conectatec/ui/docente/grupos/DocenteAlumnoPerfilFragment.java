package com.conectatec.ui.docente.grupos;

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
import com.conectatec.data.model.AlumnoPerfil;
import com.conectatec.databinding.FragmentDocenteAlumnoPerfilBinding;
import com.conectatec.ui.common.UiState;
import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteAlumnoPerfilFragment extends Fragment {

    private FragmentDocenteAlumnoPerfilBinding binding;
    private DocenteAlumnoPerfilViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteAlumnoPerfilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        int alumnoId     = args != null ? args.getInt("alumnoId", 0)         : 0;
        String nombre    = args != null ? args.getString("nombre", "—")      : "—";
        String iniciales = args != null ? args.getString("iniciales", "?")   : "?";
        String correo    = args != null ? args.getString("correo", "—")      : "—";
        String matricula = args != null ? args.getString("matricula", "—")   : "—";

        binding.btnBackPerfilAlumno.setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());

        viewModel = new ViewModelProvider(this).get(DocenteAlumnoPerfilViewModel.class);
        observeViewModel();
        viewModel.cargarPerfil(alumnoId, nombre, iniciales, correo, matricula);

        binding.btnMensajePerfilAlumno.setOnClickListener(v -> {
            Bundle chatArgs = new Bundle();
            chatArgs.putInt("salaId", 4);
            Navigation.findNavController(v)
                    .navigate(R.id.action_perfil_alumno_to_conversacion, chatArgs);
        });
    }

    private void observeViewModel() {
        viewModel.getState().observe(getViewLifecycleOwner(), state -> {
            binding.progressBarAlumnoPerfil.setVisibility(
                    state instanceof UiState.Loading ? View.VISIBLE : View.GONE);
            if (state instanceof UiState.Success) {
                AlumnoPerfil perfil = ((UiState.Success<AlumnoPerfil>) state).data;
                poblarVistas(perfil);
            } else if (state instanceof UiState.Error) {
                Snackbar.make(binding.getRoot(),
                        ((UiState.Error<?>) state).mensaje, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void poblarVistas(AlumnoPerfil perfil) {
        binding.tvInicialesPerfilAlumno.setText(perfil.iniciales);
        binding.tvNombrePerfilAlumno.setText(perfil.nombre);
        binding.tvCorreoPerfilAlumno.setText(perfil.correo);
        binding.tvMatriculaPerfilAlumno.setText(perfil.matricula);
        binding.tvSemestrePerfilAlumno.setText(perfil.semestre);
        binding.tvCarreraPerfilAlumno.setText(perfil.carrera);
        binding.tvInscritoPerfilAlumno.setText(perfil.fechaInscripcion);

        int progreso = perfil.totalActividades > 0
                ? (int) (perfil.entregadas * 100f / perfil.totalActividades) : 0;
        binding.progressActividadAlumno.setProgress(progreso);
        binding.tvActEntregadas.setText(perfil.entregadas + "/" + perfil.totalActividades);
        binding.tvActCalificadas.setText(String.valueOf(perfil.calificadas));
        binding.tvActPendientes.setText(String.valueOf(perfil.pendientes));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
