package com.conectatec.ui.docente.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.conectatec.R;
import com.conectatec.databinding.FragmentDocenteChatBinding;
import com.conectatec.ui.common.ScrollRevealAnimator;
import com.conectatec.ui.docente.chat.adapter.SalaDocenteAdapter;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Lista de salas de chat del docente.
 *
 * Muestra:
 *  - Header con badge de mensajes no leídos.
 *  - Chips para filtrar por tipo: Todos / Grupos / Privados.
 *  - RecyclerView con 5 salas dummy (3 grupales + 2 privadas).
 *  - Al tocar una sala navega a la conversación.
 */
@AndroidEntryPoint
public class DocenteChatFragment extends Fragment {

    private FragmentDocenteChatBinding binding;
    private SalaDocenteAdapter adapter;
    private ScrollRevealAnimator scrollRevealAnimator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        setupChips();
        renderHeader();
        scrollRevealAnimator = new ScrollRevealAnimator(binding.rvSalasChat);
        scrollRevealAnimator.triggerInicial();
    }

    private void setupRecyclerView() {
        adapter = new SalaDocenteAdapter(sala -> {
            NavController nav = NavHostFragment.findNavController(this);
            Bundle args = new Bundle();
            args.putInt("salaId", sala.id);
            nav.navigate(R.id.action_chat_to_conversacion, args);
        });
        binding.rvSalasChat.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvSalasChat.setAdapter(adapter);
        toggleEmpty();
    }

    private void setupChips() {
        binding.chipGroupChat.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            int id = checkedIds.get(0);
            if      (id == R.id.chipChatTodos)    adapter.setFiltroTipo(null);
            else if (id == R.id.chipChatGrupos)   adapter.setFiltroTipo("GRUPO");
            else if (id == R.id.chipChatPrivados) adapter.setFiltroTipo("PRIVADO");
            toggleEmpty();
            scrollRevealAnimator.triggerInicial();
        });
    }

    private void renderHeader() {
        int total = adapter.getTotalNoLeidos();
        if (total > 0) {
            binding.tvBadgeTotalNoLeidos.setVisibility(View.VISIBLE);
            binding.tvBadgeTotalNoLeidos.setText(String.valueOf(total));
        } else {
            binding.tvBadgeTotalNoLeidos.setVisibility(View.GONE);
        }
    }

    private void toggleEmpty() {
        boolean empty = adapter.getItemCount() == 0;
        binding.rvSalasChat.setVisibility(empty ? View.GONE : View.VISIBLE);
        binding.emptyStateChat.getRoot().setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
