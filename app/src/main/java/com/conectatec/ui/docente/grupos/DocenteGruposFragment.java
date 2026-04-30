package com.conectatec.ui.docente.grupos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.conectatec.databinding.FragmentDocenteGruposPlaceholderBinding;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Stub del modulo Grupos del Docente. Sera reemplazado por la implementacion real
 * en el plan 3 (grupos). Solo muestra un texto centrado.
 */
@AndroidEntryPoint
public class DocenteGruposFragment extends Fragment {

    private FragmentDocenteGruposPlaceholderBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteGruposPlaceholderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
