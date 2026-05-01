package com.conectatec.data.repository;

import com.conectatec.data.model.Mensaje;
import com.conectatec.data.model.Sala;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ChatRepositoryImpl implements ChatRepository {

    @Inject
    public ChatRepositoryImpl() {}

    @Override
    public List<Sala> getSalas() throws Exception {
        Thread.sleep(300);
        return new ArrayList<>(Arrays.asList(
                new Sala(1, "Programación Móvil 6A", "GRUPO",   "PM", "Recuerden la entrega del viernes", "10:32", 3),
                new Sala(2, "Bases de Datos 4B",     "GRUPO",   "BD", "¿Hay duda sobre el ER?",           "Ayer",  0),
                new Sala(3, "Cálculo Integral 2A",   "GRUPO",   "CI", "Práctica resuelta arriba",         "Lun",   1),
                new Sala(4, "Ana López",             "PRIVADO", "AL", "Profe, una consulta…",             "11:05", 2),
                new Sala(5, "Diego Ruiz",            "PRIVADO", "DR", "Gracias por la asesoría",          "Mar",   0)
        ));
    }

    @Override
    public List<Mensaje> getMensajes(int salaId) throws Exception {
        Thread.sleep(300);
        switch (salaId) {
            case 1: return new ArrayList<>(Arrays.asList(
                    new Mensaje(1, "Buenos días, profe.",               "09:01", false, "Ana López",    "AL", false),
                    new Mensaje(2, "Buenos días a todos.",              "09:02", true,  "Yo",           "CB", false),
                    new Mensaje(2, "Buenos días a todos.",              "09:02", true,  "Yo",           "CB", false),
                    new Mensaje(2, "Buenos días a todos.",              "09:02", true,  "Yo",           "CB", false),
                    new Mensaje(2, "Buenos días a todos.",              "09:02", true,  "Yo",           "CB", false),
                    new Mensaje(2, "Buenos días a todos.",              "09:02", true,  "Yo",           "CB", false),
                    new Mensaje(2, "Buenos días a todos.",              "09:02", true,  "Yo",           "CB", false),
                    new Mensaje(2, "Buenos días a todos.",              "09:02", true,  "Yo",           "CB", false),
                    new Mensaje(2, "Buenos días a todos.",              "09:02", true,  "Yo",           "CB", false),
                    new Mensaje(2, "Buenos días a todos.",              "09:02", true,  "Yo",           "CB", false),
                    new Mensaje(2, "Buenos días a todos.",              "09:02", true,  "Yo",           "CB", false),
                    new Mensaje(2, "Buenos días a todos.",              "09:02", true,  "Yo",           "CB", false),
                    new Mensaje(2, "Buenos días a todos.",              "09:02", true,  "Yo",           "CB", false),
                    new Mensaje(2, "Buenos días a todos.",              "09:02", true,  "Yo",           "CB", false),
                    new Mensaje(2, "Buenos días a todos.",              "09:02", true,  "Yo",           "CB", false),
                    new Mensaje(2, "Buenos días a todos.",              "09:02", true,  "Yo",           "CB", false),
                    new Mensaje(2, "Buenos días a todos.",              "09:02", true,  "Yo",           "CB", false),
                    new Mensaje(2, "Buenos días a todos.",              "09:02", true,  "Yo",           "CB", false),
                    new Mensaje(2, "Buenos días a todos.",              "09:02", true,  "Yo",           "CB", false),
                    new Mensaje(3, "¿La entrega es por classroom?",     "09:05", false, "Bruno García", "BG", false),
                    new Mensaje(3, "¿La entrega es por classroom?",     "09:05", false, "Bruno García", "BG", false),
                    new Mensaje(3, "¿La entrega es por classroom?",     "09:05", false, "Bruno García", "BG", false),
                    new Mensaje(3, "¿La entrega es por classroom?",     "09:05", false, "Bruno García", "BG", false),
                    new Mensaje(3, "¿La entrega es por classroom?",     "09:05", false, "Bruno García", "BG", false),
                    new Mensaje(3, "¿La entrega es por classroom?",     "09:05", false, "Bruno García", "BG", false),
                    new Mensaje(3, "¿La entrega es por classroom?",     "09:05", false, "Bruno García", "BG", false),
                    new Mensaje(3, "¿La entrega es por classroom?",     "09:05", false, "Bruno García", "BG", false),
                    new Mensaje(4, "Sí, sólo desde la app.",            "09:06", true,  "Yo",           "CB", false),
                    new Mensaje(4, "Sí, sólo desde la app.",            "09:06", true,  "Yo",           "CB", false),
                    new Mensaje(4, "Sí, sólo desde la app.",            "09:06", true,  "Yo",           "CB", false),
                    new Mensaje(5, "¿Se puede en parejas?",             "10:10", false, "Carla Méndez", "CM", false),
                    new Mensaje(5, "¿Se puede en parejas?",             "10:10", false, "Carla Méndez", "CM", false),
                    new Mensaje(5, "¿Se puede en parejas?",             "10:10", false, "Carla Méndez", "CM", false),
                    new Mensaje(5, "¿Se puede en parejas?",             "10:10", false, "Carla Méndez", "CM", false),
                    new Mensaje(5, "¿Se puede en parejas?",             "10:10", false, "Carla Méndez", "CM", false),
                    new Mensaje(5, "¿Se puede en parejas?",             "10:10", false, "Carla Méndez", "CM", false),
                    new Mensaje(6, "Esta vez es individual.",           "10:12", true,  "Yo",           "CB", false),
                    new Mensaje(6, "Esta vez es individual.",           "10:12", true,  "Yo",           "CB", false),
                    new Mensaje(6, "Esta vez es individual.",           "10:12", true,  "Yo",           "CB", false),
                    new Mensaje(6, "Esta vez es individual.",           "10:12", true,  "Yo",           "CB", false),
                    new Mensaje(7, "Recuerden la entrega del viernes.", "10:30", true,  "Yo",           "CB", false),
                    new Mensaje(7, "Recuerden la entrega del viernes.", "10:30", true,  "Yo",           "CB", false),
                    new Mensaje(7, "Recuerden la entrega del viernes.", "10:30", true,  "Yo",           "CB", false),
                    new Mensaje(8, "¡Gracias profe!",                   "10:32", false, "Ana López",    "AL", false),
                    new Mensaje(8, "¡Gracias profe!",                   "10:32", false, "Ana López",    "AL", false)
            ));
            case 2: return new ArrayList<>(Arrays.asList(
                    new Mensaje(1, "Profe, ¿el ER lleva atributos derivados?", "08:30", false, "Lucía Pérez", "LP", false),
                    new Mensaje(2, "Sólo si los justifican.",                  "08:35", true,  "Yo",          "CB", false),
                    new Mensaje(3, "¿Cuántas entidades mínimo?",               "09:00", false, "Mario Gómez", "MG", false),
                    new Mensaje(4, "Mínimo 5, máximo 8.",                      "09:02", true,  "Yo",          "CB", false),
                    new Mensaje(5, "Ok profe.",                                "09:03", false, "Mario Gómez", "MG", false),
                    new Mensaje(6, "Subí un ejemplo en la plataforma.",        "11:00", true,  "Yo",          "CB", false),
                    new Mensaje(7, "¡Excelente, gracias!",                     "11:05", false, "Lucía Pérez", "LP", false),
                    new Mensaje(8, "¿Hay duda sobre el ER?",                   "Ayer",  true,  "Yo",          "CB", false)
            ));
            case 3: return new ArrayList<>(Arrays.asList(
                    new Mensaje(1, "Profe, ¿la práctica 5 era a mano?",   "07:50", false, "Sofía Ramos", "SR", false),
                    new Mensaje(2, "Sí, escaneada en PDF.",                "07:52", true,  "Yo",          "CB", false),
                    new Mensaje(3, "¿Hasta cuándo es la entrega?",        "07:55", false, "Iván Castro", "IC", false),
                    new Mensaje(4, "Hasta el lunes a las 23:59.",         "07:56", true,  "Yo",          "CB", false),
                    new Mensaje(5, "¡Perfecto!",                          "07:57", false, "Iván Castro", "IC", false),
                    new Mensaje(6, "Recuerden incluir el procedimiento.", "08:00", true,  "Yo",          "CB", false),
                    new Mensaje(7, "Subí la práctica resuelta arriba.",   "Lun",   true,  "Yo",          "CB", true),
                    new Mensaje(8, "Muchas gracias profe.",               "Lun",   false, "Sofía Ramos", "SR", false)
            ));
            case 4: return new ArrayList<>(Arrays.asList(
                    new Mensaje(1, "Profe, una consulta…",                "10:55", false, "Ana López", "AL", false),
                    new Mensaje(2, "Claro, dime.",                        "10:56", true,  "Yo",        "CB", false),
                    new Mensaje(3, "No me llegó el correo del proyecto.", "10:58", false, "Ana López", "AL", false),
                    new Mensaje(4, "Reviso ahorita en plataforma.",       "10:59", true,  "Yo",        "CB", false),
                    new Mensaje(5, "Te lo envío de nuevo.",               "11:01", true,  "Yo",        "CB", false),
                    new Mensaje(6, "¿Lo recibiste?",                      "11:03", true,  "Yo",        "CB", false),
                    new Mensaje(7, "¡Sí, ya llegó!",                      "11:04", false, "Ana López", "AL", false),
                    new Mensaje(8, "Profe, una consulta…",                "11:05", false, "Ana López", "AL", false)
            ));
            case 5: return new ArrayList<>(Arrays.asList(
                    new Mensaje(1, "Profe, ¿podemos tener asesoría?", "Lun", false, "Diego Ruiz", "DR", false),
                    new Mensaje(2, "Claro, ¿qué tema?",               "Lun", true,  "Yo",         "CB", false),
                    new Mensaje(3, "Integrales por partes.",           "Lun", false, "Diego Ruiz", "DR", false),
                    new Mensaje(4, "¿Te queda mañana 1pm?",           "Lun", true,  "Yo",         "CB", false),
                    new Mensaje(5, "Sí, perfecto.",                   "Lun", false, "Diego Ruiz", "DR", false),
                    new Mensaje(6, "Nos vemos en el cubículo.",        "Mar", true,  "Yo",         "CB", false),
                    new Mensaje(7, "Ahí estaré.",                     "Mar", false, "Diego Ruiz", "DR", false),
                    new Mensaje(8, "Gracias por la asesoría.",         "Mar", false, "Diego Ruiz", "DR", false)
            ));
            default: return new ArrayList<>();
        }
    }
}
