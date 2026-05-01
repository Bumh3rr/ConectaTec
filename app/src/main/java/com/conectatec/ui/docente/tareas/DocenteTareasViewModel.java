package com.conectatec.ui.docente.tareas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.conectatec.data.model.Grupo;
import com.conectatec.data.repository.GruposRepository;
import com.conectatec.ui.common.UiState;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DocenteTareasViewModel extends ViewModel {

    private final GruposRepository repository;
    private final MutableLiveData<UiState<List<Grupo>>> state = new MutableLiveData<>();
    private static final Executor BG = Executors.newSingleThreadExecutor();

    @Inject
    public DocenteTareasViewModel(GruposRepository repository) {
        this.repository = repository;
    }

    public LiveData<UiState<List<Grupo>>> getState() { return state; }

    public void cargarDatos() {
        if (state.getValue() instanceof UiState.Loading) return;
        state.setValue(UiState.Loading.get());
        BG.execute(() -> {
            try {
                List<Grupo> grupos = repository.getGrupos();
                state.postValue(new UiState.Success<>(grupos));
            } catch (Exception e) {
                state.postValue(new UiState.Error<>(e.getMessage() != null
                        ? e.getMessage() : "Error al cargar grupos"));
            }
        });
    }
}
