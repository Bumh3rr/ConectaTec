package com.conectatec.ui.docente.tareas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.conectatec.data.model.Entrega;
import com.conectatec.data.repository.TareasRepository;
import com.conectatec.ui.common.UiState;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DocenteCalificarEntregaViewModel extends ViewModel {

    private final TareasRepository repository;
    private final MutableLiveData<UiState<Entrega>> state = new MutableLiveData<>();
    private static final Executor BG = Executors.newSingleThreadExecutor();

    @Inject
    public DocenteCalificarEntregaViewModel(TareasRepository repository) {
        this.repository = repository;
    }

    public LiveData<UiState<Entrega>> getState() { return state; }

    public void cargarEntrega(int tareaId, int alumnoId) {
        if (state.getValue() instanceof UiState.Loading) return;
        state.setValue(UiState.Loading.get());
        BG.execute(() -> {
            try {
                Entrega entrega = repository.getEntrega(tareaId, alumnoId);
                state.postValue(new UiState.Success<>(entrega));
            } catch (Exception e) {
                state.postValue(new UiState.Error<>(e.getMessage() != null
                        ? e.getMessage() : "Entrega no encontrada"));
            }
        });
    }
}
