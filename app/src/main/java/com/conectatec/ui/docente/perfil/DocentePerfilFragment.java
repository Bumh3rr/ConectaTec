package com.conectatec.ui.docente.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.conectatec.databinding.FragmentDocentePerfilPlaceholderBinding;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Stub del Perfil del Docente. Sera reemplazado por la implementacion real
 * en el plan 2 (dashboard-perfil). Solo muestra un texto centrado.
 */
@AndroidEntryPoint
public class DocentePerfilFragment extends Fragment {

    private FragmentDocentePerfilPlaceholderBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocentePerfilPlaceholderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
