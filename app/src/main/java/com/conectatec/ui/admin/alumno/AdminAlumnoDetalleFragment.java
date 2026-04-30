package com.conectatec.ui.admin.alumno;

import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.conectatec.R;
import com.conectatec.databinding.FragmentAlumnoDetalleBinding;

public class AdminAlumnoDetalleFragment extends Fragment {

    private FragmentAlumnoDetalleBinding binding;

    private static class DetalleAlumno {
        final int      id;
        final String   nombre;
        final String   iniciales;
        final String   correo;
        final String   carrera;
        final String   semestre;
        final String   matricula;
        final String   fechaInscripcion;
        final boolean  cuentaActiva;
        final String[] grupos;
        final String[][] entregas; // {titulo, estado, fechaVencimiento}

        DetalleAlumno(int id, String nombre, String iniciales, String correo,
                      String carrera, String semestre, String matricula,
                      String fechaInscripcion, boolean cuentaActiva,
                      String[] grupos, String[][] entregas) {
            this.id               = id;
            this.nombre           = nombre;
            this.iniciales        = iniciales;
            this.correo           = correo;
            this.carrera          = carrera;
            this.semestre         = semestre;
            this.matricula        = matricula;
            this.fechaInscripcion = fechaInscripcion;
            this.cuentaActiva     = cuentaActiva;
            this.grupos           = grupos;
            this.entregas         = entregas;
        }
    }

    private static final String ISC = "Ing. en Sistemas Computacionales";
    private static final String IRT = "Ing. en Redes y Telecomunicaciones";
    private static final String IS  = "Ingeniería en Software";

    private static final DetalleAlumno[] DATOS = {
        // ── Grupo 1: Ingeniería de Software – 6A ──────────────────────────
        new DetalleAlumno(1, "María González", "MG", "m.gonzalez@tec.mx",
            ISC, "6° Semestre", "L19010001", "15/08/2019", true,
            new String[]{"Ingeniería de Software – 6A", "Bases de Datos – 4B"},
            new String[][]{
                {"Proyecto Final de Semestre", "EN_CURSO",   "30/05/2025"},
                {"Reporte de Investigación",   "EN_CURSO",   "25/05/2025"},
                {"Diseño de Arquitectura SW",  "PENDIENTE",  "15/06/2025"},
            }),
        new DetalleAlumno(2, "Jorge Hernández", "JH", "j.hernandez@tec.mx",
            ISC, "6° Semestre", "L19010002", "15/08/2019", true,
            new String[]{"Ingeniería de Software – 6A"},
            new String[][]{
                {"Proyecto Final de Semestre", "EN_CURSO",   "30/05/2025"},
                {"Reporte de Investigación",   "PENDIENTE",  "25/05/2025"},
                {"Diseño de Arquitectura SW",  "PENDIENTE",  "15/06/2025"},
            }),
        new DetalleAlumno(3, "Ana Laura Méndez", "AM", "a.mendez@tec.mx",
            ISC, "6° Semestre", "L19010003", "15/08/2019", true,
            new String[]{"Ingeniería de Software – 6A"},
            new String[][]{
                {"Proyecto Final de Semestre", "EN_CURSO",   "30/05/2025"},
                {"Diseño de Arquitectura SW",  "PENDIENTE",  "15/06/2025"},
                {"Reporte de Investigación",   "EN_CURSO",   "25/05/2025"},
            }),
        new DetalleAlumno(4, "Roberto Sánchez", "RS", "r.sanchez@tec.mx",
            ISC, "6° Semestre", "L19010004", "15/08/2019", true,
            new String[]{"Ingeniería de Software – 6A"},
            new String[][]{
                {"Proyecto Final de Semestre", "PENDIENTE",  "30/05/2025"},
                {"Diseño de Arquitectura SW",  "PENDIENTE",  "15/06/2025"},
                {"Reporte de Investigación",   "EN_CURSO",   "25/05/2025"},
            }),
        new DetalleAlumno(5, "Daniela Ortiz", "DO", "d.ortiz@tec.mx",
            ISC, "6° Semestre", "L19010005", "15/08/2019", true,
            new String[]{"Ingeniería de Software – 6A"},
            new String[][]{
                {"Proyecto Final de Semestre", "EN_CURSO",   "30/05/2025"},
                {"Reporte de Investigación",   "EN_CURSO",   "25/05/2025"},
                {"Diseño de Arquitectura SW",  "PENDIENTE",  "15/06/2025"},
            }),
        new DetalleAlumno(6, "Samuel Vega", "SV", "s.vega@tec.mx",
            ISC, "6° Semestre", "L19010006", "15/08/2019", true,
            new String[]{"Ingeniería de Software – 6A"},
            new String[][]{
                {"Proyecto Final de Semestre", "EN_CURSO",   "30/05/2025"},
                {"Reporte de Investigación",   "COMPLETADA", "25/05/2025"},
                {"Diseño de Arquitectura SW",  "PENDIENTE",  "15/06/2025"},
            }),
        new DetalleAlumno(7, "Fernanda López", "FL", "f.lopez@tec.mx",
            ISC, "6° Semestre", "L19010007", "15/08/2019", true,
            new String[]{"Ingeniería de Software – 6A"},
            new String[][]{
                {"Proyecto Final de Semestre", "COMPLETADA", "30/05/2025"},
                {"Reporte de Investigación",   "EN_CURSO",   "25/05/2025"},
                {"Diseño de Arquitectura SW",  "PENDIENTE",  "15/06/2025"},
            }),
        new DetalleAlumno(8, "Carlos Ruiz", "CR", "c.ruiz@tec.mx",
            ISC, "6° Semestre", "L19010008", "15/08/2019", true,
            new String[]{"Ingeniería de Software – 6A"},
            new String[][]{
                {"Proyecto Final de Semestre", "COMPLETADA", "30/05/2025"},
                {"Reporte de Investigación",   "EN_CURSO",   "25/05/2025"},
                {"Diseño de Arquitectura SW",  "PENDIENTE",  "15/06/2025"},
            }),

        // ── Grupo 2: Bases de Datos – 4B ──────────────────────────────────
        new DetalleAlumno(11, "Luis Pérez", "LP", "l.perez@tec.mx",
            ISC, "4° Semestre", "L21020001", "15/08/2021", true,
            new String[]{"Bases de Datos – 4B"},
            new String[][]{
                {"Parcial 2 – Bases de Datos",  "COMPLETADA", "15/04/2025"},
                {"Ensayo de Ética Profesional", "PENDIENTE",  "20/05/2025"},
                {"Examen Final Cálculo II",      "VENCIDA",    "10/04/2025"},
            }),
        new DetalleAlumno(12, "Sofia Torres", "ST", "s.torres@tec.mx",
            ISC, "4° Semestre", "L21020002", "15/08/2021", true,
            new String[]{"Bases de Datos – 4B"},
            new String[][]{
                {"Parcial 2 – Bases de Datos",  "COMPLETADA", "15/04/2025"},
                {"Ensayo de Ética Profesional", "PENDIENTE",  "20/05/2025"},
                {"Examen Final Cálculo II",      "COMPLETADA", "10/04/2025"},
            }),
        new DetalleAlumno(13, "Diego Martínez", "DM", "d.martinez@tec.mx",
            ISC, "4° Semestre", "L21020003", "15/08/2021", true,
            new String[]{"Bases de Datos – 4B"},
            new String[][]{
                {"Parcial 2 – Bases de Datos",  "COMPLETADA", "15/04/2025"},
                {"Ensayo de Ética Profesional", "PENDIENTE",  "20/05/2025"},
                {"Examen Final Cálculo II",      "COMPLETADA", "10/04/2025"},
            }),
        new DetalleAlumno(14, "Valeria Cruz", "VC", "v.cruz@tec.mx",
            ISC, "4° Semestre", "L21020004", "15/08/2021", true,
            new String[]{"Bases de Datos – 4B"},
            new String[][]{
                {"Parcial 2 – Bases de Datos",  "COMPLETADA", "15/04/2025"},
                {"Ensayo de Ética Profesional", "PENDIENTE",  "20/05/2025"},
                {"Examen Final Cálculo II",      "COMPLETADA", "10/04/2025"},
            }),
        new DetalleAlumno(15, "Pablo Guerrero", "PG", "p.guerrero@tec.mx",
            ISC, "4° Semestre", "L21020005", "15/08/2021", true,
            new String[]{"Bases de Datos – 4B"},
            new String[][]{
                {"Parcial 2 – Bases de Datos",  "COMPLETADA", "15/04/2025"},
                {"Ensayo de Ética Profesional", "PENDIENTE",  "20/05/2025"},
                {"Examen Final Cálculo II",      "VENCIDA",    "10/04/2025"},
            }),
        new DetalleAlumno(16, "Isabel Hernández", "IH", "i.hernandez@tec.mx",
            ISC, "4° Semestre", "L21020006", "15/08/2021", true,
            new String[]{"Bases de Datos – 4B"},
            new String[][]{
                {"Parcial 2 – Bases de Datos",  "COMPLETADA", "15/04/2025"},
                {"Ensayo de Ética Profesional", "PENDIENTE",  "20/05/2025"},
                {"Examen Final Cálculo II",      "VENCIDA",    "10/04/2025"},
            }),

        // ── Grupo 3: Redes de Computadoras – 5C ───────────────────────────
        new DetalleAlumno(21, "Carlos Ruiz", "CR", "c.ruiz2@tec.mx",
            IRT, "5° Semestre", "L20030001", "15/08/2020", true,
            new String[]{"Redes de Computadoras – 5C"},
            new String[][]{
                {"Práctica de Laboratorio #3",  "EN_CURSO",   "05/05/2025"},
                {"Proyecto Final de Semestre",  "COMPLETADA", "20/03/2025"},
                {"Ensayo de Ética Profesional", "PENDIENTE",  "20/05/2025"},
            }),
        new DetalleAlumno(22, "Fernanda López", "FL", "f.lopez2@tec.mx",
            IRT, "5° Semestre", "L20030002", "15/08/2020", true,
            new String[]{"Redes de Computadoras – 5C"},
            new String[][]{
                {"Práctica de Laboratorio #3",  "PENDIENTE",  "05/05/2025"},
                {"Proyecto Final de Semestre",  "COMPLETADA", "20/03/2025"},
                {"Ensayo de Ética Profesional", "PENDIENTE",  "20/05/2025"},
            }),
        new DetalleAlumno(23, "Miguel Ángel Díaz", "MD", "m.diaz@tec.mx",
            IRT, "5° Semestre", "L20030003", "15/08/2020", true,
            new String[]{"Redes de Computadoras – 5C"},
            new String[][]{
                {"Práctica de Laboratorio #3",  "EN_CURSO",   "05/05/2025"},
                {"Reporte de Investigación",    "COMPLETADA", "25/05/2025"},
                {"Ensayo de Ética Profesional", "PENDIENTE",  "20/05/2025"},
            }),
        new DetalleAlumno(24, "Patricia Morales", "PM", "p.morales@tec.mx",
            IRT, "5° Semestre", "L20030004", "15/08/2020", true,
            new String[]{"Redes de Computadoras – 5C"},
            new String[][]{
                {"Práctica de Laboratorio #3",  "PENDIENTE",  "05/05/2025"},
                {"Reporte de Investigación",    "PENDIENTE",  "25/05/2025"},
                {"Ensayo de Ética Profesional", "PENDIENTE",  "20/05/2025"},
            }),
        new DetalleAlumno(25, "Ricardo Jiménez", "RJ", "r.jimenez@tec.mx",
            IRT, "5° Semestre", "L20030005", "15/08/2020", true,
            new String[]{"Redes de Computadoras – 5C"},
            new String[][]{
                {"Práctica de Laboratorio #3",  "EN_CURSO",   "05/05/2025"},
                {"Reporte de Investigación",    "EN_CURSO",   "25/05/2025"},
                {"Ensayo de Ética Profesional", "PENDIENTE",  "20/05/2025"},
            }),
        new DetalleAlumno(26, "Claudia Reyes", "CR", "c.reyes@tec.mx",
            IRT, "5° Semestre", "L20030006", "15/08/2020", false,
            new String[]{"Redes de Computadoras – 5C"},
            new String[][]{
                {"Práctica de Laboratorio #3",  "VENCIDA",    "05/05/2025"},
                {"Reporte de Investigación",    "VENCIDA",    "25/05/2025"},
                {"Ensayo de Ética Profesional", "PENDIENTE",  "20/05/2025"},
            }),

        // ── Grupo 4: Cálculo Integral – 2A ────────────────────────────────
        new DetalleAlumno(31, "Andrea Castro", "AC", "a.castro@tec.mx",
            ISC, "2° Semestre", "L23040001", "15/08/2023", true,
            new String[]{"Cálculo Integral – 2A"},
            new String[][]{
                {"Examen Final Cálculo II",      "VENCIDA",    "10/04/2025"},
                {"Ensayo de Ética Profesional", "PENDIENTE",  "20/05/2025"},
                {"Parcial 2 – Bases de Datos",  "COMPLETADA", "15/04/2025"},
            }),
        new DetalleAlumno(32, "Héctor Mendoza", "HM", "h.mendoza@tec.mx",
            ISC, "2° Semestre", "L23040002", "15/08/2023", true,
            new String[]{"Cálculo Integral – 2A"},
            new String[][]{
                {"Examen Final Cálculo II",      "COMPLETADA", "10/04/2025"},
                {"Ensayo de Ética Profesional", "PENDIENTE",  "20/05/2025"},
                {"Parcial 2 – Bases de Datos",  "COMPLETADA", "15/04/2025"},
            }),
        new DetalleAlumno(33, "Lucía Flores", "LF", "l.flores@tec.mx",
            ISC, "2° Semestre", "L23040003", "15/08/2023", true,
            new String[]{"Cálculo Integral – 2A"},
            new String[][]{
                {"Examen Final Cálculo II",      "VENCIDA",    "10/04/2025"},
                {"Ensayo de Ética Profesional", "PENDIENTE",  "20/05/2025"},
                {"Parcial 2 – Bases de Datos",  "COMPLETADA", "15/04/2025"},
            }),
        new DetalleAlumno(34, "Pablo Guerrero", "PG", "p.guerrero2@tec.mx",
            ISC, "2° Semestre", "L23040004", "15/08/2023", true,
            new String[]{"Cálculo Integral – 2A"},
            new String[][]{
                {"Examen Final Cálculo II",      "COMPLETADA", "10/04/2025"},
                {"Ensayo de Ética Profesional", "PENDIENTE",  "20/05/2025"},
                {"Parcial 2 – Bases de Datos",  "COMPLETADA", "15/04/2025"},
            }),
        new DetalleAlumno(35, "Isabel Hernández", "IH", "i.hernandez2@tec.mx",
            ISC, "2° Semestre", "L23040005", "15/08/2023", true,
            new String[]{"Cálculo Integral – 2A"},
            new String[][]{
                {"Examen Final Cálculo II",      "VENCIDA",    "10/04/2025"},
                {"Ensayo de Ética Profesional", "PENDIENTE",  "20/05/2025"},
                {"Parcial 2 – Bases de Datos",  "COMPLETADA", "15/04/2025"},
            }),
        new DetalleAlumno(36, "Ricardo Jiménez", "RJ", "r.jimenez2@tec.mx",
            ISC, "2° Semestre", "L23040006", "15/08/2023", true,
            new String[]{"Cálculo Integral – 2A"},
            new String[][]{
                {"Examen Final Cálculo II",      "COMPLETADA", "10/04/2025"},
                {"Ensayo de Ética Profesional", "PENDIENTE",  "20/05/2025"},
                {"Parcial 2 – Bases de Datos",  "PENDIENTE",  "15/04/2025"},
            }),
        new DetalleAlumno(37, "Andrés Vargas", "AV", "a.vargas@tec.mx",
            ISC, "2° Semestre", "L23040007", "15/08/2023", true,
            new String[]{"Cálculo Integral – 2A"},
            new String[][]{
                {"Examen Final Cálculo II",      "VENCIDA",    "10/04/2025"},
                {"Ensayo de Ética Profesional", "PENDIENTE",  "20/05/2025"},
                {"Parcial 2 – Bases de Datos",  "PENDIENTE",  "15/04/2025"},
            }),

        // ── Grupo 5: Programación Orientada – 3D ──────────────────────────
        new DetalleAlumno(41, "Gabriela Luna", "GL", "g.luna@tec.mx",
            IS, "3° Semestre", "L22050001", "15/08/2022", true,
            new String[]{"Programación Orientada – 3D"},
            new String[][]{
                {"Taller de Programación OO",   "COMPLETADA", "20/03/2025"},
                {"Práctica de Laboratorio #3",  "COMPLETADA", "05/05/2025"},
                {"Ensayo de Ética Profesional", "PENDIENTE",  "20/05/2025"},
            }),
        new DetalleAlumno(42, "Óscar Domínguez", "OD", "o.dominguez@tec.mx",
            IS, "3° Semestre", "L22050002", "15/08/2022", true,
            new String[]{"Programación Orientada – 3D"},
            new String[][]{
                {"Taller de Programación OO",   "COMPLETADA", "20/03/2025"},
                {"Práctica de Laboratorio #3",  "COMPLETADA", "05/05/2025"},
                {"Ensayo de Ética Profesional", "PENDIENTE",  "20/05/2025"},
            }),
        new DetalleAlumno(43, "Natalia Ramírez", "NR", "n.ramirez@tec.mx",
            IS, "3° Semestre", "L22050003", "15/08/2022", true,
            new String[]{"Programación Orientada – 3D"},
            new String[][]{
                {"Taller de Programación OO",   "COMPLETADA", "20/03/2025"},
                {"Práctica de Laboratorio #3",  "EN_CURSO",   "05/05/2025"},
                {"Ensayo de Ética Profesional", "PENDIENTE",  "20/05/2025"},
            }),
        new DetalleAlumno(44, "Emmanuel Torres", "ET", "e.torres@tec.mx",
            IS, "3° Semestre", "L22050004", "15/08/2022", true,
            new String[]{"Programación Orientada – 3D"},
            new String[][]{
                {"Taller de Programación OO",   "COMPLETADA", "20/03/2025"},
                {"Práctica de Laboratorio #3",  "EN_CURSO",   "05/05/2025"},
                {"Ensayo de Ética Profesional", "PENDIENTE",  "20/05/2025"},
            }),
        new DetalleAlumno(45, "Andrés Vargas", "AV", "a.vargas2@tec.mx",
            IS, "3° Semestre", "L22050005", "15/08/2022", true,
            new String[]{"Programación Orientada – 3D"},
            new String[][]{
                {"Taller de Programación OO",   "COMPLETADA", "20/03/2025"},
                {"Práctica de Laboratorio #3",  "PENDIENTE",  "05/05/2025"},
                {"Ensayo de Ética Profesional", "PENDIENTE",  "20/05/2025"},
            }),
        new DetalleAlumno(46, "Claudia Reyes", "CR", "c.reyes2@tec.mx",
            IS, "3° Semestre", "L22050006", "15/08/2022", true,
            new String[]{"Programación Orientada – 3D"},
            new String[][]{
                {"Taller de Programación OO",   "COMPLETADA", "20/03/2025"},
                {"Práctica de Laboratorio #3",  "EN_CURSO",   "05/05/2025"},
                {"Ensayo de Ética Profesional", "PENDIENTE",  "20/05/2025"},
            }),

        // ── Grupo 6: Sistemas Operativos – 5A ─────────────────────────────
        new DetalleAlumno(51, "Eduardo Morales", "EM", "e.morales@tec.mx",
            ISC, "5° Semestre", "L20060001", "15/08/2020", true,
            new String[]{"Sistemas Operativos – 5A", "Redes de Computadoras – 5C"},
            new String[][]{
                {"Proyecto Final de Semestre",  "EN_CURSO",   "30/05/2025"},
                {"Reporte de Investigación",    "EN_CURSO",   "25/05/2025"},
                {"Parcial 2 – Bases de Datos",  "COMPLETADA", "15/04/2025"},
            }),
        new DetalleAlumno(52, "Carmen Vázquez", "CV", "c.vazquez@tec.mx",
            ISC, "5° Semestre", "L20060002", "15/08/2020", true,
            new String[]{"Sistemas Operativos – 5A"},
            new String[][]{
                {"Proyecto Final de Semestre",  "EN_CURSO",   "30/05/2025"},
                {"Reporte de Investigación",    "COMPLETADA", "25/05/2025"},
                {"Parcial 2 – Bases de Datos",  "COMPLETADA", "15/04/2025"},
            }),
        new DetalleAlumno(53, "Rodrigo Fuentes", "RF", "r.fuentes@tec.mx",
            ISC, "5° Semestre", "L20060003", "15/08/2020", true,
            new String[]{"Sistemas Operativos – 5A"},
            new String[][]{
                {"Proyecto Final de Semestre",  "COMPLETADA", "30/05/2025"},
                {"Reporte de Investigación",    "COMPLETADA", "25/05/2025"},
                {"Parcial 2 – Bases de Datos",  "COMPLETADA", "15/04/2025"},
            }),
        new DetalleAlumno(54, "Paola Serrano", "PS", "p.serrano@tec.mx",
            ISC, "5° Semestre", "L20060004", "15/08/2020", true,
            new String[]{"Sistemas Operativos – 5A"},
            new String[][]{
                {"Proyecto Final de Semestre",  "EN_CURSO",   "30/05/2025"},
                {"Reporte de Investigación",    "EN_CURSO",   "25/05/2025"},
                {"Parcial 2 – Bases de Datos",  "COMPLETADA", "15/04/2025"},
            }),
        new DetalleAlumno(55, "Antonio Ríos", "AR", "a.rios@tec.mx",
            ISC, "5° Semestre", "L20060005", "15/08/2020", true,
            new String[]{"Sistemas Operativos – 5A"},
            new String[][]{
                {"Proyecto Final de Semestre",  "PENDIENTE",  "30/05/2025"},
                {"Reporte de Investigación",    "PENDIENTE",  "25/05/2025"},
                {"Parcial 2 – Bases de Datos",  "COMPLETADA", "15/04/2025"},
            }),
        new DetalleAlumno(56, "Mariana Peña", "MP", "m.pena@tec.mx",
            ISC, "5° Semestre", "L20060006", "15/08/2020", true,
            new String[]{"Sistemas Operativos – 5A"},
            new String[][]{
                {"Proyecto Final de Semestre",  "EN_CURSO",   "30/05/2025"},
                {"Reporte de Investigación",    "EN_CURSO",   "25/05/2025"},
                {"Parcial 2 – Bases de Datos",  "COMPLETADA", "15/04/2025"},
            }),
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAlumnoDetalleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int alumnoId = getArguments() != null ? getArguments().getInt("usuarioId", 1) : 1;
        cargarDatos(buscarPorId(alumnoId));
        binding.btnVolverAlumno.setOnClickListener(v -> requireActivity().onBackPressed());
    }

    private DetalleAlumno buscarPorId(int id) {
        for (DetalleAlumno a : DATOS) {
            if (a.id == id) return a;
        }
        return DATOS[0];
    }

    private void cargarDatos(DetalleAlumno a) {
        binding.tvInicialesAlumno.setText(a.iniciales);
        binding.tvNombreAlumno.setText(a.nombre);
        binding.tvCorreoAlumno.setText(a.correo);
        binding.tvCarreraAlumno.setText(a.carrera);

        binding.tvMatricula.setText(a.matricula);
        binding.tvSemestre.setText(a.semestre);
        binding.tvFechaInscripcion.setText(a.fechaInscripcion);
        binding.tvEstadoCuentaAlumno.setText(a.cuentaActiva ? "Activa" : "Desactivada");
        binding.tvEstadoCuentaAlumno.setTextColor(ContextCompat.getColor(requireContext(),
                a.cuentaActiva ? R.color.colorSuccess : R.color.colorError));

        cargarGrupos(a);
        cargarEntregas(a);
    }

    private void cargarGrupos(DetalleAlumno a) {
        binding.containerGruposAlumno.removeAllViews();
        for (String grupo : a.grupos) {
            TextView tv = new TextView(requireContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.bottomMargin = dpToPx(8);
            tv.setLayoutParams(lp);
            tv.setText("• " + grupo);
            tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOnSurface));
            tv.setTextSize(13f);
            binding.containerGruposAlumno.addView(tv);
        }
    }

    private void cargarEntregas(DetalleAlumno a) {
        binding.containerEntregasAlumno.removeAllViews();
        for (String[] entrega : a.entregas) {
            String titulo = entrega[0];
            String estado = entrega[1];
            String fecha  = entrega[2];

            LinearLayout fila = new LinearLayout(requireContext());
            fila.setOrientation(LinearLayout.HORIZONTAL);
            fila.setGravity(android.view.Gravity.CENTER_VERTICAL);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.bottomMargin = dpToPx(12);
            fila.setLayoutParams(lp);

            // Columna izquierda: título + fecha (peso=1)
            LinearLayout columnaIzq = new LinearLayout(requireContext());
            columnaIzq.setOrientation(LinearLayout.VERTICAL);
            columnaIzq.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            TextView tvTitulo = new TextView(requireContext());
            tvTitulo.setText(titulo);
            tvTitulo.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOnSurface));
            tvTitulo.setTextSize(13f);
            tvTitulo.setMaxLines(1);
            tvTitulo.setEllipsize(TextUtils.TruncateAt.END);

            TextView tvFecha = new TextView(requireContext());
            tvFecha.setText("Vence: " + fecha);
            tvFecha.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOnSurfaceVariant));
            tvFecha.setTextSize(11f);

            columnaIzq.addView(tvTitulo);
            columnaIzq.addView(tvFecha);

            // Chip de estado
            TextView tvChip = new TextView(requireContext());
            tvChip.setText(etiquetaEstado(estado));
            tvChip.setTextSize(10f);
            tvChip.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            tvChip.setTypeface(null, Typeface.BOLD);
            tvChip.setPadding(dpToPx(8), dpToPx(3), dpToPx(8), dpToPx(3));
            tvChip.setBackgroundResource(chipParaEstado(estado));
            LinearLayout.LayoutParams chipLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            chipLp.leftMargin = dpToPx(8);
            tvChip.setLayoutParams(chipLp);

            fila.addView(columnaIzq);
            fila.addView(tvChip);
            binding.containerEntregasAlumno.addView(fila);
        }
    }

    private String etiquetaEstado(String estado) {
        switch (estado) {
            case "COMPLETADA": return "Entregado";
            case "VENCIDA":    return "Vencida";
            case "PENDIENTE":  return "Pendiente";
            default:           return "En curso";
        }
    }

    private int chipParaEstado(String estado) {
        switch (estado) {
            case "COMPLETADA": return R.drawable.bg_chip_estudiante;
            case "VENCIDA":    return R.drawable.bg_chip_admin;
            case "PENDIENTE":  return R.drawable.bg_chip_pendiente;
            default:           return R.drawable.bg_chip_docente;
        }
    }

    private int dpToPx(int dp) {
        return Math.round(dp * requireContext().getResources().getDisplayMetrics().density);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
