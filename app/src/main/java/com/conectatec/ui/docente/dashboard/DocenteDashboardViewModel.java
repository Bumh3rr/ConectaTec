package com.conectatec.ui.docente.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.conectatec.data.model.DashboardResumen;
import com.conectatec.data.repository.DashboardRepository;
import com.conectatec.ui.common.UiState;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DocenteDashboardViewModel extends ViewModel {

    private final DashboardRepository repository;
    private final MutableLiveData<UiState<DashboardResumen>> state = new MutableLiveData<>();
    private static final Executor BG = Executors.newSingleThreadExecutor();

    @Inject
    public DocenteDashboardViewModel(DashboardRepository repository) {
        this.repository = repository;
    }

    public LiveData<UiState<DashboardResumen>> getState() { return state; }

    public void cargarDatos() {
        if (state.getValue() instanceof UiState.Loading) return;
        state.setValue(UiState.Loading.get());
        BG.execute(() -> {
            try {
                DashboardResumen resumen = repository.getResumen();
                state.postValue(new UiState.Success<>(resumen));
            } catch (Exception e) {
                state.postValue(new UiState.Error<>(e.getMessage() != null
                        ? e.getMessage() : "Error al cargar el dashboard"));
            }
        });
    }
}
