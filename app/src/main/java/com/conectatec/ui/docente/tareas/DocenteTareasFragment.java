package com.conectatec.ui.docente.tareas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.conectatec.databinding.FragmentDocenteTareasPlaceholderBinding;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Stub del modulo Tareas del Docente. Sera reemplazado por la implementacion real
 * en el plan 4 (tareas). Solo muestra un texto centrado.
 */
@AndroidEntryPoint
public class DocenteTareasFragment extends Fragment {

    private FragmentDocenteTareasPlaceholderBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteTareasPlaceholderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
