package com.conectatec.data.repository;

import com.conectatec.data.model.Bloque;
import com.conectatec.data.model.Entrega;
import com.conectatec.data.model.Tarea;

import java.util.List;

public interface TareasRepository {
    List<Bloque> getBloques(int grupoId) throws Exception;
    List<Tarea> getTareas(int grupoId, int bloqueId) throws Exception;
    List<Entrega> getEntregas(int tareaId) throws Exception;
    Entrega getEntrega(int tareaId, int alumnoId) throws Exception;
    Tarea getTareaById(int tareaId) throws Exception;
}
