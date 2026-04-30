package com.conectatec.ui.docente.chat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatec.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MensajeDocenteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_TYPE_RECIBIDO = 0;
    public static final int VIEW_TYPE_ENVIADO  = 1;

    public static final class MensajeDummyDocente {
        public final int id;
        public final String texto;
        public final String hora;
        public final boolean esMio;
        public final String autorNombre;
        public final String autorIniciales;
        public final boolean tieneAdjunto;

        public MensajeDummyDocente(int id, String texto, String hora, boolean esMio,
                                   String autorNombre, String autorIniciales, boolean tieneAdjunto) {
            this.id = id;
            this.texto = texto;
            this.hora = hora;
            this.esMio = esMio;
            this.autorNombre = autorNombre;
            this.autorIniciales = autorIniciales;
            this.tieneAdjunto = tieneAdjunto;
        }
    }

    private final List<MensajeDummyDocente> mensajes;

    public MensajeDocenteAdapter(List<MensajeDummyDocente> mensajes) {
        this.mensajes = (mensajes != null) ? mensajes : new ArrayList<>();
    }

    public void agregarMensaje(MensajeDummyDocente m) {
        mensajes.add(m);
        notifyItemInserted(mensajes.size() - 1);
    }

    @Override
    public int getItemViewType(int position) {
        return mensajes.get(position).esMio ? VIEW_TYPE_ENVIADO : VIEW_TYPE_RECIBIDO;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_ENVIADO) {
            View v = inf.inflate(R.layout.item_mensaje_enviado, parent, false);
            return new EnviadoViewHolder(v);
        } else {
            View v = inf.inflate(R.layout.item_mensaje_recibido, parent, false);
            return new RecibidoViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MensajeDummyDocente m = mensajes.get(position);
        if (holder instanceof EnviadoViewHolder) {
            EnviadoViewHolder h = (EnviadoViewHolder) holder;
            h.tvTexto.setText(m.texto);
            h.tvHora.setText(m.hora);
        } else if (holder instanceof RecibidoViewHolder) {
            RecibidoViewHolder h = (RecibidoViewHolder) holder;
            h.tvAvatar.setText(m.autorIniciales);
            h.tvAutor.setText(m.autorNombre);
            h.tvTexto.setText(m.texto);
            h.tvHora.setText(m.hora);
        }
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    static class EnviadoViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTexto;
        final TextView tvHora;
        EnviadoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTexto = itemView.findViewById(R.id.tvTextoEnviado);
            tvHora  = itemView.findViewById(R.id.tvHoraEnviado);
        }
    }

    static class RecibidoViewHolder extends RecyclerView.ViewHolder {
        final TextView tvAvatar;
        final TextView tvAutor;
        final TextView tvTexto;
        final TextView tvHora;
        RecibidoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAvatar = itemView.findViewById(R.id.tvAvatarRecibido);
            tvAutor  = itemView.findViewById(R.id.tvAutorRecibido);
            tvTexto  = itemView.findViewById(R.id.tvTextoRecibido);
            tvHora   = itemView.findViewById(R.id.tvHoraRecibido);
        }
    }

    public static List<MensajeDummyDocente> cargarParaSala(int salaId) {
        switch (salaId) {
            case 1:
                return new ArrayList<>(Arrays.asList(
                        new MensajeDummyDocente(1, "Buenos días, profe.",                "09:01", false, "Ana López",    "AL", false),
                        new MensajeDummyDocente(2, "Buenos días a todos.",               "09:02", true,  "Yo",           "CB", false),
                        new MensajeDummyDocente(3, "¿La entrega es por classroom?",      "09:05", false, "Bruno García", "BG", false),
                        new MensajeDummyDocente(4, "Sí, sólo desde la app.",             "09:06", true,  "Yo",           "CB", false),
                        new MensajeDummyDocente(5, "¿Se puede en parejas?",              "10:10", false, "Carla Méndez", "CM", false),
                        new MensajeDummyDocente(6, "Esta vez es individual.",            "10:12", true,  "Yo",           "CB", false),
                        new MensajeDummyDocente(7, "Recuerden la entrega del viernes.",  "10:30", true,  "Yo",           "CB", false),
                        new MensajeDummyDocente(8, "¡Gracias profe!",                    "10:32", false, "Ana López",    "AL", false)
                ));
            case 2:
                return new ArrayList<>(Arrays.asList(
                        new MensajeDummyDocente(1, "Profe, ¿el ER lleva atributos derivados?", "08:30", false, "Lucía Pérez", "LP", false),
                        new MensajeDummyDocente(2, "Sólo si los justifican.",                  "08:35", true,  "Yo",          "CB", false),
                        new MensajeDummyDocente(3, "¿Cuántas entidades mínimo?",               "09:00", false, "Mario Gómez", "MG", false),
                        new MensajeDummyDocente(4, "Mínimo 5, máximo 8.",                      "09:02", true,  "Yo",          "CB", false),
                        new MensajeDummyDocente(5, "Ok profe.",                                "09:03", false, "Mario Gómez", "MG", false),
                        new MensajeDummyDocente(6, "Subí un ejemplo en la plataforma.",        "11:00", true,  "Yo",          "CB", false),
                        new MensajeDummyDocente(7, "¡Excelente, gracias!",                     "11:05", false, "Lucía Pérez", "LP", false),
                        new MensajeDummyDocente(8, "¿Hay duda sobre el ER?",                   "Ayer",  true,  "Yo",          "CB", false)
                ));
            case 3:
                return new ArrayList<>(Arrays.asList(
                        new MensajeDummyDocente(1, "Profe, ¿la práctica 5 era a mano?",     "07:50", false, "Sofía Ramos", "SR", false),
                        new MensajeDummyDocente(2, "Sí, escaneada en PDF.",                  "07:52", true,  "Yo",          "CB", false),
                        new MensajeDummyDocente(3, "¿Hasta cuándo es la entrega?",           "07:55", false, "Iván Castro", "IC", false),
                        new MensajeDummyDocente(4, "Hasta el lunes a las 23:59.",            "07:56", true,  "Yo",          "CB", false),
                        new MensajeDummyDocente(5, "¡Perfecto!",                             "07:57", false, "Iván Castro", "IC", false),
                        new MensajeDummyDocente(6, "Recuerden incluir el procedimiento.",    "08:00", true,  "Yo",          "CB", false),
                        new MensajeDummyDocente(7, "Subí la práctica resuelta arriba.",      "Lun",   true,  "Yo",          "CB", true),
                        new MensajeDummyDocente(8, "Muchas gracias profe.",                  "Lun",   false, "Sofía Ramos", "SR", false)
                ));
            case 4:
                return new ArrayList<>(Arrays.asList(
                        new MensajeDummyDocente(1, "Profe, una consulta…",                   "10:55", false, "Ana López", "AL", false),
                        new MensajeDummyDocente(2, "Claro, dime.",                           "10:56", true,  "Yo",        "CB", false),
                        new MensajeDummyDocente(3, "No me llegó el correo del proyecto.",    "10:58", false, "Ana López", "AL", false),
                        new MensajeDummyDocente(4, "Reviso ahorita en plataforma.",          "10:59", true,  "Yo",        "CB", false),
                        new MensajeDummyDocente(5, "Te lo envío de nuevo.",                  "11:01", true,  "Yo",        "CB", false),
                        new MensajeDummyDocente(6, "¿Lo recibiste?",                         "11:03", true,  "Yo",        "CB", false),
                        new MensajeDummyDocente(7, "¡Sí, ya llegó!",                         "11:04", false, "Ana López", "AL", false),
                        new MensajeDummyDocente(8, "Profe, una consulta…",                   "11:05", false, "Ana López", "AL", false)
                ));
            case 5:
                return new ArrayList<>(Arrays.asList(
                        new MensajeDummyDocente(1, "Profe, ¿podemos tener asesoría?",        "Lun", false, "Diego Ruiz", "DR", false),
                        new MensajeDummyDocente(2, "Claro, ¿qué tema?",                      "Lun", true,  "Yo",         "CB", false),
                        new MensajeDummyDocente(3, "Integrales por partes.",                 "Lun", false, "Diego Ruiz", "DR", false),
                        new MensajeDummyDocente(4, "¿Te queda mañana 1pm?",                  "Lun", true,  "Yo",         "CB", false),
                        new MensajeDummyDocente(5, "Sí, perfecto.",                          "Lun", false, "Diego Ruiz", "DR", false),
                        new MensajeDummyDocente(6, "Nos vemos en el cubículo.",              "Mar", true,  "Yo",         "CB", false),
                        new MensajeDummyDocente(7, "Ahí estaré.",                            "Mar", false, "Diego Ruiz", "DR", false),
                        new MensajeDummyDocente(8, "Gracias por la asesoría.",               "Mar", false, "Diego Ruiz", "DR", false)
                ));
            default:
                return new ArrayList<>();
        }
    }
}
