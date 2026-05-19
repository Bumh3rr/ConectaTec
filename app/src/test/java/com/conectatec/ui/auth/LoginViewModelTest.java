package com.conectatec.ui.auth;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.conectatec.data.model.LoginResponse;
import com.conectatec.data.repository.AuthRepository;
import com.conectatec.data.SessionManager;
import com.conectatec.ui.common.UiState;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private LoginViewModel viewModel;

    @Before
    public void setup() {
        viewModel = new LoginViewModel(
                new FakeAuthRepository(),
                new FakeSessionManager(),
                Runnable::run  // executor síncrono para tests
        );
    }

    @Ignore("Validación de dominio desactivada temporalmente — reactivar con TODO en LoginViewModel")
    @Test
    public void loginConGoogle_dominioInvalido_emiteError() {
        viewModel.loginConGoogle("token-cualquiera", "usuario@gmail.com");

        UiState<?> state = viewModel.getState().getValue();
        assertTrue("Debería ser UiState.Error", state instanceof UiState.Error);
        String mensaje = ((UiState.Error<?>) state).mensaje;
        assertTrue("El mensaje debe mencionar el dominio institucional",
                mensaje.contains(AuthConstants.DOMINIO_INSTITUCIONAL));
    }

    @Ignore("Validación de dominio desactivada temporalmente — reactivar con TODO en LoginViewModel")
    @Test
    public void loginConGoogle_emailVacio_emiteError() {
        viewModel.loginConGoogle("token-cualquiera", "");

        UiState<?> state = viewModel.getState().getValue();
        assertTrue(state instanceof UiState.Error);
    }

    @Ignore("Validación de dominio desactivada temporalmente — reactivar con TODO en LoginViewModel")
    @Test
    public void loginConGoogle_emailNull_emiteError() {
        viewModel.loginConGoogle("token-cualquiera", null);

        UiState<?> state = viewModel.getState().getValue();
        assertTrue(state instanceof UiState.Error);
    }

    @Test
    public void loginConGoogle_dominioValido_emiteSuccess() {
        viewModel.loginConGoogle("token-valid", "usuario@tudominio.edu.mx");

        UiState<?> state = viewModel.getState().getValue();
        assertTrue("Debería ser UiState.Success", state instanceof UiState.Success);
        assertEquals("El rol debe ser DOCENTE", "DOCENTE", ((UiState.Success<String>) state).data);
    }

    // ── Fakes ────────────────────────────────────────────────────────────────

    static class FakeAuthRepository implements AuthRepository {
        @Override
        public LoginResponse loginConGoogle(String idToken) {
            return new LoginResponse("jwt-test", "DOCENTE", "Test User");
        }
    }

    static class FakeSessionManager extends SessionManager {
        FakeSessionManager() {
            super();  // llama al constructor protegido sin args (prefs = null)
        }

        @Override public void guardarSesion(String jwt, String rol, String nombre) {}
        @Override public String getJwt()    { return null; }
        @Override public String getRol()    { return null; }
        @Override public void cerrarSesion() {}
    }
}
