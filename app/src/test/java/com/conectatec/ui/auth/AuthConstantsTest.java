package com.conectatec.ui.auth;

import org.junit.Test;
import static org.junit.Assert.*;

public class AuthConstantsTest {

    @Test
    public void esDominioValido_correoInstitucional_retornaTrue() {
        assertTrue(AuthConstants.esDominioValido("alumno@tudominio.edu.mx"));
    }

    @Test
    public void esDominioValido_correoGmail_retornaFalse() {
        assertFalse(AuthConstants.esDominioValido("alumno@gmail.com"));
    }

    @Test
    public void esDominioValido_emailVacio_retornaFalse() {
        assertFalse(AuthConstants.esDominioValido(""));
    }

    @Test
    public void esDominioValido_soloArroba_retornaFalse() {
        assertFalse(AuthConstants.esDominioValido("@"));
    }

    @Test
    public void esDominioValido_emailNull_retornaFalse() {
        assertFalse(AuthConstants.esDominioValido(null));
    }

    @Test
    public void esDominioValido_emailConDominioConcatenado_retornaFalse() {
        assertFalse(AuthConstants.esDominioValido("atacante@evil.com@tudominio.edu.mx"));
    }
}
