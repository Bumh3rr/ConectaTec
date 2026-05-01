package com.conectatec.model;

public class ActividadItem {
    public static final int TIPO_USUARIO  = 1;
    public static final int TIPO_TAREA    = 2;
    public static final int TIPO_ENTREGA  = 3;
    public static final int TIPO_GRUPO    = 4;
    public static final int TIPO_MENSAJE  = 5;
    public static final int TIPO_AVISO    = 6;

    public final String titulo;
    public final String descripcion;
    public final String timestamp;
    public final int tipoIcono;
    public final int colorAvatar;

    public ActividadItem(String titulo, String descripcion, String timestamp,
                         int tipoIcono, int colorAvatar) {
        this.titulo      = titulo;
        this.descripcion = descripcion;
        this.timestamp   = timestamp;
        this.tipoIcono   = tipoIcono;
        this.colorAvatar = colorAvatar;
    }
}
