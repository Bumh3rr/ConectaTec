package com.conectatec.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.conectatec.data.SessionManager;
import com.conectatec.data.model.LoginResponse;
import com.conectatec.data.repository.AuthRepository;
import com.conectatec.ui.common.UiState;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginViewModel extends ViewModel {

    private final AuthRepository authRepository;
    private final SessionManager sessionManager;
    private final Executor executor;

    private final MutableLiveData<UiState<String>> state = new MutableLiveData<>();

    @Inject
    public LoginViewModel(AuthRepository authRepository, SessionManager sessionManager) {
        this.authRepository = authRepository;
        this.sessionManager = sessionManager;
        this.executor       = Executors.newSingleThreadExecutor();
    }

    /** Constructor package-private para tests unitarios. */
    LoginViewModel(AuthRepository authRepository, SessionManager sessionManager, Executor executor) {
        this.authRepository = authRepository;
        this.sessionManager = sessionManager;
        this.executor       = executor;
    }

    public LiveData<UiState<String>> getState() {
        return state;
    }

    /**
     * Valida el dominio del email y, si es institucional, llama al backend.
     * El estado resultante se emite en getState():
     *   - UiState.Error si el dominio no es válido
     *   - UiState.Loading mientras se espera respuesta del backend
     *   - UiState.Success<String> con el rol si el backend responde OK
     *   - UiState.Error con mensaje si el backend falla
     */
    public void loginConGoogle(String idToken, String email) {
        // TODO: reactivar cuando el dominio institucional esté configurado en AuthConstants
        // if (!AuthConstants.esDominioValido(email)) {
        //     state.setValue(new UiState.Error<>("Solo se permiten cuentas " + AuthConstants.DOMINIO_INSTITUCIONAL));
        //     return;
        // }

        state.setValue(UiState.Loading.get());
        executor.execute(() -> {
            try {
                LoginResponse resp = authRepository.loginConGoogle(idToken);
                sessionManager.guardarSesion(resp.jwt, resp.rol, resp.nombre);
                state.postValue(new UiState.Success<>(resp.rol));
            } catch (Exception e) {
                String msg = e.getMessage() != null ? e.getMessage() : "Error de autenticación";
                state.postValue(new UiState.Error<>(msg));
            }
        });
    }
}
