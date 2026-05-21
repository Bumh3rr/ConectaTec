# Dashboard Charts — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Agregar gráficas interactivas (PieChart donut y BarChart) a los dashboards del Docente y del Admin usando MPAndroidChart v3.1.0, manteniendo la armonía visual del proyecto.

**Architecture:** Ambos fragments cargan datos dummy hardcodeados directamente en `configurarGraficas()` — sin ViewModel nuevo ni repos adicionales. El layout del Docente elimina los 2 KPI cards redundantes (grupos/alumnos) y la card de avisos del día; los reemplaza con 3 nuevas cards (donut, barras, progreso). El layout del Admin elimina la sección "Actividad Reciente" y agrega 2 nuevas cards (donut usuarios, barras actividades).

**Tech Stack:** MPAndroidChart v3.1.0 (JitPack), Java 11, ViewBinding, ContextCompat, ColorStateList, EntradaAnimator

---

## Mapa de archivos

| Archivo | Cambio |
|---|---|
| `app/build.gradle.kts` | Agregar dependencia MPAndroidChart |
| `app/src/main/res/layout/fragment_docente_dashboard.xml` | Quitar KPI row 1 + cardActividadHoy; agregar 3 cards nuevas |
| `app/src/main/java/com/conectatec/ui/docente/dashboard/DocenteDashboardFragment.java` | Quitar 2 listeners; actualizar observer; agregar `configurarGraficas()` |
| `app/src/main/res/layout/fragment_admin_dashboard.xml` | Quitar sección "ACTIVIDAD RECIENTE"; agregar 2 cards nuevas |
| `app/src/main/java/com/conectatec/ui/admin/dashboard/AdminDashboardFragment.java` | Agregar `@AndroidEntryPoint`, `EntradaAnimator`, `configurarGraficas()` |

---

## Task 1: Agregar dependencia MPAndroidChart

**Files:**
- Modify: `app/build.gradle.kts`

- [ ] **Step 1: Agregar la dependencia al bloque `dependencies`**

Abre `app/build.gradle.kts`. Dentro del bloque `dependencies {` (al final, antes del cierre), agrega:

```kotlin
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
```

(JitPack ya está declarado en `settings.gradle.kts` — no hay que tocar ese archivo.)

- [ ] **Step 2: Sincronizar Gradle**

En Android Studio: **File → Sync Project with Gradle Files**, o en terminal:

```bash
cd /home/crisgoat/Desarrollo/ConectaTec
./gradlew :app:dependencies --configuration releaseRuntimeClasspath | grep -i "mpandroid"
```

Resultado esperado: una línea con `com.github.PhilJay:MPAndroidChart:v3.1.0`

- [ ] **Step 3: Commit**

```bash
git add app/build.gradle.kts
git commit -m "build: agregar MPAndroidChart v3.1.0 para gráficas en dashboards"
```

---

## Task 2: Actualizar layout del Dashboard Docente

**Files:**
- Modify: `app/src/main/res/layout/fragment_docente_dashboard.xml`

> **Referencia de datos para las gráficas (consistentes con `TareasRepositoryImpl.DATASET`):**
> - Donut: calificadas=14, entregadas=16, pendientes=9 (total=39, completud=77%)
> - Barras grupos: PM 6A=18, BD 4B=16, Cálculo 2A=13
> - Progreso tareas: "Práctica 1: Layouts" 12/18, "Proyecto Final" 5/18, "Trabajo de investigación" 8/18

- [ ] **Step 1: Eliminar Fila 1 del `layoutKpisDocente`**

En `fragment_docente_dashboard.xml`, dentro de `<LinearLayout android:id="@+id/layoutKpisDocente"`, localiza y **elimina completo** el primer `<LinearLayout>` hijo (el que contiene `cardGruposActivosDocente` y `cardAlumnosTotales`). También elimina el `<LinearLayout android:layout_marginTop="8dp">` wrapper de la Fila 2 y conviértelo al mismo nivel (quita solo el wrapper, conserva las 2 cards de "Tareas activas" y "Por revisar").

El resultado de `layoutKpisDocente` debe quedar así:

```xml
<LinearLayout
    android:id="@+id/layoutKpisDocente"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="horizontal"
    android:layout_marginBottom="12dp">

    <com.google.android.material.card.MaterialCardView
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:layout_marginEnd="6dp"
        app:cardBackgroundColor="@color/colorSurface"
        app:cardCornerRadius="12dp"
        app:cardElevation="0dp"
        app:strokeWidth="0dp">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:padding="16dp">

            <FrameLayout
                android:layout_width="36dp"
                android:layout_height="36dp"
                android:background="@drawable/bg_chip_docente">

                <ImageView
                    android:layout_width="18dp"
                    android:layout_height="18dp"
                    android:layout_gravity="center"
                    android:src="@drawable/ic_assignment"
                    android:contentDescription="@null"
                    app:tint="@color/white" />

            </FrameLayout>

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Tareas activas"
                android:textColor="@color/colorOnSurfaceVariant"
                android:textSize="11sp"
                android:layout_marginTop="10dp"
                android:layout_marginBottom="4dp" />

            <TextView
                android:id="@+id/tvTareasActivasDashboard"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="5"
                android:textColor="@color/colorOnSurface"
                android:textSize="26sp"
                android:textStyle="bold"
                android:fontFamily="sans-serif-medium" />

        </LinearLayout>

    </com.google.android.material.card.MaterialCardView>

    <com.google.android.material.card.MaterialCardView
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:layout_marginStart="6dp"
        app:cardBackgroundColor="@color/colorSurface"
        app:cardCornerRadius="12dp"
        app:cardElevation="0dp"
        app:strokeWidth="0dp">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:padding="16dp">

            <FrameLayout
                android:layout_width="36dp"
                android:layout_height="36dp"
                android:background="@drawable/bg_chip_pendiente">

                <ImageView
                    android:layout_width="18dp"
                    android:layout_height="18dp"
                    android:layout_gravity="center"
                    android:src="@drawable/ic_grading"
                    android:contentDescription="@null"
                    app:tint="@color/white" />

            </FrameLayout>

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Por revisar"
                android:textColor="@color/colorOnSurfaceVariant"
                android:textSize="11sp"
                android:layout_marginTop="10dp"
                android:layout_marginBottom="4dp" />

            <TextView
                android:id="@+id/tvPorRevisarDashboard"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="5"
                android:textColor="@color/colorOnSurface"
                android:textSize="26sp"
                android:textStyle="bold"
                android:fontFamily="sans-serif-medium" />

        </LinearLayout>

    </com.google.android.material.card.MaterialCardView>

</LinearLayout>
```

- [ ] **Step 2: Eliminar `cardActividadHoyDashboard`**

Localiza y elimina el `<com.google.android.material.card.MaterialCardView android:id="@+id/cardActividadHoyDashboard"` completo (incluyendo su cierre `</com.google.android.material.card.MaterialCardView>`).

- [ ] **Step 3: Agregar `cardDonutEntregas` después de `layoutKpisDocente`**

Inserta lo siguiente en el layout de contenido, después de `layoutKpisDocente` y antes de `cardTareasRecientesDashboard`:

```xml
<!-- ── CARD DONUT: ESTADO GLOBAL DE ENTREGAS ─────── -->
<com.google.android.material.card.MaterialCardView
    android:id="@+id/cardDonutEntregas"
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
        android:orientation="vertical">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            android:gravity="center_vertical"
            android:paddingHorizontal="16dp"
            android:paddingTop="16dp"
            android:paddingBottom="10dp">

            <ImageView
                android:layout_width="16dp"
                android:layout_height="16dp"
                android:layout_marginEnd="8dp"
                android:src="@drawable/ic_grading"
                android:contentDescription="@null"
                app:tint="@color/colorPrimary" />

            <TextView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="Estado global de entregas"
                android:textColor="@color/colorOnSurface"
                android:textSize="13sp"
                android:textStyle="bold" />

        </LinearLayout>

        <View
            android:layout_width="match_parent"
            android:layout_height="1dp"
            android:background="@color/colorDivider"
            android:layout_marginHorizontal="16dp" />

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            android:gravity="center_vertical"
            android:padding="16dp">

            <com.github.mikephil.charting.charts.PieChart
                android:id="@+id/chartDonutEntregas"
                android:layout_width="0dp"
                android:layout_height="160dp"
                android:layout_weight="1" />

            <LinearLayout
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:orientation="vertical"
                android:layout_marginStart="12dp">

                <LinearLayout
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:gravity="center_vertical"
                    android:layout_marginBottom="8dp">
                    <View
                        android:layout_width="10dp"
                        android:layout_height="10dp"
                        android:background="@drawable/bg_circle_success"
                        android:layout_marginEnd="8dp" />
                    <TextView
                        android:id="@+id/tvLeyendaCalificadas"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Calificadas"
                        android:textColor="@color/colorOnSurfaceVariant"
                        android:textSize="11sp" />
                </LinearLayout>

                <LinearLayout
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:gravity="center_vertical"
                    android:layout_marginBottom="8dp">
                    <View
                        android:layout_width="10dp"
                        android:layout_height="10dp"
                        android:background="@drawable/bg_chip_docente"
                        android:layout_marginEnd="8dp" />
                    <TextView
                        android:id="@+id/tvLeyendaEntregadas"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Entregadas"
                        android:textColor="@color/colorOnSurfaceVariant"
                        android:textSize="11sp" />
                </LinearLayout>

                <LinearLayout
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:gravity="center_vertical">
                    <View
                        android:layout_width="10dp"
                        android:layout_height="10dp"
                        android:background="@drawable/bg_chip_pendiente"
                        android:layout_marginEnd="8dp" />
                    <TextView
                        android:id="@+id/tvLeyendaPendientes"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Pendientes"
                        android:textColor="@color/colorOnSurfaceVariant"
                        android:textSize="11sp" />
                </LinearLayout>

            </LinearLayout>

        </LinearLayout>

    </LinearLayout>

</com.google.android.material.card.MaterialCardView>
```

- [ ] **Step 4: Agregar `cardBarrasGrupos` después de `cardDonutEntregas`**

```xml
<!-- ── CARD BARRAS: ALUMNOS POR GRUPO ────────────── -->
<com.google.android.material.card.MaterialCardView
    android:id="@+id/cardBarrasGrupos"
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
        android:orientation="vertical">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            android:gravity="center_vertical"
            android:paddingHorizontal="16dp"
            android:paddingTop="16dp"
            android:paddingBottom="10dp">

            <ImageView
                android:layout_width="16dp"
                android:layout_height="16dp"
                android:layout_marginEnd="8dp"
                android:src="@drawable/ic_group"
                android:contentDescription="@null"
                app:tint="@color/colorPrimary" />

            <TextView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="Alumnos por grupo"
                android:textColor="@color/colorOnSurface"
                android:textSize="13sp"
                android:textStyle="bold" />

        </LinearLayout>

        <View
            android:layout_width="match_parent"
            android:layout_height="1dp"
            android:background="@color/colorDivider"
            android:layout_marginHorizontal="16dp" />

        <com.github.mikephil.charting.charts.BarChart
            android:id="@+id/chartBarrasGrupos"
            android:layout_width="match_parent"
            android:layout_height="180dp"
            android:layout_marginHorizontal="8dp"
            android:layout_marginVertical="8dp" />

    </LinearLayout>

</com.google.android.material.card.MaterialCardView>
```

- [ ] **Step 5: Agregar `cardProgresoTareas` después de `cardBarrasGrupos`**

```xml
<!-- ── CARD PROGRESO: ENTREGAS POR TAREA ─────────── -->
<com.google.android.material.card.MaterialCardView
    android:id="@+id/cardProgresoTareas"
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
        android:orientation="vertical">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            android:gravity="center_vertical"
            android:paddingHorizontal="16dp"
            android:paddingTop="16dp"
            android:paddingBottom="10dp">

            <ImageView
                android:layout_width="16dp"
                android:layout_height="16dp"
                android:layout_marginEnd="8dp"
                android:src="@drawable/ic_assignment"
                android:contentDescription="@null"
                app:tint="@color/colorPrimary" />

            <TextView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="Progreso de entregas"
                android:textColor="@color/colorOnSurface"
                android:textSize="13sp"
                android:textStyle="bold" />

        </LinearLayout>

        <View
            android:layout_width="match_parent"
            android:layout_height="1dp"
            android:background="@color/colorDivider"
            android:layout_marginHorizontal="16dp" />

        <!-- Fila 1 -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:paddingHorizontal="16dp"
            android:paddingTop="14dp"
            android:paddingBottom="10dp">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:gravity="center_vertical"
                android:layout_marginBottom="6dp">

                <TextView
                    android:id="@+id/tvTituloProgreso1"
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:text="Práctica 1: Layouts"
                    android:textColor="@color/colorOnSurface"
                    android:textSize="12sp"
                    android:maxLines="1"
                    android:ellipsize="end" />

                <TextView
                    android:id="@+id/tvFraccionProgreso1"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="12 / 18"
                    android:textColor="@color/colorOnSurfaceVariant"
                    android:textSize="11sp"
                    android:layout_marginStart="8dp" />

            </LinearLayout>

            <ProgressBar
                android:id="@+id/pbProgreso1"
                style="@style/Widget.AppCompat.ProgressBar.Horizontal"
                android:layout_width="match_parent"
                android:layout_height="8dp"
                android:max="18"
                android:progress="12" />

        </LinearLayout>

        <View
            android:layout_width="match_parent"
            android:layout_height="1dp"
            android:background="@color/colorDivider"
            android:layout_marginHorizontal="16dp" />

        <!-- Fila 2 -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:paddingHorizontal="16dp"
            android:paddingTop="14dp"
            android:paddingBottom="10dp">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:gravity="center_vertical"
                android:layout_marginBottom="6dp">

                <TextView
                    android:id="@+id/tvTituloProgreso2"
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:text="Proyecto Final"
                    android:textColor="@color/colorOnSurface"
                    android:textSize="12sp"
                    android:maxLines="1"
                    android:ellipsize="end" />

                <TextView
                    android:id="@+id/tvFraccionProgreso2"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="5 / 18"
                    android:textColor="@color/colorOnSurfaceVariant"
                    android:textSize="11sp"
                    android:layout_marginStart="8dp" />

            </LinearLayout>

            <ProgressBar
                android:id="@+id/pbProgreso2"
                style="@style/Widget.AppCompat.ProgressBar.Horizontal"
                android:layout_width="match_parent"
                android:layout_height="8dp"
                android:max="18"
                android:progress="5" />

        </LinearLayout>

        <View
            android:layout_width="match_parent"
            android:layout_height="1dp"
            android:background="@color/colorDivider"
            android:layout_marginHorizontal="16dp" />

        <!-- Fila 3 -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:paddingHorizontal="16dp"
            android:paddingTop="14dp"
            android:paddingBottom="14dp">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:gravity="center_vertical"
                android:layout_marginBottom="6dp">

                <TextView
                    android:id="@+id/tvTituloProgreso3"
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:text="Trabajo de investigación"
                    android:textColor="@color/colorOnSurface"
                    android:textSize="12sp"
                    android:maxLines="1"
                    android:ellipsize="end" />

                <TextView
                    android:id="@+id/tvFraccionProgreso3"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="8 / 18"
                    android:textColor="@color/colorOnSurfaceVariant"
                    android:textSize="11sp"
                    android:layout_marginStart="8dp" />

            </LinearLayout>

            <ProgressBar
                android:id="@+id/pbProgreso3"
                style="@style/Widget.AppCompat.ProgressBar.Horizontal"
                android:layout_width="match_parent"
                android:layout_height="8dp"
                android:max="18"
                android:progress="8" />

        </LinearLayout>

    </LinearLayout>

</com.google.android.material.card.MaterialCardView>
```

- [ ] **Step 6: Verificar que el orden final del contenido sea:**
  1. Header
  2. `cardBienvenidaDocente`
  3. `layoutKpisDocente` (solo Tareas activas + Por revisar)
  4. `cardDonutEntregas`
  5. `cardBarrasGrupos`
  6. `cardProgresoTareas`
  7. `cardTareasRecientesDashboard`

- [ ] **Step 7: Commit**

```bash
git add app/src/main/res/layout/fragment_docente_dashboard.xml
git commit -m "feat: rediseñar layout dashboard docente con gráficas"
```

---

## Task 3: Implementar lógica de gráficas en DocenteDashboardFragment

**Files:**
- Modify: `app/src/main/java/com/conectatec/ui/docente/dashboard/DocenteDashboardFragment.java`

- [ ] **Step 1: Actualizar imports**

Reemplaza la sección de imports del archivo por:

```java
package com.conectatec.ui.docente.dashboard;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.conectatec.R;
import com.conectatec.data.model.DashboardResumen;
import com.conectatec.databinding.FragmentDocenteDashboardBinding;
import com.conectatec.ui.common.EntradaAnimator;
import com.conectatec.ui.common.UiState;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
```

- [ ] **Step 2: Actualizar `observeViewModel()` — quitar bindings de IDs eliminados**

Los IDs `tvTotalGruposDashboardDocente` y `tvTotalAlumnos` ya no existen en el layout. Reemplaza el método `observeViewModel()` por:

```java
private void observeViewModel() {
    viewModel.getState().observe(getViewLifecycleOwner(), state -> {
        binding.progressBarDashboard.setVisibility(
                state instanceof UiState.Loading ? View.VISIBLE : View.GONE);
        if (state instanceof UiState.Success) {
            EntradaAnimator.animar(
                binding.cardBienvenidaDocente,
                binding.layoutKpisDocente,
                binding.cardDonutEntregas,
                binding.cardBarrasGrupos,
                binding.cardProgresoTareas,
                binding.cardTareasRecientesDashboard
            );
        } else if (state instanceof UiState.Error) {
            Snackbar.make(binding.getRoot(),
                    ((UiState.Error<?>) state).mensaje, Snackbar.LENGTH_LONG).show();
            EntradaAnimator.animar(
                binding.cardBienvenidaDocente,
                binding.layoutKpisDocente,
                binding.cardDonutEntregas,
                binding.cardBarrasGrupos,
                binding.cardProgresoTareas,
                binding.cardTareasRecientesDashboard
            );
        }
    });
}
```

- [ ] **Step 3: Actualizar `setupListeners()` — quitar listeners de IDs eliminados**

Los cards `cardGruposActivosDocente` y `cardAlumnosTotales` ya no existen. Reemplaza `setupListeners()` por:

```java
private void setupListeners() {
    binding.tvVerTodasTareasDashboard.setOnClickListener(v ->
            navigateToTab(v, R.id.docenteTareasFragment));
}
```

- [ ] **Step 4: Agregar llamada a `configurarGraficas()` en `onViewCreated()`**

Reemplaza `onViewCreated()` por:

```java
@Override
public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(this).get(DocenteDashboardViewModel.class);
    observeViewModel();
    viewModel.cargarDatos();
    setupListeners();
    configurarGraficas();
}
```

- [ ] **Step 5: Agregar el método `configurarGraficas()` y sus helpers**

Agrega estos métodos al final de la clase (antes del `onDestroyView()`):

```java
private void configurarGraficas() {
    configurarDonutEntregas(binding.chartDonutEntregas);
    configurarBarrasGrupos(binding.chartBarrasGrupos);
    configurarProgresoTareas();
}

private void configurarDonutEntregas(PieChart chart) {
    // Datos consistentes con TareasRepositoryImpl.DATASET (tareas EN_CURSO)
    int calificadas = 14;
    int entregadas  = 16;
    int pendientes  = 9;
    int total = calificadas + entregadas + pendientes;

    List<PieEntry> entries = new ArrayList<>();
    entries.add(new PieEntry(calificadas));
    entries.add(new PieEntry(entregadas));
    entries.add(new PieEntry(pendientes));

    PieDataSet dataSet = new PieDataSet(entries, "");
    dataSet.setColors(
        ContextCompat.getColor(requireContext(), R.color.colorSuccess),
        ContextCompat.getColor(requireContext(), R.color.colorPrimary),
        ContextCompat.getColor(requireContext(), R.color.colorWarning)
    );
    dataSet.setValueTextSize(0f);
    dataSet.setValueTextColor(Color.TRANSPARENT);
    dataSet.setSliceSpace(2f);

    PieData data = new PieData(dataSet);
    chart.setData(data);
    chart.setHoleRadius(58f);
    chart.setTransparentCircleRadius(61f);
    chart.setHoleColor(Color.TRANSPARENT);
    chart.setTransparentCircleColor(Color.TRANSPARENT);

    int pct = total > 0 ? ((calificadas + entregadas) * 100 / total) : 0;
    chart.setCenterText(pct + "%");
    chart.setCenterTextSize(18f);
    chart.setCenterTextColor(ContextCompat.getColor(requireContext(), R.color.colorOnSurface));
    chart.setCenterTextTypeface(android.graphics.Typeface.DEFAULT_BOLD);

    chart.getDescription().setEnabled(false);
    chart.setDrawBorders(false);
    chart.setTouchEnabled(false);
    chart.getLegend().setEnabled(false);
    chart.setDrawEntryLabels(false);
    chart.setBackgroundColor(Color.TRANSPARENT);
    chart.animateY(800, Easing.EaseInOutQuad);
    chart.invalidate();

    // Actualizar leyenda manual
    binding.tvLeyendaCalificadas.setText("Calificadas  " + calificadas);
    binding.tvLeyendaEntregadas.setText("Entregadas  " + entregadas);
    binding.tvLeyendaPendientes.setText("Pendientes  " + pendientes);
}

private void configurarBarrasGrupos(BarChart chart) {
    List<BarEntry> entries = new ArrayList<>();
    entries.add(new BarEntry(0f, 18f)); // PM 6A
    entries.add(new BarEntry(1f, 16f)); // BD 4B
    entries.add(new BarEntry(2f, 13f)); // Cálculo 2A

    BarDataSet dataSet = new BarDataSet(entries, "");
    dataSet.setColors(
        ContextCompat.getColor(requireContext(), R.color.colorChipDocente),
        ContextCompat.getColor(requireContext(), R.color.colorChipEstudiante),
        ContextCompat.getColor(requireContext(), R.color.colorChipAdmin)
    );
    dataSet.setValueTextSize(11f);
    dataSet.setValueTextColor(
        ContextCompat.getColor(requireContext(), R.color.colorOnSurface));

    BarData data = new BarData(dataSet);
    data.setBarWidth(0.5f);
    chart.setData(data);

    String[] grupos = {"PM 6A", "BD 4B", "Cálculo"};
    XAxis xAxis = chart.getXAxis();
    xAxis.setValueFormatter(new IndexAxisValueFormatter(grupos));
    xAxis.setGranularity(1f);
    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    xAxis.setTextColor(
        ContextCompat.getColor(requireContext(), R.color.colorOnSurfaceVariant));
    xAxis.setDrawGridLines(false);
    xAxis.setTextSize(10f);
    xAxis.setDrawAxisLine(false);

    chart.getAxisLeft().setTextColor(
        ContextCompat.getColor(requireContext(), R.color.colorOnSurfaceVariant));
    chart.getAxisLeft().setDrawGridLines(false);
    chart.getAxisLeft().setAxisMinimum(0f);
    chart.getAxisLeft().setAxisMaximum(22f);
    chart.getAxisLeft().setDrawAxisLine(false);
    chart.getAxisRight().setEnabled(false);

    chart.getDescription().setEnabled(false);
    chart.setDrawBorders(false);
    chart.setTouchEnabled(false);
    chart.getLegend().setEnabled(false);
    chart.setFitBars(true);
    chart.setBackgroundColor(Color.TRANSPARENT);
    chart.animateY(700, Easing.EaseInOutQuad);
    chart.invalidate();
}

private void configurarProgresoTareas() {
    // Datos: primeras 3 tareas EN_CURSO de TareasRepositoryImpl.DATASET
    // Tarea 1: Práctica 1: Layouts, 12/18 (67%) → colorPrimary
    // Tarea 3: Proyecto Final,       5/18 (28%) → colorWarning
    // Tarea 4: Trabajo investigación,8/18 (44%) → colorPrimary
    setProgreso(binding.pbProgreso1, 12, 18);
    setProgreso(binding.pbProgreso2,  5, 18);
    setProgreso(binding.pbProgreso3,  8, 18);
}

private void setProgreso(android.widget.ProgressBar pb, int entregadas, int total) {
    int colorRes;
    int pct = total > 0 ? (entregadas * 100 / total) : 0;
    if      (pct >= 70) colorRes = R.color.colorSuccess;
    else if (pct >= 40) colorRes = R.color.colorPrimary;
    else                colorRes = R.color.colorWarning;
    pb.setMax(total);
    pb.setProgress(entregadas);
    pb.setProgressTintList(ColorStateList.valueOf(
        ContextCompat.getColor(requireContext(), colorRes)));
    pb.setProgressBackgroundTintList(ColorStateList.valueOf(
        ContextCompat.getColor(requireContext(), R.color.colorSurfaceVariant)));
}
```

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/conectatec/ui/docente/dashboard/DocenteDashboardFragment.java
git commit -m "feat: agregar gráficas de donut, barras y progreso al dashboard docente"
```

---

## Task 4: Actualizar layout del Dashboard Admin

**Files:**
- Modify: `app/src/main/res/layout/fragment_admin_dashboard.xml`

- [ ] **Step 1: Eliminar la sección "ACTIVIDAD RECIENTE"**

En `fragment_admin_dashboard.xml`, localiza el comentario `<!-- ══ ACTIVIDAD RECIENTE ══` y elimina **todo** desde ese comentario hasta el final del archivo (antes del cierre `</LinearLayout>` del LinearLayout hijo del ScrollView y el cierre del propio `</ScrollView>`).

El archivo debe terminar así después de los KPIs:

```xml
            </LinearLayout>
            <!-- fin del stats grid LinearLayout -->

        </LinearLayout>
        <!-- fin del LinearLayout hijo del ScrollView -->

    </ScrollView>

</ScrollView>  <!-- o el elemento raíz que corresponda -->
```

> Nota: Mantén todos los cierres correctos. El `</LinearLayout>` del `<!-- ══ STATS GRID -->` y el `</LinearLayout>` del LinearLayout principal hijo del ScrollView deben quedar intactos.

- [ ] **Step 2: Agregar `cardDonutUsuarios` después del stats grid**

Dentro del LinearLayout principal (después del `<!-- ══ STATS GRID -->` y sus cierres, dentro del padding container), agrega:

```xml
<!-- ── CARD DONUT: USUARIOS POR ROL ──────────────── -->
<com.google.android.material.card.MaterialCardView
    android:id="@+id/cardDonutUsuarios"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginHorizontal="16dp"
    android:layout_marginTop="12dp"
    android:layout_marginBottom="12dp"
    app:cardBackgroundColor="@color/colorSurface"
    app:cardCornerRadius="12dp"
    app:cardElevation="0dp"
    app:strokeWidth="0dp">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            android:gravity="center_vertical"
            android:paddingHorizontal="16dp"
            android:paddingTop="16dp"
            android:paddingBottom="10dp">

            <ImageView
                android:layout_width="16dp"
                android:layout_height="16dp"
                android:layout_marginEnd="8dp"
                android:src="@drawable/ic_people"
                android:contentDescription="@null"
                app:tint="@color/colorPrimary" />

            <TextView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="Usuarios por rol"
                android:textColor="@color/colorOnSurface"
                android:textSize="13sp"
                android:textStyle="bold" />

        </LinearLayout>

        <View
            android:layout_width="match_parent"
            android:layout_height="1dp"
            android:background="@color/colorDivider"
            android:layout_marginHorizontal="16dp" />

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            android:gravity="center_vertical"
            android:padding="16dp">

            <com.github.mikephil.charting.charts.PieChart
                android:id="@+id/chartDonutUsuarios"
                android:layout_width="0dp"
                android:layout_height="160dp"
                android:layout_weight="1" />

            <LinearLayout
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:orientation="vertical"
                android:layout_marginStart="12dp">

                <LinearLayout
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:gravity="center_vertical"
                    android:layout_marginBottom="8dp">
                    <View
                        android:layout_width="10dp"
                        android:layout_height="10dp"
                        android:background="@drawable/bg_chip_admin"
                        android:layout_marginEnd="8dp" />
                    <TextView
                        android:id="@+id/tvLeyendaAdmin"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Admin  1"
                        android:textColor="@color/colorOnSurfaceVariant"
                        android:textSize="11sp" />
                </LinearLayout>

                <LinearLayout
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:gravity="center_vertical"
                    android:layout_marginBottom="8dp">
                    <View
                        android:layout_width="10dp"
                        android:layout_height="10dp"
                        android:background="@drawable/bg_chip_docente"
                        android:layout_marginEnd="8dp" />
                    <TextView
                        android:id="@+id/tvLeyendaDocente"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Docente  2"
                        android:textColor="@color/colorOnSurfaceVariant"
                        android:textSize="11sp" />
                </LinearLayout>

                <LinearLayout
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:gravity="center_vertical"
                    android:layout_marginBottom="8dp">
                    <View
                        android:layout_width="10dp"
                        android:layout_height="10dp"
                        android:background="@drawable/bg_chip_estudiante"
                        android:layout_marginEnd="8dp" />
                    <TextView
                        android:id="@+id/tvLeyendaEstudiante"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Estudiante  2"
                        android:textColor="@color/colorOnSurfaceVariant"
                        android:textSize="11sp" />
                </LinearLayout>

                <LinearLayout
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:gravity="center_vertical">
                    <View
                        android:layout_width="10dp"
                        android:layout_height="10dp"
                        android:background="@drawable/bg_chip_pendiente"
                        android:layout_marginEnd="8dp" />
                    <TextView
                        android:id="@+id/tvLeyendaPendiente"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Pendiente  2"
                        android:textColor="@color/colorOnSurfaceVariant"
                        android:textSize="11sp" />
                </LinearLayout>

            </LinearLayout>

        </LinearLayout>

    </LinearLayout>

</com.google.android.material.card.MaterialCardView>
```

- [ ] **Step 3: Agregar `cardBarrasActividades` después de `cardDonutUsuarios`**

```xml
<!-- ── CARD BARRAS: ACTIVIDADES POR ESTADO ───────── -->
<com.google.android.material.card.MaterialCardView
    android:id="@+id/cardBarrasActividades"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginHorizontal="16dp"
    android:layout_marginBottom="100dp"
    app:cardBackgroundColor="@color/colorSurface"
    app:cardCornerRadius="12dp"
    app:cardElevation="0dp"
    app:strokeWidth="0dp">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            android:gravity="center_vertical"
            android:paddingHorizontal="16dp"
            android:paddingTop="16dp"
            android:paddingBottom="10dp">

            <ImageView
                android:layout_width="16dp"
                android:layout_height="16dp"
                android:layout_marginEnd="8dp"
                android:src="@drawable/ic_assignment"
                android:contentDescription="@null"
                app:tint="@color/colorPrimary" />

            <TextView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="Actividades por estado"
                android:textColor="@color/colorOnSurface"
                android:textSize="13sp"
                android:textStyle="bold" />

        </LinearLayout>

        <View
            android:layout_width="match_parent"
            android:layout_height="1dp"
            android:background="@color/colorDivider"
            android:layout_marginHorizontal="16dp" />

        <com.github.mikephil.charting.charts.BarChart
            android:id="@+id/chartBarrasActividades"
            android:layout_width="match_parent"
            android:layout_height="180dp"
            android:layout_marginHorizontal="8dp"
            android:layout_marginVertical="8dp" />

    </LinearLayout>

</com.google.android.material.card.MaterialCardView>
```

- [ ] **Step 4: Eliminar `paddingBottom="100dp"` del LinearLayout raíz del ScrollView**

El padding de 100dp que antes estaba en el LinearLayout raíz del ScrollView ya está en el `cardBarrasActividades` con `layout_marginBottom="100dp"`. Verifica que el LinearLayout padre no tenga `paddingBottom` duplicado — si lo tiene, elimínalo.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/res/layout/fragment_admin_dashboard.xml
git commit -m "feat: rediseñar layout dashboard admin con gráficas"
```

---

## Task 5: Implementar lógica de gráficas en AdminDashboardFragment

**Files:**
- Modify: `app/src/main/java/com/conectatec/ui/admin/dashboard/AdminDashboardFragment.java`

- [ ] **Step 1: Reemplazar el archivo completo con la nueva implementación**

```java
package com.conectatec.ui.admin.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.conectatec.R;
import com.conectatec.databinding.FragmentAdminDashboardBinding;
import com.conectatec.ui.common.EntradaAnimator;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AdminDashboardFragment extends Fragment {

    private FragmentAdminDashboardBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupListeners();
        configurarGraficas();
        EntradaAnimator.animar(
            binding.cardUsuarios,
            binding.cardPendientes,
            binding.cardGrupos,
            binding.cardTareas,
            binding.cardDonutUsuarios,
            binding.cardBarrasActividades
        );
    }

    private void setupListeners() {
        binding.cardUsuarios.setOnClickListener(v ->
                navigateToTab(v, R.id.adminUsuariosFragment));
        binding.cardPendientes.setOnClickListener(v ->
                navigateToTab(v, R.id.adminUsuariosFragment));
        binding.cardGrupos.setOnClickListener(v ->
                navigateToTab(v, R.id.adminGruposFragment));
        binding.cardTareas.setOnClickListener(v ->
                navigateToTab(v, R.id.adminActividadesFragment));
    }

    private void configurarGraficas() {
        configurarDonutUsuarios(binding.chartDonutUsuarios);
        configurarBarrasActividades(binding.chartBarrasActividades);
    }

    private void configurarDonutUsuarios(PieChart chart) {
        // Datos de UsuarioAdminAdapter: 1 ADMIN, 2 DOCENTE, 2 ESTUDIANTE, 2 PENDIENTE
        int admin      = 1;
        int docente    = 2;
        int estudiante = 2;
        int pendiente  = 2;
        int total = admin + docente + estudiante + pendiente;

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(admin));
        entries.add(new PieEntry(docente));
        entries.add(new PieEntry(estudiante));
        entries.add(new PieEntry(pendiente));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(
            ContextCompat.getColor(requireContext(), R.color.colorChipAdmin),
            ContextCompat.getColor(requireContext(), R.color.colorChipDocente),
            ContextCompat.getColor(requireContext(), R.color.colorChipEstudiante),
            ContextCompat.getColor(requireContext(), R.color.colorChipPendiente)
        );
        dataSet.setValueTextSize(0f);
        dataSet.setValueTextColor(Color.TRANSPARENT);
        dataSet.setSliceSpace(2f);

        PieData data = new PieData(dataSet);
        chart.setData(data);
        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);
        chart.setHoleColor(Color.TRANSPARENT);
        chart.setTransparentCircleColor(Color.TRANSPARENT);
        chart.setCenterText(String.valueOf(total));
        chart.setCenterTextSize(18f);
        chart.setCenterTextColor(
            ContextCompat.getColor(requireContext(), R.color.colorOnSurface));
        chart.setCenterTextTypeface(android.graphics.Typeface.DEFAULT_BOLD);
        chart.getDescription().setEnabled(false);
        chart.setDrawBorders(false);
        chart.setTouchEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setDrawEntryLabels(false);
        chart.setBackgroundColor(Color.TRANSPARENT);
        chart.animateY(800, Easing.EaseInOutQuad);
        chart.invalidate();

        // Leyenda manual
        binding.tvLeyendaAdmin.setText("Admin  " + admin);
        binding.tvLeyendaDocente.setText("Docente  " + docente);
        binding.tvLeyendaEstudiante.setText("Estudiante  " + estudiante);
        binding.tvLeyendaPendiente.setText("Pendiente  " + pendiente);
    }

    private void configurarBarrasActividades(BarChart chart) {
        // Datos de ActividadAdminAdapter: EN_CURSO=3, COMPLETADA=2, PENDIENTE=2, VENCIDA=1
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 3f)); // EN_CURSO
        entries.add(new BarEntry(1f, 2f)); // COMPLETADA
        entries.add(new BarEntry(2f, 2f)); // PENDIENTE
        entries.add(new BarEntry(3f, 1f)); // VENCIDA

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(
            ContextCompat.getColor(requireContext(), R.color.colorPrimary),
            ContextCompat.getColor(requireContext(), R.color.colorSuccess),
            ContextCompat.getColor(requireContext(), R.color.colorWarning),
            ContextCompat.getColor(requireContext(), R.color.colorError)
        );
        dataSet.setValueTextSize(11f);
        dataSet.setValueTextColor(
            ContextCompat.getColor(requireContext(), R.color.colorOnSurface));

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.5f);
        chart.setData(data);

        String[] estados = {"En Curso", "Completada", "Pendiente", "Vencida"};
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(estados));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(
            ContextCompat.getColor(requireContext(), R.color.colorOnSurfaceVariant));
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(10f);
        xAxis.setDrawAxisLine(false);

        chart.getAxisLeft().setTextColor(
            ContextCompat.getColor(requireContext(), R.color.colorOnSurfaceVariant));
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisLeft().setAxisMinimum(0f);
        chart.getAxisLeft().setAxisMaximum(5f);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisRight().setEnabled(false);

        chart.getDescription().setEnabled(false);
        chart.setDrawBorders(false);
        chart.setTouchEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setFitBars(true);
        chart.setBackgroundColor(Color.TRANSPARENT);
        chart.animateY(700, Easing.EaseInOutQuad);
        chart.invalidate();
    }

    private void navigateToTab(View v, int destId) {
        androidx.navigation.NavController nav = Navigation.findNavController(v);
        NavOptions opts = new NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setRestoreState(true)
                .setPopUpTo(nav.getGraph().getStartDestinationId(), false, true)
                .build();
        nav.navigate(destId, null, opts);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
```

- [ ] **Step 2: Verificar que el proyecto compila sin errores**

```bash
./gradlew :app:compileDebugJavaSources 2>&1 | grep -E "error:|warning:" | head -20
```

Resultado esperado: sin líneas con `error:`.

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/conectatec/ui/admin/dashboard/AdminDashboardFragment.java
git commit -m "feat: agregar gráficas de donut y barras al dashboard admin"
```

---

## Verificación final

- [ ] **Instalar en dispositivo/emulador y navegar a Dashboard Docente**
  - Se ven 3 gráficas: donut con el porcentaje de completitud en el centro, barras verticales por grupo, barras de progreso por tarea
  - Las animaciones se ejecutan al entrar al tab
  - Los colores son consistentes con el resto de la app

- [ ] **Navegar a Dashboard Admin**
  - Se ven 2 gráficas: donut con total de usuarios en el centro, barras verticales por estado
  - La sección "Actividad Reciente" ya no aparece
  - Las 4 KPI cards siguen siendo clickeables y navegan correctamente

- [ ] **Verificar que los demás tabs no están afectados** (Grupos, Tareas, Chat, Perfil del Docente; Grupos, Actividades, Usuarios, Perfil del Admin)
