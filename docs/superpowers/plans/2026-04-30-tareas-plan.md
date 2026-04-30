# Módulo Tareas Docente Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implementar el módulo Tareas del Docente con drill-down completo: Grupo → Bloque → Tareas → (Crear | Entregas → Calificar). 6 fragments, 4 adapters, datos dummy estáticos.

**Architecture:** Espejo estructural del módulo Actividades del Admin con drill-down extra. Datos dummy locales por adapter (clase interna estática `*DummyDocente`). Navegación con `Navigation.findNavController().navigate(actionId, args)`. Sin `NavOptions` en sub-destinos (la animación viene del XML).

**Tech Stack:** Java 11, Hilt (`@AndroidEntryPoint`), ViewBinding, Navigation Component (acciones declaradas en `nav_docente.xml`), `DatePickerDialog` (Android nativo), `LinearProgressIndicator` (Material 3), `Snackbar`, ConectaTec drawables (`bg_chip_*`, `bg_avatar_placeholder`, `bg_card`, `ic_plus`, `ic_paperclip`, `ic_arrow_left`).

**Depende de:**
- **Plan 1 (infra):** `nav_docente.xml` con todas las acciones del módulo Tareas declaradas (`action_tareas_to_bloques`, `action_bloques_to_tareas_bloque`, `action_tareas_bloque_to_crear`, `action_tareas_bloque_to_entregas`, `action_entregas_to_calificar`); destinos `docenteTareasFragment`, `docenteBloquesFragment`, `docenteTareasBloqueFragment`, `docenteCrearTareaFragment`, `docenteEntregasFragment`, `docenteCalificarEntregaFragment`; drawables `ic_plus`, `ic_paperclip`, `ic_arrow_left`; mapeo de sub-destinos del módulo Tareas a tab índice 2 en `MainDocenteActivity.addOnDestinationChangedListener`.
- **Plan 3 (grupos):** existencia de `item_grupo_docente.xml` (reutilizado por `GrupoTareasDocenteAdapter`) y del dataset canónico de los 3 grupos del docente.

**REGLA DE COORDINACIÓN CON PLAN 3 (CRÍTICA):** los 3 grupos cargados en `GrupoTareasDocenteAdapter.cargarDatosDummy()` DEBEN coincidir EXACTAMENTE con el dataset del `GrupoDocenteAdapter` del Plan 3 en los campos `id`, `nombre`, `materia`, `totalAlumnos` y `fechaCreacion`:

| id | nombre | materia | totalAlumnos | fechaCreacion |
|---|---|---|---|---|
| 1 | Programación Móvil 6A | Programación Móvil | 18 | 01/02/2026 |
| 2 | Bases de Datos 4B | Bases de Datos | 16 | 15/01/2026 |
| 3 | Cálculo Integral 2A | Cálculo Integral | 13 | 20/01/2026 |

`GrupoTareasDocenteAdapter` NO incluye `codigoUnion` (no se usa en el flujo de Tareas). Verificación con `grep` al final del plan.

---

## Estructura de archivos

### Java a crear (10)
- `app/src/main/java/com/conectatec/ui/docente/tareas/DocenteTareasFragment.java` *(reemplaza stub si existe)*
- `app/src/main/java/com/conectatec/ui/docente/tareas/DocenteBloquesFragment.java`
- `app/src/main/java/com/conectatec/ui/docente/tareas/DocenteTareasBloqueFragment.java`
- `app/src/main/java/com/conectatec/ui/docente/tareas/DocenteCrearTareaFragment.java`
- `app/src/main/java/com/conectatec/ui/docente/tareas/DocenteEntregasFragment.java`
- `app/src/main/java/com/conectatec/ui/docente/tareas/DocenteCalificarEntregaFragment.java`
- `app/src/main/java/com/conectatec/ui/docente/tareas/adapter/GrupoTareasDocenteAdapter.java`
- `app/src/main/java/com/conectatec/ui/docente/tareas/adapter/BloqueDocenteAdapter.java`
- `app/src/main/java/com/conectatec/ui/docente/tareas/adapter/TareaDocenteAdapter.java`
- `app/src/main/java/com/conectatec/ui/docente/tareas/adapter/EntregaDocenteAdapter.java`

### Layouts a crear (9)
- `app/src/main/res/layout/fragment_docente_tareas.xml`
- `app/src/main/res/layout/fragment_docente_bloques.xml`
- `app/src/main/res/layout/fragment_docente_tareas_bloque.xml`
- `app/src/main/res/layout/fragment_docente_crear_tarea.xml`
- `app/src/main/res/layout/fragment_docente_entregas.xml`
- `app/src/main/res/layout/fragment_docente_calificar_entrega.xml`
- `app/src/main/res/layout/item_bloque_docente.xml`
- `app/src/main/res/layout/item_tarea_docente.xml`
- `app/src/main/res/layout/item_entrega_docente.xml`

### Layouts reutilizados (NO duplicar)
- `app/src/main/res/layout/item_grupo_docente.xml` — del Plan 3, usado por `GrupoTareasDocenteAdapter`.
- `app/src/main/res/layout/layout_empty_state.xml` — para estados vacíos.

### Recursos a modificar
- `app/src/main/res/values/strings.xml` — agregar ~40 strings nuevos.

---

## Tareas

### Task 1: Strings del módulo Tareas

**Files:**
- Modify: `app/src/main/res/values/strings.xml`

- [ ] **Step 1: Agregar el bloque de strings al final de `strings.xml`, antes de `</resources>`**

```xml
    <!-- ════════════ Módulo Tareas Docente ════════════ -->
    <!-- Subtítulos / títulos -->
    <string name="subtitle_docente_tareas">Selecciona un grupo</string>
    <string name="title_bloques">Bloques</string>
    <string name="title_tareas_bloque">Tareas del bloque</string>
    <string name="title_nueva_tarea">Nueva tarea</string>
    <string name="title_entregas">Entregas</string>
    <string name="title_calificar">Calificar entrega</string>

    <!-- Botones -->
    <string name="btn_crear_tarea">Nueva tarea</string>
    <string name="btn_publicar_tarea">Publicar tarea</string>
    <string name="btn_calificar">Guardar calificación</string>
    <string name="btn_actualizar_calificacion">Actualizar calificación</string>

    <!-- Hints de inputs -->
    <string name="hint_titulo_tarea">Título</string>
    <string name="hint_descripcion_tarea">Descripción</string>
    <string name="hint_fecha_limite">Fecha límite</string>
    <string name="hint_calificacion_max">Calificación máxima</string>
    <string name="hint_calificacion">Calificación (0-100)</string>
    <string name="hint_retroalimentacion">Retroalimentación</string>

    <!-- Tipos de tarea -->
    <string name="label_tipo_tarea">TAREA</string>
    <string name="label_tipo_trabajo">TRABAJO</string>
    <string name="label_tipo_examen">EXAMEN</string>
    <string name="label_tipo_proyecto">PROYECTO</string>

    <!-- Estados de entrega -->
    <string name="label_estado_borrador">BORRADOR</string>
    <string name="label_estado_entregada">ENTREGADA</string>
    <string name="label_estado_calificada">CALIFICADA</string>
    <string name="label_estado_sin_entregar">SIN ENTREGAR</string>

    <!-- Etiquetas varias -->
    <string name="label_archivo_adjunto">documento.pdf</string>
    <string name="label_progreso_entregas">Progreso de entregas</string>
    <string name="label_total_alumnos">Total</string>
    <string name="label_entregadas">Entregadas</string>
    <string name="label_pendientes">Pendientes</string>

    <!-- Chips de tipo -->
    <string name="chip_tipo_todas">Todas</string>
    <string name="chip_tipo_tarea">Tarea</string>
    <string name="chip_tipo_trabajo">Trabajo</string>
    <string name="chip_tipo_examen">Examen</string>
    <string name="chip_tipo_proyecto">Proyecto</string>

    <!-- Chips de estado de tarea -->
    <string name="chip_estado_todas">Todas</string>
    <string name="chip_estado_en_curso">En curso</string>
    <string name="chip_estado_vencida">Vencida</string>
    <string name="chip_estado_completada">Completada</string>

    <!-- Chips de filtro de entregas -->
    <string name="chip_entregas_todas">Todas</string>
    <string name="chip_entregas_pendientes">Pendientes</string>
    <string name="chip_entregas_entregadas">Entregadas</string>
    <string name="chip_entregas_calificadas">Calificadas</string>
    <string name="chip_entregas_sin_entregar">Sin entregar</string>

    <!-- Sufijos / formatos -->
    <string name="tareas_sufijo_entregas">%1$d/%2$d entregas</string>
    <string name="bloque_sufijo_tareas">%1$d tareas</string>
    <string name="bloque_nombre_formato">Bloque %1$d</string>
```

- [ ] **Step 2: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
git add app/src/main/res/values/strings.xml
git commit -m "feat(docente-tareas): agregar strings del módulo tareas"
```

---

### Task 2: Verificar reutilización de `item_grupo_docente.xml`

**Files:**
- Verify: `app/src/main/res/layout/item_grupo_docente.xml` (debe existir, creado por Plan 3)

- [ ] **Step 1: Confirmar que el archivo existe**

Run: `test -f app/src/main/res/layout/item_grupo_docente.xml && echo OK || echo MISSING`
Expected: `OK`

- [ ] **Step 2: Confirmar que contiene los IDs `tvNombreGrupoDocenteItem`, `tvMateriaGrupoDocenteItem`, `tvAlumnosGrupoDocenteItem` y `tvFechaCreacionGrupoDocenteItem`**

Run: `grep -E "tvNombreGrupoDocenteItem|tvMateriaGrupoDocenteItem|tvAlumnosGrupoDocenteItem|tvFechaCreacionGrupoDocenteItem" app/src/main/res/layout/item_grupo_docente.xml | wc -l`
Expected: valor `>= 4`.

Si `MISSING` o el grep no encuentra los IDs, **detener este plan**: el Plan 3 debe ejecutarse antes (o el adapter creado en Task 6 referenciará campos inexistentes). Si el Plan 3 usa otros nombres de IDs, ajustar `GrupoTareasDocenteAdapter` en Task 6 a esos IDs (no es necesario tocar el XML).

- [ ] **Step 3: Sin commit (paso de verificación)**

---

### Task 3: Item layout `item_bloque_docente.xml`

**Files:**
- Create: `app/src/main/res/layout/item_bloque_docente.xml`

- [ ] **Step 1: Crear el archivo con el contenido completo**

```xml
<?xml version="1.0" encoding="utf-8"?>
<com.google.android.material.card.MaterialCardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginBottom="12dp"
    android:clickable="true"
    android:focusable="true"
    app:cardBackgroundColor="@color/colorSurface"
    app:cardCornerRadius="12dp"
    app:cardElevation="0dp"
    app:strokeWidth="0dp">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:gravity="center_vertical"
        android:padding="20dp">

        <TextView
            android:id="@+id/tvNumeroBloque"
            android:layout_width="56dp"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:text="1"
            android:textColor="@color/colorPrimary"
            android:textSize="36sp"
            android:textStyle="bold"
            android:fontFamily="sans-serif-medium"
            android:layout_marginEnd="16dp" />

        <LinearLayout
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:orientation="vertical">

            <TextView
                android:id="@+id/tvNombreBloque"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Bloque 1"
                android:textColor="@color/colorOnSurface"
                android:textSize="16sp"
                android:textStyle="bold"
                android:fontFamily="sans-serif-medium" />

            <TextView
                android:id="@+id/tvTotalTareasBloque"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="2dp"
                android:text="3 tareas"
                android:textColor="@color/colorOnSurfaceVariant"
                android:textSize="12sp" />

        </LinearLayout>

        <ImageView
            android:layout_width="20dp"
            android:layout_height="20dp"
            android:src="@drawable/ic_arrow_left"
            android:rotation="180"
            android:contentDescription="Abrir bloque"
            app:tint="@color/colorOnSurfaceVariant" />

    </LinearLayout>

</com.google.android.material.card.MaterialCardView>
```

- [ ] **Step 2: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Confirmar cero hex literales**

Run: `grep -E "#[0-9A-Fa-f]{6}" app/src/main/res/layout/item_bloque_docente.xml`
Expected: salida vacía.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/res/layout/item_bloque_docente.xml
git commit -m "feat(docente-tareas): item layout de bloque con número grande y flecha"
```

---

### Task 4: Item layout `item_tarea_docente.xml`

**Files:**
- Create: `app/src/main/res/layout/item_tarea_docente.xml`

- [ ] **Step 1: Crear el archivo**

```xml
<?xml version="1.0" encoding="utf-8"?>
<com.google.android.material.card.MaterialCardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginBottom="10dp"
    android:clickable="true"
    android:focusable="true"
    app:cardBackgroundColor="@color/colorSurface"
    app:cardCornerRadius="12dp"
    app:cardElevation="0dp"
    app:strokeWidth="0dp">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:paddingHorizontal="16dp"
        android:paddingTop="14dp"
        android:paddingBottom="14dp">

        <!-- Fila 1: chip tipo + chip estado -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            android:gravity="center_vertical"
            android:layout_marginBottom="8dp">

            <TextView
                android:id="@+id/tvChipTipoTarea"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="TAREA"
                android:textColor="@color/white"
                android:textSize="9sp"
                android:textStyle="bold"
                android:letterSpacing="0.05"
                android:paddingHorizontal="8dp"
                android:paddingVertical="3dp"
                android:background="@drawable/bg_chip_docente" />

            <View
                android:layout_width="0dp"
                android:layout_height="1dp"
                android:layout_weight="1" />

            <TextView
                android:id="@+id/tvChipEstadoTarea"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="EN CURSO"
                android:textColor="@color/white"
                android:textSize="9sp"
                android:textStyle="bold"
                android:letterSpacing="0.05"
                android:paddingHorizontal="8dp"
                android:paddingVertical="3dp"
                android:background="@drawable/bg_chip_docente" />

        </LinearLayout>

        <!-- Fila 2: título -->
        <TextView
            android:id="@+id/tvTituloTarea"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Práctica 1: Layouts"
            android:textColor="@color/colorOnSurface"
            android:textSize="15sp"
            android:textStyle="bold"
            android:fontFamily="sans-serif-medium"
            android:layout_marginBottom="6dp" />

        <!-- Fila 3: fecha vence + entregas -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            android:gravity="center_vertical">

            <TextView
                android:id="@+id/tvFechaVenceTarea"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:text="Vence: 10/05/2026"
                android:textColor="@color/colorOnSurfaceVariant"
                android:textSize="12sp" />

            <TextView
                android:id="@+id/tvEntregasTarea"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="12/18 entregas"
                android:textColor="@color/colorOnSurfaceVariant"
                android:textSize="12sp"
                android:textStyle="bold" />

        </LinearLayout>

    </LinearLayout>

</com.google.android.material.card.MaterialCardView>
```

- [ ] **Step 2: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Cero hex**

Run: `grep -E "#[0-9A-Fa-f]{6}" app/src/main/res/layout/item_tarea_docente.xml`
Expected: salida vacía.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/res/layout/item_tarea_docente.xml
git commit -m "feat(docente-tareas): item layout de tarea con chips de tipo y estado"
```

---

### Task 5: Item layout `item_entrega_docente.xml`

**Files:**
- Create: `app/src/main/res/layout/item_entrega_docente.xml`

- [ ] **Step 1: Crear el archivo**

```xml
<?xml version="1.0" encoding="utf-8"?>
<com.google.android.material.card.MaterialCardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginBottom="10dp"
    android:clickable="true"
    android:focusable="true"
    app:cardBackgroundColor="@color/colorSurface"
    app:cardCornerRadius="12dp"
    app:cardElevation="0dp"
    app:strokeWidth="0dp">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:gravity="center_vertical"
        android:paddingHorizontal="16dp"
        android:paddingTop="12dp"
        android:paddingBottom="12dp">

        <FrameLayout
            android:layout_width="40dp"
            android:layout_height="40dp"
            android:layout_marginEnd="14dp"
            android:background="@drawable/bg_avatar_placeholder">

            <TextView
                android:id="@+id/tvInicialesEntrega"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_gravity="center"
                android:text="AL"
                android:textColor="@color/colorOnSurface"
                android:textSize="13sp"
                android:textStyle="bold" />

        </FrameLayout>

        <LinearLayout
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:orientation="vertical">

            <TextView
                android:id="@+id/tvNombreEntrega"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Ana López"
                android:textColor="@color/colorOnSurface"
                android:textSize="14sp"
                android:textStyle="bold"
                android:fontFamily="sans-serif-medium" />

            <TextView
                android:id="@+id/tvFechaEntrega"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="2dp"
                android:text="Entregada: 05/05/2026"
                android:textColor="@color/colorOnSurfaceVariant"
                android:textSize="11sp" />

        </LinearLayout>

        <LinearLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:gravity="end">

            <TextView
                android:id="@+id/tvChipEstadoEntrega"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="ENTREGADA"
                android:textColor="@color/white"
                android:textSize="9sp"
                android:textStyle="bold"
                android:letterSpacing="0.05"
                android:paddingHorizontal="8dp"
                android:paddingVertical="3dp"
                android:background="@drawable/bg_chip_estudiante" />

            <TextView
                android:id="@+id/tvCalificacionEntrega"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="6dp"
                android:layout_gravity="end"
                android:text="85/100"
                android:textColor="@color/colorSuccess"
                android:textSize="12sp"
                android:textStyle="bold"
                android:visibility="gone" />

        </LinearLayout>

    </LinearLayout>

</com.google.android.material.card.MaterialCardView>
```

- [ ] **Step 2: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Cero hex**

Run: `grep -E "#[0-9A-Fa-f]{6}" app/src/main/res/layout/item_entrega_docente.xml`
Expected: salida vacía.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/res/layout/item_entrega_docente.xml
git commit -m "feat(docente-tareas): item layout de entrega con avatar, estado y calificación"
```

---

### Task 6: Adapter `GrupoTareasDocenteAdapter`

**Files:**
- Create: `app/src/main/java/com/conectatec/ui/docente/tareas/adapter/GrupoTareasDocenteAdapter.java`

- [ ] **Step 1: Crear el adapter completo**

```java
package com.conectatec.ui.docente.tareas.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatec.databinding.ItemGrupoDocenteBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter que lista los grupos del docente como entry-point del módulo Tareas.
 * Reutiliza el layout {@code item_grupo_docente.xml} (creado en Plan 3).
 *
 * Los IDs, nombres, materias, totalAlumnos y fechaCreacion DEBEN coincidir
 * EXACTAMENTE con los datos del GrupoDocenteAdapter del Plan 3.
 */
public class GrupoTareasDocenteAdapter
        extends RecyclerView.Adapter<GrupoTareasDocenteAdapter.ViewHolder> {

    public static class GrupoDummyTareas {
        public final int id;
        public final String nombre;
        public final String materia;
        public final int totalAlumnos;
        public final String fechaCreacion;
        public final boolean activo;

        public GrupoDummyTareas(int id, String nombre, String materia,
                                int totalAlumnos, String fechaCreacion, boolean activo) {
            this.id = id;
            this.nombre = nombre;
            this.materia = materia;
            this.totalAlumnos = totalAlumnos;
            this.fechaCreacion = fechaCreacion;
            this.activo = activo;
        }
    }

    public interface OnGrupoClickListener {
        void onClick(GrupoDummyTareas grupo);
    }

    private final List<GrupoDummyTareas> lista = new ArrayList<>();
    private OnGrupoClickListener listener;

    public GrupoTareasDocenteAdapter() {
        cargarDatosDummy();
    }

    public void setOnClickListener(OnGrupoClickListener l) {
        this.listener = l;
    }

    public int conteo() {
        return lista.size();
    }

    private void cargarDatosDummy() {
        lista.clear();
        // CRÍTICO: deben coincidir con GrupoDocenteAdapter del Plan 3.
        lista.add(new GrupoDummyTareas(1,
                "Programación Móvil 6A", "Programación Móvil",
                18, "01/02/2026", true));
        lista.add(new GrupoDummyTareas(2,
                "Bases de Datos 4B", "Bases de Datos",
                16, "15/01/2026", true));
        lista.add(new GrupoDummyTareas(3,
                "Cálculo Integral 2A", "Cálculo Integral",
                13, "20/01/2026", true));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemGrupoDocenteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        GrupoDummyTareas g = lista.get(position);
        h.bind(g);
        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(g);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemGrupoDocenteBinding b;

        ViewHolder(ItemGrupoDocenteBinding binding) {
            super(binding.getRoot());
            this.b = binding;
        }

        void bind(GrupoDummyTareas g) {
            b.tvNombreGrupoDocenteItem.setText(g.nombre);
            b.tvMateriaGrupoDocenteItem.setText(g.materia);
            b.tvAlumnosGrupoDocenteItem.setText(String.valueOf(g.totalAlumnos));
            b.tvFechaCreacionGrupoDocenteItem.setText(g.fechaCreacion);
        }
    }
}
```

> **NOTA:** si el `item_grupo_docente.xml` del Plan 3 usa otros IDs distintos a `tvNombreGrupoDocenteItem`, `tvMateriaGrupoDocenteItem`, `tvAlumnosGrupoDocenteItem`, `tvFechaCreacionGrupoDocenteItem`, ajustar el método `bind()` arriba para que coincida (sin tocar el XML del Plan 3).

- [ ] **Step 2: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/conectatec/ui/docente/tareas/adapter/GrupoTareasDocenteAdapter.java
git commit -m "feat(docente-tareas): adapter de grupos para entry del módulo tareas"
```

---

### Task 7: Adapter `BloqueDocenteAdapter`

**Files:**
- Create: `app/src/main/java/com/conectatec/ui/docente/tareas/adapter/BloqueDocenteAdapter.java`

- [ ] **Step 1: Crear el adapter**

```java
package com.conectatec.ui.docente.tareas.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatec.R;
import com.conectatec.databinding.ItemBloqueDocenteBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter que muestra los 3 bloques de un grupo del docente.
 * El total de tareas por bloque depende del grupoId (lo carga el Fragment).
 */
public class BloqueDocenteAdapter
        extends RecyclerView.Adapter<BloqueDocenteAdapter.ViewHolder> {

    public static class BloqueDummyDocente {
        public final int id;
        public final int numero;
        public final String nombre;
        public final int totalTareas;

        public BloqueDummyDocente(int id, int numero, String nombre, int totalTareas) {
            this.id = id;
            this.numero = numero;
            this.nombre = nombre;
            this.totalTareas = totalTareas;
        }
    }

    public interface OnBloqueClickListener {
        void onClick(BloqueDummyDocente bloque);
    }

    private final List<BloqueDummyDocente> lista = new ArrayList<>();
    private OnBloqueClickListener listener;

    public BloqueDocenteAdapter() { }

    public void setOnClickListener(OnBloqueClickListener l) {
        this.listener = l;
    }

    /**
     * Carga los 3 bloques con totales calculados según el grupoId.
     * Conteos derivados del dataset oficial del módulo Tareas:
     *  - Grupo 1: bloque1=2, bloque2=1, bloque3=1
     *  - Grupo 2: bloque1=1, bloque2=1, bloque3=0
     *  - Grupo 3: bloque1=1, bloque2=1, bloque3=0
     */
    public void cargarParaGrupo(int grupoId) {
        lista.clear();
        int t1, t2, t3;
        switch (grupoId) {
            case 1: t1 = 2; t2 = 1; t3 = 1; break;
            case 2: t1 = 1; t2 = 1; t3 = 0; break;
            case 3: t1 = 1; t2 = 1; t3 = 0; break;
            default: t1 = 0; t2 = 0; t3 = 0; break;
        }
        lista.add(new BloqueDummyDocente(1, 1, "Bloque 1", t1));
        lista.add(new BloqueDummyDocente(2, 2, "Bloque 2", t2));
        lista.add(new BloqueDummyDocente(3, 3, "Bloque 3", t3));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemBloqueDocenteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        BloqueDummyDocente b = lista.get(position);
        h.bind(b);
        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(b);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemBloqueDocenteBinding b;

        ViewHolder(ItemBloqueDocenteBinding binding) {
            super(binding.getRoot());
            this.b = binding;
        }

        void bind(BloqueDummyDocente bl) {
            b.tvNumeroBloque.setText(String.valueOf(bl.numero));
            b.tvNombreBloque.setText(bl.nombre);
            b.tvTotalTareasBloque.setText(b.getRoot().getContext()
                    .getString(R.string.bloque_sufijo_tareas, bl.totalTareas));
        }
    }
}
```

- [ ] **Step 2: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/conectatec/ui/docente/tareas/adapter/BloqueDocenteAdapter.java
git commit -m "feat(docente-tareas): adapter de bloques con totales derivados por grupo"
```

---

### Task 8: Adapter `TareaDocenteAdapter` (dataset 8 tareas)

**Files:**
- Create: `app/src/main/java/com/conectatec/ui/docente/tareas/adapter/TareaDocenteAdapter.java`

- [ ] **Step 1: Crear el adapter completo con dataset y filtros**

```java
package com.conectatec.ui.docente.tareas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatec.R;
import com.conectatec.databinding.ItemTareaDocenteBinding;

import java.util.ArrayList;
import java.util.List;

public class TareaDocenteAdapter
        extends RecyclerView.Adapter<TareaDocenteAdapter.ViewHolder> {

    // ── Constantes ────────────────────────────────────────────────────────────
    public static final String TIPO_TAREA    = "TAREA";
    public static final String TIPO_TRABAJO  = "TRABAJO";
    public static final String TIPO_EXAMEN   = "EXAMEN";
    public static final String TIPO_PROYECTO = "PROYECTO";

    public static final String EST_EN_CURSO  = "EN_CURSO";
    public static final String EST_VENCIDA   = "VENCIDA";
    public static final String EST_COMPLETADA = "COMPLETADA";

    // ── Modelo ────────────────────────────────────────────────────────────────
    public static class TareaDummyDocente {
        public final int id;
        public final String titulo;
        public final String tipo;     // TIPO_*
        public final String estado;   // EST_*
        public final int grupoId;
        public final int bloqueId;
        public final String fechaPublicacion;
        public final String fechaVence;
        public final int totalAlumnos;
        public final int entregadas;

        public TareaDummyDocente(int id, String titulo, String tipo, String estado,
                                 int grupoId, int bloqueId, String fechaPublicacion,
                                 String fechaVence, int totalAlumnos, int entregadas) {
            this.id = id;
            this.titulo = titulo;
            this.tipo = tipo;
            this.estado = estado;
            this.grupoId = grupoId;
            this.bloqueId = bloqueId;
            this.fechaPublicacion = fechaPublicacion;
            this.fechaVence = fechaVence;
            this.totalAlumnos = totalAlumnos;
            this.entregadas = entregadas;
        }
    }

    // ── Dataset oficial del módulo (8 tareas) ─────────────────────────────────
    public static final List<TareaDummyDocente> DATASET = new ArrayList<>();
    static {
        DATASET.add(new TareaDummyDocente(1, "Práctica 1: Layouts",
                TIPO_TAREA, EST_EN_CURSO, 1, 1,
                "01/02/2026", "10/05/2026", 18, 12));
        DATASET.add(new TareaDummyDocente(2, "Examen Parcial 1",
                TIPO_EXAMEN, EST_COMPLETADA, 1, 1,
                "15/02/2026", "28/02/2026", 18, 18));
        DATASET.add(new TareaDummyDocente(3, "Proyecto Final",
                TIPO_PROYECTO, EST_EN_CURSO, 1, 2,
                "01/03/2026", "30/05/2026", 18, 5));
        DATASET.add(new TareaDummyDocente(4, "Trabajo de investigación",
                TIPO_TRABAJO, EST_EN_CURSO, 1, 3,
                "10/04/2026", "25/05/2026", 18, 8));
        DATASET.add(new TareaDummyDocente(5, "Tarea 1: Modelo ER",
                TIPO_TAREA, EST_COMPLETADA, 2, 1,
                "20/01/2026", "05/02/2026", 16, 16));
        DATASET.add(new TareaDummyDocente(6, "Examen parcial 2",
                TIPO_EXAMEN, EST_EN_CURSO, 2, 2,
                "15/03/2026", "12/05/2026", 16, 0));
        DATASET.add(new TareaDummyDocente(7, "Tarea 5: Integrales",
                TIPO_TAREA, EST_VENCIDA, 3, 1,
                "01/02/2026", "20/04/2026", 13, 9));
        DATASET.add(new TareaDummyDocente(8, "Tarea 6: Series",
                TIPO_TAREA, EST_EN_CURSO, 3, 2,
                "10/04/2026", "08/05/2026", 13, 4));
    }

    public static TareaDummyDocente buscarPorId(int id) {
        for (TareaDummyDocente t : DATASET) {
            if (t.id == id) return t;
        }
        return null;
    }

    // ── Listener ──────────────────────────────────────────────────────────────
    public interface OnTareaClickListener {
        void onClick(TareaDummyDocente tarea);
    }

    // ── Estado interno ────────────────────────────────────────────────────────
    private final List<TareaDummyDocente> lista = new ArrayList<>();
    private final List<TareaDummyDocente> listaCompleta = new ArrayList<>();
    private OnTareaClickListener listener;

    private String filtroTipo = null;     // null = todas
    private String filtroEstado = null;   // null = todas

    public TareaDocenteAdapter() { }

    public void setOnClickListener(OnTareaClickListener l) {
        this.listener = l;
    }

    public int conteo() {
        return lista.size();
    }

    /** Carga todas las tareas que pertenecen al par (grupoId, bloqueId). */
    public void cargarParaBloque(int grupoId, int bloqueId) {
        listaCompleta.clear();
        for (TareaDummyDocente t : DATASET) {
            if (t.grupoId == grupoId && t.bloqueId == bloqueId) {
                listaCompleta.add(t);
            }
        }
        aplicarFiltros();
    }

    public void filtrar(@Nullable String tipo, @Nullable String estado) {
        this.filtroTipo = tipo;
        this.filtroEstado = estado;
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        lista.clear();
        for (TareaDummyDocente t : listaCompleta) {
            boolean okTipo = (filtroTipo == null) || filtroTipo.equals(t.tipo);
            boolean okEst  = (filtroEstado == null) || filtroEstado.equals(t.estado);
            if (okTipo && okEst) lista.add(t);
        }
        notifyDataSetChanged();
    }

    // ── RecyclerView.Adapter ──────────────────────────────────────────────────
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemTareaDocenteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        TareaDummyDocente t = lista.get(position);
        h.bind(t);
        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(t);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    // ── ViewHolder ────────────────────────────────────────────────────────────
    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTareaDocenteBinding b;

        ViewHolder(ItemTareaDocenteBinding binding) {
            super(binding.getRoot());
            this.b = binding;
        }

        void bind(TareaDummyDocente t) {
            Context ctx = b.getRoot().getContext();
            b.tvTituloTarea.setText(t.titulo);
            b.tvFechaVenceTarea.setText("Vence: " + t.fechaVence);
            b.tvEntregasTarea.setText(ctx.getString(
                    R.string.tareas_sufijo_entregas, t.entregadas, t.totalAlumnos));

            aplicarChipTipo(t.tipo);
            aplicarChipEstado(t.estado);
        }

        private void aplicarChipTipo(String tipo) {
            int drawable;
            String label;
            switch (tipo) {
                case TIPO_TRABAJO:
                    drawable = R.drawable.bg_chip_pendiente; label = "TRABAJO"; break;
                case TIPO_EXAMEN:
                    drawable = R.drawable.bg_chip_admin;     label = "EXAMEN";  break;
                case TIPO_PROYECTO:
                    drawable = R.drawable.bg_chip_estudiante; label = "PROYECTO"; break;
                default: // TIPO_TAREA
                    drawable = R.drawable.bg_chip_docente;   label = "TAREA";   break;
            }
            b.tvChipTipoTarea.setBackgroundResource(drawable);
            b.tvChipTipoTarea.setText(label);
        }

        private void aplicarChipEstado(String estado) {
            int drawable;
            String label;
            switch (estado) {
                case EST_VENCIDA:
                    drawable = R.drawable.bg_chip_admin;     label = "VENCIDA";    break;
                case EST_COMPLETADA:
                    drawable = R.drawable.bg_chip_estudiante; label = "COMPLETADA"; break;
                default: // EST_EN_CURSO
                    drawable = R.drawable.bg_chip_docente;   label = "EN CURSO";   break;
            }
            b.tvChipEstadoTarea.setBackgroundResource(drawable);
            b.tvChipEstadoTarea.setText(label);
        }
    }
}
```

- [ ] **Step 2: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Verificar dataset (8 tareas, todos los pares grupoId+bloqueId válidos)**

Run: `grep -c "new TareaDummyDocente(" app/src/main/java/com/conectatec/ui/docente/tareas/adapter/TareaDocenteAdapter.java`
Expected: `8`

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/conectatec/ui/docente/tareas/adapter/TareaDocenteAdapter.java
git commit -m "feat(docente-tareas): adapter de tareas con dataset de 8 entradas y filtros tipo+estado"
```

---

### Task 9: Adapter `EntregaDocenteAdapter` con `cargarParaTarea()`

**Files:**
- Create: `app/src/main/java/com/conectatec/ui/docente/tareas/adapter/EntregaDocenteAdapter.java`

- [ ] **Step 1: Crear el adapter completo**

```java
package com.conectatec.ui.docente.tareas.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatec.R;
import com.conectatec.databinding.ItemEntregaDocenteBinding;

import java.util.ArrayList;
import java.util.List;

public class EntregaDocenteAdapter
        extends RecyclerView.Adapter<EntregaDocenteAdapter.ViewHolder> {

    // ── Constantes de estado ──────────────────────────────────────────────────
    public static final int ESTADO_BORRADOR     = 0;
    public static final int ESTADO_ENTREGADA    = 1;
    public static final int ESTADO_CALIFICADA   = 2;
    public static final int ESTADO_SIN_ENTREGAR = 3;

    // ── Modelo ────────────────────────────────────────────────────────────────
    public static class EntregaDummyDocente {
        public final int alumnoId;
        public final String alumnoNombre;
        public final String alumnoIniciales;
        public final int estado;
        public final String fechaEntrega;       // null si SIN_ENTREGAR
        public final Integer calificacion;      // null si no calificada

        public EntregaDummyDocente(int alumnoId, String alumnoNombre, String alumnoIniciales,
                                   int estado, String fechaEntrega, Integer calificacion) {
            this.alumnoId = alumnoId;
            this.alumnoNombre = alumnoNombre;
            this.alumnoIniciales = alumnoIniciales;
            this.estado = estado;
            this.fechaEntrega = fechaEntrega;
            this.calificacion = calificacion;
        }
    }

    // ── Listener ──────────────────────────────────────────────────────────────
    public interface OnEntregaClickListener {
        void onClick(EntregaDummyDocente entrega);
    }

    // ── Estado interno ────────────────────────────────────────────────────────
    private final List<EntregaDummyDocente> lista = new ArrayList<>();
    private final List<EntregaDummyDocente> listaCompleta = new ArrayList<>();
    private OnEntregaClickListener listener;
    private Integer filtroEstado = null;   // null = todas

    public EntregaDocenteAdapter() { }

    public void setOnClickListener(OnEntregaClickListener l) {
        this.listener = l;
    }

    public int conteo() { return lista.size(); }
    public int conteoTotal() { return listaCompleta.size(); }

    public int conteoEntregadas() {
        int c = 0;
        for (EntregaDummyDocente e : listaCompleta) {
            if (e.estado == ESTADO_ENTREGADA || e.estado == ESTADO_CALIFICADA) c++;
        }
        return c;
    }

    public int conteoPendientes() {
        return conteoTotal() - conteoEntregadas();
    }

    /**
     * Carga 6 entregas dummy para la tarea indicada.
     * El grupoId se deriva de la tarea (ver TareaDocenteAdapter.buscarPorId).
     * Mezcla siempre: 2 ENTREGADAS, 2 CALIFICADAS (con nota), 1 BORRADOR, 1 SIN_ENTREGAR.
     */
    public void cargarParaTarea(int tareaId) {
        listaCompleta.clear();

        // Resolver grupoId desde el dataset central de tareas.
        TareaDocenteAdapter.TareaDummyDocente t =
                TareaDocenteAdapter.buscarPorId(tareaId);
        int grupoId = (t != null) ? t.grupoId : 1;

        String[] nombres = nombresParaGrupo(grupoId);
        String[] iniciales = inicialesParaGrupo(grupoId);
        int baseId = grupoId * 100;        // 100, 200, 300

        // Estados fijos por orden: ENT, ENT, CAL, CAL, BORR, SIN
        int[] estados = {
                ESTADO_ENTREGADA, ESTADO_ENTREGADA,
                ESTADO_CALIFICADA, ESTADO_CALIFICADA,
                ESTADO_BORRADOR, ESTADO_SIN_ENTREGAR
        };
        String[] fechas = {
                "05/05/2026", "06/05/2026",
                "03/05/2026", "04/05/2026",
                "—", null
        };
        Integer[] notas = { null, null, 92, 78, null, null };

        for (int i = 0; i < 6; i++) {
            listaCompleta.add(new EntregaDummyDocente(
                    baseId + (i + 1),
                    nombres[i], iniciales[i],
                    estados[i], fechas[i], notas[i]));
        }

        aplicarFiltros();
    }

    public void filtrar(@Nullable Integer estado) {
        this.filtroEstado = estado;
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        lista.clear();
        for (EntregaDummyDocente e : listaCompleta) {
            if (filtroEstado == null || filtroEstado == e.estado) {
                lista.add(e);
            }
        }
        notifyDataSetChanged();
    }

    /** Nombres dummy alineados con Plan 3 (grupo 1) y nombres distintos para 2 y 3. */
    private static String[] nombresParaGrupo(int grupoId) {
        switch (grupoId) {
            case 1: return new String[]{
                    "Ana López", "Bruno García", "Carla Méndez",
                    "Diego Ruiz", "Elena Torres", "Fernando Vega" };
            case 2: return new String[]{
                    "Gabriela Luna", "Héctor Pérez", "Isabel Romero",
                    "Javier Castro", "Karla Soto", "Luis Mendoza" };
            case 3: return new String[]{
                    "María Reyes", "Néstor Aguilar", "Olivia Cano",
                    "Pablo Núñez", "Quetzal Ríos", "Rocío Salinas" };
            default: return new String[]{
                    "Alumno 1", "Alumno 2", "Alumno 3",
                    "Alumno 4", "Alumno 5", "Alumno 6" };
        }
    }

    private static String[] inicialesParaGrupo(int grupoId) {
        String[] nombres = nombresParaGrupo(grupoId);
        String[] inis = new String[nombres.length];
        for (int i = 0; i < nombres.length; i++) {
            inis[i] = calcularIniciales(nombres[i]);
        }
        return inis;
    }

    private static String calcularIniciales(String nombre) {
        String[] partes = nombre.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(2, partes.length); i++) {
            if (!partes[i].isEmpty()) sb.append(partes[i].charAt(0));
        }
        return sb.toString().toUpperCase();
    }

    // ── RecyclerView.Adapter ──────────────────────────────────────────────────
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemEntregaDocenteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        EntregaDummyDocente e = lista.get(position);
        h.bind(e);
        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(e);
        });
    }

    @Override
    public int getItemCount() { return lista.size(); }

    // ── ViewHolder ────────────────────────────────────────────────────────────
    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemEntregaDocenteBinding b;

        ViewHolder(ItemEntregaDocenteBinding binding) {
            super(binding.getRoot());
            this.b = binding;
        }

        void bind(EntregaDummyDocente e) {
            b.tvInicialesEntrega.setText(e.alumnoIniciales);
            b.tvNombreEntrega.setText(e.alumnoNombre);

            int chipDrawable;
            String chipLabel;
            String fechaTxt;
            switch (e.estado) {
                case ESTADO_ENTREGADA:
                    chipDrawable = R.drawable.bg_chip_estudiante;
                    chipLabel = "ENTREGADA";
                    fechaTxt = "Entregada: " + (e.fechaEntrega != null ? e.fechaEntrega : "—");
                    break;
                case ESTADO_CALIFICADA:
                    chipDrawable = R.drawable.bg_chip_estudiante;
                    chipLabel = "CALIFICADA";
                    fechaTxt = "Entregada: " + (e.fechaEntrega != null ? e.fechaEntrega : "—");
                    break;
                case ESTADO_BORRADOR:
                    chipDrawable = R.drawable.bg_chip_pendiente;
                    chipLabel = "BORRADOR";
                    fechaTxt = "Sin envío";
                    break;
                default: // ESTADO_SIN_ENTREGAR
                    chipDrawable = R.drawable.bg_chip_admin;
                    chipLabel = "SIN ENTREGAR";
                    fechaTxt = "—";
                    break;
            }
            b.tvChipEstadoEntrega.setBackgroundResource(chipDrawable);
            b.tvChipEstadoEntrega.setText(chipLabel);
            b.tvFechaEntrega.setText(fechaTxt);

            if (e.calificacion != null) {
                b.tvCalificacionEntrega.setVisibility(View.VISIBLE);
                b.tvCalificacionEntrega.setText(e.calificacion + "/100");
            } else {
                b.tvCalificacionEntrega.setVisibility(View.GONE);
            }
        }
    }
}
```

- [ ] **Step 2: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/conectatec/ui/docente/tareas/adapter/EntregaDocenteAdapter.java
git commit -m "feat(docente-tareas): adapter de entregas con cargarParaTarea y filtros"
```

---

### Task 10: Layout `fragment_docente_tareas.xml`

**Files:**
- Create: `app/src/main/res/layout/fragment_docente_tareas.xml`

- [ ] **Step 1: Crear el layout (entry point del módulo)**

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/colorBackground"
    android:orientation="vertical">

    <!-- Header raíz -->
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:gravity="center_vertical"
        android:background="@color/colorSurface"
        android:paddingTop="48dp"
        android:paddingBottom="20dp"
        android:paddingHorizontal="20dp">

        <View
            android:layout_width="4dp"
            android:layout_height="36dp"
            android:background="@drawable/bg_accent_bar"
            android:layout_marginEnd="14dp" />

        <LinearLayout
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:orientation="vertical">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/title_docente_tareas"
                android:textColor="@color/colorOnSurface"
                android:textSize="26sp"
                android:textStyle="bold"
                android:fontFamily="sans-serif-medium" />

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="2dp"
                android:text="@string/subtitle_docente_tareas"
                android:textColor="@color/colorOnSurfaceVariant"
                android:textSize="12sp"
                android:letterSpacing="0.03" />

        </LinearLayout>

        <LinearLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:gravity="center">

            <TextView
                android:id="@+id/tvHeaderTotalGruposTareas"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="3"
                android:textColor="@color/colorChipDocente"
                android:textSize="24sp"
                android:textStyle="bold" />

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="grupos"
                android:textColor="@color/colorOnSurfaceVariant"
                android:textSize="10sp"
                android:letterSpacing="0.05" />

        </LinearLayout>

    </LinearLayout>

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rvTareasGrupos"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:paddingHorizontal="16dp"
        android:paddingTop="16dp"
        android:paddingBottom="88dp"
        android:clipToPadding="false"
        android:scrollbars="none" />

    <include
        android:id="@+id/emptyStateTareasGrupos"
        layout="@layout/layout_empty_state"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:visibility="gone" />

</LinearLayout>
```

- [ ] **Step 2: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Cero hex**

Run: `grep -E "#[0-9A-Fa-f]{6}" app/src/main/res/layout/fragment_docente_tareas.xml`
Expected: salida vacía.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/res/layout/fragment_docente_tareas.xml
git commit -m "feat(docente-tareas): layout entry point de tareas con lista de grupos"
```

---

### Task 11: Java `DocenteTareasFragment` (reemplaza stub si existe)

**Files:**
- Create or overwrite: `app/src/main/java/com/conectatec/ui/docente/tareas/DocenteTareasFragment.java`

- [ ] **Step 1: Crear/reemplazar el archivo**

```java
package com.conectatec.ui.docente.tareas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.conectatec.R;
import com.conectatec.databinding.FragmentDocenteTareasBinding;
import com.conectatec.ui.docente.tareas.adapter.GrupoTareasDocenteAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteTareasFragment extends Fragment {

    private FragmentDocenteTareasBinding binding;
    private GrupoTareasDocenteAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteTareasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        adapter = new GrupoTareasDocenteAdapter();
        adapter.setOnClickListener(g -> {
            Bundle args = new Bundle();
            args.putInt("grupoId", g.id);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_tareas_to_bloques, args);
        });
        binding.rvTareasGrupos.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvTareasGrupos.setAdapter(adapter);

        binding.tvHeaderTotalGruposTareas.setText(String.valueOf(adapter.conteo()));

        boolean vacio = adapter.conteo() == 0;
        binding.rvTareasGrupos.setVisibility(vacio ? View.GONE : View.VISIBLE);
        binding.emptyStateTareasGrupos.getRoot().setVisibility(vacio ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
```

- [ ] **Step 2: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Confirmar `@AndroidEntryPoint` y `binding = null`**

Run: `grep -E "@AndroidEntryPoint|binding = null" app/src/main/java/com/conectatec/ui/docente/tareas/DocenteTareasFragment.java | wc -l`
Expected: `>= 2`

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/conectatec/ui/docente/tareas/DocenteTareasFragment.java
git commit -m "feat(docente-tareas): entry fragment del módulo con lista de grupos"
```

---

### Task 12: Layout `fragment_docente_bloques.xml`

**Files:**
- Create: `app/src/main/res/layout/fragment_docente_bloques.xml`

- [ ] **Step 1: Crear el layout**

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/colorBackground"
    android:orientation="vertical">

    <!-- Back header -->
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:gravity="center_vertical"
        android:background="@color/colorSurface"
        android:paddingTop="48dp"
        android:paddingBottom="16dp"
        android:paddingHorizontal="16dp">

        <ImageView
            android:id="@+id/btnVolverBloques"
            android:layout_width="36dp"
            android:layout_height="36dp"
            android:src="@drawable/ic_arrow_left"
            android:contentDescription="Volver"
            android:padding="6dp"
            android:clickable="true"
            android:focusable="true"
            android:background="?attr/selectableItemBackgroundBorderless"
            app:tint="@color/colorOnSurface" />

        <LinearLayout
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:orientation="vertical"
            android:layout_marginStart="8dp">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/app_name"
                android:textColor="@color/colorPrimary"
                android:textSize="9sp"
                android:textStyle="bold"
                android:letterSpacing="0.18" />

            <TextView
                android:id="@+id/tvTituloBloques"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/title_bloques"
                android:textColor="@color/colorOnSurface"
                android:textSize="20sp"
                android:textStyle="bold"
                android:fontFamily="sans-serif-medium" />

            <TextView
                android:id="@+id/tvSubtituloBloques"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Programación Móvil 6A"
                android:textColor="@color/colorOnSurfaceVariant"
                android:textSize="12sp" />

        </LinearLayout>

    </LinearLayout>

    <View
        android:layout_width="match_parent"
        android:layout_height="3dp"
        android:background="@color/colorPrimary" />

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rvBloques"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:paddingHorizontal="16dp"
        android:paddingTop="20dp"
        android:paddingBottom="88dp"
        android:clipToPadding="false"
        android:scrollbars="none" />

</LinearLayout>
```

- [ ] **Step 2: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Cero hex**

Run: `grep -E "#[0-9A-Fa-f]{6}" app/src/main/res/layout/fragment_docente_bloques.xml`
Expected: salida vacía.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/res/layout/fragment_docente_bloques.xml
git commit -m "feat(docente-tareas): layout de bloques con back header"
```

---

### Task 13: Java `DocenteBloquesFragment`

**Files:**
- Create: `app/src/main/java/com/conectatec/ui/docente/tareas/DocenteBloquesFragment.java`

- [ ] **Step 1: Crear el fragment**

```java
package com.conectatec.ui.docente.tareas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.conectatec.R;
import com.conectatec.databinding.FragmentDocenteBloquesBinding;
import com.conectatec.ui.docente.tareas.adapter.BloqueDocenteAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteBloquesFragment extends Fragment {

    private FragmentDocenteBloquesBinding binding;
    private BloqueDocenteAdapter adapter;
    private int grupoId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteBloquesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        grupoId = getArguments() != null ? getArguments().getInt("grupoId", 1) : 1;

        binding.tvSubtituloBloques.setText(nombreDeGrupo(grupoId));
        binding.btnVolverBloques.setOnClickListener(v ->
                requireActivity().onBackPressed());

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        adapter = new BloqueDocenteAdapter();
        adapter.setOnClickListener(b -> {
            Bundle args = new Bundle();
            args.putInt("grupoId", grupoId);
            args.putInt("bloqueId", b.id);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_bloques_to_tareas_bloque, args);
        });
        binding.rvBloques.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvBloques.setAdapter(adapter);
        adapter.cargarParaGrupo(grupoId);
    }

    private String nombreDeGrupo(int id) {
        switch (id) {
            case 1: return "Programación Móvil 6A";
            case 2: return "Bases de Datos 4B";
            case 3: return "Cálculo Integral 2A";
            default: return "Grupo";
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
```

- [ ] **Step 2: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/conectatec/ui/docente/tareas/DocenteBloquesFragment.java
git commit -m "feat(docente-tareas): fragment de bloques con drill-down al detalle"
```

---

### Task 14: Layout `fragment_docente_tareas_bloque.xml`

**Files:**
- Create: `app/src/main/res/layout/fragment_docente_tareas_bloque.xml`

- [ ] **Step 1: Crear el layout (back header + botón crear + 2 chip rows + lista)**

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/colorBackground"
    android:orientation="vertical">

    <!-- Back header -->
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:gravity="center_vertical"
        android:background="@color/colorSurface"
        android:paddingTop="48dp"
        android:paddingBottom="16dp"
        android:paddingHorizontal="16dp">

        <ImageView
            android:id="@+id/btnVolverTareasBloque"
            android:layout_width="36dp"
            android:layout_height="36dp"
            android:src="@drawable/ic_arrow_left"
            android:contentDescription="Volver"
            android:padding="6dp"
            android:clickable="true"
            android:focusable="true"
            android:background="?attr/selectableItemBackgroundBorderless"
            app:tint="@color/colorOnSurface" />

        <LinearLayout
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:orientation="vertical"
            android:layout_marginStart="8dp">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/app_name"
                android:textColor="@color/colorPrimary"
                android:textSize="9sp"
                android:textStyle="bold"
                android:letterSpacing="0.18" />

            <TextView
                android:id="@+id/tvTituloTareasBloque"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/title_tareas_bloque"
                android:textColor="@color/colorOnSurface"
                android:textSize="20sp"
                android:textStyle="bold"
                android:fontFamily="sans-serif-medium" />

            <TextView
                android:id="@+id/tvSubtituloTareasBloque"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Bloque 1 — Programación Móvil 6A"
                android:textColor="@color/colorOnSurfaceVariant"
                android:textSize="12sp" />

        </LinearLayout>

    </LinearLayout>

    <View
        android:layout_width="match_parent"
        android:layout_height="3dp"
        android:background="@color/colorPrimary" />

    <!-- Botón crear tarea -->
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:paddingHorizontal="16dp"
        android:paddingTop="14dp">

        <com.google.android.material.button.MaterialButton
            android:id="@+id/btnCrearTareaBloque"
            android:layout_width="match_parent"
            android:layout_height="48dp"
            android:text="@string/btn_crear_tarea"
            app:icon="@drawable/ic_plus"
            app:iconGravity="textStart"
            app:cornerRadius="12dp" />

    </LinearLayout>

    <!-- Chips de tipo -->
    <HorizontalScrollView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:scrollbars="none"
        android:paddingHorizontal="16dp"
        android:paddingTop="12dp">

        <LinearLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:orientation="horizontal">

            <com.google.android.material.chip.Chip
                android:id="@+id/chipTipoTodas"
                style="@style/Widget.Material3.Chip.Filter"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginEnd="8dp"
                android:text="@string/chip_tipo_todas"
                android:checked="true" />

            <com.google.android.material.chip.Chip
                android:id="@+id/chipTipoTarea"
                style="@style/Widget.Material3.Chip.Filter"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginEnd="8dp"
                android:text="@string/chip_tipo_tarea" />

            <com.google.android.material.chip.Chip
                android:id="@+id/chipTipoTrabajo"
                style="@style/Widget.Material3.Chip.Filter"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginEnd="8dp"
                android:text="@string/chip_tipo_trabajo" />

            <com.google.android.material.chip.Chip
                android:id="@+id/chipTipoExamen"
                style="@style/Widget.Material3.Chip.Filter"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginEnd="8dp"
                android:text="@string/chip_tipo_examen" />

            <com.google.android.material.chip.Chip
                android:id="@+id/chipTipoProyecto"
                style="@style/Widget.Material3.Chip.Filter"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/chip_tipo_proyecto" />

        </LinearLayout>

    </HorizontalScrollView>

    <!-- Chips de estado -->
    <HorizontalScrollView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:scrollbars="none"
        android:paddingHorizontal="16dp"
        android:paddingTop="6dp"
        android:paddingBottom="10dp">

        <LinearLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:orientation="horizontal">

            <com.google.android.material.chip.Chip
                android:id="@+id/chipEstadoTodas"
                style="@style/Widget.Material3.Chip.Filter"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginEnd="8dp"
                android:text="@string/chip_estado_todas"
                android:checked="true" />

            <com.google.android.material.chip.Chip
                android:id="@+id/chipEstadoEnCurso"
                style="@style/Widget.Material3.Chip.Filter"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginEnd="8dp"
                android:text="@string/chip_estado_en_curso" />

            <com.google.android.material.chip.Chip
                android:id="@+id/chipEstadoVencida"
                style="@style/Widget.Material3.Chip.Filter"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginEnd="8dp"
                android:text="@string/chip_estado_vencida" />

            <com.google.android.material.chip.Chip
                android:id="@+id/chipEstadoCompletada"
                style="@style/Widget.Material3.Chip.Filter"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/chip_estado_completada" />

        </LinearLayout>

    </HorizontalScrollView>

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rvTareasBloque"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:paddingHorizontal="16dp"
        android:paddingTop="4dp"
        android:paddingBottom="88dp"
        android:clipToPadding="false"
        android:scrollbars="none" />

    <include
        android:id="@+id/emptyStateTareasBloque"
        layout="@layout/layout_empty_state"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:visibility="gone" />

</LinearLayout>
```

- [ ] **Step 2: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Cero hex y cero `BottomNavigationView`**

Run: `grep -E "#[0-9A-Fa-f]{6}|BottomNavigationView" app/src/main/res/layout/fragment_docente_tareas_bloque.xml`
Expected: salida vacía.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/res/layout/fragment_docente_tareas_bloque.xml
git commit -m "feat(docente-tareas): layout de tareas del bloque con filtros tipo y estado"
```

---

### Task 15: Java `DocenteTareasBloqueFragment`

**Files:**
- Create: `app/src/main/java/com/conectatec/ui/docente/tareas/DocenteTareasBloqueFragment.java`

- [ ] **Step 1: Crear el fragment con filtros y navegación**

```java
package com.conectatec.ui.docente.tareas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.conectatec.R;
import com.conectatec.databinding.FragmentDocenteTareasBloqueBinding;
import com.conectatec.ui.docente.tareas.adapter.TareaDocenteAdapter;
import com.conectatec.ui.docente.tareas.adapter.TareaDocenteAdapter.TareaDummyDocente;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteTareasBloqueFragment extends Fragment {

    private FragmentDocenteTareasBloqueBinding binding;
    private TareaDocenteAdapter adapter;

    private int grupoId;
    private int bloqueId;

    private String filtroTipo = null;
    private String filtroEstado = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteTareasBloqueBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        grupoId  = getArguments() != null ? getArguments().getInt("grupoId", 1) : 1;
        bloqueId = getArguments() != null ? getArguments().getInt("bloqueId", 1) : 1;

        binding.tvSubtituloTareasBloque.setText(
                "Bloque " + bloqueId + " — " + nombreDeGrupo(grupoId));
        binding.btnVolverTareasBloque.setOnClickListener(v ->
                requireActivity().onBackPressed());

        setupRecyclerView();
        setupChipsTipo();
        setupChipsEstado();
        setupBotonCrear();
        actualizarVista();
    }

    private void setupRecyclerView() {
        adapter = new TareaDocenteAdapter();
        adapter.setOnClickListener(t -> {
            Bundle args = new Bundle();
            args.putInt("tareaId", t.id);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_tareas_bloque_to_entregas, args);
        });
        binding.rvTareasBloque.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvTareasBloque.setAdapter(adapter);
        adapter.cargarParaBloque(grupoId, bloqueId);
    }

    private void setupChipsTipo() {
        binding.chipTipoTodas.setOnCheckedChangeListener((c, checked) -> {
            if (checked) { filtroTipo = null; aplicar(); }
        });
        binding.chipTipoTarea.setOnCheckedChangeListener((c, checked) -> {
            if (checked) { filtroTipo = TareaDocenteAdapter.TIPO_TAREA; aplicar(); }
        });
        binding.chipTipoTrabajo.setOnCheckedChangeListener((c, checked) -> {
            if (checked) { filtroTipo = TareaDocenteAdapter.TIPO_TRABAJO; aplicar(); }
        });
        binding.chipTipoExamen.setOnCheckedChangeListener((c, checked) -> {
            if (checked) { filtroTipo = TareaDocenteAdapter.TIPO_EXAMEN; aplicar(); }
        });
        binding.chipTipoProyecto.setOnCheckedChangeListener((c, checked) -> {
            if (checked) { filtroTipo = TareaDocenteAdapter.TIPO_PROYECTO; aplicar(); }
        });
    }

    private void setupChipsEstado() {
        binding.chipEstadoTodas.setOnCheckedChangeListener((c, checked) -> {
            if (checked) { filtroEstado = null; aplicar(); }
        });
        binding.chipEstadoEnCurso.setOnCheckedChangeListener((c, checked) -> {
            if (checked) { filtroEstado = TareaDocenteAdapter.EST_EN_CURSO; aplicar(); }
        });
        binding.chipEstadoVencida.setOnCheckedChangeListener((c, checked) -> {
            if (checked) { filtroEstado = TareaDocenteAdapter.EST_VENCIDA; aplicar(); }
        });
        binding.chipEstadoCompletada.setOnCheckedChangeListener((c, checked) -> {
            if (checked) { filtroEstado = TareaDocenteAdapter.EST_COMPLETADA; aplicar(); }
        });
    }

    private void setupBotonCrear() {
        binding.btnCrearTareaBloque.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putInt("grupoId", grupoId);
            args.putInt("bloqueId", bloqueId);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_tareas_bloque_to_crear, args);
        });
    }

    private void aplicar() {
        adapter.filtrar(filtroTipo, filtroEstado);
        actualizarVista();
    }

    private void actualizarVista() {
        boolean vacio = adapter.conteo() == 0;
        binding.rvTareasBloque.setVisibility(vacio ? View.GONE : View.VISIBLE);
        binding.emptyStateTareasBloque.getRoot().setVisibility(vacio ? View.VISIBLE : View.GONE);
    }

    private String nombreDeGrupo(int id) {
        switch (id) {
            case 1: return "Programación Móvil 6A";
            case 2: return "Bases de Datos 4B";
            case 3: return "Cálculo Integral 2A";
            default: return "Grupo";
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
```

- [ ] **Step 2: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/conectatec/ui/docente/tareas/DocenteTareasBloqueFragment.java
git commit -m "feat(docente-tareas): fragment de tareas del bloque con filtros tipo+estado"
```

---

### Task 16: Layout `fragment_docente_crear_tarea.xml`

**Files:**
- Create: `app/src/main/res/layout/fragment_docente_crear_tarea.xml`

- [ ] **Step 1: Crear el layout (back header + ScrollView con form + botón Publicar)**

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/colorBackground"
    android:orientation="vertical">

    <!-- Back header -->
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:gravity="center_vertical"
        android:background="@color/colorSurface"
        android:paddingTop="48dp"
        android:paddingBottom="16dp"
        android:paddingHorizontal="16dp">

        <ImageView
            android:id="@+id/btnVolverCrearTarea"
            android:layout_width="36dp"
            android:layout_height="36dp"
            android:src="@drawable/ic_arrow_left"
            android:contentDescription="Volver"
            android:padding="6dp"
            android:clickable="true"
            android:focusable="true"
            android:background="?attr/selectableItemBackgroundBorderless"
            app:tint="@color/colorOnSurface" />

        <LinearLayout
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:orientation="vertical"
            android:layout_marginStart="8dp">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/app_name"
                android:textColor="@color/colorPrimary"
                android:textSize="9sp"
                android:textStyle="bold"
                android:letterSpacing="0.18" />

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/title_nueva_tarea"
                android:textColor="@color/colorOnSurface"
                android:textSize="20sp"
                android:textStyle="bold"
                android:fontFamily="sans-serif-medium" />

        </LinearLayout>

    </LinearLayout>

    <View
        android:layout_width="match_parent"
        android:layout_height="3dp"
        android:background="@color/colorPrimary" />

    <ScrollView
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:fillViewport="true">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:paddingHorizontal="16dp"
            android:paddingTop="20dp"
            android:paddingBottom="120dp">

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/tilTituloTareaNueva"
                style="@style/Widget.Material3.TextInputLayout.OutlinedBox"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginBottom="12dp"
                android:hint="@string/hint_titulo_tarea"
                app:boxBackgroundColor="@color/colorSurface"
                app:boxCornerRadiusTopStart="10dp"
                app:boxCornerRadiusTopEnd="10dp"
                app:boxCornerRadiusBottomStart="10dp"
                app:boxCornerRadiusBottomEnd="10dp"
                app:boxStrokeColor="@color/colorBorder"
                app:hintTextColor="@color/colorOnSurfaceVariant">

                <com.google.android.material.textfield.TextInputEditText
                    android:id="@+id/etTituloTareaNueva"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:inputType="textCapSentences"
                    android:textColor="@color/colorOnSurface"
                    android:textSize="14sp" />

            </com.google.android.material.textfield.TextInputLayout>

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/tilDescripcionTareaNueva"
                style="@style/Widget.Material3.TextInputLayout.OutlinedBox"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginBottom="12dp"
                android:hint="@string/hint_descripcion_tarea"
                app:boxBackgroundColor="@color/colorSurface"
                app:boxCornerRadiusTopStart="10dp"
                app:boxCornerRadiusTopEnd="10dp"
                app:boxCornerRadiusBottomStart="10dp"
                app:boxCornerRadiusBottomEnd="10dp"
                app:boxStrokeColor="@color/colorBorder"
                app:hintTextColor="@color/colorOnSurfaceVariant">

                <com.google.android.material.textfield.TextInputEditText
                    android:id="@+id/etDescripcionTareaNueva"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:inputType="textMultiLine|textCapSentences"
                    android:minLines="4"
                    android:gravity="top|start"
                    android:textColor="@color/colorOnSurface"
                    android:textSize="14sp" />

            </com.google.android.material.textfield.TextInputLayout>

            <!-- Spinner de tipo -->
            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginBottom="6dp"
                android:text="@string/label_tipo_tarea"
                android:textColor="@color/colorOnSurfaceVariant"
                android:textSize="11sp"
                android:textStyle="bold"
                android:letterSpacing="0.05" />

            <Spinner
                android:id="@+id/spinnerTipoTareaNueva"
                android:layout_width="match_parent"
                android:layout_height="48dp"
                android:layout_marginBottom="12dp"
                android:background="@drawable/bg_input" />

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/tilFechaLimiteTareaNueva"
                style="@style/Widget.Material3.TextInputLayout.OutlinedBox"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginBottom="12dp"
                android:hint="@string/hint_fecha_limite"
                app:boxBackgroundColor="@color/colorSurface"
                app:boxCornerRadiusTopStart="10dp"
                app:boxCornerRadiusTopEnd="10dp"
                app:boxCornerRadiusBottomStart="10dp"
                app:boxCornerRadiusBottomEnd="10dp"
                app:boxStrokeColor="@color/colorBorder"
                app:hintTextColor="@color/colorOnSurfaceVariant">

                <com.google.android.material.textfield.TextInputEditText
                    android:id="@+id/etFechaLimiteTareaNueva"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:focusable="false"
                    android:clickable="true"
                    android:inputType="none"
                    android:textColor="@color/colorOnSurface"
                    android:textSize="14sp" />

            </com.google.android.material.textfield.TextInputLayout>

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/tilCalificacionMaxTareaNueva"
                style="@style/Widget.Material3.TextInputLayout.OutlinedBox"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginBottom="20dp"
                android:hint="@string/hint_calificacion_max"
                app:boxBackgroundColor="@color/colorSurface"
                app:boxCornerRadiusTopStart="10dp"
                app:boxCornerRadiusTopEnd="10dp"
                app:boxCornerRadiusBottomStart="10dp"
                app:boxCornerRadiusBottomEnd="10dp"
                app:boxStrokeColor="@color/colorBorder"
                app:hintTextColor="@color/colorOnSurfaceVariant">

                <com.google.android.material.textfield.TextInputEditText
                    android:id="@+id/etCalificacionMaxTareaNueva"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:inputType="number"
                    android:text="100"
                    android:textColor="@color/colorOnSurface"
                    android:textSize="14sp" />

            </com.google.android.material.textfield.TextInputLayout>

            <com.google.android.material.button.MaterialButton
                android:id="@+id/btnPublicarTarea"
                android:layout_width="match_parent"
                android:layout_height="52dp"
                android:text="@string/btn_publicar_tarea"
                app:cornerRadius="12dp" />

        </LinearLayout>

    </ScrollView>

</LinearLayout>
```

- [ ] **Step 2: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Cero hex**

Run: `grep -E "#[0-9A-Fa-f]{6}" app/src/main/res/layout/fragment_docente_crear_tarea.xml`
Expected: salida vacía.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/res/layout/fragment_docente_crear_tarea.xml
git commit -m "feat(docente-tareas): layout para crear tarea con form completo"
```

---

### Task 17: Java `DocenteCrearTareaFragment`

**Files:**
- Create: `app/src/main/java/com/conectatec/ui/docente/tareas/DocenteCrearTareaFragment.java`

- [ ] **Step 1: Crear el fragment con DatePicker, validación y Snackbar**

```java
package com.conectatec.ui.docente.tareas;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.conectatec.databinding.FragmentDocenteCrearTareaBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteCrearTareaFragment extends Fragment {

    private FragmentDocenteCrearTareaBinding binding;
    private int grupoId;
    private int bloqueId;

    private static final String[] TIPOS = { "TAREA", "TRABAJO", "EXAMEN", "PROYECTO" };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteCrearTareaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        grupoId  = getArguments() != null ? getArguments().getInt("grupoId", 1) : 1;
        bloqueId = getArguments() != null ? getArguments().getInt("bloqueId", 1) : 1;

        binding.btnVolverCrearTarea.setOnClickListener(v ->
                requireActivity().onBackPressed());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, TIPOS);
        binding.spinnerTipoTareaNueva.setAdapter(adapter);

        binding.etFechaLimiteTareaNueva.setOnClickListener(v -> abrirDatePicker());
        binding.btnPublicarTarea.setOnClickListener(v -> onPublicarClicked());
    }

    private void abrirDatePicker() {
        Calendar c = Calendar.getInstance();
        DatePickerDialog dlg = new DatePickerDialog(requireContext(),
                (DatePickerDialog dialog, int year, int month, int day) -> {
                    String fecha = String.format(Locale.getDefault(),
                            "%02d/%02d/%04d", day, month + 1, year);
                    binding.etFechaLimiteTareaNueva.setText(fecha);
                },
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dlg.show();
    }

    private void onPublicarClicked() {
        String titulo = binding.etTituloTareaNueva.getText() != null
                ? binding.etTituloTareaNueva.getText().toString().trim() : "";
        String fecha  = binding.etFechaLimiteTareaNueva.getText() != null
                ? binding.etFechaLimiteTareaNueva.getText().toString().trim() : "";

        if (titulo.isEmpty()) {
            binding.tilTituloTareaNueva.setError("Requerido");
            return;
        }
        if (fecha.isEmpty()) {
            Snackbar.make(binding.getRoot(),
                    "Selecciona una fecha límite", Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Datos no consumidos por ahora — los usará el backend al integrar:
        //   String descripcion, String tipoSeleccionado, String calificacionMax,
        //   int grupoId, int bloqueId
        // TODO: llamar a TareaService.crearTarea() — luego NotificacionService.enviar(tipo=1)

        Snackbar.make(binding.getRoot(),
                "Tarea creada", Snackbar.LENGTH_SHORT).show();
        Navigation.findNavController(requireView()).popBackStack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
```

- [ ] **Step 2: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Confirmar TODO en español**

Run: `grep -n "TODO: llamar a TareaService.crearTarea" app/src/main/java/com/conectatec/ui/docente/tareas/DocenteCrearTareaFragment.java`
Expected: 1 línea encontrada.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/conectatec/ui/docente/tareas/DocenteCrearTareaFragment.java
git commit -m "feat(docente-tareas): fragment crear tarea con DatePicker, spinner y snackbar"
```

---

### Task 18: Layout `fragment_docente_entregas.xml`

**Files:**
- Create: `app/src/main/res/layout/fragment_docente_entregas.xml`

- [ ] **Step 1: Crear el layout (back header + card resumen + chips + lista)**

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/colorBackground"
    android:orientation="vertical">

    <!-- Back header -->
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:gravity="center_vertical"
        android:background="@color/colorSurface"
        android:paddingTop="48dp"
        android:paddingBottom="16dp"
        android:paddingHorizontal="16dp">

        <ImageView
            android:id="@+id/btnVolverEntregas"
            android:layout_width="36dp"
            android:layout_height="36dp"
            android:src="@drawable/ic_arrow_left"
            android:contentDescription="Volver"
            android:padding="6dp"
            android:clickable="true"
            android:focusable="true"
            android:background="?attr/selectableItemBackgroundBorderless"
            app:tint="@color/colorOnSurface" />

        <LinearLayout
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:orientation="vertical"
            android:layout_marginStart="8dp">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/app_name"
                android:textColor="@color/colorPrimary"
                android:textSize="9sp"
                android:textStyle="bold"
                android:letterSpacing="0.18" />

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/title_entregas"
                android:textColor="@color/colorOnSurface"
                android:textSize="20sp"
                android:textStyle="bold"
                android:fontFamily="sans-serif-medium" />

            <TextView
                android:id="@+id/tvSubtituloEntregas"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Tarea"
                android:textColor="@color/colorOnSurfaceVariant"
                android:textSize="12sp"
                android:maxLines="1"
                android:ellipsize="end" />

        </LinearLayout>

    </LinearLayout>

    <View
        android:layout_width="match_parent"
        android:layout_height="3dp"
        android:background="@color/colorPrimary" />

    <!-- Card resumen -->
    <com.google.android.material.card.MaterialCardView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginHorizontal="16dp"
        android:layout_marginTop="16dp"
        app:cardBackgroundColor="@color/colorSurface"
        app:cardCornerRadius="12dp"
        app:cardElevation="0dp"
        app:strokeWidth="0dp">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:padding="20dp">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/label_progreso_entregas"
                android:textColor="@color/colorOnSurfaceVariant"
                android:textSize="11sp"
                android:textStyle="bold"
                android:letterSpacing="0.08"
                android:layout_marginBottom="12dp" />

            <com.google.android.material.progressindicator.LinearProgressIndicator
                android:id="@+id/progressEntregasResumen"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:indeterminate="false"
                android:max="100"
                android:progress="0"
                app:trackColor="@color/colorDivider"
                app:indicatorColor="@color/colorSuccess"
                app:trackCornerRadius="4dp"
                android:layout_marginBottom="16dp" />

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal">

                <LinearLayout
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:orientation="vertical"
                    android:gravity="center">

                    <TextView
                        android:id="@+id/tvStatTotalEntregas"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="0"
                        android:textColor="@color/colorOnSurface"
                        android:textSize="22sp"
                        android:textStyle="bold" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="@string/label_total_alumnos"
                        android:textColor="@color/colorOnSurfaceVariant"
                        android:textSize="11sp" />

                </LinearLayout>

                <View
                    android:layout_width="1dp"
                    android:layout_height="36dp"
                    android:background="@color/colorDivider" />

                <LinearLayout
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:orientation="vertical"
                    android:gravity="center">

                    <TextView
                        android:id="@+id/tvStatEntregadasEntregas"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="0"
                        android:textColor="@color/colorSuccess"
                        android:textSize="22sp"
                        android:textStyle="bold" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="@string/label_entregadas"
                        android:textColor="@color/colorOnSurfaceVariant"
                        android:textSize="11sp" />

                </LinearLayout>

                <View
                    android:layout_width="1dp"
                    android:layout_height="36dp"
                    android:background="@color/colorDivider" />

                <LinearLayout
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:orientation="vertical"
                    android:gravity="center">

                    <TextView
                        android:id="@+id/tvStatPendientesEntregas"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="0"
                        android:textColor="@color/colorWarning"
                        android:textSize="22sp"
                        android:textStyle="bold" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="@string/label_pendientes"
                        android:textColor="@color/colorOnSurfaceVariant"
                        android:textSize="11sp" />

                </LinearLayout>

            </LinearLayout>

        </LinearLayout>

    </com.google.android.material.card.MaterialCardView>

    <!-- Chips de filtro -->
    <HorizontalScrollView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:scrollbars="none"
        android:paddingHorizontal="16dp"
        android:paddingTop="12dp"
        android:paddingBottom="6dp">

        <LinearLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:orientation="horizontal">

            <com.google.android.material.chip.Chip
                android:id="@+id/chipEntregasTodas"
                style="@style/Widget.Material3.Chip.Filter"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginEnd="8dp"
                android:text="@string/chip_entregas_todas"
                android:checked="true" />

            <com.google.android.material.chip.Chip
                android:id="@+id/chipEntregasPendientes"
                style="@style/Widget.Material3.Chip.Filter"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginEnd="8dp"
                android:text="@string/chip_entregas_pendientes" />

            <com.google.android.material.chip.Chip
                android:id="@+id/chipEntregasEntregadas"
                style="@style/Widget.Material3.Chip.Filter"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginEnd="8dp"
                android:text="@string/chip_entregas_entregadas" />

            <com.google.android.material.chip.Chip
                android:id="@+id/chipEntregasCalificadas"
                style="@style/Widget.Material3.Chip.Filter"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginEnd="8dp"
                android:text="@string/chip_entregas_calificadas" />

            <com.google.android.material.chip.Chip
                android:id="@+id/chipEntregasSinEntregar"
                style="@style/Widget.Material3.Chip.Filter"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/chip_entregas_sin_entregar" />

        </LinearLayout>

    </HorizontalScrollView>

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rvEntregas"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:paddingHorizontal="16dp"
        android:paddingTop="4dp"
        android:paddingBottom="88dp"
        android:clipToPadding="false"
        android:scrollbars="none" />

    <include
        android:id="@+id/emptyStateEntregas"
        layout="@layout/layout_empty_state"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:visibility="gone" />

</LinearLayout>
```

- [ ] **Step 2: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Cero hex**

Run: `grep -E "#[0-9A-Fa-f]{6}" app/src/main/res/layout/fragment_docente_entregas.xml`
Expected: salida vacía.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/res/layout/fragment_docente_entregas.xml
git commit -m "feat(docente-tareas): layout de entregas con resumen y filtros"
```

---

### Task 19: Java `DocenteEntregasFragment`

**Files:**
- Create: `app/src/main/java/com/conectatec/ui/docente/tareas/DocenteEntregasFragment.java`

- [ ] **Step 1: Crear el fragment con resumen y filtros**

```java
package com.conectatec.ui.docente.tareas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.conectatec.R;
import com.conectatec.databinding.FragmentDocenteEntregasBinding;
import com.conectatec.ui.docente.tareas.adapter.EntregaDocenteAdapter;
import com.conectatec.ui.docente.tareas.adapter.TareaDocenteAdapter;
import com.conectatec.ui.docente.tareas.adapter.TareaDocenteAdapter.TareaDummyDocente;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteEntregasFragment extends Fragment {

    private FragmentDocenteEntregasBinding binding;
    private EntregaDocenteAdapter adapter;
    private int tareaId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteEntregasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tareaId = getArguments() != null ? getArguments().getInt("tareaId", 1) : 1;

        TareaDummyDocente t = TareaDocenteAdapter.buscarPorId(tareaId);
        binding.tvSubtituloEntregas.setText(t != null ? t.titulo : "Tarea");

        binding.btnVolverEntregas.setOnClickListener(v ->
                requireActivity().onBackPressed());

        setupRecyclerView();
        setupChips();
        actualizarResumen();
    }

    private void setupRecyclerView() {
        adapter = new EntregaDocenteAdapter();
        adapter.setOnClickListener(e -> {
            Bundle args = new Bundle();
            args.putInt("tareaId", tareaId);
            args.putInt("alumnoId", e.alumnoId);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_entregas_to_calificar, args);
        });
        binding.rvEntregas.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvEntregas.setAdapter(adapter);
        adapter.cargarParaTarea(tareaId);
    }

    private void setupChips() {
        binding.chipEntregasTodas.setOnCheckedChangeListener((c, checked) -> {
            if (checked) { adapter.filtrar(null); actualizarVista(); }
        });
        binding.chipEntregasPendientes.setOnCheckedChangeListener((c, checked) -> {
            if (checked) {
                adapter.filtrar(EntregaDocenteAdapter.ESTADO_BORRADOR);
                actualizarVista();
            }
        });
        binding.chipEntregasEntregadas.setOnCheckedChangeListener((c, checked) -> {
            if (checked) {
                adapter.filtrar(EntregaDocenteAdapter.ESTADO_ENTREGADA);
                actualizarVista();
            }
        });
        binding.chipEntregasCalificadas.setOnCheckedChangeListener((c, checked) -> {
            if (checked) {
                adapter.filtrar(EntregaDocenteAdapter.ESTADO_CALIFICADA);
                actualizarVista();
            }
        });
        binding.chipEntregasSinEntregar.setOnCheckedChangeListener((c, checked) -> {
            if (checked) {
                adapter.filtrar(EntregaDocenteAdapter.ESTADO_SIN_ENTREGAR);
                actualizarVista();
            }
        });
    }

    private void actualizarResumen() {
        int total = adapter.conteoTotal();
        int entregadas = adapter.conteoEntregadas();
        int pendientes = adapter.conteoPendientes();
        int progreso = total > 0 ? (int) (entregadas * 100f / total) : 0;

        binding.tvStatTotalEntregas.setText(String.valueOf(total));
        binding.tvStatEntregadasEntregas.setText(String.valueOf(entregadas));
        binding.tvStatPendientesEntregas.setText(String.valueOf(pendientes));
        binding.progressEntregasResumen.setProgress(progreso);

        actualizarVista();
    }

    private void actualizarVista() {
        boolean vacio = adapter.conteo() == 0;
        binding.rvEntregas.setVisibility(vacio ? View.GONE : View.VISIBLE);
        binding.emptyStateEntregas.getRoot().setVisibility(vacio ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
```

- [ ] **Step 2: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/conectatec/ui/docente/tareas/DocenteEntregasFragment.java
git commit -m "feat(docente-tareas): fragment de entregas con resumen y filtros"
```

---

### Task 20: Layout `fragment_docente_calificar_entrega.xml`

**Files:**
- Create: `app/src/main/res/layout/fragment_docente_calificar_entrega.xml`

- [ ] **Step 1: Crear el layout (back + 3 cards + botón guardar)**

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/colorBackground"
    android:orientation="vertical">

    <!-- Back header -->
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:gravity="center_vertical"
        android:background="@color/colorSurface"
        android:paddingTop="48dp"
        android:paddingBottom="16dp"
        android:paddingHorizontal="16dp">

        <ImageView
            android:id="@+id/btnVolverCalificar"
            android:layout_width="36dp"
            android:layout_height="36dp"
            android:src="@drawable/ic_arrow_left"
            android:contentDescription="Volver"
            android:padding="6dp"
            android:clickable="true"
            android:focusable="true"
            android:background="?attr/selectableItemBackgroundBorderless"
            app:tint="@color/colorOnSurface" />

        <LinearLayout
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:orientation="vertical"
            android:layout_marginStart="8dp">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/app_name"
                android:textColor="@color/colorPrimary"
                android:textSize="9sp"
                android:textStyle="bold"
                android:letterSpacing="0.18" />

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/title_calificar"
                android:textColor="@color/colorOnSurface"
                android:textSize="20sp"
                android:textStyle="bold"
                android:fontFamily="sans-serif-medium" />

        </LinearLayout>

    </LinearLayout>

    <View
        android:layout_width="match_parent"
        android:layout_height="3dp"
        android:background="@color/colorPrimary" />

    <ScrollView
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:fillViewport="true">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:paddingHorizontal="16dp"
            android:paddingTop="20dp"
            android:paddingBottom="120dp">

            <!-- Card alumno -->
            <com.google.android.material.card.MaterialCardView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginBottom="12dp"
                app:cardBackgroundColor="@color/colorSurface"
                app:cardCornerRadius="12dp"
                app:cardElevation="0dp"
                app:strokeWidth="0dp">

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:gravity="center_vertical"
                    android:padding="20dp">

                    <FrameLayout
                        android:layout_width="64dp"
                        android:layout_height="64dp"
                        android:layout_marginEnd="16dp"
                        android:background="@drawable/bg_avatar_placeholder">

                        <TextView
                            android:id="@+id/tvInicialesAlumnoCalif"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:layout_gravity="center"
                            android:text="AL"
                            android:textColor="@color/colorOnSurface"
                            android:textSize="20sp"
                            android:textStyle="bold" />

                    </FrameLayout>

                    <LinearLayout
                        android:layout_width="0dp"
                        android:layout_height="wrap_content"
                        android:layout_weight="1"
                        android:orientation="vertical">

                        <TextView
                            android:id="@+id/tvNombreAlumnoCalif"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="Ana López"
                            android:textColor="@color/colorOnSurface"
                            android:textSize="18sp"
                            android:textStyle="bold"
                            android:fontFamily="sans-serif-medium" />

                        <TextView
                            android:id="@+id/tvCorreoAlumnoCalif"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:layout_marginTop="2dp"
                            android:text="alumno@correo.com"
                            android:textColor="@color/colorOnSurfaceVariant"
                            android:textSize="12sp" />

                        <TextView
                            android:id="@+id/tvChipEstadoCalif"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:layout_marginTop="6dp"
                            android:text="ENTREGADA"
                            android:textColor="@color/white"
                            android:textSize="9sp"
                            android:textStyle="bold"
                            android:letterSpacing="0.05"
                            android:paddingHorizontal="8dp"
                            android:paddingVertical="3dp"
                            android:background="@drawable/bg_chip_estudiante" />

                    </LinearLayout>

                </LinearLayout>

            </com.google.android.material.card.MaterialCardView>

            <!-- Card entrega -->
            <com.google.android.material.card.MaterialCardView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginBottom="12dp"
                app:cardBackgroundColor="@color/colorSurface"
                app:cardCornerRadius="12dp"
                app:cardElevation="0dp"
                app:strokeWidth="0dp">

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="vertical"
                    android:padding="20dp">

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="ENTREGA"
                        android:textColor="@color/colorOnSurfaceVariant"
                        android:textSize="11sp"
                        android:textStyle="bold"
                        android:letterSpacing="0.08"
                        android:layout_marginBottom="12dp" />

                    <View
                        android:layout_width="match_parent"
                        android:layout_height="1dp"
                        android:background="@color/colorDivider"
                        android:layout_marginBottom="12dp" />

                    <TextView
                        android:id="@+id/tvFechaEntregaCalif"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Entregada: 05/05/2026"
                        android:textColor="@color/colorOnSurface"
                        android:textSize="13sp"
                        android:layout_marginBottom="10dp" />

                    <LinearLayout
                        android:id="@+id/containerArchivoCalif"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:orientation="horizontal"
                        android:gravity="center_vertical"
                        android:clickable="true"
                        android:focusable="true"
                        android:background="?attr/selectableItemBackground"
                        android:paddingVertical="6dp">

                        <ImageView
                            android:layout_width="20dp"
                            android:layout_height="20dp"
                            android:layout_marginEnd="8dp"
                            android:src="@drawable/ic_paperclip"
                            android:contentDescription="Archivo adjunto"
                            app:tint="@color/colorPrimary" />

                        <TextView
                            android:id="@+id/tvArchivoCalif"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="@string/label_archivo_adjunto"
                            android:textColor="@color/colorPrimary"
                            android:textSize="13sp"
                            android:textStyle="bold" />

                    </LinearLayout>

                    <TextView
                        android:id="@+id/tvComentariosAlumno"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:layout_marginTop="12dp"
                        android:text="Adjunto el documento con los layouts solicitados. Cualquier comentario es bienvenido."
                        android:textColor="@color/colorOnSurface"
                        android:textSize="13sp"
                        android:lineSpacingExtra="3dp" />

                </LinearLayout>

            </com.google.android.material.card.MaterialCardView>

            <!-- Card calificación -->
            <com.google.android.material.card.MaterialCardView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginBottom="20dp"
                app:cardBackgroundColor="@color/colorSurface"
                app:cardCornerRadius="12dp"
                app:cardElevation="0dp"
                app:strokeWidth="0dp">

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="vertical"
                    android:padding="20dp">

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="CALIFICACIÓN"
                        android:textColor="@color/colorOnSurfaceVariant"
                        android:textSize="11sp"
                        android:textStyle="bold"
                        android:letterSpacing="0.08"
                        android:layout_marginBottom="12dp" />

                    <View
                        android:layout_width="match_parent"
                        android:layout_height="1dp"
                        android:background="@color/colorDivider"
                        android:layout_marginBottom="14dp" />

                    <com.google.android.material.textfield.TextInputLayout
                        android:id="@+id/tilCalificacionEntrega"
                        style="@style/Widget.Material3.TextInputLayout.OutlinedBox"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:layout_marginBottom="12dp"
                        android:hint="@string/hint_calificacion"
                        app:boxBackgroundColor="@color/colorSurface"
                        app:boxCornerRadiusTopStart="10dp"
                        app:boxCornerRadiusTopEnd="10dp"
                        app:boxCornerRadiusBottomStart="10dp"
                        app:boxCornerRadiusBottomEnd="10dp"
                        app:boxStrokeColor="@color/colorBorder"
                        app:hintTextColor="@color/colorOnSurfaceVariant">

                        <com.google.android.material.textfield.TextInputEditText
                            android:id="@+id/etCalificacionEntrega"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:inputType="number"
                            android:textColor="@color/colorOnSurface"
                            android:textSize="14sp" />

                    </com.google.android.material.textfield.TextInputLayout>

                    <com.google.android.material.textfield.TextInputLayout
                        android:id="@+id/tilRetroalimentacionEntrega"
                        style="@style/Widget.Material3.TextInputLayout.OutlinedBox"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:hint="@string/hint_retroalimentacion"
                        app:boxBackgroundColor="@color/colorSurface"
                        app:boxCornerRadiusTopStart="10dp"
                        app:boxCornerRadiusTopEnd="10dp"
                        app:boxCornerRadiusBottomStart="10dp"
                        app:boxCornerRadiusBottomEnd="10dp"
                        app:boxStrokeColor="@color/colorBorder"
                        app:hintTextColor="@color/colorOnSurfaceVariant">

                        <com.google.android.material.textfield.TextInputEditText
                            android:id="@+id/etRetroalimentacionEntrega"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:inputType="textMultiLine|textCapSentences"
                            android:minLines="3"
                            android:gravity="top|start"
                            android:textColor="@color/colorOnSurface"
                            android:textSize="14sp" />

                    </com.google.android.material.textfield.TextInputLayout>

                </LinearLayout>

            </com.google.android.material.card.MaterialCardView>

            <com.google.android.material.button.MaterialButton
                android:id="@+id/btnGuardarCalificacion"
                android:layout_width="match_parent"
                android:layout_height="52dp"
                android:text="@string/btn_calificar"
                app:cornerRadius="12dp" />

        </LinearLayout>

    </ScrollView>

</LinearLayout>
```

- [ ] **Step 2: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Cero hex**

Run: `grep -E "#[0-9A-Fa-f]{6}" app/src/main/res/layout/fragment_docente_calificar_entrega.xml`
Expected: salida vacía.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/res/layout/fragment_docente_calificar_entrega.xml
git commit -m "feat(docente-tareas): layout para calificar entrega con tres cards"
```

---

### Task 21: Java `DocenteCalificarEntregaFragment`

**Files:**
- Create: `app/src/main/java/com/conectatec/ui/docente/tareas/DocenteCalificarEntregaFragment.java`

- [ ] **Step 1: Crear el fragment con pre-llenado y botón dinámico**

```java
package com.conectatec.ui.docente.tareas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.conectatec.R;
import com.conectatec.databinding.FragmentDocenteCalificarEntregaBinding;
import com.conectatec.ui.docente.tareas.adapter.EntregaDocenteAdapter;
import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteCalificarEntregaFragment extends Fragment {

    private FragmentDocenteCalificarEntregaBinding binding;
    private int tareaId;
    private int alumnoId;

    private boolean yaCalificada = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteCalificarEntregaBinding.inflate(
                inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tareaId  = getArguments() != null ? getArguments().getInt("tareaId", 1) : 1;
        alumnoId = getArguments() != null ? getArguments().getInt("alumnoId", 0) : 0;

        binding.btnVolverCalificar.setOnClickListener(v ->
                requireActivity().onBackPressed());

        cargarDatosAlumno();

        binding.btnGuardarCalificacion.setOnClickListener(v -> onGuardarClicked());
    }

    /**
     * Resuelve los datos del alumno con la misma lógica del adapter de entregas
     * (carga las 6 entregas dummy de la tarea y busca por alumnoId).
     */
    private void cargarDatosAlumno() {
        EntregaDocenteAdapter helper = new EntregaDocenteAdapter();
        helper.cargarParaTarea(tareaId);

        EntregaDocenteAdapter.EntregaDummyDocente entrega = null;
        for (int i = 0; i < helper.conteoTotal(); i++) {
            // accedemos vía filtrar(null) ya aplicado por cargarParaTarea
        }
        // Recorrer la lista filtrada (que tras cargarParaTarea contiene las 6).
        // Recolectamos todas para buscar:
        // (helper.lista no es público — exponemos vía un loop sobre todos los estados)
        // Más simple: recargar y consultar getter que ya existe.
        for (EntregaDocenteAdapter.EntregaDummyDocente e : EntregaDocenteAdapter
                .obtenerListaActual(helper)) {
            if (e.alumnoId == alumnoId) {
                entrega = e;
                break;
            }
        }

        if (entrega == null) {
            Snackbar.make(binding.getRoot(),
                    "Entrega no encontrada", Snackbar.LENGTH_SHORT).show();
            return;
        }

        binding.tvNombreAlumnoCalif.setText(entrega.alumnoNombre);
        binding.tvInicialesAlumnoCalif.setText(entrega.alumnoIniciales);
        binding.tvCorreoAlumnoCalif.setText(
                entrega.alumnoNombre.toLowerCase().replace(" ", ".") + "@conectatec.mx");

        aplicarChipEstado(entrega);

        if (entrega.fechaEntrega != null) {
            binding.tvFechaEntregaCalif.setText("Entregada: " + entrega.fechaEntrega);
            binding.containerArchivoCalif.setVisibility(View.VISIBLE);
        } else {
            binding.tvFechaEntregaCalif.setText("Sin entrega registrada");
            binding.containerArchivoCalif.setVisibility(View.GONE);
        }

        if (entrega.estado == EntregaDocenteAdapter.ESTADO_CALIFICADA
                && entrega.calificacion != null) {
            yaCalificada = true;
            binding.etCalificacionEntrega.setText(String.valueOf(entrega.calificacion));
            binding.btnGuardarCalificacion.setText(R.string.btn_actualizar_calificacion);
        }
    }

    private void aplicarChipEstado(EntregaDocenteAdapter.EntregaDummyDocente e) {
        int drawable;
        String label;
        switch (e.estado) {
            case EntregaDocenteAdapter.ESTADO_ENTREGADA:
                drawable = R.drawable.bg_chip_estudiante; label = "ENTREGADA"; break;
            case EntregaDocenteAdapter.ESTADO_CALIFICADA:
                drawable = R.drawable.bg_chip_estudiante; label = "CALIFICADA"; break;
            case EntregaDocenteAdapter.ESTADO_BORRADOR:
                drawable = R.drawable.bg_chip_pendiente;  label = "BORRADOR";  break;
            default:
                drawable = R.drawable.bg_chip_admin;      label = "SIN ENTREGAR"; break;
        }
        binding.tvChipEstadoCalif.setBackgroundResource(drawable);
        binding.tvChipEstadoCalif.setText(label);
    }

    private void onGuardarClicked() {
        String calif = binding.etCalificacionEntrega.getText() != null
                ? binding.etCalificacionEntrega.getText().toString().trim() : "";
        if (calif.isEmpty()) {
            binding.tilCalificacionEntrega.setError("Requerido");
            return;
        }
        try {
            int valor = Integer.parseInt(calif);
            if (valor < 0 || valor > 100) {
                binding.tilCalificacionEntrega.setError("Debe estar entre 0 y 100");
                return;
            }
        } catch (NumberFormatException nfe) {
            binding.tilCalificacionEntrega.setError("Número inválido");
            return;
        }

        // TODO: llamar a EntregaService.calificar() — luego NotificacionService.enviar(tipo=3)

        Snackbar.make(binding.getRoot(),
                yaCalificada ? "Calificación actualizada" : "Calificación guardada",
                Snackbar.LENGTH_SHORT).show();
        Navigation.findNavController(requireView()).popBackStack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
```

> **NOTA importante:** el método `EntregaDocenteAdapter.obtenerListaActual(helper)` referenciado arriba debe agregarse como helper estático en `EntregaDocenteAdapter` (Task 9 ya creado). Si no quieres agregar un getter público a la lista interna, sustituye el bloque anterior por el método auxiliar más simple del paso 2.

- [ ] **Step 2: Agregar al adapter `EntregaDocenteAdapter` el método estático auxiliar (Editar el archivo creado en Task 9)**

Justo encima del método `private static String[] nombresParaGrupo`, agregar:

```java
/** Helper estático para que otros componentes puedan iterar sobre la lista cargada. */
public static List<EntregaDummyDocente> obtenerListaActual(EntregaDocenteAdapter a) {
    return new ArrayList<>(a.listaCompleta);
}
```

- [ ] **Step 3: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 4: Confirmar TODO en español**

Run: `grep -n "TODO: llamar a EntregaService.calificar" app/src/main/java/com/conectatec/ui/docente/tareas/DocenteCalificarEntregaFragment.java`
Expected: 1 línea.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/conectatec/ui/docente/tareas/DocenteCalificarEntregaFragment.java app/src/main/java/com/conectatec/ui/docente/tareas/adapter/EntregaDocenteAdapter.java
git commit -m "feat(docente-tareas): fragment calificar entrega con pre-llenado y validación"
```

---

### Task 22: Verificación final del módulo

**Files:**
- (sólo verificación, sin cambios de código)

- [ ] **Step 1: Build limpio**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 2: Inventario de archivos del módulo**

Run: `find app/src/main/java/com/conectatec/ui/docente/tareas -name "*.java" | wc -l`
Expected: `10` (6 fragments + 4 adapters)

Run: `find app/src/main/res/layout -name "*tarea*" -o -name "*bloque*" -o -name "*entrega*" | wc -l`
Expected: `>= 9` (los 9 layouts creados aquí; podrían existir más del Plan 3 si reutilizan nombres similares).

- [ ] **Step 3: Verificación de hex literales (cero)**

Run:
```bash
grep -rn "#[0-9A-Fa-f]\{6\}" app/src/main/res/layout/fragment_docente_tareas.xml \
    app/src/main/res/layout/fragment_docente_bloques.xml \
    app/src/main/res/layout/fragment_docente_tareas_bloque.xml \
    app/src/main/res/layout/fragment_docente_crear_tarea.xml \
    app/src/main/res/layout/fragment_docente_entregas.xml \
    app/src/main/res/layout/fragment_docente_calificar_entrega.xml \
    app/src/main/res/layout/item_bloque_docente.xml \
    app/src/main/res/layout/item_tarea_docente.xml \
    app/src/main/res/layout/item_entrega_docente.xml
```
Expected: salida vacía.

- [ ] **Step 4: `@AndroidEntryPoint` en los 6 fragments**

Run: `grep -L "@AndroidEntryPoint" app/src/main/java/com/conectatec/ui/docente/tareas/*.java`
Expected: salida vacía (todos tienen la anotación).

- [ ] **Step 5: `binding = null` en los 6 fragments**

Run: `grep -L "binding = null" app/src/main/java/com/conectatec/ui/docente/tareas/*.java`
Expected: salida vacía.

- [ ] **Step 6: Cero `BottomNavigationView`**

Run: `grep -rn "BottomNavigationView" app/src/main/java/com/conectatec/ui/docente/tareas/ app/src/main/res/layout/fragment_docente_tareas*.xml app/src/main/res/layout/fragment_docente_bloques.xml app/src/main/res/layout/fragment_docente_crear_tarea.xml app/src/main/res/layout/fragment_docente_entregas.xml app/src/main/res/layout/fragment_docente_calificar_entrega.xml`
Expected: salida vacía.

- [ ] **Step 7: TODOs de backend en español (mínimo 2 en este módulo)**

Run: `grep -rn "TODO: llamar a" app/src/main/java/com/conectatec/ui/docente/tareas/ | wc -l`
Expected: `>= 2`

Que incluyan:
```bash
grep -rn "TODO: llamar a TareaService.crearTarea" app/src/main/java/com/conectatec/ui/docente/tareas/
grep -rn "TODO: llamar a EntregaService.calificar" app/src/main/java/com/conectatec/ui/docente/tareas/
```
Expected: cada uno con `>= 1` línea.

- [ ] **Step 8: REGLA DE COORDINACIÓN CON PLAN 3 — IDs y nombres de los 3 grupos**

Run:
```bash
grep -E "GrupoDummyTareas\(1, *\"Programación Móvil 6A\"" app/src/main/java/com/conectatec/ui/docente/tareas/adapter/GrupoTareasDocenteAdapter.java
grep -E "GrupoDummyTareas\(2, *\"Bases de Datos 4B\""    app/src/main/java/com/conectatec/ui/docente/tareas/adapter/GrupoTareasDocenteAdapter.java
grep -E "GrupoDummyTareas\(3, *\"Cálculo Integral 2A\""  app/src/main/java/com/conectatec/ui/docente/tareas/adapter/GrupoTareasDocenteAdapter.java
```
Expected: cada comando con 1 línea encontrada.

Run para confirmar misma materia/totalAlumnos/fechaCreacion contra Plan 3:
```bash
grep -E "\"Programación Móvil\".*18.*\"01/02/2026\"" app/src/main/java/com/conectatec/ui/docente/tareas/adapter/GrupoTareasDocenteAdapter.java
grep -E "\"Bases de Datos\".*16.*\"15/01/2026\""    app/src/main/java/com/conectatec/ui/docente/tareas/adapter/GrupoTareasDocenteAdapter.java
grep -E "\"Cálculo Integral\".*13.*\"20/01/2026\"" app/src/main/java/com/conectatec/ui/docente/tareas/adapter/GrupoTareasDocenteAdapter.java
```
Expected: cada comando con 1 línea encontrada.

- [ ] **Step 9: Confirmar dataset central de 8 tareas con grupoId+bloqueId válidos**

Run:
```bash
grep -E "new TareaDummyDocente\([0-9]+, *\".*\", *\"(TAREA|TRABAJO|EXAMEN|PROYECTO)\", *\"(EN_CURSO|VENCIDA|COMPLETADA)\", *[1-3], *[1-3]," app/src/main/java/com/conectatec/ui/docente/tareas/adapter/TareaDocenteAdapter.java | wc -l
```
Expected: `8`

- [ ] **Step 10: Commit final del plan**

```bash
git add -A
git commit --allow-empty -m "chore(docente-tareas): verificación final módulo tareas docente"
```

---

## Self-review

**1. Cobertura de spec §6.4 (módulo Tareas):**
- §6.4.1 `DocenteTareasFragment` → Task 10 (layout) + Task 11 (Java).
- §6.4.2 `DocenteBloquesFragment` → Task 12 + Task 13.
- §6.4.3 `DocenteTareasBloqueFragment` → Task 14 + Task 15 (filtros tipo + estado).
- §6.4.4 `DocenteCrearTareaFragment` → Task 16 + Task 17 (DatePicker, spinner, TODO `TareaService.crearTarea`).
- §6.4.5 `DocenteEntregasFragment` → Task 18 + Task 19 (LinearProgressIndicator, 3 stats, 5 chips).
- §6.4.6 `DocenteCalificarEntregaFragment` → Task 20 + Task 21 (3 cards, pre-llenado, TODO `EntregaService.calificar`).
- 4 adapters → Tasks 6, 7, 8, 9.
- 3 item layouts nuevos + 1 reutilizado → Tasks 3, 4, 5 + verificación en Task 2.

**2. Mapeo de chips (§7.1, §7.2) consistente:**
- TIPO TAREA → `bg_chip_docente` ✓
- TIPO TRABAJO → `bg_chip_pendiente` ✓
- TIPO EXAMEN → `bg_chip_admin` ✓
- TIPO PROYECTO → `bg_chip_estudiante` ✓
- ESTADO EN_CURSO → `bg_chip_docente` ✓
- ESTADO VENCIDA → `bg_chip_admin` ✓
- ESTADO COMPLETADA → `bg_chip_estudiante` ✓
- ENTREGA ENTREGADA / CALIFICADA → `bg_chip_estudiante` ✓
- ENTREGA BORRADOR → `bg_chip_pendiente` ✓
- ENTREGA SIN_ENTREGAR → `bg_chip_admin` ✓

**3. Acciones de navegación referenciadas (todas declaradas en `nav_docente.xml` por Plan 1):**
- `R.id.action_tareas_to_bloques` (Task 11)
- `R.id.action_bloques_to_tareas_bloque` (Task 13)
- `R.id.action_tareas_bloque_to_crear` (Task 15)
- `R.id.action_tareas_bloque_to_entregas` (Task 15)
- `R.id.action_entregas_to_calificar` (Task 19)

**4. Coordinación con Plan 3:** los 3 grupos en `GrupoTareasDocenteAdapter` (Task 6) coinciden con el dataset del Plan 3 en id, nombre, materia, totalAlumnos y fechaCreacion. Verificación con grep en Task 22 step 8.

**5. Dataset de 8 tareas:** todas con `grupoId ∈ {1,2,3}` y `bloqueId ∈ {1,2,3}`. Distribución: G1B1=2, G1B2=1, G1B3=1, G2B1=1, G2B2=1, G3B1=1, G3B2=1 = 8 ✓.

**6. Sin placeholders:** todo el código está completo. Las 22 tareas son ejecutables paso a paso.

---

## Resumen ejecutivo del plan

- **22 tareas** en orden estricto: strings → items → adapters → fragments + layouts pareados.
- **Tiempo estimado:** ~3.5–4.5 horas en ejecución sub-agéntica (avg ~10 min/tarea, con build entre cada una).
- **6 fragments**, **4 adapters**, **9 layouts nuevos** (3 items + 6 fragments), **1 layout reutilizado** del Plan 3.
- **2 TODOs de backend** sembrados (`TareaService.crearTarea`, `EntregaService.calificar`).
- **Regla de coordinación con Plan 3** anotada en cabecera y verificada con grep en Task 22.
