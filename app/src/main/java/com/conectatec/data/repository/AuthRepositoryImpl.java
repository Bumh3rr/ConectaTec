package com.conectatec.data.repository;

import com.conectatec.data.model.LoginResponse;
import com.conectatec.data.network.AuthService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AuthRepositoryImpl implements AuthRepository {

    private final AuthService authService;

    @Inject
    public AuthRepositoryImpl(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public LoginResponse loginConGoogle(String idToken) throws Exception {
        // TODO: llamar a authService.loginConGoogle() cuando el backend esté listo
        // Descomentar las líneas siguientes y eliminar el bloque dummy:
        //
        // Map<String, String> body = new HashMap<>();
        // body.put("idToken", idToken);
        // Response<LoginResponse> response = authService.loginConGoogle(body).execute();
        // if (!response.isSuccessful()) {
        //     throw new Exception("Error " + response.code() + ": " + response.message());
        // }
        // return response.body();

        // Dummy para desarrollo paralelo con el backend:
        Thread.sleep(400);
        return new LoginResponse("jwt-dummy-google", "DOCENTE", "Usuario Demo Google");
    }
}
