package com.conectatec.ui.docente.tareas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.conectatec.data.model.Entrega;
import com.conectatec.data.repository.TareasRepository;
import com.conectatec.ui.common.UiState;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DocenteEntregasViewModel extends ViewModel {

    private final TareasRepository repository;
    private final MutableLiveData<UiState<List<Entrega>>> state = new MutableLiveData<>();
    private final MutableLiveData<String> tituloTarea = new MutableLiveData<>("Tarea");
    private static final Executor BG = Executors.newSingleThreadExecutor();

    @Inject
    public DocenteEntregasViewModel(TareasRepository repository) {
        this.repository = repository;
    }

    public LiveData<UiState<List<Entrega>>> getState() { return state; }
    public LiveData<String> getTituloTarea() { return tituloTarea; }

    public void cargarEntregas(int tareaId) {
        if (state.getValue() instanceof UiState.Loading) return;
        state.setValue(UiState.Loading.get());
        BG.execute(() -> {
            try {
                try {
                    tituloTarea.postValue(repository.getTareaById(tareaId).titulo);
                } catch (Exception ignored) {}
                List<Entrega> entregas = repository.getEntregas(tareaId);
                state.postValue(new UiState.Success<>(entregas));
            } catch (Exception e) {
                state.postValue(new UiState.Error<>(e.getMessage() != null
                        ? e.getMessage() : "Error al cargar entregas"));
            }
        });
    }
}
