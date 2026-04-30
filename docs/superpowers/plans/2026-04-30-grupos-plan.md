# Módulo Grupos Docente Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Implementar el módulo Grupos del Docente: lista de grupos del docente, creación con QR placeholder, detalle de grupo (info, miembros preview, avisos, tareas link) y lista completa de miembros.

**Architecture:** Espejo estructural del módulo Grupos del Admin con datos dummy por adapter.

**Tech Stack:** Java 11, Hilt, ViewBinding, Navigation Component.

**Depende de:** Plan 1 (infra) completo.
**Bloquea:** Plan 4 (tareas) — el modelo `GrupoDummyDocente` y los IDs de los 3 grupos deben ser idénticos al modelo equivalente que el Plan 4 declarará localmente (`GrupoDummyTareas` en `GrupoTareasDocenteAdapter`).

### Punto de coordinación con Plan 4 (LEER ANTES DE IMPLEMENTAR)

El Plan 4 (`tareas`) declarará **su propio** adapter `GrupoTareasDocenteAdapter` con una clase interna `GrupoDummyTareas` que es un subconjunto de campos del modelo de este plan (sin `codigoUnion`, sin `activo`). Para que el Plan 4 funcione, los siguientes valores DEBEN replicarse exactamente:

| id | nombre                    | materia              | totalAlumnos | fechaCreacion |
|----|---------------------------|----------------------|--------------|---------------|
| 1  | Programación Móvil 6A     | Programación Móvil   | 18           | 01/02/2026    |
| 2  | Bases de Datos 4B         | Bases de Datos       | 16           | 15/01/2026    |
| 3  | Cálculo Integral 2A       | Cálculo Integral     | 13           | 20/01/2026    |

La Tarea 14 incluye un grep de verificación final para garantizar que estos 3 nombres existen literalmente en `GrupoDocenteAdapter.java`.

---

## Mapa de archivos

### Java (a crear)
- `app/src/main/java/com/conectatec/ui/docente/grupos/adapter/GrupoDocenteAdapter.java` (nuevo)
- `app/src/main/java/com/conectatec/ui/docente/grupos/adapter/MiembroGrupoDocenteAdapter.java` (nuevo)
- `app/src/main/java/com/conectatec/ui/docente/grupos/DocenteCrearGrupoFragment.java` (nuevo)
- `app/src/main/java/com/conectatec/ui/docente/grupos/DocenteGrupoDetalleFragment.java` (nuevo)
- `app/src/main/java/com/conectatec/ui/docente/grupos/DocenteMiembrosGrupoFragment.java` (nuevo)

### Java (a modificar)
- `app/src/main/java/com/conectatec/ui/docente/grupos/DocenteGruposFragment.java` (reemplaza el stub creado por el Plan 1)

### Layouts (a crear)
- `app/src/main/res/layout/fragment_docente_grupos.xml`
- `app/src/main/res/layout/fragment_docente_crear_grupo.xml`
- `app/src/main/res/layout/fragment_docente_grupo_detalle.xml`
- `app/src/main/res/layout/fragment_docente_miembros_grupo.xml`
- `app/src/main/res/layout/item_grupo_docente.xml`
- `app/src/main/res/layout/item_miembro_grupo_docente.xml`

### Strings (a modificar)
- `app/src/main/res/values/strings.xml`

---

## Convenciones (recordatorio rápido)

- `@AndroidEntryPoint` en todos los Fragments.
- ViewBinding: `binding = null` en `onDestroyView()`.
- Args via `getArguments().getInt("grupoId", 0)`.
- Cero `#RRGGBB` en XML — solo `@color/*` y `@drawable/*`.
- Chips de rol/estado: `TextView` con `bg_chip_*` (no Material Chip).
- Empty state via `<include layout="@layout/layout_empty_state"/>`.
- TODOs en español: `// TODO: llamar a [Servicio].[metodo]()`.
- Sub-destinos usan `Navigation.navigate(actionId, args)` directamente (sin NavOptions, animación viene del nav graph).
- Cada tarea termina con `git add` específico + `git commit` convencional.
- Mensajes de commit en formato `feat(docente-grupos): descripción corta`.

---

## Task 1: Strings nuevos

**Files:**
- Modify: `app/src/main/res/values/strings.xml`

- [ ] **Step 1: Agregar los strings nuevos al final del `<resources>`**

Añade antes de `</resources>` en `app/src/main/res/values/strings.xml`:

```xml
    <!-- ═══ Módulo Grupos Docente ═══ -->
    <string name="subtitle_docente_grupos">Tus grupos académicos</string>
    <string name="btn_crear_grupo">Nuevo grupo</string>
    <string name="hint_buscar_grupo_docente">Buscar grupo...</string>
    <string name="hint_nombre_grupo_nuevo">Nombre del grupo</string>
    <string name="hint_materia_grupo_nuevo">Materia</string>
    <string name="hint_descripcion_grupo_nuevo">Descripción (opcional)</string>
    <string name="btn_listo">Listo</string>
    <string name="btn_ver_qr">Ver QR</string>
    <string name="btn_publicar_aviso">Publicar aviso</string>
    <string name="label_codigo_union">Código de unión</string>
    <string name="label_compartir_codigo">Comparte este código con tus alumnos</string>
    <string name="title_detalle_grupo_docente">Detalle de grupo</string>
    <string name="title_miembros_grupo_docente">Miembros del grupo</string>
    <string name="title_nuevo_grupo">Nuevo grupo</string>
```

- [ ] **Step 2: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
git add app/src/main/res/values/strings.xml
git commit -m "feat(docente-grupos): add string resources for grupos module"
```

---

## Task 2: Item layout `item_grupo_docente.xml`

**Files:**
- Create: `app/src/main/res/layout/item_grupo_docente.xml`

- [ ] **Step 1: Crear el archivo**

Contenido completo:

```xml
<?xml version="1.0" encoding="utf-8"?>
<!-- Item de lista de grupos para el Docente -->
<com.google.android.material.card.MaterialCardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginBottom="10dp"
    app:cardBackgroundColor="@color/colorSurface"
    app:cardCornerRadius="12dp"
    app:cardElevation="0dp"
    app:strokeWidth="0dp"
    android:clickable="true"
    android:focusable="true">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="16dp">

        <!-- Fila superior: nombre + badge alumnos -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            android:gravity="center_vertical"
            android:layout_marginBottom="4dp">

            <TextView
                android:id="@+id/tvNombreGrupoDocente"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:text="Nombre del grupo"
                android:textColor="@color/colorOnSurface"
                android:textSize="16sp"
                android:textStyle="bold"
                android:maxLines="1"
                android:ellipsize="end" />

            <LinearLayout
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:gravity="center_vertical"
                android:layout_marginStart="8dp"
                android:paddingHorizontal="10dp"
                android:paddingVertical="4dp"
                android:background="@drawable/bg_chip_docente">

                <ImageView
                    android:layout_width="14dp"
                    android:layout_height="14dp"
                    android:src="@drawable/ic_users"
                    android:contentDescription="@null"
                    app:tint="@color/white"
                    android:layout_marginEnd="4dp" />

                <TextView
                    android:id="@+id/tvAlumnosGrupoDocente"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="0"
                    android:textColor="@color/white"
                    android:textSize="11sp"
                    android:textStyle="bold" />

            </LinearLayout>

        </LinearLayout>

        <!-- Materia -->
        <TextView
            android:id="@+id/tvMateriaGrupoDocente"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Materia"
            android:textColor="@color/colorOnSurfaceVariant"
            android:textSize="12sp"
            android:layout_marginBottom="8dp" />

        <!-- Fecha de creación -->
        <TextView
            android:id="@+id/tvFechaGrupoDocente"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Creado el 01/02/2026"
            android:textColor="@color/colorOnSurfaceVariant"
            android:textSize="11sp" />

    </LinearLayout>

</com.google.android.material.card.MaterialCardView>
```

- [ ] **Step 2: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
git add app/src/main/res/layout/item_grupo_docente.xml
git commit -m "feat(docente-grupos): add item layout for grupo card"
```

---

## Task 3: Item layout `item_miembro_grupo_docente.xml`

**Files:**
- Create: `app/src/main/res/layout/item_miembro_grupo_docente.xml`

- [ ] **Step 1: Crear el archivo**

```xml
<?xml version="1.0" encoding="utf-8"?>
<!-- Item de miembro (alumno) en lista de grupo del Docente -->
<com.google.android.material.card.MaterialCardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginBottom="8dp"
    app:cardBackgroundColor="@color/colorSurface"
    app:cardCornerRadius="12dp"
    app:cardElevation="0dp"
    app:strokeWidth="0dp">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:gravity="center_vertical"
        android:padding="16dp">

        <!-- Avatar 44dp con iniciales -->
        <FrameLayout
            android:layout_width="44dp"
            android:layout_height="44dp"
            android:background="@drawable/bg_avatar_placeholder">

            <TextView
                android:id="@+id/tvInicialesMiembroDocente"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:gravity="center"
                android:text="AL"
                android:textColor="@color/white"
                android:textSize="15sp"
                android:textStyle="bold" />

        </FrameLayout>

        <LinearLayout
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:orientation="vertical"
            android:layout_marginStart="12dp">

            <TextView
                android:id="@+id/tvNombreMiembroDocente"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="Nombre Apellido"
                android:textColor="@color/colorOnSurface"
                android:textSize="14sp"
                android:textStyle="bold"
                android:maxLines="1"
                android:ellipsize="end" />

            <TextView
                android:id="@+id/tvCorreoMiembroDocente"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="2dp"
                android:text="correo@tec.mx"
                android:textColor="@color/colorOnSurfaceVariant"
                android:textSize="12sp"
                android:maxLines="1"
                android:ellipsize="end" />

            <TextView
                android:id="@+id/tvMatriculaMiembroDocente"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="2dp"
                android:text="A01234567"
                android:textColor="@color/colorOnSurfaceVariant"
                android:textSize="11sp"
                android:letterSpacing="0.05" />

        </LinearLayout>

    </LinearLayout>

</com.google.android.material.card.MaterialCardView>
```

- [ ] **Step 2: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
git add app/src/main/res/layout/item_miembro_grupo_docente.xml
git commit -m "feat(docente-grupos): add item layout for miembro card"
```

---

## Task 4: Adapter `GrupoDocenteAdapter`

**Files:**
- Create: `app/src/main/java/com/conectatec/ui/docente/grupos/adapter/GrupoDocenteAdapter.java`

- [ ] **Step 1: Crear el adapter completo**

```java
package com.conectatec.ui.docente.grupos.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatec.databinding.ItemGrupoDocenteBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter de la lista de grupos del Docente con datos dummy.
 * Los IDs (1,2,3) y nombres deben coincidir con el modelo
 * GrupoDummyTareas declarado en el módulo Tareas (Plan 4).
 */
public class GrupoDocenteAdapter
        extends RecyclerView.Adapter<GrupoDocenteAdapter.ViewHolder> {

    /** Modelo dummy de un grupo del docente. */
    public static class GrupoDummyDocente {
        public final int id;
        public final String nombre;
        public final String materia;
        public final int totalAlumnos;
        public final String fechaCreacion;
        public final String codigoUnion;
        public final boolean activo;

        public GrupoDummyDocente(int id, String nombre, String materia,
                                 int totalAlumnos, String fechaCreacion,
                                 String codigoUnion, boolean activo) {
            this.id = id;
            this.nombre = nombre;
            this.materia = materia;
            this.totalAlumnos = totalAlumnos;
            this.fechaCreacion = fechaCreacion;
            this.codigoUnion = codigoUnion;
            this.activo = activo;
        }
    }

    public interface OnGrupoClickListener {
        void onClick(GrupoDummyDocente grupo, int position);
    }

    private final List<GrupoDummyDocente> lista = new ArrayList<>();
    private final List<GrupoDummyDocente> listaCompleta = new ArrayList<>();
    private final OnGrupoClickListener listener;

    public GrupoDocenteAdapter(OnGrupoClickListener listener) {
        this.listener = listener;
        cargarDatosDummy();
    }

    private void cargarDatosDummy() {
        lista.clear();
        listaCompleta.clear();
        lista.add(new GrupoDummyDocente(1, "Programación Móvil 6A", "Programación Móvil", 18, "01/02/2026", "TC-9X4P", true));
        lista.add(new GrupoDummyDocente(2, "Bases de Datos 4B",      "Bases de Datos",      16, "15/01/2026", "TC-K3M2", true));
        lista.add(new GrupoDummyDocente(3, "Cálculo Integral 2A",    "Cálculo Integral",    13, "20/01/2026", "TC-7Z8R", true));
        listaCompleta.addAll(lista);
        notifyDataSetChanged();
    }

    /** Aplica un filtro de texto sobre nombre y materia. */
    public void filtrar(String query) {
        lista.clear();
        String q = query == null ? "" : query.toLowerCase(Locale.getDefault()).trim();
        for (GrupoDummyDocente g : listaCompleta) {
            boolean ok = q.isEmpty()
                    || g.nombre.toLowerCase(Locale.getDefault()).contains(q)
                    || g.materia.toLowerCase(Locale.getDefault()).contains(q);
            if (ok) lista.add(g);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGrupoDocenteBinding b = ItemGrupoDocenteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GrupoDummyDocente g = lista.get(position);
        holder.b.tvNombreGrupoDocente.setText(g.nombre);
        holder.b.tvMateriaGrupoDocente.setText(g.materia);
        holder.b.tvAlumnosGrupoDocente.setText(String.valueOf(g.totalAlumnos));
        holder.b.tvFechaGrupoDocente.setText("Creado el " + g.fechaCreacion);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(g, holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemGrupoDocenteBinding b;
        ViewHolder(ItemGrupoDocenteBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}
```

- [ ] **Step 2: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/conectatec/ui/docente/grupos/adapter/GrupoDocenteAdapter.java
git commit -m "feat(docente-grupos): add GrupoDocenteAdapter with dummy dataset"
```

---

## Task 5: Adapter `MiembroGrupoDocenteAdapter`

**Files:**
- Create: `app/src/main/java/com/conectatec/ui/docente/grupos/adapter/MiembroGrupoDocenteAdapter.java`

- [ ] **Step 1: Crear el adapter completo**

```java
package com.conectatec.ui.docente.grupos.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatec.databinding.ItemMiembroGrupoDocenteBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter de miembros (alumnos) de un grupo del docente.
 * No carga datos en el constructor — la lista se inyecta con setLista().
 */
public class MiembroGrupoDocenteAdapter
        extends RecyclerView.Adapter<MiembroGrupoDocenteAdapter.ViewHolder> {

    /** Modelo dummy de un alumno. */
    public static class MiembroDummyDocente {
        public final int id;
        public final String nombre;
        public final String iniciales;
        public final String correo;
        public final String matricula;

        public MiembroDummyDocente(int id, String nombre, String iniciales,
                                   String correo, String matricula) {
            this.id = id;
            this.nombre = nombre;
            this.iniciales = iniciales;
            this.correo = correo;
            this.matricula = matricula;
        }
    }

    private final List<MiembroDummyDocente> lista = new ArrayList<>();

    public void setLista(List<MiembroDummyDocente> nueva) {
        lista.clear();
        lista.addAll(nueva);
        notifyDataSetChanged();
    }

    public int conteo() { return lista.size(); }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMiembroGrupoDocenteBinding b = ItemMiembroGrupoDocenteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MiembroDummyDocente m = lista.get(position);
        holder.b.tvInicialesMiembroDocente.setText(m.iniciales);
        holder.b.tvNombreMiembroDocente.setText(m.nombre);
        holder.b.tvCorreoMiembroDocente.setText(m.correo);
        holder.b.tvMatriculaMiembroDocente.setText(m.matricula);
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemMiembroGrupoDocenteBinding b;
        ViewHolder(ItemMiembroGrupoDocenteBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}
```

- [ ] **Step 2: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/conectatec/ui/docente/grupos/adapter/MiembroGrupoDocenteAdapter.java
git commit -m "feat(docente-grupos): add MiembroGrupoDocenteAdapter"
```

---

## Task 6: Layout `fragment_docente_grupos.xml`

**Files:**
- Create: `app/src/main/res/layout/fragment_docente_grupos.xml`

- [ ] **Step 1: Crear el layout**

```xml
<?xml version="1.0" encoding="utf-8"?>
<!-- Pantalla de Grupos del Docente: header + buscador + botón crear + lista -->
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/colorBackground"
    android:orientation="vertical">

    <!-- ══ HEADER ══════════════════════════════════════════════════ -->
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
                android:text="@string/title_docente_grupos"
                android:textColor="@color/colorOnSurface"
                android:textSize="26sp"
                android:textStyle="bold"
                android:fontFamily="sans-serif-medium" />

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="2dp"
                android:text="@string/subtitle_docente_grupos"
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
                android:id="@+id/tvHeaderTotalGruposDocente"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="3"
                android:textColor="@color/colorChipDocente"
                android:textSize="24sp"
                android:textStyle="bold"
                android:fontFamily="sans-serif-medium" />

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="grupos"
                android:textColor="@color/colorOnSurfaceVariant"
                android:textSize="10sp"
                android:letterSpacing="0.05"
                android:gravity="center" />

        </LinearLayout>

    </LinearLayout>

    <!-- Botón crear grupo -->
    <com.google.android.material.button.MaterialButton
        android:id="@+id/btnCrearGrupo"
        style="@style/Widget.Material3.Button"
        android:layout_width="match_parent"
        android:layout_height="48dp"
        android:layout_marginHorizontal="16dp"
        android:layout_marginTop="14dp"
        android:text="@string/btn_crear_grupo"
        android:textSize="14sp"
        android:letterSpacing="0.03"
        app:icon="@drawable/ic_plus"
        app:iconGravity="textStart"
        app:iconPadding="8dp"
        app:iconSize="20dp"
        app:cornerRadius="10dp" />

    <!-- Buscador -->
    <com.google.android.material.textfield.TextInputLayout
        android:id="@+id/tilBuscarGrupoDocente"
        style="@style/Widget.Material3.TextInputLayout.OutlinedBox"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginHorizontal="16dp"
        android:layout_marginTop="12dp"
        android:layout_marginBottom="6dp"
        android:hint="@string/hint_buscar_grupo_docente"
        app:boxBackgroundColor="@color/colorSurface"
        app:boxCornerRadiusTopStart="10dp"
        app:boxCornerRadiusTopEnd="10dp"
        app:boxCornerRadiusBottomStart="10dp"
        app:boxCornerRadiusBottomEnd="10dp"
        app:boxStrokeColor="@color/colorBorder"
        app:boxStrokeWidth="1dp"
        app:startIconDrawable="@android:drawable/ic_menu_search"
        app:startIconTint="@color/colorOnSurfaceVariant"
        app:hintTextColor="@color/colorOnSurfaceVariant">

        <com.google.android.material.textfield.TextInputEditText
            android:id="@+id/etBuscarGrupoDocente"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:inputType="textFilter"
            android:maxLines="1"
            android:paddingVertical="10dp"
            android:textColor="@color/colorOnSurface"
            android:textSize="14sp" />

    </com.google.android.material.textfield.TextInputLayout>

    <!-- Lista -->
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rvGruposDocente"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:paddingHorizontal="16dp"
        android:paddingTop="8dp"
        android:paddingBottom="88dp"
        android:clipToPadding="false"
        android:scrollbars="none" />

    <!-- Estado vacío -->
    <include
        android:id="@+id/emptyStateGruposDocente"
        layout="@layout/layout_empty_state"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:visibility="gone" />

</LinearLayout>
```

- [ ] **Step 2: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
git add app/src/main/res/layout/fragment_docente_grupos.xml
git commit -m "feat(docente-grupos): add layout for lista de grupos"
```

---

## Task 7: Java `DocenteGruposFragment` (reemplaza el stub)

**Files:**
- Modify: `app/src/main/java/com/conectatec/ui/docente/grupos/DocenteGruposFragment.java`

- [ ] **Step 1: Reemplazar el contenido completo del archivo**

```java
package com.conectatec.ui.docente.grupos;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.conectatec.R;
import com.conectatec.databinding.FragmentDocenteGruposBinding;
import com.conectatec.ui.docente.grupos.adapter.GrupoDocenteAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteGruposFragment extends Fragment {

    private FragmentDocenteGruposBinding binding;
    private GrupoDocenteAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteGruposBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        setupBusqueda();
        setupBotones();
        actualizarUI();
    }

    private void setupRecyclerView() {
        adapter = new GrupoDocenteAdapter((grupo, pos) -> {
            Bundle args = new Bundle();
            args.putInt("grupoId", grupo.id);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_grupos_to_detalle, args);
        });
        binding.rvGruposDocente.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvGruposDocente.setAdapter(adapter);
    }

    private void setupBusqueda() {
        binding.etBuscarGrupoDocente.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filtrar(s != null ? s.toString() : "");
                actualizarUI();
            }
        });
    }

    private void setupBotones() {
        binding.btnCrearGrupo.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_grupos_to_crear));
    }

    private void actualizarUI() {
        boolean vacio = adapter.getItemCount() == 0;
        binding.rvGruposDocente.setVisibility(vacio ? View.GONE : View.VISIBLE);
        binding.emptyStateGruposDocente.getRoot().setVisibility(vacio ? View.VISIBLE : View.GONE);
        binding.tvHeaderTotalGruposDocente.setText(String.valueOf(adapter.getItemCount()));
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
git add app/src/main/java/com/conectatec/ui/docente/grupos/DocenteGruposFragment.java
git commit -m "feat(docente-grupos): implement lista de grupos fragment"
```

---

## Task 8: Layout `fragment_docente_crear_grupo.xml`

**Files:**
- Create: `app/src/main/res/layout/fragment_docente_crear_grupo.xml`

- [ ] **Step 1: Crear el layout**

```xml
<?xml version="1.0" encoding="utf-8"?>
<!-- Crear nuevo grupo: form + sección de éxito con QR -->
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
            android:id="@+id/btnVolverCrearGrupo"
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
                android:text="@string/title_nuevo_grupo"
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
        android:layout_height="match_parent"
        android:fillViewport="true">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:paddingHorizontal="20dp"
            android:paddingTop="20dp"
            android:paddingBottom="32dp">

            <!-- ══ FORM ══════════════════════════════════════════════ -->
            <LinearLayout
                android:id="@+id/layoutFormCrearGrupo"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical">

                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/tilNombreGrupoNuevo"
                    style="@style/Widget.Material3.TextInputLayout.OutlinedBox"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginBottom="12dp"
                    android:hint="@string/hint_nombre_grupo_nuevo"
                    app:boxBackgroundColor="@color/colorSurface"
                    app:boxStrokeColor="@color/colorBorder"
                    app:hintTextColor="@color/colorOnSurfaceVariant">

                    <com.google.android.material.textfield.TextInputEditText
                        android:id="@+id/etNombreGrupoNuevo"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:inputType="textCapSentences"
                        android:textColor="@color/colorOnSurface"
                        android:textSize="14sp" />

                </com.google.android.material.textfield.TextInputLayout>

                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/tilMateriaGrupoNuevo"
                    style="@style/Widget.Material3.TextInputLayout.OutlinedBox"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginBottom="12dp"
                    android:hint="@string/hint_materia_grupo_nuevo"
                    app:boxBackgroundColor="@color/colorSurface"
                    app:boxStrokeColor="@color/colorBorder"
                    app:hintTextColor="@color/colorOnSurfaceVariant">

                    <com.google.android.material.textfield.TextInputEditText
                        android:id="@+id/etMateriaGrupoNuevo"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:inputType="textCapSentences"
                        android:textColor="@color/colorOnSurface"
                        android:textSize="14sp" />

                </com.google.android.material.textfield.TextInputLayout>

                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/tilDescripcionGrupoNuevo"
                    style="@style/Widget.Material3.TextInputLayout.OutlinedBox"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginBottom="20dp"
                    android:hint="@string/hint_descripcion_grupo_nuevo"
                    app:boxBackgroundColor="@color/colorSurface"
                    app:boxStrokeColor="@color/colorBorder"
                    app:hintTextColor="@color/colorOnSurfaceVariant">

                    <com.google.android.material.textfield.TextInputEditText
                        android:id="@+id/etDescripcionGrupoNuevo"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:inputType="textMultiLine|textCapSentences"
                        android:minLines="3"
                        android:maxLines="6"
                        android:gravity="top|start"
                        android:textColor="@color/colorOnSurface"
                        android:textSize="14sp" />

                </com.google.android.material.textfield.TextInputLayout>

                <com.google.android.material.button.MaterialButton
                    android:id="@+id/btnCrearGrupoNuevo"
                    style="@style/Widget.Material3.Button"
                    android:layout_width="match_parent"
                    android:layout_height="52dp"
                    android:text="@string/btn_crear_grupo"
                    android:textSize="15sp"
                    android:letterSpacing="0.05"
                    app:cornerRadius="10dp" />

            </LinearLayout>

            <!-- ══ SECCIÓN DE ÉXITO (QR + código) ══════════════════ -->
            <LinearLayout
                android:id="@+id/layoutQrExito"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical"
                android:gravity="center_horizontal"
                android:visibility="gone">

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
                        android:gravity="center_horizontal"
                        android:padding="24dp">

                        <ImageView
                            android:layout_width="200dp"
                            android:layout_height="200dp"
                            android:src="@drawable/ic_qr_placeholder"
                            android:contentDescription="@null"
                            android:layout_marginBottom="16dp" />

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="@string/label_codigo_union"
                            android:textColor="@color/colorOnSurfaceVariant"
                            android:textSize="11sp"
                            android:letterSpacing="0.08" />

                        <TextView
                            android:id="@+id/tvCodigoUnion"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:layout_marginTop="4dp"
                            android:text="TC-XXXX"
                            android:textColor="@color/colorPrimary"
                            android:textSize="24sp"
                            android:textStyle="bold"
                            android:fontFamily="monospace"
                            android:letterSpacing="0.1" />

                        <TextView
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:layout_marginTop="12dp"
                            android:text="@string/label_compartir_codigo"
                            android:textColor="@color/colorOnSurfaceVariant"
                            android:textSize="12sp"
                            android:gravity="center" />

                    </LinearLayout>

                </com.google.android.material.card.MaterialCardView>

                <com.google.android.material.button.MaterialButton
                    android:id="@+id/btnListoCrearGrupo"
                    style="@style/Widget.Material3.Button"
                    android:layout_width="match_parent"
                    android:layout_height="52dp"
                    android:text="@string/btn_listo"
                    android:textSize="15sp"
                    android:letterSpacing="0.05"
                    app:cornerRadius="10dp" />

            </LinearLayout>

        </LinearLayout>

    </ScrollView>

</LinearLayout>
```

- [ ] **Step 2: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
git add app/src/main/res/layout/fragment_docente_crear_grupo.xml
git commit -m "feat(docente-grupos): add crear grupo layout with QR success state"
```

---

## Task 9: Java `DocenteCrearGrupoFragment`

**Files:**
- Create: `app/src/main/java/com/conectatec/ui/docente/grupos/DocenteCrearGrupoFragment.java`

- [ ] **Step 1: Crear el fragment**

```java
package com.conectatec.ui.docente.grupos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.conectatec.databinding.FragmentDocenteCrearGrupoBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Random;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteCrearGrupoFragment extends Fragment {

    private FragmentDocenteCrearGrupoBinding binding;
    private static final String CHARSET =
            "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // sin I/O/0/1 para evitar confusión visual

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteCrearGrupoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnVolverCrearGrupo.setOnClickListener(v ->
                requireActivity().onBackPressed());

        binding.btnCrearGrupoNuevo.setOnClickListener(v -> onCrearClicked());

        binding.btnListoCrearGrupo.setOnClickListener(v ->
                Navigation.findNavController(v).popBackStack());
    }

    private void onCrearClicked() {
        String nombre = binding.etNombreGrupoNuevo.getText() != null
                ? binding.etNombreGrupoNuevo.getText().toString().trim() : "";

        if (nombre.isEmpty()) {
            binding.tilNombreGrupoNuevo.setError("El nombre es obligatorio");
            return;
        }
        binding.tilNombreGrupoNuevo.setError(null);

        // TODO: llamar a GrupoService.crearGrupo() para obtener QR real y token desde backend
        String codigo = generarCodigoUnion();
        binding.tvCodigoUnion.setText(codigo);

        binding.layoutFormCrearGrupo.setVisibility(View.GONE);
        binding.layoutQrExito.setVisibility(View.VISIBLE);

        Snackbar.make(binding.getRoot(), "Grupo creado: " + nombre,
                Snackbar.LENGTH_SHORT).show();
    }

    /** Genera un código del estilo "TC-9X4P" con 4 caracteres alfanuméricos. */
    private String generarCodigoUnion() {
        Random r = new Random();
        StringBuilder sb = new StringBuilder("TC-");
        for (int i = 0; i < 4; i++) {
            sb.append(CHARSET.charAt(r.nextInt(CHARSET.length())));
        }
        return sb.toString();
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
git add app/src/main/java/com/conectatec/ui/docente/grupos/DocenteCrearGrupoFragment.java
git commit -m "feat(docente-grupos): implement crear grupo with random codigo"
```

---

## Task 10: Layout `fragment_docente_grupo_detalle.xml`

**Files:**
- Create: `app/src/main/res/layout/fragment_docente_grupo_detalle.xml`

- [ ] **Step 1: Crear el layout**

```xml
<?xml version="1.0" encoding="utf-8"?>
<!-- Detalle de grupo del Docente: info, miembros preview, avisos, tareas -->
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
            android:id="@+id/btnVolverGrupoDetalleDocente"
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
                android:text="@string/title_detalle_grupo_docente"
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
        android:paddingBottom="32dp">

        <!-- ══ Card INFORMACIÓN ══════════════════════════════════ -->
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
                    android:text="INFORMACIÓN"
                    android:textColor="@color/colorOnSurfaceVariant"
                    android:textSize="11sp"
                    android:letterSpacing="0.1"
                    android:textStyle="bold"
                    android:layout_marginBottom="12dp" />

                <TextView
                    android:id="@+id/tvNombreGrupoDetalleDocente"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:text="Programación Móvil 6A"
                    android:textColor="@color/colorOnSurface"
                    android:textSize="20sp"
                    android:textStyle="bold"
                    android:fontFamily="sans-serif-medium" />

                <TextView
                    android:id="@+id/tvMateriaDetalleDocente"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="2dp"
                    android:text="Programación Móvil"
                    android:textColor="@color/colorOnSurfaceVariant"
                    android:textSize="14sp"
                    android:layout_marginBottom="14dp" />

                <View
                    android:layout_width="match_parent"
                    android:layout_height="1dp"
                    android:background="@color/colorDivider"
                    android:layout_marginBottom="12dp" />

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:layout_marginBottom="8dp">

                    <TextView
                        android:layout_width="0dp"
                        android:layout_height="wrap_content"
                        android:layout_weight="1"
                        android:text="@string/label_codigo_union"
                        android:textColor="@color/colorOnSurfaceVariant"
                        android:textSize="13sp" />

                    <TextView
                        android:id="@+id/tvCodigoUnionDetalleDocente"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="TC-XXXX"
                        android:textColor="@color/colorOnSurface"
                        android:textSize="13sp"
                        android:textStyle="bold"
                        android:fontFamily="monospace"
                        android:letterSpacing="0.05" />

                </LinearLayout>

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:layout_marginBottom="8dp">

                    <TextView
                        android:layout_width="0dp"
                        android:layout_height="wrap_content"
                        android:layout_weight="1"
                        android:text="Fecha de creación"
                        android:textColor="@color/colorOnSurfaceVariant"
                        android:textSize="13sp" />

                    <TextView
                        android:id="@+id/tvFechaCreacionDetalleDocente"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="01/02/2026"
                        android:textColor="@color/colorOnSurface"
                        android:textSize="13sp"
                        android:textStyle="bold" />

                </LinearLayout>

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:layout_marginBottom="14dp">

                    <TextView
                        android:layout_width="0dp"
                        android:layout_height="wrap_content"
                        android:layout_weight="1"
                        android:text="Total alumnos"
                        android:textColor="@color/colorOnSurfaceVariant"
                        android:textSize="13sp" />

                    <TextView
                        android:id="@+id/tvTotalAlumnosDetalleDocente"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="0"
                        android:textColor="@color/colorOnSurface"
                        android:textSize="13sp"
                        android:textStyle="bold" />

                </LinearLayout>

                <com.google.android.material.button.MaterialButton
                    android:id="@+id/btnVerQrDetalleDocente"
                    style="@style/Widget.Material3.Button.OutlinedButton"
                    android:layout_width="wrap_content"
                    android:layout_height="40dp"
                    android:text="@string/btn_ver_qr"
                    android:textSize="12sp"
                    android:letterSpacing="0.03"
                    android:paddingHorizontal="16dp"
                    app:cornerRadius="10dp"
                    app:strokeColor="@color/colorPrimary"
                    android:textColor="@color/colorPrimary" />

                <!-- Sección QR (gone por defecto) -->
                <LinearLayout
                    android:id="@+id/layoutQrDetalleDocente"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="vertical"
                    android:gravity="center_horizontal"
                    android:layout_marginTop="14dp"
                    android:visibility="gone">

                    <ImageView
                        android:layout_width="180dp"
                        android:layout_height="180dp"
                        android:src="@drawable/ic_qr_placeholder"
                        android:contentDescription="@null" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_marginTop="8dp"
                        android:text="@string/label_compartir_codigo"
                        android:textColor="@color/colorOnSurfaceVariant"
                        android:textSize="12sp" />

                </LinearLayout>

            </LinearLayout>

        </com.google.android.material.card.MaterialCardView>

        <!-- ══ Card MIEMBROS preview 5 ════════════════════════════ -->
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

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:gravity="center_vertical"
                    android:layout_marginBottom="12dp">

                    <TextView
                        android:layout_width="0dp"
                        android:layout_height="wrap_content"
                        android:layout_weight="1"
                        android:text="MIEMBROS"
                        android:textColor="@color/colorOnSurfaceVariant"
                        android:textSize="11sp"
                        android:letterSpacing="0.1"
                        android:textStyle="bold" />

                    <TextView
                        android:id="@+id/tvVerTodosMiembrosDocente"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Ver todos"
                        android:textColor="@color/colorPrimary"
                        android:textSize="12sp"
                        android:textStyle="bold"
                        android:clickable="true"
                        android:focusable="true" />

                </LinearLayout>

                <View
                    android:layout_width="match_parent"
                    android:layout_height="1dp"
                    android:background="@color/colorDivider"
                    android:layout_marginBottom="12dp" />

                <LinearLayout
                    android:id="@+id/containerMiembrosGrupoDocente"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="vertical" />

            </LinearLayout>

        </com.google.android.material.card.MaterialCardView>

        <!-- ══ Card AVISOS preview 3 ══════════════════════════════ -->
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
                    android:text="AVISOS"
                    android:textColor="@color/colorOnSurfaceVariant"
                    android:textSize="11sp"
                    android:letterSpacing="0.1"
                    android:textStyle="bold"
                    android:layout_marginBottom="12dp" />

                <View
                    android:layout_width="match_parent"
                    android:layout_height="1dp"
                    android:background="@color/colorDivider"
                    android:layout_marginBottom="12dp" />

                <LinearLayout
                    android:id="@+id/containerAvisosGrupoDocente"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="vertical"
                    android:layout_marginBottom="14dp" />

                <com.google.android.material.button.MaterialButton
                    android:id="@+id/btnPublicarAviso"
                    style="@style/Widget.Material3.Button.OutlinedButton"
                    android:layout_width="match_parent"
                    android:layout_height="44dp"
                    android:text="@string/btn_publicar_aviso"
                    android:textSize="13sp"
                    android:letterSpacing="0.03"
                    app:cornerRadius="10dp"
                    app:strokeColor="@color/colorPrimary"
                    android:textColor="@color/colorPrimary" />

            </LinearLayout>

        </com.google.android.material.card.MaterialCardView>

        <!-- ══ Card TAREAS PUBLICADAS ════════════════════════════ -->
        <com.google.android.material.card.MaterialCardView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
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
                    android:text="TAREAS PUBLICADAS"
                    android:textColor="@color/colorOnSurfaceVariant"
                    android:textSize="11sp"
                    android:letterSpacing="0.1"
                    android:textStyle="bold"
                    android:layout_marginBottom="12dp" />

                <TextView
                    android:id="@+id/tvTotalTareasGrupoDocente"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="0 tareas"
                    android:textColor="@color/colorOnSurface"
                    android:textSize="22sp"
                    android:textStyle="bold"
                    android:layout_marginBottom="14dp" />

                <com.google.android.material.button.MaterialButton
                    android:id="@+id/btnVerTareasGrupo"
                    style="@style/Widget.Material3.Button"
                    android:layout_width="match_parent"
                    android:layout_height="44dp"
                    android:text="Ver tareas"
                    android:textSize="13sp"
                    android:letterSpacing="0.03"
                    app:cornerRadius="10dp" />

            </LinearLayout>

        </com.google.android.material.card.MaterialCardView>

    </LinearLayout>

    </ScrollView>

</LinearLayout>
```

- [ ] **Step 2: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
git add app/src/main/res/layout/fragment_docente_grupo_detalle.xml
git commit -m "feat(docente-grupos): add detalle de grupo layout with 4 cards"
```

---

## Task 11: Java `DocenteGrupoDetalleFragment`

**Files:**
- Create: `app/src/main/java/com/conectatec/ui/docente/grupos/DocenteGrupoDetalleFragment.java`

- [ ] **Step 1: Crear el fragment**

```java
package com.conectatec.ui.docente.grupos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.conectatec.R;
import com.conectatec.databinding.FragmentDocenteGrupoDetalleBinding;
import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteGrupoDetalleFragment extends Fragment {

    private FragmentDocenteGrupoDetalleBinding binding;
    private int grupoId = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteGrupoDetalleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        grupoId = getArguments() != null ? getArguments().getInt("grupoId", 1) : 1;

        cargarDatosGrupo();
        cargarMiembrosPreview();
        cargarAvisosPreview();
        setupListeners();
    }

    private void cargarDatosGrupo() {
        String nombre, materia, codigo, fecha;
        int alumnos, totalTareas;
        switch (grupoId) {
            case 2:
                nombre = "Bases de Datos 4B";
                materia = "Bases de Datos";
                codigo = "TC-K3M2";
                fecha = "15/01/2026";
                alumnos = 16;
                totalTareas = 2;
                break;
            case 3:
                nombre = "Cálculo Integral 2A";
                materia = "Cálculo Integral";
                codigo = "TC-7Z8R";
                fecha = "20/01/2026";
                alumnos = 13;
                totalTareas = 2;
                break;
            case 1:
            default:
                nombre = "Programación Móvil 6A";
                materia = "Programación Móvil";
                codigo = "TC-9X4P";
                fecha = "01/02/2026";
                alumnos = 18;
                totalTareas = 4;
                break;
        }
        binding.tvNombreGrupoDetalleDocente.setText(nombre);
        binding.tvMateriaDetalleDocente.setText(materia);
        binding.tvCodigoUnionDetalleDocente.setText(codigo);
        binding.tvFechaCreacionDetalleDocente.setText(fecha);
        binding.tvTotalAlumnosDetalleDocente.setText(String.valueOf(alumnos));
        binding.tvTotalTareasGrupoDocente.setText(totalTareas + " tareas");
    }

    private void cargarMiembrosPreview() {
        binding.containerMiembrosGrupoDocente.removeAllViews();
        String[] preview;
        switch (grupoId) {
            case 2:
                preview = new String[]{ "Gabriel Pérez", "Helena Soto", "Iván Castro",
                        "Julia Domínguez", "Karla Romero" };
                break;
            case 3:
                preview = new String[]{ "Mario Reyes", "Nadia Salas", "Óscar Aguilar",
                        "Patricia Núñez", "Quirino Jiménez" };
                break;
            case 1:
            default:
                preview = new String[]{ "Ana López", "Bruno García", "Carla Méndez",
                        "Diego Ruiz", "Elena Torres" };
                break;
        }
        for (String nombre : preview) {
            TextView tv = new TextView(requireContext());
            tv.setText("• " + nombre);
            tv.setTextColor(requireContext().getColor(R.color.colorOnSurfaceVariant));
            tv.setTextSize(13f);
            tv.setPadding(0, 0, 0, 8);
            binding.containerMiembrosGrupoDocente.addView(tv);
        }
    }

    private void cargarAvisosPreview() {
        binding.containerAvisosGrupoDocente.removeAllViews();
        String[] avisos = {
                "Recordatorio: lectura del capítulo 4 para el viernes",
                "Cambio de horario: jueves a las 11:00 hrs",
                "Material adicional disponible en la plataforma"
        };
        for (String aviso : avisos) {
            TextView tv = new TextView(requireContext());
            tv.setText("• " + aviso);
            tv.setTextColor(requireContext().getColor(R.color.colorOnSurfaceVariant));
            tv.setTextSize(13f);
            tv.setPadding(0, 0, 0, 8);
            tv.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            binding.containerAvisosGrupoDocente.addView(tv);
        }
    }

    private void setupListeners() {
        binding.btnVolverGrupoDetalleDocente.setOnClickListener(v ->
                requireActivity().onBackPressed());

        binding.btnVerQrDetalleDocente.setOnClickListener(v -> {
            boolean visible = binding.layoutQrDetalleDocente.getVisibility() == View.VISIBLE;
            binding.layoutQrDetalleDocente.setVisibility(visible ? View.GONE : View.VISIBLE);
        });

        binding.tvVerTodosMiembrosDocente.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putInt("grupoId", grupoId);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_grupo_detalle_to_miembros, args);
        });

        binding.btnPublicarAviso.setOnClickListener(v -> {
            // TODO: llamar a AvisoService.publicar()
            Snackbar.make(binding.getRoot(), "Aviso publicado",
                    Snackbar.LENGTH_SHORT).show();
        });

        binding.btnVerTareasGrupo.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putInt("grupoId", grupoId);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_grupo_detalle_to_tareas_grupo, args);
        });
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
git add app/src/main/java/com/conectatec/ui/docente/grupos/DocenteGrupoDetalleFragment.java
git commit -m "feat(docente-grupos): implement detalle de grupo with 4 cards"
```

---

## Task 12: Layout `fragment_docente_miembros_grupo.xml`

**Files:**
- Create: `app/src/main/res/layout/fragment_docente_miembros_grupo.xml`

- [ ] **Step 1: Crear el layout**

```xml
<?xml version="1.0" encoding="utf-8"?>
<!-- Lista completa de miembros (alumnos) del grupo del Docente -->
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
            android:id="@+id/btnVolverMiembrosDocente"
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
                android:text="@string/title_miembros_grupo_docente"
                android:textColor="@color/colorOnSurface"
                android:textSize="20sp"
                android:textStyle="bold"
                android:fontFamily="sans-serif-medium" />

        </LinearLayout>

        <TextView
            android:id="@+id/tvTotalMiembrosHeaderDocente"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="0"
            android:textColor="@color/white"
            android:textSize="12sp"
            android:textStyle="bold"
            android:paddingHorizontal="10dp"
            android:paddingVertical="4dp"
            android:background="@drawable/bg_chip_docente" />

    </LinearLayout>

    <View
        android:layout_width="match_parent"
        android:layout_height="3dp"
        android:background="@color/colorPrimary" />

    <!-- Subtítulo dinámico -->
    <TextView
        android:id="@+id/tvSubtituloMiembrosDocente"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:paddingHorizontal="20dp"
        android:paddingTop="14dp"
        android:paddingBottom="10dp"
        android:text="Grupo"
        android:textColor="@color/colorOnSurfaceVariant"
        android:textSize="13sp" />

    <!-- RecyclerView -->
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rvMiembrosGrupoDocente"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:paddingHorizontal="16dp"
        android:paddingBottom="88dp"
        android:clipToPadding="false"
        android:overScrollMode="never" />

    <!-- Empty state -->
    <include
        android:id="@+id/emptyStateMiembrosDocente"
        layout="@layout/layout_empty_state"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:visibility="gone" />

</LinearLayout>
```

- [ ] **Step 2: Verificar build**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
git add app/src/main/res/layout/fragment_docente_miembros_grupo.xml
git commit -m "feat(docente-grupos): add miembros del grupo layout"
```

---

## Task 13: Java `DocenteMiembrosGrupoFragment`

**Files:**
- Create: `app/src/main/java/com/conectatec/ui/docente/grupos/DocenteMiembrosGrupoFragment.java`

- [ ] **Step 1: Crear el fragment**

```java
package com.conectatec.ui.docente.grupos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.conectatec.databinding.FragmentDocenteMiembrosGrupoBinding;
import com.conectatec.ui.docente.grupos.adapter.MiembroGrupoDocenteAdapter;
import com.conectatec.ui.docente.grupos.adapter.MiembroGrupoDocenteAdapter.MiembroDummyDocente;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteMiembrosGrupoFragment extends Fragment {

    private FragmentDocenteMiembrosGrupoBinding binding;
    private MiembroGrupoDocenteAdapter adapter;

    // 6 alumnos por grupo (IDs 101-106 / 201-206 / 301-306)
    private static final MiembroDummyDocente[][] MIEMBROS_POR_GRUPO = {
        // grupoId 1 — Programación Móvil 6A
        {
            new MiembroDummyDocente(101, "Ana López",       "AL", "a.lopez@tec.mx",    "A20101"),
            new MiembroDummyDocente(102, "Bruno García",    "BG", "b.garcia@tec.mx",   "A20102"),
            new MiembroDummyDocente(103, "Carla Méndez",    "CM", "c.mendez@tec.mx",   "A20103"),
            new MiembroDummyDocente(104, "Diego Ruiz",      "DR", "d.ruiz@tec.mx",     "A20104"),
            new MiembroDummyDocente(105, "Elena Torres",    "ET", "e.torres@tec.mx",   "A20105"),
            new MiembroDummyDocente(106, "Fernando Vega",   "FV", "f.vega@tec.mx",     "A20106"),
        },
        // grupoId 2 — Bases de Datos 4B
        {
            new MiembroDummyDocente(201, "Gabriel Pérez",   "GP", "g.perez@tec.mx",    "A20201"),
            new MiembroDummyDocente(202, "Helena Soto",     "HS", "h.soto@tec.mx",     "A20202"),
            new MiembroDummyDocente(203, "Iván Castro",     "IC", "i.castro@tec.mx",   "A20203"),
            new MiembroDummyDocente(204, "Julia Domínguez", "JD", "j.dominguez@tec.mx","A20204"),
            new MiembroDummyDocente(205, "Karla Romero",    "KR", "k.romero@tec.mx",   "A20205"),
            new MiembroDummyDocente(206, "Leonardo Flores", "LF", "l.flores@tec.mx",   "A20206"),
        },
        // grupoId 3 — Cálculo Integral 2A
        {
            new MiembroDummyDocente(301, "Mario Reyes",     "MR", "m.reyes@tec.mx",    "A20301"),
            new MiembroDummyDocente(302, "Nadia Salas",     "NS", "n.salas@tec.mx",    "A20302"),
            new MiembroDummyDocente(303, "Óscar Aguilar",   "OA", "o.aguilar@tec.mx",  "A20303"),
            new MiembroDummyDocente(304, "Patricia Núñez",  "PN", "p.nunez@tec.mx",    "A20304"),
            new MiembroDummyDocente(305, "Quirino Jiménez", "QJ", "q.jimenez@tec.mx",  "A20305"),
            new MiembroDummyDocente(306, "Renata Ortiz",    "RO", "r.ortiz@tec.mx",    "A20306"),
        },
    };

    private static final String[] NOMBRES_GRUPO = {
            "Programación Móvil 6A",
            "Bases de Datos 4B",
            "Cálculo Integral 2A",
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteMiembrosGrupoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int grupoId = getArguments() != null ? getArguments().getInt("grupoId", 1) : 1;

        setupRecyclerView(grupoId);
        binding.btnVolverMiembrosDocente.setOnClickListener(v ->
                requireActivity().onBackPressed());
    }

    private void setupRecyclerView(int grupoId) {
        adapter = new MiembroGrupoDocenteAdapter();

        int idx = Math.max(0, Math.min(grupoId - 1, MIEMBROS_POR_GRUPO.length - 1));
        MiembroDummyDocente[] plantilla = MIEMBROS_POR_GRUPO[idx];

        List<MiembroDummyDocente> lista = new ArrayList<>();
        for (MiembroDummyDocente m : plantilla) {
            lista.add(m);
        }
        adapter.setLista(lista);

        binding.rvMiembrosGrupoDocente.setLayoutManager(
                new LinearLayoutManager(requireContext()));
        binding.rvMiembrosGrupoDocente.setAdapter(adapter);

        // Header dinámico
        String nombreGrupo = idx < NOMBRES_GRUPO.length ? NOMBRES_GRUPO[idx] : "Grupo";
        binding.tvSubtituloMiembrosDocente.setText("Miembros — " + nombreGrupo);
        binding.tvTotalMiembrosHeaderDocente.setText(String.valueOf(adapter.conteo()));

        boolean vacio = adapter.conteo() == 0;
        binding.rvMiembrosGrupoDocente.setVisibility(vacio ? View.GONE : View.VISIBLE);
        binding.emptyStateMiembrosDocente.getRoot()
                .setVisibility(vacio ? View.VISIBLE : View.GONE);
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
git add app/src/main/java/com/conectatec/ui/docente/grupos/DocenteMiembrosGrupoFragment.java
git commit -m "feat(docente-grupos): implement miembros del grupo with 6 alumnos"
```

---

## Task 14: Verificación final del módulo

**Files:**
- (sin cambios — sólo greps y build final)

- [ ] **Step 1: Verificar inventario de archivos**

Run:
```bash
find app/src/main/java/com/conectatec/ui/docente/grupos -name "*.java" | sort
```
Expected (4 fragments + 2 adapters = 6 archivos):
```
.../docente/grupos/DocenteCrearGrupoFragment.java
.../docente/grupos/DocenteGrupoDetalleFragment.java
.../docente/grupos/DocenteGruposFragment.java
.../docente/grupos/DocenteMiembrosGrupoFragment.java
.../docente/grupos/adapter/GrupoDocenteAdapter.java
.../docente/grupos/adapter/MiembroGrupoDocenteAdapter.java
```

```bash
ls app/src/main/res/layout/ | grep -E "(docente_grupos|docente_crear_grupo|docente_grupo_detalle|docente_miembros_grupo|grupo_docente|miembro_grupo_docente)" | sort
```
Expected (4 fragments + 2 items = 6 layouts):
```
fragment_docente_crear_grupo.xml
fragment_docente_grupo_detalle.xml
fragment_docente_grupos.xml
fragment_docente_miembros_grupo.xml
item_grupo_docente.xml
item_miembro_grupo_docente.xml
```

- [ ] **Step 2: Verificar cero hex literales en layouts del módulo**

Run:
```bash
grep -rn "#[0-9A-Fa-f]\{6\}" \
  app/src/main/res/layout/fragment_docente_grupos.xml \
  app/src/main/res/layout/fragment_docente_crear_grupo.xml \
  app/src/main/res/layout/fragment_docente_grupo_detalle.xml \
  app/src/main/res/layout/fragment_docente_miembros_grupo.xml \
  app/src/main/res/layout/item_grupo_docente.xml \
  app/src/main/res/layout/item_miembro_grupo_docente.xml
```
Expected: vacío (sin output).

- [ ] **Step 3: Verificar `@AndroidEntryPoint` en todos los fragments**

Run:
```bash
grep -L "@AndroidEntryPoint" \
  app/src/main/java/com/conectatec/ui/docente/grupos/DocenteGruposFragment.java \
  app/src/main/java/com/conectatec/ui/docente/grupos/DocenteCrearGrupoFragment.java \
  app/src/main/java/com/conectatec/ui/docente/grupos/DocenteGrupoDetalleFragment.java \
  app/src/main/java/com/conectatec/ui/docente/grupos/DocenteMiembrosGrupoFragment.java
```
Expected: vacío.

- [ ] **Step 4: Verificar `binding = null` en todos los fragments**

Run:
```bash
grep -L "binding = null" \
  app/src/main/java/com/conectatec/ui/docente/grupos/DocenteGruposFragment.java \
  app/src/main/java/com/conectatec/ui/docente/grupos/DocenteCrearGrupoFragment.java \
  app/src/main/java/com/conectatec/ui/docente/grupos/DocenteGrupoDetalleFragment.java \
  app/src/main/java/com/conectatec/ui/docente/grupos/DocenteMiembrosGrupoFragment.java
```
Expected: vacío.

- [ ] **Step 5: Verificar TODOs en español del módulo**

Run:
```bash
grep -rn "TODO: llamar" app/src/main/java/com/conectatec/ui/docente/grupos/
```
Expected: al menos 2 ocurrencias:
- `DocenteCrearGrupoFragment` → `// TODO: llamar a GrupoService.crearGrupo()...`
- `DocenteGrupoDetalleFragment` → `// TODO: llamar a AvisoService.publicar()`

- [ ] **Step 6: Verificar coordinación con Plan 4 — IDs y nombres de grupos**

Run:
```bash
grep -E "(Programación Móvil 6A|Bases de Datos 4B|Cálculo Integral 2A)" \
  app/src/main/java/com/conectatec/ui/docente/grupos/adapter/GrupoDocenteAdapter.java
```
Expected: 3 líneas (una por cada nombre de grupo). Estos 3 nombres son los que el Plan 4 debe replicar literalmente en `GrupoTareasDocenteAdapter`.

- [ ] **Step 7: Verificar que las acciones del nav graph existen (referenciadas por el Plan 1)**

Run:
```bash
grep -E "(action_grupos_to_crear|action_grupos_to_detalle|action_grupo_detalle_to_miembros|action_grupo_detalle_to_tareas_grupo)" \
  app/src/main/res/navigation/nav_docente.xml
```
Expected: 4 líneas (una por acción). Si falta alguna, el Plan 1 está incompleto y este módulo no compilará al ejecutarse en runtime.

- [ ] **Step 8: Build final del módulo completo**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 9: Commit de cierre del módulo**

```bash
git commit --allow-empty -m "feat(docente-grupos): module complete — verified inventory, hex-free layouts, AndroidEntryPoint, binding lifecycle, Spanish TODOs"
```

---

## Self-review

**1. Spec coverage (§6.3 completo):**
- §6.3.1 `DocenteGruposFragment` → Tasks 6, 7
- §6.3.2 `DocenteCrearGrupoFragment` → Tasks 8, 9
- §6.3.3 `DocenteGrupoDetalleFragment` → Tasks 10, 11
- §6.3.4 `DocenteMiembrosGrupoFragment` → Tasks 12, 13
- 2 adapters → Tasks 4, 5
- 2 items → Tasks 2, 3
- Strings nuevos → Task 1
- Verificación final → Task 14

**2. Sin placeholders:** Cada paso contiene código completo o comandos exactos con expected output.

**3. IDs y nombres de los 3 grupos coinciden con el spec §6.3.1:** Sí — verificados en `cargarDatosDummy()` de `GrupoDocenteAdapter` y replicados en el `switch(grupoId)` de `DocenteGrupoDetalleFragment`.

**4. Acciones del nav graph referenciadas:**
- `action_grupos_to_crear` (Task 7)
- `action_grupos_to_detalle` (Task 7)
- `action_grupo_detalle_to_miembros` (Task 11)
- `action_grupo_detalle_to_tareas_grupo` (Task 11)
Todas existen en `nav_docente.xml` del Plan 1 según §4.3 del spec.

**5. Orden de tareas:**
- Strings (1) antes de layouts que los usan (2, 3, 6, 8, 10, 12).
- Items (2, 3) antes de adapters que los inflan (4, 5).
- Adapters (4, 5) antes de fragments que los usan (7, 11, 13).
- Layouts (6, 8, 10, 12) antes de los Java que generan el ViewBinding (7, 9, 11, 13).

**6. Coordinación con Plan 4:** Documentada en cabecera + Task 14 Step 6 (grep verificación).
