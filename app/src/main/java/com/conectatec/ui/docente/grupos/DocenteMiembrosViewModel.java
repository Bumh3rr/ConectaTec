package com.conectatec.ui.docente.grupos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.conectatec.data.model.Miembro;
import com.conectatec.data.repository.GruposRepository;
import com.conectatec.ui.common.UiState;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DocenteMiembrosViewModel extends ViewModel {

    private final GruposRepository repository;
    private final MutableLiveData<UiState<List<Miembro>>> state = new MutableLiveData<>();
    private static final Executor BG = Executors.newSingleThreadExecutor();

    @Inject
    public DocenteMiembrosViewModel(GruposRepository repository) {
        this.repository = repository;
    }

    public LiveData<UiState<List<Miembro>>> getState() { return state; }

    public void cargarMiembros(int grupoId) {
        if (state.getValue() instanceof UiState.Loading) return;
        state.setValue(UiState.Loading.get());
        BG.execute(() -> {
            try {
                List<Miembro> miembros = repository.getMiembros(grupoId);
                state.postValue(new UiState.Success<>(miembros));
            } catch (Exception e) {
                state.postValue(new UiState.Error<>(e.getMessage() != null
                        ? e.getMessage() : "Error al cargar miembros"));
            }
        });
    }
}
