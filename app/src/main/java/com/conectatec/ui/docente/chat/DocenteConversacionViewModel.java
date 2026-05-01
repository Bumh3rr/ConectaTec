package com.conectatec.ui.docente.chat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.conectatec.data.model.Mensaje;
import com.conectatec.data.repository.ChatRepository;
import com.conectatec.ui.common.UiState;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DocenteConversacionViewModel extends ViewModel {

    private final ChatRepository repository;
    private final MutableLiveData<UiState<List<Mensaje>>> state = new MutableLiveData<>();
    private static final Executor BG = Executors.newSingleThreadExecutor();

    @Inject
    public DocenteConversacionViewModel(ChatRepository repository) {
        this.repository = repository;
    }

    public LiveData<UiState<List<Mensaje>>> getState() { return state; }

    public void cargarMensajes(int salaId) {
        if (state.getValue() instanceof UiState.Loading) return;
        state.setValue(UiState.Loading.get());
        BG.execute(() -> {
            try {
                List<Mensaje> mensajes = repository.getMensajes(salaId);
                state.postValue(new UiState.Success<>(mensajes));
            } catch (Exception e) {
                state.postValue(new UiState.Error<>(e.getMessage() != null
                        ? e.getMessage() : "Error al cargar mensajes"));
            }
        });
    }
}
