package com.conectatec.ui.auth.registro;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.conectatec.R;
import com.conectatec.databinding.FragmentRegistroFotoBinding;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegistroFotoFragment extends Fragment {

    private FragmentRegistroFotoBinding binding;
    private RegistroViewModel viewModel;
    private Uri fotoUri;

    private final ActivityResultLauncher<String> galeriaLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    fotoUri = uri;
                    mostrarFotoSeleccionada(uri);
                }
            });

    private final ActivityResultLauncher<Uri> camaraLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
                if (success && fotoUri != null) {
                    mostrarFotoSeleccionada(fotoUri);
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRegistroFotoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(RegistroViewModel.class);

        if (viewModel.getFotoUri() != null) {
            fotoUri = viewModel.getFotoUri();
            mostrarFotoSeleccionada(fotoUri);
        }

        setupListeners();
    }

    private void setupListeners() {
        binding.btnTomarFoto.setOnClickListener(v -> {
//            fotoUri = crearUriParaFoto();
//            camaraLauncher.launch(fotoUri);
            binding.btnContinuar.setEnabled(true);
            binding.btnContinuar.setAlpha(1.0f);
        });

        binding.btnGaleria.setOnClickListener(v ->
                galeriaLauncher.launch("image/*")
        );

        binding.btnContinuar.setOnClickListener(v -> {
            viewModel.setFotoUri(fotoUri);
            iniciarAnimacionEscaneo();
        });
    }

    private void mostrarFotoSeleccionada(Uri uri) {
        binding.ivFotoPreview.setImageURI(uri);
        binding.ivFotoPreview.setVisibility(View.VISIBLE);
        binding.ivFotoPlaceholder.setVisibility(View.GONE);
        binding.tvEstadoFoto.setText("Foto lista ✓");
        binding.tvEstadoFoto.setTextColor(requireContext().getColor(R.color.colorSuccess));
        binding.btnContinuar.setEnabled(true);
        binding.btnContinuar.setAlpha(1.0f);
    }

    private void iniciarAnimacionEscaneo() {
        // Deshabilitar controles durante el escaneo
        binding.btnContinuar.setEnabled(false);
        binding.btnTomarFoto.setEnabled(false);
        binding.btnGaleria.setEnabled(false);

        // Mostrar elementos de escaneo
        binding.viewScanLine.setVisibility(View.VISIBLE);
        binding.overlayEscaneo.setVisibility(View.VISIBLE);
        binding.tvEstadoFoto.setText("Verificando identidad...");
        binding.tvEstadoFoto.setTextColor(requireContext().getColor(R.color.colorPrimary));

        float halfCircle = 96 * requireContext().getResources().getDisplayMetrics().density;

        // Animación de la línea de escaneo: 3 pasadas de arriba a abajo
        ObjectAnimator scanLine = ObjectAnimator.ofFloat(
                binding.viewScanLine, "translationY", -halfCircle, halfCircle);
        scanLine.setDuration(900);
        scanLine.setRepeatCount(2);
        scanLine.setInterpolator(new LinearInterpolator());

        // Pulsación de las esquinas del marco durante el escaneo
        ObjectAnimator cornersPulse = ObjectAnimator.ofFloat(
                binding.ivScanFrame, "alpha", 1f, 0.3f, 1f);
        cornersPulse.setDuration(900);
        cornersPulse.setRepeatCount(2);
        cornersPulse.setInterpolator(new LinearInterpolator());

        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(scanLine, cornersPulse);

        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isAdded()) return;

                // Ocultar elementos de escaneo
                binding.viewScanLine.setVisibility(View.GONE);
                binding.overlayEscaneo.setVisibility(View.GONE);
                binding.ivScanFrame.setAlpha(1f);

                // Flash de éxito en las esquinas
                ObjectAnimator successFlash = ObjectAnimator.ofFloat(
                        binding.ivScanFrame, "alpha", 0f, 1f);
                successFlash.setDuration(400);
                successFlash.start();

                // Actualizar estado a verificado
                binding.tvEstadoFoto.setText("Identidad verificada ✓");
                binding.tvEstadoFoto.setTextColor(
                        requireContext().getColor(R.color.colorSuccess));

                // Navegar tras breve pausa
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    if (isAdded()) {
                        Navigation.findNavController(requireView())
                                .navigate(R.id.action_foto_to_verificando);
                    }
                }, 600);
            }
        });

        animSet.start();
    }

    private Uri crearUriParaFoto() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(new Date());
        File archivo = new File(
                requireContext().getExternalFilesDir("Pictures"),
                "foto_" + timestamp + ".jpg"
        );
        return FileProvider.getUriForFile(
                requireContext(),
                requireContext().getPackageName() + ".fileprovider",
                archivo
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
