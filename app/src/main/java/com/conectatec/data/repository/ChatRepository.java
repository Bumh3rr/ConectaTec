package com.conectatec.data.repository;

import com.conectatec.data.model.Mensaje;
import com.conectatec.data.model.Sala;

import java.util.List;

public interface ChatRepository {
    List<Sala> getSalas() throws Exception;
    List<Mensaje> getMensajes(int salaId) throws Exception;
}
