package com.conectatec.data.repository;

import com.conectatec.data.model.Bloque;
import com.conectatec.data.model.Entrega;
import com.conectatec.data.model.Tarea;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TareasRepositoryImpl implements TareasRepository {

    private static final List<Tarea> DATASET = new ArrayList<>();

    static {
        DATASET.add(new Tarea(1,  "Práctica 1: Layouts",     Tarea.TIPO_TAREA,    Tarea.EST_EN_CURSO,   1, 1, "01/02/2026", "10/05/2026", 18, 12));
        DATASET.add(new Tarea(2,  "Examen Parcial 1",         Tarea.TIPO_EXAMEN,   Tarea.EST_COMPLETADA, 1, 1, "15/02/2026", "28/02/2026", 18, 18));
        DATASET.add(new Tarea(3,  "Proyecto Final",           Tarea.TIPO_PROYECTO, Tarea.EST_EN_CURSO,   1, 2, "01/03/2026", "30/05/2026", 18,  5));
        DATASET.add(new Tarea(4,  "Trabajo de investigación", Tarea.TIPO_TRABAJO,  Tarea.EST_EN_CURSO,   1, 3, "10/04/2026", "25/05/2026", 18,  8));
        DATASET.add(new Tarea(5,  "Tarea 1: Modelo ER",       Tarea.TIPO_TAREA,    Tarea.EST_COMPLETADA, 2, 1, "20/01/2026", "05/02/2026", 16, 16));
        DATASET.add(new Tarea(6,  "Examen parcial 2",         Tarea.TIPO_EXAMEN,   Tarea.EST_EN_CURSO,   2, 2, "15/03/2026", "12/05/2026", 16,  0));
        DATASET.add(new Tarea(7,  "Tarea 5: Integrales",      Tarea.TIPO_TAREA,    Tarea.EST_VENCIDA,    3, 1, "01/02/2026", "20/04/2026", 13,  9));
        DATASET.add(new Tarea(8,  "Tarea 6: Series",          Tarea.TIPO_TAREA,    Tarea.EST_EN_CURSO,   3, 2, "10/04/2026", "08/05/2026", 13,  4));
    }

    private static final String[][] NOMBRES_POR_GRUPO = {
            {"Ana López",    "Bruno García",  "Carla Méndez", "Diego Ruiz",   "Elena Torres", "Fernando Vega"},
            {"Gabriela Luna","Héctor Pérez",  "Isabel Romero","Javier Castro","Karla Soto",   "Luis Mendoza"},
            {"María Reyes",  "Néstor Aguilar","Olivia Cano",  "Pablo Núñez",  "Quetzal Ríos", "Rocío Salinas"}
    };

    @Inject
    public TareasRepositoryImpl() {}

    @Override
    public List<Bloque> getBloques(int grupoId) throws Exception {
        Thread.sleep(300);
        int t1, t2, t3;
        switch (grupoId) {
            case 1: t1 = 2; t2 = 1; t3 = 1; break;
            case 2: t1 = 1; t2 = 1; t3 = 0; break;
            case 3: t1 = 1; t2 = 1; t3 = 0; break;
            default: t1 = 0; t2 = 0; t3 = 0; break;
        }
        List<Bloque> lista = new ArrayList<>();
        lista.add(new Bloque(1, 1, "Bloque 1", t1));
        lista.add(new Bloque(2, 2, "Bloque 2", t2));
        lista.add(new Bloque(3, 3, "Bloque 3", t3));
        return lista;
    }

    @Override
    public List<Tarea> getTareas(int grupoId, int bloqueId) throws Exception {
        Thread.sleep(300);
        List<Tarea> result = new ArrayList<>();
        for (Tarea t : DATASET) {
            if (t.grupoId == grupoId && t.bloqueId == bloqueId) result.add(t);
        }
        return result;
    }

    @Override
    public List<Entrega> getEntregas(int tareaId) throws Exception {
        Thread.sleep(300);
        Tarea tarea = buscarPorId(tareaId);
        int grupoId = (tarea != null) ? tarea.grupoId : 1;
        int idx = Math.max(0, Math.min(grupoId - 1, NOMBRES_POR_GRUPO.length - 1));
        String[] nombres = NOMBRES_POR_GRUPO[idx];
        int baseId = grupoId * 100;

        int[] estados  = {Entrega.ESTADO_ENTREGADA, Entrega.ESTADO_ENTREGADA,
                          Entrega.ESTADO_CALIFICADA, Entrega.ESTADO_CALIFICADA,
                          Entrega.ESTADO_BORRADOR,   Entrega.ESTADO_SIN_ENTREGAR};
        String[] fechas = {"05/05/2026", "06/05/2026", "03/05/2026", "04/05/2026", "—", null};
        Integer[] notas = {null, null, 92, 78, null, null};

        List<Entrega> lista = new ArrayList<>();
        for (int i = 0; i < nombres.length; i++) {
            String[] partes = nombres[i].split(" ");
            StringBuilder inis = new StringBuilder();
            for (int j = 0; j < Math.min(2, partes.length); j++) {
                if (!partes[j].isEmpty()) inis.append(partes[j].charAt(0));
            }
            lista.add(new Entrega(baseId + (i + 1), nombres[i],
                    inis.toString().toUpperCase(), estados[i], fechas[i], notas[i]));
        }
        return lista;
    }

    @Override
    public Entrega getEntrega(int tareaId, int alumnoId) throws Exception {
        for (Entrega e : getEntregas(tareaId)) {
            if (e.alumnoId == alumnoId) return e;
        }
        throw new Exception("Entrega no encontrada");
    }

    @Override
    public Tarea getTareaById(int tareaId) throws Exception {
        Tarea t = buscarPorId(tareaId);
        if (t == null) throw new Exception("Tarea no encontrada");
        return t;
    }

    public static Tarea buscarPorId(int id) {
        for (Tarea t : DATASET) {
            if (t.id == id) return t;
        }
        return null;
    }
}
