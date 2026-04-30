# Dashboard Activity Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implementar `DashboardActivity` diferenciada por rol (ADMINISTRADOR / DOCENTE) con KPI cards animadas, gráfica MPAndroidChart y lista de actividad reciente con datos mock.

**Architecture:** Activity independiente que lee el JWT de SharedPreferences, decodifica el claim `rol` en Base64 nativo Android, y delega la UI a `DashboardViewModel` (LiveData con datos mock). Los layouts son `activity_dashboard.xml` (raíz), `item_kpi_card.xml` e `item_actividad_reciente.xml`.

**Tech Stack:** Java 11, ViewBinding, Hilt, ViewModel + LiveData, MPAndroidChart v3.1.0 (JitPack), Glide, ObjectAnimator, android.util.Base64 + org.json.JSONObject.

---

### Task 1: Agregar colores del dashboard a colors.xml

**Files:**
- Modify: `app/src/main/res/values/colors.xml`

- [ ] **Step 1: Agregar bloque de colores del dashboard**

Abrir `app/src/main/res/values/colors.xml` y agregar antes del tag `</resources>`:

```xml
    <!-- Dashboard Activity (modo claro) -->
    <color name="colorDashBg">#F4F6FA</color>
    <color name="colorDashCard">#FFFFFF</color>
    <color name="colorDashPrimary">#6C63FF</color>
    <color name="colorDashText">#1A1A2E</color>
    <color name="colorDashTextSecondary">#6B7280</color>
    <color name="colorDashTextTertiary">#9CA3AF</color>
    <color name="colorDashGreen">#10B981</color>
    <color name="colorDashAmber">#F59E0B</color>
    <color name="colorDashRed">#EF4444</color>
    <color name="colorDashBlue">#3B82F6</color>
    <color name="colorDashViolet">#8B5CF6</color>
    <color name="colorDashDivider">#E5E7EB</color>
```

- [ ] **Step 2: Verificar que no hay literales hex en archivos XML existentes rotos**

```bash
grep -r "#[0-9A-Fa-f]\{6\}" app/src/main/res/layout/ | grep -v "\.java" | wc -l
```
Expected: 0 (o número igual al que había antes — no debe haber aumentado).

- [ ] **Step 3: Commit**

```bash
git add app/src/main/res/values/colors.xml
git commit -m "feat(dashboard): agregar paleta de colores modo claro"
```

---

### Task 2: Agregar dependencia MPAndroidChart

**Files:**
- Modify: `settings.gradle.kts`
- Modify: `gradle/libs.versions.toml`
- Modify: `app/build.gradle.kts`

- [ ] **Step 1: Agregar JitPack al bloque `repositories` en settings.gradle.kts**

Cambiar el bloque `dependencyResolutionManagement.repositories`:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

- [ ] **Step 2: Agregar versión y librería en libs.versions.toml**

En `[versions]` agregar:
```toml
mpandroidchart = "v3.1.0"
arch-core-testing = "2.2.0"
```

En `[libraries]` agregar:
```toml
mpandroidchart       = { group = "com.github.PhilJay", name = "MPAndroidChart",  version.ref = "mpandroidchart" }
arch-core-testing    = { group = "androidx.arch.core",  name = "core-testing",   version.ref = "arch-core-testing" }
```

- [ ] **Step 3: Agregar implementación en app/build.gradle.kts**

En el bloque `dependencies`, agregar:
```kotlin
    // MPAndroidChart
    implementation(libs.mpandroidchart)

    // Testing — LiveData
    testImplementation(libs.arch.core.testing)
```

- [ ] **Step 4: Verificar que Gradle sincroniza correctamente**

```bash
./gradlew assembleDebug --dry-run 2>&1 | tail -5
```
Expected: terminado sin `BUILD FAILED`.

- [ ] **Step 5: Commit**

```bash
git add settings.gradle.kts gradle/libs.versions.toml app/build.gradle.kts
git commit -m "feat(dashboard): agregar MPAndroidChart v3.1.0 desde JitPack"
```

---

### Task 3: Crear íconos vectoriales para las KPI cards

**Files:**
- Create: `app/src/main/res/drawable/ic_people.xml`
- Create: `app/src/main/res/drawable/ic_group.xml`
- Create: `app/src/main/res/drawable/ic_assignment.xml`
- Create: `app/src/main/res/drawable/ic_pending.xml`
- Create: `app/src/main/res/drawable/ic_class.xml`
- Create: `app/src/main/res/drawable/ic_inbox.xml`
- Create: `app/src/main/res/drawable/ic_grading.xml`
- Create: `app/src/main/res/drawable/ic_bar_chart.xml`
- Create: `app/src/main/res/drawable/ic_notifications.xml`

- [ ] **Step 1: Crear ic_people.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="@android:color/black"
        android:pathData="M16,11c1.66,0 2.99,-1.34 2.99,-3S17.66,5 16,5c-1.66,0 -3,1.34 -3,3s1.34,3 3,3zm-8,0c1.66,0 2.99,-1.34 2.99,-3S9.66,5 8,5C6.34,5 5,6.34 5,8s1.34,3 3,3zm0,2c-2.33,0 -7,1.17 -7,3.5L1,19h14v-2.5c0,-2.33 -4.67,-3.5 -7,-3.5zm8,0c-0.29,0 -0.62,0.02 -0.97,0.05 1.16,0.84 1.97,1.97 1.97,3.45L23,19h-6v-2.5c0,-1.48 -0.81,-2.61 -1.97,-3.45z"/>
</vector>
```

- [ ] **Step 2: Crear ic_group.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="@android:color/black"
        android:pathData="M12,13c2.67,0 8,1.34 8,4v2L4,19v-2c0,-2.66 5.33,-4 8,-4zm0,-2c-2.21,0 -4,-1.79 -4,-4s1.79,-4 4,-4 4,1.79 4,4-1.79,4 -4,4zm6,2.18c1.38,0.62 2,1.61 2,2.82v2h-2v-2c0,-1.18 -0.59,-2.2 -2,-2.82zM6,15.18C4.6,15.8 4,16.82 4,18v2L2,20v-2c0,-1.21 0.62,-2.2 2,-2.82z"/>
</vector>
```

- [ ] **Step 3: Crear ic_assignment.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="@android:color/black"
        android:pathData="M19,3h-4.18C14.4,1.84 13.3,1 12,1c-1.3,0 -2.4,0.84 -2.82,2L5,3c-1.1,0 -2,0.9 -2,2v14c0,1.1 0.9,2 2,2h14c1.1,0 2,-0.9 2,-2L21,5c0,-1.1 -0.9,-2 -2,-2zm-7,0c0.55,0 1,0.45 1,1s-0.45,1 -1,1 -1,-0.45 -1,-1 0.45,-1 1,-1zm2,14L7,17v-2h7v2zm3,-4L7,13v-2h10v2zm0,-4L7,9L7,7h10v2z"/>
</vector>
```

- [ ] **Step 4: Crear ic_pending.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="@android:color/black"
        android:pathData="M12,2C6.48,2 2,6.48 2,12s4.48,10 10,10 10,-4.48 10,-10S17.52,2 12,2zm0,18c-4.42,0 -8,-3.58 -8,-8s3.58,-8 8,-8 8,3.58 8,8-3.58,8 -8,8zm0.5,-13L11,7v6l5.25,3.15 0.75,-1.23 -4.5,-2.67z"/>
</vector>
```

- [ ] **Step 5: Crear ic_class.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="@android:color/black"
        android:pathData="M18,2L6,2c-1.1,0 -2,0.9 -2,2v16c0,1.1 0.9,2 2,2h12c1.1,0 2,-0.9 2,-2L20,4c0,-1.1 -0.9,-2 -2,-2zM6,4h5v8l-2.5,-1.5L6,12L6,4z"/>
</vector>
```

- [ ] **Step 6: Crear ic_inbox.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="@android:color/black"
        android:pathData="M19,3L5,3c-1.1,0 -2,0.9 -2,2v14c0,1.1 0.9,2 2,2h14c1.1,0 2,-0.9 2,-2L21,5c0,-1.1 -0.9,-2 -2,-2zm0,12h-4c0,1.66 -1.35,3 -3,3s-3,-1.34 -3,-3L5,15L5,5h14v10z"/>
</vector>
```

- [ ] **Step 7: Crear ic_grading.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="@android:color/black"
        android:pathData="M19,3L5,3c-1.1,0 -2,0.9 -2,2v14c0,1.1 0.9,2 2,2h14c1.1,0 2,-0.9 2,-2L21,5c0,-1.1 -0.9,-2 -2,-2zm-9.5,14L5,12.5l1.41,-1.41 3.09,3.08 7.09,-7.09L18,8.5l-8.5,8.5zm5.5,-9h-5L10,6h5v2z"/>
</vector>
```

- [ ] **Step 8: Crear ic_bar_chart.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="@android:color/black"
        android:pathData="M5,9.2h3L8,19L5,19zM10.6,5h2.8v14h-2.8zM16.2,13L19,13v6h-2.8z"/>
</vector>
```

- [ ] **Step 9: Crear ic_notifications.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="@android:color/black"
        android:pathData="M12,22c1.1,0 2,-0.9 2,-2h-4c0,1.1 0.9,2 2,2zm6,-6v-5c0,-3.07 -1.64,-5.64 -4.5,-6.32L13.5,4c0,-0.83 -0.67,-1.5 -1.5,-1.5s-1.5,0.67 -1.5,1.5v0.68C7.63,5.36 6,7.92 6,11v5l-2,2v1h16v-1l-2,-2z"/>
</vector>
```

- [ ] **Step 10: Build para verificar que los drawables parsean**

```bash
./gradlew assembleDebug 2>&1 | grep -E "error|BUILD" | tail -5
```
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 11: Commit**

```bash
git add app/src/main/res/drawable/ic_people.xml \
        app/src/main/res/drawable/ic_group.xml \
        app/src/main/res/drawable/ic_assignment.xml \
        app/src/main/res/drawable/ic_pending.xml \
        app/src/main/res/drawable/ic_class.xml \
        app/src/main/res/drawable/ic_inbox.xml \
        app/src/main/res/drawable/ic_grading.xml \
        app/src/main/res/drawable/ic_bar_chart.xml \
        app/src/main/res/drawable/ic_notifications.xml
git commit -m "feat(dashboard): agregar íconos vectoriales para KPI cards"
```

---

### Task 4: Crear modelos de datos

**Files:**
- Create: `app/src/main/java/com/conectatec/model/ActividadItem.java`
- Create: `app/src/main/java/com/conectatec/model/DashboardStats.java`
- Create: `app/src/main/java/com/conectatec/model/DocenteStats.java`
- Test: `app/src/test/java/com/conectatec/model/ActividadItemTest.java`

- [ ] **Step 1: Escribir test fallido para ActividadItem**

Crear `app/src/test/java/com/conectatec/model/ActividadItemTest.java`:

```java
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
```

- [ ] **Step 2: Correr test para verificar que falla**

```bash
./gradlew test --tests "com.conectatec.model.ActividadItemTest" 2>&1 | tail -10
```
Expected: `FAILED` (clase no existe aún).

- [ ] **Step 3: Crear ActividadItem.java**

Crear `app/src/main/java/com/conectatec/model/ActividadItem.java`:

```java
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
```

- [ ] **Step 4: Crear DashboardStats.java**

Crear `app/src/main/java/com/conectatec/model/DashboardStats.java`:

```java
package com.conectatec.model;

public class DashboardStats {
    public int usuariosTotales;
    public int docentesActivos;
    public int gruposActivos;
    public int tareasPublicadas;
    public int entregasPendientes;
    public int mensajesHoy;
    public int[] usuariosPorRol;
    public int[] entregasPorDia;
}
```

- [ ] **Step 5: Crear DocenteStats.java**

Crear `app/src/main/java/com/conectatec/model/DocenteStats.java`:

```java
package com.conectatec.model;

public class DocenteStats {
    public int misGrupos;
    public int tareasActivas;
    public int entregasHoy;
    public int pendientesCalificar;
    public int alumnosTotales;
    public float promedioGeneral;
    public int[] entregasPorDia;
}
```

- [ ] **Step 6: Correr test para verificar que pasa**

```bash
./gradlew test --tests "com.conectatec.model.ActividadItemTest" 2>&1 | tail -5
```
Expected: `BUILD SUCCESSFUL`, `1 test completed`.

- [ ] **Step 7: Commit**

```bash
git add app/src/main/java/com/conectatec/model/ \
        app/src/test/java/com/conectatec/model/
git commit -m "feat(dashboard): agregar modelos ActividadItem, DashboardStats, DocenteStats"
```

---

### Task 5: Crear DashboardViewModel con tests

**Files:**
- Create: `app/src/main/java/com/conectatec/ui/dashboard/DashboardViewModel.java`
- Test: `app/src/test/java/com/conectatec/ui/dashboard/DashboardViewModelTest.java`

- [ ] **Step 1: Escribir test fallido para DashboardViewModel**

Crear `app/src/test/java/com/conectatec/ui/dashboard/DashboardViewModelTest.java`:

```java
package com.conectatec.ui.dashboard;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.conectatec.model.ActividadItem;
import com.conectatec.model.DashboardStats;
import com.conectatec.model.DocenteStats;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DashboardViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private DashboardViewModel viewModel;

    @Before
    public void setup() {
        viewModel = new DashboardViewModel();
    }

    @Test
    public void cargarDatosAdmin_populatesAdminStats() {
        viewModel.cargarDatosAdmin("");
        DashboardStats stats = viewModel.adminStats.getValue();
        assertNotNull(stats);
        assertTrue(stats.usuariosTotales > 0);
        assertNotNull(stats.usuariosPorRol);
        assertEquals(3, stats.usuariosPorRol.length);
        assertNotNull(stats.entregasPorDia);
        assertEquals(7, stats.entregasPorDia.length);
    }

    @Test
    public void cargarDatosAdmin_populatesActividadReciente() {
        viewModel.cargarDatosAdmin("");
        List<ActividadItem> actividad = viewModel.actividadReciente.getValue();
        assertNotNull(actividad);
        assertFalse(actividad.isEmpty());
        for (ActividadItem item : actividad) {
            assertNotNull(item.titulo);
            assertNotNull(item.descripcion);
            assertNotNull(item.timestamp);
        }
    }

    @Test
    public void cargarDatosDocente_populatesDocenteStats() {
        viewModel.cargarDatosDocente("");
        DocenteStats stats = viewModel.docenteStats.getValue();
        assertNotNull(stats);
        assertTrue(stats.misGrupos > 0);
        assertTrue(stats.promedioGeneral > 0f);
        assertNotNull(stats.entregasPorDia);
        assertEquals(7, stats.entregasPorDia.length);
    }

    @Test
    public void cargarDatosDocente_populatesActividadReciente() {
        viewModel.cargarDatosDocente("");
        List<ActividadItem> actividad = viewModel.actividadReciente.getValue();
        assertNotNull(actividad);
        assertFalse(actividad.isEmpty());
    }
}
```

- [ ] **Step 2: Correr test para verificar que falla**

```bash
./gradlew test --tests "com.conectatec.ui.dashboard.DashboardViewModelTest" 2>&1 | tail -5
```
Expected: `FAILED` (clase no existe aún).

- [ ] **Step 3: Crear DashboardViewModel.java**

Crear `app/src/main/java/com/conectatec/ui/dashboard/DashboardViewModel.java`:

```java
package com.conectatec.ui.dashboard;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.conectatec.model.ActividadItem;
import com.conectatec.model.DashboardStats;
import com.conectatec.model.DocenteStats;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends ViewModel {

    public final MutableLiveData<DashboardStats> adminStats = new MutableLiveData<>();
    public final MutableLiveData<DocenteStats> docenteStats = new MutableLiveData<>();
    public final MutableLiveData<List<ActividadItem>> actividadReciente = new MutableLiveData<>();

    public void cargarDatosAdmin(String token) {
        // TODO: llamar a DashboardService.obtenerEstadisticasAdmin()
        DashboardStats stats = new DashboardStats();
        stats.usuariosTotales   = 142;
        stats.docentesActivos   = 18;
        stats.gruposActivos     = 24;
        stats.tareasPublicadas  = 67;
        stats.entregasPendientes = 9;
        stats.mensajesHoy       = 31;
        stats.usuariosPorRol    = new int[]{18, 105, 19};
        stats.entregasPorDia    = new int[]{12, 8, 15, 6, 20, 10, 14};
        adminStats.setValue(stats);

        List<ActividadItem> actividad = new ArrayList<>();
        actividad.add(new ActividadItem("María González",
                "Se registró como estudiante", "09:14",
                ActividadItem.TIPO_USUARIO, 0xFF6C63FF));
        actividad.add(new ActividadItem("Prog. Móvil 6A",
                "Tarea: Proyecto Final publicada", "08:45",
                ActividadItem.TIPO_TAREA, 0xFF10B981));
        actividad.add(new ActividadItem("Carlos Ruiz",
                "Entrega enviada: Actividad 3", "08:30",
                ActividadItem.TIPO_ENTREGA, 0xFFF59E0B));
        actividad.add(new ActividadItem("Bases de Datos 4B",
                "Grupo actualizado por docente", "07:55",
                ActividadItem.TIPO_GRUPO, 0xFFEF4444));
        actividad.add(new ActividadItem("Soporte",
                "Nuevo mensaje de usuario pendiente", "07:20",
                ActividadItem.TIPO_MENSAJE, 0xFF3B82F6));
        actividad.add(new ActividadItem("Sistema",
                "3 usuarios pendientes de aprobación", "06:00",
                ActividadItem.TIPO_AVISO, 0xFF8B5CF6));
        actividadReciente.setValue(actividad);
    }

    public void cargarDatosDocente(String token) {
        // TODO: llamar a DashboardService.obtenerEstadisticasDocente()
        DocenteStats stats = new DocenteStats();
        stats.misGrupos           = 3;
        stats.tareasActivas       = 8;
        stats.entregasHoy         = 5;
        stats.pendientesCalificar = 12;
        stats.alumnosTotales      = 47;
        stats.promedioGeneral     = 8.2f;
        stats.entregasPorDia      = new int[]{4, 7, 2, 9, 5, 3, 8};
        docenteStats.setValue(stats);

        List<ActividadItem> actividad = new ArrayList<>();
        actividad.add(new ActividadItem("Ana López",
                "Entregó: Actividad 3 — Prog. Móvil 6A", "10:05",
                ActividadItem.TIPO_ENTREGA, 0xFF6C63FF));
        actividad.add(new ActividadItem("Prog. Móvil 6A",
                "Tarea Proyecto Final publicada", "09:30",
                ActividadItem.TIPO_TAREA, 0xFF10B981));
        actividad.add(new ActividadItem("Juan Pérez",
                "Entregó: Parcial 2 — Bases de Datos 4B", "08:50",
                ActividadItem.TIPO_ENTREGA, 0xFFF59E0B));
        actividad.add(new ActividadItem("Laura Torres",
                "Mensaje: ¿Puede ampliar la fecha?", "08:10",
                ActividadItem.TIPO_MENSAJE, 0xFFEF4444));
        actividad.add(new ActividadItem("Cálculo Integral 2A",
                "5 entregas nuevas pendientes", "07:45",
                ActividadItem.TIPO_AVISO, 0xFF3B82F6));
        actividadReciente.setValue(actividad);
    }
}
```

- [ ] **Step 4: Correr tests y verificar que pasan**

```bash
./gradlew test --tests "com.conectatec.ui.dashboard.DashboardViewModelTest" 2>&1 | tail -5
```
Expected: `BUILD SUCCESSFUL`, `4 tests completed`.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/conectatec/ui/dashboard/DashboardViewModel.java \
        app/src/test/java/com/conectatec/ui/dashboard/DashboardViewModelTest.java
git commit -m "feat(dashboard): agregar DashboardViewModel con datos mock y tests"
```

---

### Task 6: Crear layouts

**Files:**
- Create: `app/src/main/res/layout/item_kpi_card.xml`
- Create: `app/src/main/res/layout/item_actividad_reciente.xml`
- Create: `app/src/main/res/layout/activity_dashboard.xml`

- [ ] **Step 1: Crear item_kpi_card.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<com.google.android.material.card.MaterialCardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="160dp"
    android:layout_height="110dp"
    android:layout_marginEnd="12dp"
    app:cardBackgroundColor="@color/colorDashCard"
    app:cardCornerRadius="14dp"
    app:cardElevation="2dp"
    app:strokeWidth="0dp">

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <View
            android:id="@+id/viewStrip"
            android:layout_width="4dp"
            android:layout_height="match_parent"
            android:background="@color/colorDashPrimary"
            android:layout_alignParentStart="true" />

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_centerVertical="true"
            android:layout_toEndOf="@id/viewStrip"
            android:orientation="vertical"
            android:paddingStart="12dp"
            android:paddingEnd="12dp">

            <ImageView
                android:id="@+id/ivKpiIcon"
                android:layout_width="22dp"
                android:layout_height="22dp"
                android:layout_marginBottom="6dp"
                android:contentDescription="@null"
                android:src="@drawable/ic_people" />

            <TextView
                android:id="@+id/tvKpiValue"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="0"
                android:textColor="@color/colorDashText"
                android:textSize="26sp"
                android:textStyle="bold" />

            <TextView
                android:id="@+id/tvKpiLabel"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:ellipsize="end"
                android:maxLines="1"
                android:text="Label"
                android:textColor="@color/colorDashTextSecondary"
                android:textSize="11sp" />
        </LinearLayout>
    </RelativeLayout>
</com.google.android.material.card.MaterialCardView>
```

- [ ] **Step 2: Crear item_actividad_reciente.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:paddingHorizontal="16dp"
    android:paddingTop="12dp"
    android:paddingBottom="12dp">

    <TextView
        android:id="@+id/tvAvatarInicial"
        android:layout_width="40dp"
        android:layout_height="40dp"
        android:gravity="center"
        android:textColor="@color/white"
        android:textSize="16sp"
        android:textStyle="bold"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toBottomOf="parent" />

    <TextView
        android:id="@+id/tvTitulo"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="12dp"
        android:layout_marginEnd="8dp"
        android:textColor="@color/colorDashText"
        android:textSize="14sp"
        android:textStyle="bold"
        app:layout_constraintEnd_toStartOf="@id/tvTimestamp"
        app:layout_constraintStart_toEndOf="@id/tvAvatarInicial"
        app:layout_constraintTop_toTopOf="@id/tvAvatarInicial" />

    <TextView
        android:id="@+id/tvDescripcion"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="12dp"
        android:layout_marginEnd="8dp"
        android:layout_marginTop="2dp"
        android:ellipsize="end"
        android:maxLines="1"
        android:textColor="@color/colorDashTextSecondary"
        android:textSize="12sp"
        app:layout_constraintEnd_toStartOf="@id/tvTimestamp"
        app:layout_constraintStart_toEndOf="@id/tvAvatarInicial"
        app:layout_constraintTop_toBottomOf="@id/tvTitulo" />

    <TextView
        android:id="@+id/tvTimestamp"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textColor="@color/colorDashTextTertiary"
        android:textSize="11sp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <View
        android:layout_width="0dp"
        android:layout_height="1dp"
        android:alpha="0.5"
        android:background="@color/colorDashDivider"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

- [ ] **Step 3: Crear activity_dashboard.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.core.widget.NestedScrollView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/colorDashBg"
    android:fillViewport="true">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:paddingBottom="32dp">

        <!-- Header -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="@color/colorDashCard"
            android:gravity="center_vertical"
            android:orientation="horizontal"
            android:paddingTop="48dp"
            android:paddingBottom="20dp"
            android:paddingStart="20dp"
            android:paddingEnd="20dp">

            <de.hdodenhof.circleimageview.CircleImageView
                android:id="@+id/ivAvatar"
                android:layout_width="40dp"
                android:layout_height="40dp"
                android:src="@drawable/bg_avatar_placeholder" />

            <LinearLayout
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:layout_marginStart="12dp"
                android:orientation="vertical">

                <TextView
                    android:id="@+id/tvNombre"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Usuario"
                    android:textColor="@color/colorDashText"
                    android:textSize="16sp"
                    android:textStyle="bold" />

                <TextView
                    android:id="@+id/tvRol"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="ROL"
                    android:textColor="@color/colorDashPrimary"
                    android:textSize="11sp"
                    android:letterSpacing="0.05" />
            </LinearLayout>

            <ImageView
                android:id="@+id/ivNotificaciones"
                android:layout_width="24dp"
                android:layout_height="24dp"
                android:contentDescription="@null"
                android:src="@drawable/ic_notifications"
                app:tint="@color/colorDashTextSecondary" />
        </LinearLayout>

        <!-- KPIs -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:paddingTop="24dp"
            android:paddingStart="20dp"
            android:paddingEnd="20dp">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginBottom="16dp"
                android:text="Resumen de actividad"
                android:textColor="@color/colorDashText"
                android:textSize="16sp"
                android:textStyle="bold" />

            <HorizontalScrollView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:clipToPadding="false"
                android:scrollbars="none">

                <LinearLayout
                    android:id="@+id/linearKpis"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:paddingBottom="8dp" />
            </HorizontalScrollView>
        </LinearLayout>

        <!-- Gráfica Admin (BarChart) -->
        <com.google.android.material.card.MaterialCardView
            android:id="@+id/cardChartAdmin"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginStart="20dp"
            android:layout_marginEnd="20dp"
            android:layout_marginTop="24dp"
            android:visibility="gone"
            app:cardBackgroundColor="@color/colorDashCard"
            app:cardCornerRadius="12dp"
            app:cardElevation="4dp"
            app:strokeWidth="0dp">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical"
                android:padding="16dp">

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginBottom="8dp"
                    android:text="Usuarios por rol"
                    android:textColor="@color/colorDashText"
                    android:textSize="14sp"
                    android:textStyle="bold" />

                <com.github.mikephil.charting.charts.BarChart
                    android:id="@+id/barChartAdmin"
                    android:layout_width="match_parent"
                    android:layout_height="200dp" />
            </LinearLayout>
        </com.google.android.material.card.MaterialCardView>

        <!-- Gráfica Docente (LineChart) -->
        <com.google.android.material.card.MaterialCardView
            android:id="@+id/cardChartDocente"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginStart="20dp"
            android:layout_marginEnd="20dp"
            android:layout_marginTop="24dp"
            android:visibility="gone"
            app:cardBackgroundColor="@color/colorDashCard"
            app:cardCornerRadius="12dp"
            app:cardElevation="4dp"
            app:strokeWidth="0dp">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical"
                android:padding="16dp">

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginBottom="8dp"
                    android:text="Entregas últimos 7 días"
                    android:textColor="@color/colorDashText"
                    android:textSize="14sp"
                    android:textStyle="bold" />

                <com.github.mikephil.charting.charts.LineChart
                    android:id="@+id/lineChartDocente"
                    android:layout_width="match_parent"
                    android:layout_height="200dp" />
            </LinearLayout>
        </com.google.android.material.card.MaterialCardView>

        <!-- Actividad reciente -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:paddingTop="24dp"
            android:paddingStart="20dp"
            android:paddingEnd="20dp">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginBottom="12dp"
                android:text="Actividad reciente"
                android:textColor="@color/colorDashText"
                android:textSize="16sp"
                android:textStyle="bold" />

            <com.google.android.material.card.MaterialCardView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                app:cardBackgroundColor="@color/colorDashCard"
                app:cardCornerRadius="12dp"
                app:cardElevation="2dp"
                app:strokeWidth="0dp">

                <androidx.recyclerview.widget.RecyclerView
                    android:id="@+id/rvActividad"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:nestedScrollingEnabled="false" />
            </com.google.android.material.card.MaterialCardView>
        </LinearLayout>

    </LinearLayout>
</androidx.core.widget.NestedScrollView>
```

- [ ] **Step 4: Verificar que los layouts compilan**

```bash
./gradlew assembleDebug 2>&1 | grep -E "error:|BUILD" | tail -5
```
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 5: Commit**

```bash
git add app/src/main/res/layout/item_kpi_card.xml \
        app/src/main/res/layout/item_actividad_reciente.xml \
        app/src/main/res/layout/activity_dashboard.xml
git commit -m "feat(dashboard): agregar layouts activity_dashboard, item_kpi_card, item_actividad_reciente"
```

---

### Task 7: Crear ActividadAdapter

**Files:**
- Create: `app/src/main/java/com/conectatec/ui/dashboard/ActividadAdapter.java`

- [ ] **Step 1: Crear ActividadAdapter.java**

```java
package com.conectatec.ui.dashboard;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.conectatec.databinding.ItemActividadRecienteBinding;
import com.conectatec.model.ActividadItem;

import java.util.ArrayList;
import java.util.List;

public class ActividadAdapter extends RecyclerView.Adapter<ActividadAdapter.ViewHolder> {

    private final List<ActividadItem> lista = new ArrayList<>();

    public void setLista(List<ActividadItem> items) {
        lista.clear();
        lista.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemActividadRecienteBinding b = ItemActividadRecienteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActividadItem item = lista.get(position);
        holder.binding.tvTitulo.setText(item.titulo);
        holder.binding.tvDescripcion.setText(item.descripcion);
        holder.binding.tvTimestamp.setText(item.timestamp);

        String inicial = item.titulo.isEmpty()
                ? "?" : String.valueOf(item.titulo.charAt(0)).toUpperCase();
        holder.binding.tvAvatarInicial.setText(inicial);

        GradientDrawable circle = new GradientDrawable();
        circle.setShape(GradientDrawable.OVAL);
        circle.setColor(item.colorAvatar);
        holder.binding.tvAvatarInicial.setBackground(circle);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemActividadRecienteBinding binding;

        ViewHolder(ItemActividadRecienteBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
```

- [ ] **Step 2: Build para verificar ViewBinding resuelto**

```bash
./gradlew assembleDebug 2>&1 | grep -E "error:|BUILD" | tail -5
```
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/conectatec/ui/dashboard/ActividadAdapter.java
git commit -m "feat(dashboard): agregar ActividadAdapter con avatar circular dinámico"
```

---

### Task 8: Crear DashboardActivity y registrar en AndroidManifest

**Files:**
- Create: `app/src/main/java/com/conectatec/ui/dashboard/DashboardActivity.java`
- Modify: `app/src/main/AndroidManifest.xml`

- [ ] **Step 1: Crear DashboardActivity.java**

```java
package com.conectatec.ui.dashboard;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.conectatec.R;
import com.conectatec.databinding.ActivityDashboardBinding;
import com.conectatec.databinding.ItemKpiCardBinding;
import com.conectatec.model.DashboardStats;
import com.conectatec.model.DocenteStats;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;
    private DashboardViewModel viewModel;
    private ActividadAdapter actividadAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        actividadAdapter = new ActividadAdapter();

        binding.rvActividad.setLayoutManager(new LinearLayoutManager(this));
        binding.rvActividad.setItemAnimator(new DefaultItemAnimator());
        binding.rvActividad.setAdapter(actividadAdapter);

        String token = getSharedPreferences("tecconnect_prefs", MODE_PRIVATE)
                .getString("jwt_token", "");
        String[] claims = extraerClaims(token);
        binding.tvNombre.setText(claims[0]);
        binding.tvRol.setText(claims[1]);

        Glide.with(this)
                .load((Object) null)
                .placeholder(R.drawable.bg_avatar_placeholder)
                .into(binding.ivAvatar);

        if ("ADMINISTRADOR".equalsIgnoreCase(claims[1])) {
            viewModel.adminStats.observe(this, this::setupAdminDashboard);
            viewModel.actividadReciente.observe(this, actividadAdapter::setLista);
            viewModel.cargarDatosAdmin(token);
        } else {
            viewModel.docenteStats.observe(this, this::setupDocenteDashboard);
            viewModel.actividadReciente.observe(this, actividadAdapter::setLista);
            viewModel.cargarDatosDocente(token);
        }
    }

    private void setupAdminDashboard(DashboardStats stats) {
        binding.cardChartAdmin.setVisibility(View.VISIBLE);
        binding.cardChartDocente.setVisibility(View.GONE);

        int[] valores  = {stats.usuariosTotales, stats.docentesActivos, stats.gruposActivos,
                          stats.tareasPublicadas, stats.entregasPendientes, stats.mensajesHoy};
        String[] labels = {"Usuarios Totales", "Docentes Activos", "Grupos Activos",
                           "Tareas Publicadas", "Pend. de Revisar", "Mensajes Hoy"};
        int[] iconos   = {R.drawable.ic_people, R.drawable.ic_school, R.drawable.ic_group,
                          R.drawable.ic_assignment, R.drawable.ic_pending, R.drawable.ic_chat};
        int[] colores  = {Color.parseColor("#6C63FF"), Color.parseColor("#10B981"),
                          Color.parseColor("#F59E0B"), Color.parseColor("#EF4444"),
                          Color.parseColor("#3B82F6"), Color.parseColor("#8B5CF6")};

        binding.linearKpis.removeAllViews();
        for (int i = 0; i < 6; i++) {
            agregarKpiCard(String.valueOf(valores[i]), labels[i], iconos[i], colores[i]);
        }
        animarKpiCards();
        configurarBarChart(stats);
    }

    private void setupDocenteDashboard(DocenteStats stats) {
        binding.cardChartAdmin.setVisibility(View.GONE);
        binding.cardChartDocente.setVisibility(View.VISIBLE);

        String[] valores = {
            String.valueOf(stats.misGrupos), String.valueOf(stats.tareasActivas),
            String.valueOf(stats.entregasHoy), String.valueOf(stats.pendientesCalificar),
            String.valueOf(stats.alumnosTotales), String.format("%.1f", stats.promedioGeneral)
        };
        String[] labels  = {"Mis Grupos", "Tareas Activas", "Entregas Hoy",
                            "Pend. Calificar", "Alumnos Totales", "Promedio General"};
        int[] iconos = {R.drawable.ic_class, R.drawable.ic_assignment, R.drawable.ic_inbox,
                        R.drawable.ic_grading, R.drawable.ic_people, R.drawable.ic_bar_chart};
        int[] colores = {Color.parseColor("#6C63FF"), Color.parseColor("#10B981"),
                         Color.parseColor("#F59E0B"), Color.parseColor("#EF4444"),
                         Color.parseColor("#3B82F6"), Color.parseColor("#8B5CF6")};

        binding.linearKpis.removeAllViews();
        for (int i = 0; i < 6; i++) {
            agregarKpiCard(valores[i], labels[i], iconos[i], colores[i]);
        }
        animarKpiCards();
        configurarLineChart(stats);
    }

    private void agregarKpiCard(String valor, String etiqueta, int iconoRes, int color) {
        ItemKpiCardBinding card = ItemKpiCardBinding.inflate(
                getLayoutInflater(), binding.linearKpis, false);
        card.tvKpiValue.setText(valor);
        card.tvKpiLabel.setText(etiqueta);
        card.ivKpiIcon.setImageResource(iconoRes);
        card.ivKpiIcon.setImageTintList(ColorStateList.valueOf(color));
        card.viewStrip.setBackgroundColor(color);
        binding.linearKpis.addView(card.getRoot());
    }

    private void animarKpiCards() {
        for (int i = 0; i < binding.linearKpis.getChildCount(); i++) {
            View card = binding.linearKpis.getChildAt(i);
            card.setAlpha(0f);
            card.setTranslationY(30f);
            ObjectAnimator fade  = ObjectAnimator.ofFloat(card, View.ALPHA, 0f, 1f);
            ObjectAnimator slide = ObjectAnimator.ofFloat(card, View.TRANSLATION_Y, 30f, 0f);
            AnimatorSet set = new AnimatorSet();
            set.playTogether(fade, slide);
            set.setDuration(300);
            set.setStartDelay((long) i * 80);
            set.start();
        }
    }

    private void configurarBarChart(DashboardStats stats) {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            entries.add(new BarEntry(i, stats.usuariosPorRol[i]));
        }
        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(Color.parseColor("#10B981"),
                          Color.parseColor("#6C63FF"),
                          Color.parseColor("#F59E0B"));
        dataSet.setDrawValues(false);
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.6f);
        binding.barChartAdmin.setData(data);
        binding.barChartAdmin.getDescription().setEnabled(false);
        binding.barChartAdmin.getLegend().setEnabled(false);
        binding.barChartAdmin.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        binding.barChartAdmin.getXAxis().setValueFormatter(
                new IndexAxisValueFormatter(new String[]{"DOCENTE", "ESTUDIANTE", "PENDIENTE"}));
        binding.barChartAdmin.getXAxis().setGranularity(1f);
        binding.barChartAdmin.getXAxis().setDrawGridLines(false);
        binding.barChartAdmin.getAxisLeft().setDrawGridLines(false);
        binding.barChartAdmin.getAxisRight().setEnabled(false);
        binding.barChartAdmin.animateY(1000);
    }

    private void configurarLineChart(DocenteStats stats) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            entries.add(new Entry(i, stats.entregasPorDia[i]));
        }
        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawFilled(true);
        dataSet.setFillAlpha(50);
        dataSet.setColor(Color.parseColor("#6C63FF"));
        dataSet.setFillColor(Color.parseColor("#6C63FF"));
        dataSet.setCircleColor(Color.parseColor("#6C63FF"));
        dataSet.setLineWidth(2f);
        dataSet.setDrawValues(false);
        LineData data = new LineData(dataSet);
        binding.lineChartDocente.setData(data);
        String[] dias = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};
        binding.lineChartDocente.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        binding.lineChartDocente.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dias));
        binding.lineChartDocente.getXAxis().setGranularity(1f);
        binding.lineChartDocente.getXAxis().setDrawGridLines(false);
        binding.lineChartDocente.getDescription().setEnabled(false);
        binding.lineChartDocente.getLegend().setEnabled(false);
        binding.lineChartDocente.getAxisLeft().setDrawGridLines(false);
        binding.lineChartDocente.getAxisRight().setEnabled(false);
        binding.lineChartDocente.animateY(800);
    }

    private String[] extraerClaims(String jwt) {
        String[] result = {"Usuario", "DOCENTE"};
        try {
            String[] parts = jwt.split("\\.");
            if (parts.length < 2) return result;
            byte[] decoded = android.util.Base64.decode(
                    parts[1], android.util.Base64.URL_SAFE | android.util.Base64.NO_PADDING);
            String payload = new String(decoded, "UTF-8");
            JSONObject json = new JSONObject(payload);
            result[0] = json.optString("nombre", "Usuario");
            result[1] = json.optString("rol", "DOCENTE");
        } catch (Exception ignored) {}
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
```

- [ ] **Step 2: Registrar DashboardActivity en AndroidManifest.xml**

Agregar dentro del tag `<application>`, antes del cierre `</application>`:

```xml
        <activity
            android:name=".ui.dashboard.DashboardActivity"
            android:exported="false" />
```

- [ ] **Step 3: Build final completo**

```bash
./gradlew assembleDebug 2>&1 | grep -E "error:|warning:|BUILD" | tail -10
```
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 4: Correr todos los tests unitarios**

```bash
./gradlew test 2>&1 | tail -10
```
Expected: `BUILD SUCCESSFUL`, todos los tests en verde.

- [ ] **Step 5: Commit final**

```bash
git add app/src/main/java/com/conectatec/ui/dashboard/DashboardActivity.java \
        app/src/main/AndroidManifest.xml
git commit -m "feat(dashboard): implementar DashboardActivity con gráficas, KPIs y animaciones"
```
