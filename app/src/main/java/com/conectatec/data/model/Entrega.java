package com.conectatec.data.model;

public class Entrega {

    public static final int ESTADO_BORRADOR     = 0;
    public static final int ESTADO_ENTREGADA    = 1;
    public static final int ESTADO_CALIFICADA   = 2;
    public static final int ESTADO_SIN_ENTREGAR = 3;

    public final int alumnoId;
    public final String alumnoNombre;
    public final String alumnoIniciales;
    public final int estado;
    public final String fechaEntrega;
    public final Integer calificacion;

    public Entrega(int alumnoId, String alumnoNombre, String alumnoIniciales,
                   int estado, String fechaEntrega, Integer calificacion) {
        this.alumnoId = alumnoId;
        this.alumnoNombre = alumnoNombre;
        this.alumnoIniciales = alumnoIniciales;
        this.estado = estado;
        this.fechaEntrega = fechaEntrega;
        this.calificacion = calificacion;
    }
}
