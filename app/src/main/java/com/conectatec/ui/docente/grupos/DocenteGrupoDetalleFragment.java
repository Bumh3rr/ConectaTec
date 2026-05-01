package com.conectatec.ui.docente.grupos;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.conectatec.R;
import com.conectatec.data.model.Grupo;
import com.conectatec.databinding.FragmentDocenteGrupoDetalleBinding;
import com.conectatec.ui.common.UiState;
import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteGrupoDetalleFragment extends Fragment {

    private static final String[][] MIEMBROS_PREVIEW = {
            {"Ana García", "Laura Méndez", "Carlos Torres"},
            {"Pedro Ruiz", "María López", "Jorge Sánchez"},
            {"Sofía Castro", "Luis Herrera", "Elena Vargas"}
    };

    private static final String[][] AVISOS_PREVIEW = {
            {"Cambio de horario – Lunes 8:00 a 9:00", "Material adicional en Classroom"},
            {"Examen parcial el viernes", "Revisar capítulos 5 y 6"},
            {"Entrega del proyecto final – 30/05", "No habrá clase el jueves"}
    };

    private static final int[] TOTAL_TAREAS = {5, 4, 3};

    private FragmentDocenteGrupoDetalleBinding binding;
    private DocenteGrupoDetalleViewModel viewModel;
    private int grupoId;
    private Grupo grupoActual;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteGrupoDetalleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        grupoId = getArguments() != null ? getArguments().getInt("grupoId", 1) : 1;
        prepararAnimacion();
        viewModel = new ViewModelProvider(this).get(DocenteGrupoDetalleViewModel.class);
        observeViewModel();
        viewModel.cargarDetalle(grupoId);
        setupListeners();
    }

    private void observeViewModel() {
        viewModel.getState().observe(getViewLifecycleOwner(), state -> {
            if (state instanceof UiState.Success) {
                Grupo grupo = ((UiState.Success<Grupo>) state).data;
                poblarVistas(grupo);
            } else if (state instanceof UiState.Error) {
                Snackbar.make(binding.getRoot(),
                        ((UiState.Error<?>) state).mensaje, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void poblarVistas(Grupo grupo) {
        grupoActual = grupo;
        binding.tvInicialesHeroGrupo.setText(iniciales(grupo.nombre));
        binding.tvNombreHeroGrupo.setText(grupo.nombre);
        binding.tvMateriaHeroGrupo.setText(grupo.materia);
        binding.tvCodigoHeroGrupo.setText(grupo.codigoUnion);
        binding.tvFechaHeroGrupo.setText("Desde " + grupo.fechaCreacion);
        binding.tvAlumnosHeroNum.setText(String.valueOf(grupo.totalAlumnos));

        int idx = Math.max(0, Math.min(grupoId - 1, TOTAL_TAREAS.length - 1));
        binding.tvTareasHeroNum.setText(String.valueOf(TOTAL_TAREAS[idx]));
        cargarMiembrosPreview(MIEMBROS_PREVIEW[idx]);
        cargarAvisosPreview(AVISOS_PREVIEW[idx]);
        animarEntrada();
    }

    private void cargarMiembrosPreview(String[] nombres) {
        binding.containerMiembrosGrupoDocente.removeAllViews();
        for (int i = 0; i < nombres.length; i++) {
            if (i > 0) {
                View div = new View(requireContext());
                LinearLayout.LayoutParams dp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(1));
                dp.setMargins(dpToPx(52), dpToPx(8), 0, dpToPx(8));
                div.setLayoutParams(dp);
                div.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorDivider));
                binding.containerMiembrosGrupoDocente.addView(div);
            }

            String nombre = nombres[i];
            LinearLayout row = new LinearLayout(requireContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setPadding(0, dpToPx(4), 0, dpToPx(4));
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            FrameLayout frame = new FrameLayout(requireContext());
            int size = dpToPx(40);
            LinearLayout.LayoutParams fp = new LinearLayout.LayoutParams(size, size);
            fp.setMarginEnd(dpToPx(12));
            frame.setLayoutParams(fp);
            frame.setBackgroundResource(R.drawable.bg_avatar_placeholder);

            TextView tvInic = new TextView(requireContext());
            FrameLayout.LayoutParams ip = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            ip.gravity = Gravity.CENTER;
            tvInic.setLayoutParams(ip);
            tvInic.setText(iniciales(nombre));
            tvInic.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOnSurface));
            tvInic.setTextSize(11f);
            tvInic.setTypeface(null, Typeface.BOLD);
            frame.addView(tvInic);
            row.addView(frame);

            LinearLayout nameCol = new LinearLayout(requireContext());
            nameCol.setOrientation(LinearLayout.VERTICAL);
            nameCol.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            TextView tvName = new TextView(requireContext());
            tvName.setText(nombre);
            tvName.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOnSurface));
            tvName.setTextSize(13f);
            tvName.setTypeface(null, Typeface.BOLD);
            nameCol.addView(tvName);

            TextView tvRol = new TextView(requireContext());
            tvRol.setText("Alumno");
            tvRol.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOnSurfaceVariant));
            tvRol.setTextSize(11f);
            nameCol.addView(tvRol);

            row.addView(nameCol);
            binding.containerMiembrosGrupoDocente.addView(row);
        }
    }

    private void cargarAvisosPreview(String[] avisos) {
        binding.containerAvisosGrupoDocente.removeAllViews();
        for (int i = 0; i < avisos.length; i++) {
            if (i > 0) {
                View div = new View(requireContext());
                LinearLayout.LayoutParams dp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(1));
                dp.setMargins(dpToPx(30), dpToPx(8), 0, dpToPx(8));
                div.setLayoutParams(dp);
                div.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorDivider));
                binding.containerAvisosGrupoDocente.addView(div);
            }

            String aviso = avisos[i];
            LinearLayout row = new LinearLayout(requireContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setPadding(0, dpToPx(4), 0, dpToPx(4));
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            ImageView icon = new ImageView(requireContext());
            int iconSize = dpToPx(18);
            LinearLayout.LayoutParams iconP = new LinearLayout.LayoutParams(iconSize, iconSize);
            iconP.setMarginEnd(dpToPx(10));
            icon.setLayoutParams(iconP);
            icon.setImageResource(R.drawable.ic_info);
            icon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorPrimary),
                    PorterDuff.Mode.SRC_IN);
            row.addView(icon);

            TextView tvAviso = new TextView(requireContext());
            tvAviso.setText(aviso);
            tvAviso.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOnSurface));
            tvAviso.setTextSize(13f);
            tvAviso.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            row.addView(tvAviso);

            binding.containerAvisosGrupoDocente.addView(row);
        }
    }

    private void setupListeners() {
        binding.btnBackDetalleGrupo.setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());

        binding.btnCopiarCodigo.setOnClickListener(v -> copiarCodigo());

        binding.btnVerQrGrupo.setOnClickListener(v -> navegarQr(v));

        binding.btnVerMiembrosGrupo.setOnClickListener(v -> navegarMiembros(v));
        binding.tvVerTodosMiembrosDocente.setOnClickListener(v -> navegarMiembros(v));

        binding.btnVerTareasGrupo.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putInt("grupoId", grupoId);
            Navigation.findNavController(v)
                    .navigate(R.id.action_grupo_detalle_to_tareas_grupo, args);
        });

        binding.btnPublicarAviso.setOnClickListener(v ->
                Snackbar.make(binding.getRoot(),
                        "Función próximamente disponible", Snackbar.LENGTH_SHORT).show());
    }

    private void copiarCodigo() {
        if (grupoActual == null) return;
        ClipboardManager cm = (ClipboardManager)
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText("Código", grupoActual.codigoUnion));
            Snackbar.make(binding.getRoot(), "Código copiado", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void navegarQr(View v) {
        if (grupoActual == null) return;
        Bundle args = new Bundle();
        args.putInt("grupoId", grupoId);
        args.putString("codigoUnion",  grupoActual.codigoUnion);
        args.putString("nombreGrupo",  grupoActual.nombre);
        args.putString("materiaGrupo", grupoActual.materia);
        args.putInt("totalAlumnos",    grupoActual.totalAlumnos);
        args.putString("fechaCreacion", grupoActual.fechaCreacion);
        Navigation.findNavController(v).navigate(R.id.action_grupo_detalle_to_qr, args);
    }

    private void navegarMiembros(View v) {
        Bundle args = new Bundle();
        args.putInt("grupoId", grupoId);
        Navigation.findNavController(v)
                .navigate(R.id.action_grupo_detalle_to_miembros, args);
    }

    private void prepararAnimacion() {
        binding.cardHeroGrupo.setAlpha(0f);
        binding.cardAccionesRapidas.setAlpha(0f);
        binding.cardMiembrosGrupo.setAlpha(0f);
        binding.cardAvisosGrupo.setAlpha(0f);
    }

    private void animarEntrada() {
        float translY = dpToPx(36);
        View[] cards = {
                binding.cardHeroGrupo,
                binding.cardAccionesRapidas,
                binding.cardMiembrosGrupo,
                binding.cardAvisosGrupo
        };
        for (int i = 0; i < cards.length; i++) {
            View card = cards[i];
            card.setTranslationY(translY);
            card.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setStartDelay(i * 75L)
                    .setDuration(320)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        }
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    private String iniciales(String nombre) {
        String[] partes = nombre.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(2, partes.length); i++) {
            if (!partes[i].isEmpty()) sb.append(partes[i].charAt(0));
        }
        return sb.toString().toUpperCase();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
