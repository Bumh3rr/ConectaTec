package com.conectatec.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class ActividadItemTest {

    @Test
    public void constructor_setsAllFields() {
        ActividadItem item = new ActividadItem("Título", "Desc", "09:00",
                ActividadItem.TIPO_TAREA, 0xFF6C63FF);
        assertEquals("Título", item.titulo);
        assertEquals("Desc", item.descripcion);
        assertEquals("09:00", item.timestamp);
        assertEquals(ActividadItem.TIPO_TAREA, item.tipoIcono);
        assertEquals(0xFF6C63FF, item.colorAvatar);
    }

    @Test
    public void constants_haveExpectedValues() {
        assertEquals(1, ActividadItem.TIPO_USUARIO);
        assertEquals(2, ActividadItem.TIPO_TAREA);
        assertEquals(3, ActividadItem.TIPO_ENTREGA);
        assertEquals(4, ActividadItem.TIPO_GRUPO);
        assertEquals(5, ActividadItem.TIPO_MENSAJE);
        assertEquals(6, ActividadItem.TIPO_AVISO);
    }
}
