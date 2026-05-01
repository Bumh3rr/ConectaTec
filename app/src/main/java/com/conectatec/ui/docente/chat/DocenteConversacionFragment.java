package com.conectatec.ui.docente.chat;

import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.conectatec.data.model.Mensaje;
import com.conectatec.databinding.FragmentDocenteConversacionBinding;
import com.conectatec.ui.common.UiState;
import com.conectatec.ui.docente.chat.adapter.MensajeDocenteAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteConversacionFragment extends Fragment {

    private FragmentDocenteConversacionBinding binding;
    private MensajeDocenteAdapter adapter;
    private DocenteConversacionViewModel viewModel;
    private int salaId;
    private ViewTreeObserver.OnGlobalLayoutListener keyboardListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteConversacionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        salaId = (getArguments() != null) ? getArguments().getInt("salaId", 0) : 0;
        renderHeader();
        setupRecyclerView();
        setupListeners();

        viewModel = new ViewModelProvider(this).get(DocenteConversacionViewModel.class);
        observeViewModel();
        viewModel.cargarMensajes(salaId);
        setupKeyboardListener();
    }

    private void renderHeader() {
        String nombre;
        String subtitulo;
        boolean esGrupo;

        switch (salaId) {
            case 1: nombre = "Programación Móvil 6A"; subtitulo = "18 miembros"; esGrupo = true;  break;
            case 2: nombre = "Bases de Datos 4B";     subtitulo = "16 miembros"; esGrupo = true;  break;
            case 3: nombre = "Cálculo Integral 2A";   subtitulo = "13 miembros"; esGrupo = true;  break;
            case 4: nombre = "Ana López";             subtitulo = "";            esGrupo = false; break;
            case 5: nombre = "Diego Ruiz";            subtitulo = "";            esGrupo = false; break;
            default: nombre = "Conversación";         subtitulo = "";            esGrupo = false; break;
        }

        binding.tvNombreSalaConversacion.setText(nombre);
        if (esGrupo) {
            binding.tvSubtituloSalaConversacion.setVisibility(View.VISIBLE);
            binding.tvSubtituloSalaConversacion.setText(subtitulo);
        } else {
            binding.tvSubtituloSalaConversacion.setVisibility(View.GONE);
        }
    }

    private void setupRecyclerView() {
        adapter = new MensajeDocenteAdapter(new ArrayList<>());
        LinearLayoutManager lm = new LinearLayoutManager(requireContext());
        lm.setStackFromEnd(true);
        binding.rvMensajesChat.setLayoutManager(lm);
        binding.rvMensajesChat.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel.getState().observe(getViewLifecycleOwner(), state -> {
            binding.progressBarMensajes.setVisibility(
                    state instanceof UiState.Loading ? View.VISIBLE : View.GONE);
            if (state instanceof UiState.Success) {
                List<Mensaje> mensajes = ((UiState.Success<List<Mensaje>>) state).data;
                adapter = new MensajeDocenteAdapter(mensajes);
                binding.rvMensajesChat.setAdapter(adapter);
                if (adapter.getItemCount() > 0) {
                    binding.rvMensajesChat.scrollToPosition(adapter.getItemCount() - 1);
                }
            } else if (state instanceof UiState.Error) {
                Snackbar.make(binding.getRoot(),
                        ((UiState.Error<?>) state).mensaje, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void setupListeners() {
        binding.btnBackConversacion.setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack());
        binding.btnEnviarChat.setOnClickListener(v -> onSendClicked());
        binding.btnAdjuntarChat.setOnClickListener(v -> onAdjuntarClicked());
    }

    private void onSendClicked() {
        // TODO: llamar a MensajeService.enviar()
        CharSequence raw = binding.etMensajeChat.getText();
        if (raw == null) return;
        String texto = raw.toString().trim();
        if (TextUtils.isEmpty(texto)) return;

        String hora = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        int nuevoId = adapter.getItemCount() + 1;
        Mensaje nuevo = new Mensaje(nuevoId, texto, hora, true, "Yo", "CB", false);

        adapter.agregarMensaje(nuevo);
        binding.rvMensajesChat.smoothScrollToPosition(adapter.getItemCount() - 1);
        binding.etMensajeChat.setText("");
    }

    private void onAdjuntarClicked() {
        // TODO: llamar a ArchivoService.adjuntar()
    }

    private void setupKeyboardListener() {
        keyboardListener = () -> {
            if (binding == null) return;
            Rect visibleFrame = new Rect();
            View decorView = requireActivity().getWindow().getDecorView();
            decorView.getWindowVisibleDisplayFrame(visibleFrame);
            int screenHeight = decorView.getHeight();
            int keyboardHeight = screenHeight - visibleFrame.bottom;

            ConstraintLayout.LayoutParams rvParams =
                    (ConstraintLayout.LayoutParams) binding.rvMensajesChat.getLayoutParams();

            if (keyboardHeight > screenHeight * 0.15f) {
                binding.barraInferiorChat.setTranslationY(-keyboardHeight);
                rvParams.bottomMargin = keyboardHeight;
                binding.rvMensajesChat.setLayoutParams(rvParams);
                if (adapter != null && adapter.getItemCount() > 0) {
                    binding.rvMensajesChat.post(() -> {
                        if (binding != null)
                            binding.rvMensajesChat.scrollToPosition(adapter.getItemCount() - 1);
                    });
                }
            } else {
                binding.barraInferiorChat.setTranslationY(0);
                rvParams.bottomMargin = 0;
                binding.rvMensajesChat.setLayoutParams(rvParams);
            }
        };
        binding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(keyboardListener);
    }

    @Override
    public void onDestroyView() {
        if (keyboardListener != null) {
            binding.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(keyboardListener);
            keyboardListener = null;
        }
        super.onDestroyView();
        binding = null;
    }
}
