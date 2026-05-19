package com.conectatec.data.repository;

import com.conectatec.data.model.LoginResponse;

public interface AuthRepository {
    /**
     * Envía el idToken de Google al backend y obtiene JWT + rol.
     * @throws Exception si el backend rechaza el token, el dominio no está autorizado,
     *                   o hay error de red.
     */
    LoginResponse loginConGoogle(String idToken) throws Exception;
}
