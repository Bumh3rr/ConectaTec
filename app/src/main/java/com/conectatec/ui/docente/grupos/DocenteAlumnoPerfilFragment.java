package com.conectatec.ui.docente.grupos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.conectatec.R;
import com.conectatec.databinding.FragmentDocenteAlumnoPerfilBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteAlumnoPerfilFragment extends Fragment {

    private FragmentDocenteAlumnoPerfilBinding binding;

    private static final String[] SEMESTRES = {
            "6° Semestre", "6° Semestre", "6° Semestre",
            "6° Semestre", "6° Semestre", "6° Semestre",
            "4° Semestre", "4° Semestre", "4° Semestre",
            "4° Semestre", "4° Semestre", "4° Semestre",
            "2° Semestre", "2° Semestre", "2° Semestre",
            "2° Semestre", "2° Semestre", "2° Semestre"
    };
    private static final String[] CARRERAS = {
            "Ing. en Sistemas Computacionales",
            "Ing. en Software",
            "Ing. en Tecnologías de la Información",
            "Ing. en Sistemas Computacionales",
            "Ing. en Software",
            "Ing. en Tecnologías de la Información"
    };
    private static final String[] FECHAS_INSCRIPCION = {
            "01/02/2026", "15/01/2026", "20/01/2026",
            "01/02/2026", "15/01/2026", "20/01/2026"
    };

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

        binding.tvInicialesPerfilAlumno.setText(iniciales);
        binding.tvNombrePerfilAlumno.setText(nombre);
        binding.tvCorreoPerfilAlumno.setText(correo);
        binding.tvMatriculaPerfilAlumno.setText(matricula);

        int sIdx = alumnoId % SEMESTRES.length;
        int cIdx = alumnoId % CARRERAS.length;
        int fIdx = alumnoId % FECHAS_INSCRIPCION.length;
        binding.tvSemestrePerfilAlumno.setText(SEMESTRES[sIdx]);
        binding.tvCarreraPerfilAlumno.setText(CARRERAS[cIdx]);
        binding.tvInscritoPerfilAlumno.setText(FECHAS_INSCRIPCION[fIdx]);

        int total      = 18;
        int entregadas = 10 + (alumnoId % 8);
        if (entregadas > total) entregadas = total;
        int calificadas = entregadas / 2;
        int pendientes  = total - entregadas;
        int progreso    = (int) (entregadas * 100f / total);

        binding.progressActividadAlumno.setProgress(progreso);
        binding.tvActEntregadas.setText(entregadas + "/" + total);
        binding.tvActCalificadas.setText(String.valueOf(calificadas));
        binding.tvActPendientes.setText(String.valueOf(pendientes));

        binding.btnMensajePerfilAlumno.setOnClickListener(v -> {
            Bundle chatArgs = new Bundle();
            chatArgs.putInt("salaId", 4);
            Navigation.findNavController(v)
                    .navigate(R.id.action_perfil_alumno_to_conversacion, chatArgs);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
