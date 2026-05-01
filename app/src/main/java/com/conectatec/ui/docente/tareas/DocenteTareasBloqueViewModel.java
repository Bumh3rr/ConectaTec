package com.conectatec.ui.docente.tareas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.conectatec.data.model.Tarea;
import com.conectatec.data.repository.TareasRepository;
import com.conectatec.ui.common.UiState;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DocenteTareasBloqueViewModel extends ViewModel {

    private final TareasRepository repository;
    private final MutableLiveData<UiState<List<Tarea>>> state = new MutableLiveData<>();
    private static final Executor BG = Executors.newSingleThreadExecutor();

    @Inject
    public DocenteTareasBloqueViewModel(TareasRepository repository) {
        this.repository = repository;
    }

    public LiveData<UiState<List<Tarea>>> getState() { return state; }

    public void cargarTareas(int grupoId, int bloqueId) {
        if (state.getValue() instanceof UiState.Loading) return;
        state.setValue(UiState.Loading.get());
        BG.execute(() -> {
            try {
                List<Tarea> tareas = repository.getTareas(grupoId, bloqueId);
                state.postValue(new UiState.Success<>(tareas));
            } catch (Exception e) {
                state.postValue(new UiState.Error<>(e.getMessage() != null
                        ? e.getMessage() : "Error al cargar tareas"));
            }
        });
    }
}
