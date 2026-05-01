package com.conectatec.ui.docente.grupos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.conectatec.data.model.Grupo;
import com.conectatec.data.repository.GruposRepository;
import com.conectatec.ui.common.UiState;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DocenteGrupoDetalleViewModel extends ViewModel {

    private final GruposRepository repository;
    private final MutableLiveData<UiState<Grupo>> state = new MutableLiveData<>();
    private static final Executor BG = Executors.newSingleThreadExecutor();

    @Inject
    public DocenteGrupoDetalleViewModel(GruposRepository repository) {
        this.repository = repository;
    }

    public LiveData<UiState<Grupo>> getState() { return state; }

    public void cargarDetalle(int grupoId) {
        if (state.getValue() instanceof UiState.Loading) return;
        state.setValue(UiState.Loading.get());
        BG.execute(() -> {
            try {
                Grupo grupo = repository.getGrupoDetalle(grupoId);
                state.postValue(new UiState.Success<>(grupo));
            } catch (Exception e) {
                state.postValue(new UiState.Error<>(e.getMessage() != null
                        ? e.getMessage() : "Error al cargar el grupo"));
            }
        });
    }
}
