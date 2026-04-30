package com.conectatec.ui.docente.chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.conectatec.databinding.FragmentDocenteConversacionBinding;
import com.conectatec.ui.docente.chat.adapter.MensajeDocenteAdapter;
import com.conectatec.ui.docente.chat.adapter.MensajeDocenteAdapter.MensajeDummyDocente;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Conversación de una sala de chat del docente.
 *
 * Muestra:
 *  - Header con nombre de sala y (para grupos) cantidad de miembros.
 *  - RecyclerView con mensajes dummy en dos viewTypes (enviado/recibido).
 *  - Barra inferior: botón adjuntar + campo de texto + botón enviar.
 */
@AndroidEntryPoint
public class DocenteConversacionFragment extends Fragment {

    private FragmentDocenteConversacionBinding binding;
    private MensajeDocenteAdapter adapter;
    private int salaId;

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
        List<MensajeDummyDocente> mensajes = MensajeDocenteAdapter.cargarParaSala(salaId);
        adapter = new MensajeDocenteAdapter(mensajes);

        LinearLayoutManager lm = new LinearLayoutManager(requireContext());
        lm.setStackFromEnd(true);
        binding.rvMensajesChat.setLayoutManager(lm);
        binding.rvMensajesChat.setAdapter(adapter);

        if (adapter.getItemCount() > 0) {
            binding.rvMensajesChat.scrollToPosition(adapter.getItemCount() - 1);
        }
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
        MensajeDummyDocente nuevo = new MensajeDummyDocente(
                nuevoId, texto, hora, true, "Yo", "CB", false);

        adapter.agregarMensaje(nuevo);
        binding.rvMensajesChat.smoothScrollToPosition(adapter.getItemCount() - 1);
        binding.etMensajeChat.setText("");
    }

    private void onAdjuntarClicked() {
        // TODO: llamar a ArchivoService.adjuntar()
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
