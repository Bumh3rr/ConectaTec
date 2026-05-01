package com.conectatec.ui.docente.chat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.conectatec.data.model.Sala;
import com.conectatec.data.repository.ChatRepository;
import com.conectatec.ui.common.UiState;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DocenteChatViewModel extends ViewModel {

    private final ChatRepository repository;
    private final MutableLiveData<UiState<List<Sala>>> state = new MutableLiveData<>();
    private static final Executor BG = Executors.newSingleThreadExecutor();

    @Inject
    public DocenteChatViewModel(ChatRepository repository) {
        this.repository = repository;
    }

    public LiveData<UiState<List<Sala>>> getState() { return state; }

    public void cargarDatos() {
        if (state.getValue() instanceof UiState.Loading) return;
        state.setValue(UiState.Loading.get());
        BG.execute(() -> {
            try {
                List<Sala> salas = repository.getSalas();
                state.postValue(new UiState.Success<>(salas));
            } catch (Exception e) {
                state.postValue(new UiState.Error<>(e.getMessage() != null
                        ? e.getMessage() : "Error al cargar conversaciones"));
            }
        });
    }
}
