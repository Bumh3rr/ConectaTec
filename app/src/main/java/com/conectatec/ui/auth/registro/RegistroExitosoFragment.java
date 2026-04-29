package com.conectatec.ui.auth.registro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.conectatec.R;
import com.conectatec.databinding.FragmentRegistroExitosoBinding;
import com.conectatec.ui.auth.RegistroActivity;

/**
 * Paso 4a — Registro exitoso.
 * Muestra confirmación y resumen de datos del usuario.
 * Al presionar Continuar va a WaitingApprovalActivity.
 */
public class RegistroExitosoFragment extends Fragment {

    private FragmentRegistroExitosoBinding binding;
    private RegistroViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRegistroExitosoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(RegistroViewModel.class);

        // Mostrar datos del usuario en el resumen
        binding.tvNombreResumen.setText(viewModel.getNombre());
        binding.tvCorreoResumen.setText(viewModel.getCorreo());

        binding.btnContinuar.setOnClickListener(v -> {
            // La Activity maneja la navegación a WaitingApproval
            ((RegistroActivity) requireActivity()).irAEspera();
        });

        animation(binding.imgCelebration, R.raw.confeti);
    }

    private void animation(LottieAnimationView lottieAnimationView, Integer animation) {
        requireActivity().runOnUiThread(() -> {
            var animate = new TranslateAnimation(0f, 0f, 0f, 5f);
            animate.setDuration(400);
            animate.setFillAfter(true);
            animate.setInterpolator(new AccelerateDecelerateInterpolator());
            lottieAnimationView.startAnimation(animate);
            lottieAnimationView.setAnimation(animation);
            lottieAnimationView.playAnimation();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}