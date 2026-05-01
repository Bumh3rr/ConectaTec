package com.conectatec.ui.docente.grupos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.conectatec.data.model.AlumnoPerfil;
import com.conectatec.data.repository.GruposRepository;
import com.conectatec.ui.common.UiState;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DocenteAlumnoPerfilViewModel extends ViewModel {

    private final GruposRepository repository;
    private final MutableLiveData<UiState<AlumnoPerfil>> state = new MutableLiveData<>();
    private static final Executor BG = Executors.newSingleThreadExecutor();

    @Inject
    public DocenteAlumnoPerfilViewModel(GruposRepository repository) {
        this.repository = repository;
    }

    public LiveData<UiState<AlumnoPerfil>> getState() { return state; }

    public void cargarPerfil(int alumnoId, String nombre, String iniciales,
                              String correo, String matricula) {
        if (state.getValue() instanceof UiState.Loading) return;
        state.setValue(UiState.Loading.get());
        BG.execute(() -> {
            try {
                AlumnoPerfil perfil = repository.getAlumnoPerfil(
                        alumnoId, nombre, iniciales, correo, matricula);
                state.postValue(new UiState.Success<>(perfil));
            } catch (Exception e) {
                state.postValue(new UiState.Error<>(e.getMessage() != null
                        ? e.getMessage() : "Error al cargar perfil"));
            }
        });
    }
}
