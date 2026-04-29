package com.conectatec.ui.auth.registro;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

/**
 * Paso 2 del flujo de registro.
 * El usuario toma o selecciona su foto de perfil.
 * Guarda la URI en RegistroViewModel y navega al paso 3.
 */
public class RegistroFotoFragment extends Fragment {

    private FragmentRegistroFotoBinding binding;
    private RegistroViewModel viewModel;
    private Uri fotoUri;

    // Launcher galería
    private final ActivityResultLauncher<String> galeriaLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    fotoUri = uri;
                    mostrarFotoSeleccionada(uri);
                }
            });

    // Launcher cámara
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

        // Si ya había una foto seleccionada (por si regresa al paso)
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
            Navigation.findNavController(v)
                    .navigate(R.id.action_foto_to_verificando);
        });
    }

    /**
     * Muestra la foto en el preview y habilita el botón continuar.
     */
    private void mostrarFotoSeleccionada(Uri uri) {
        binding.ivFotoPreview.setImageURI(uri);
        binding.ivFotoPreview.setBorderColor(
                requireContext().getColor(R.color.colorPrimary)
        );
        binding.tvEstadoFoto.setText("Foto lista ✓");
        binding.tvEstadoFoto.setTextColor(
                requireContext().getColor(R.color.colorSuccess)
        );
        // Habilitar botón continuar
        binding.btnContinuar.setEnabled(true);
        binding.btnContinuar.setAlpha(1.0f);
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