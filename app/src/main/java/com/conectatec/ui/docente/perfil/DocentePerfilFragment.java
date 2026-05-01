package com.conectatec.ui.docente.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.conectatec.databinding.FragmentDocentePerfilBinding;
import com.conectatec.ui.auth.LoginActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocentePerfilFragment extends Fragment {

    private FragmentDocentePerfilBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocentePerfilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepararAnimacion();
        setupListeners();
        animarEntrada();
    }

    private void setupListeners() {
        binding.rowNotificacionesDocente.setOnClickListener(v -> { /* placeholder */ });
        binding.rowTemaDocente.setOnClickListener(v -> { /* placeholder */ });
        binding.rowIdiomaDocente.setOnClickListener(v -> { /* placeholder */ });
        binding.btnCerrarSesionDocente.setOnClickListener(v -> cerrarSesion());
    }

    private void cerrarSesion() {
        // TODO: llamar a SessionService.cerrarSesion()
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().overridePendingTransition(
                android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void prepararAnimacion() {
        binding.cardHeroPerfilDocente.setAlpha(0f);
        binding.cardInfoCuentaDocente.setAlpha(0f);
        binding.cardConfigDocente.setAlpha(0f);
        binding.btnCerrarSesionDocente.setAlpha(0f);
    }

    private void animarEntrada() {
        float translY = getResources().getDisplayMetrics().density * 32;
        View[] views = {
                binding.cardHeroPerfilDocente,
                binding.cardInfoCuentaDocente,
                binding.cardConfigDocente,
                binding.btnCerrarSesionDocente
        };
        for (int i = 0; i < views.length; i++) {
            View card = views[i];
            card.setTranslationY(translY);
            card.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setStartDelay(i * 80L)
                    .setDuration(300)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
