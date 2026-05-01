package com.conectatec.data.repository;

import com.conectatec.data.model.AlumnoPerfil;
import com.conectatec.data.model.Grupo;
import com.conectatec.data.model.Miembro;

import java.util.List;

public interface GruposRepository {
    List<Grupo> getGrupos() throws Exception;
    Grupo getGrupoDetalle(int grupoId) throws Exception;
    List<Miembro> getMiembros(int grupoId) throws Exception;
    AlumnoPerfil getAlumnoPerfil(int alumnoId, String nombre, String iniciales,
                                  String correo, String matricula) throws Exception;
}
