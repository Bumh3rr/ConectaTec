# Dashboard Charts — Diseño

**Fecha:** 2026-05-21
**Alcance:** Módulo Docente (`DocenteDashboardFragment`) y Módulo Admin (`AdminDashboardFragment`)
**Objetivo:** Agregar gráficas visuales (donut y barras) a ambos dashboards para hacer la información estadística más comprensible de un vistazo, manteniendo la armonía visual del proyecto.

---

## Dependencia nueva

### MPAndroidChart v3.1.0

- **Repositorio:** JitPack (`https://jitpack.io`)
- **Artifact:** `com.github.PhilJay:MPAndroidChart:v3.1.0`

**Cambios de configuración:**

`settings.gradle.kts` — agregar en `dependencyResolutionManagement.repositories`:
```kotlin
maven { url = uri("https://jitpack.io") }
```

`app/build.gradle.kts` — agregar en `dependencies`:
```kotlin
implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
```

**Tipos de gráfica usados:**
- `PieChart` (con `holeRadius` para efecto donut) — estado global de entregas
- `BarChart` — datos por grupo o por estado de actividad
- `ProgressBar` nativo Android — progreso por tarea (no requiere MPAndroidChart, es más liviano)

---

## Dashboard Docente (`DocenteDashboardFragment`)

### Layout final — orden de secciones

1. **Header** — sin cambios (barra de acento, título "Inicio", saludo, avatar)
2. **`cardBienvenidaDocente`** — sin cambios (hero card azul: nombre, stats grupos/alumnos/tareas)
3. **`layoutKpisDocente`** — reducido a 1 fila de 2 tarjetas: "Tareas activas" + "Por revisar"
4. **`cardDonutEntregas`** _(nuevo)_ — PieChart donut: estado global de entregas
5. **`cardBarrasGrupos`** _(nuevo)_ — BarChart: alumnos por grupo
6. **`cardProgresoTareas`** _(nuevo)_ — ProgressBars: progreso de entregas por tarea activa
7. **`cardTareasRecientesDashboard`** — se conserva (lista de tareas recientes)

**Eliminado:** `cardActividadHoyDashboard` (avisos del día) — reemplazado por las gráficas.

### Gráfica 1 — `cardDonutEntregas` (PieChart)

- **Título:** "Estado global de entregas"
- **Tipo:** `PieChart` con `holeRadius = 58f`, `transparentCircleRadius = 61f`
- **Segmentos:**
  | Segmento | Color | Dato |
  |---|---|---|
  | Calificadas | `#4CAF50` (`colorSuccess`) | Entregas con calificación != null |
  | Entregadas | `#1976D2` (`colorPrimary`) | Entregas sin calificar (estado ENTREGADA) |
  | Pendientes | `#F57C00` (`colorWarning`) | Alumnos sin entrega (estado SIN_ENTREGAR) |
- **Centro del donut:** porcentaje de completitud `(calificadas + entregadas) / total * 100`
- **Leyenda:** lateral derecha, con nombre, porcentaje y conteo absoluto
- **Animación:** `animateY(800, Easing.EaseInOutQuad)`
- **Estilo:** sin descripción, sin borde, fondo transparente, texto en `colorOnSurface`
- **Datos:** derivados de `EntregaDocenteAdapter.cargarParaTarea()` agregando todas las tareas activas de los 3 grupos

### Gráfica 2 — `cardBarrasGrupos` (BarChart)

- **Título:** "Alumnos por grupo"
- **Tipo:** `BarChart`
- **Barras:** una por grupo (3 grupos del docente)
  | Grupo | Color |
  |---|---|
  | Prog. Móvil 6A | `#1976D2` |
  | Bases de Datos 4B | `#388E3C` |
  | Cálculo Integral 2A | `#7B1FA2` |
- **Eje X:** etiquetas con nombre abreviado del grupo
- **Eje Y:** total de alumnos por grupo, escala 0–20
- **Estilo:** sin leyenda, sin cuadrícula, barras con esquinas redondeadas (radius 4dp), fondo transparente
- **Animación:** `animateY(700, Easing.EaseInOutQuad)`
- **Datos:** `GrupoTareasDocenteAdapter` — campo `totalAlumnos` de cada grupo

### Sección 3 — `cardProgresoTareas` (ProgressBar nativo)

- **Título:** "Progreso de entregas"
- **Contenido:** 3 filas (una por tarea activa más reciente), cada fila con:
  - Nombre de tarea (truncado a 1 línea)
  - Fracción: `entregadas / totalAlumnos`
  - `ProgressBar` horizontal, `max = totalAlumnos`, `progress = entregadas`
- **Color de la barra según porcentaje:**
  - ≥ 70% → `colorSuccess` (`#4CAF50`)
  - 40–69% → `colorPrimary` (`#1976D2`)
  - < 40% → `colorWarning` (`#F57C00`)
- **Datos:** primeras 3 tareas del `TareaDocenteAdapter.DATASET` con estado `EN_CURSO`

### Java — `DocenteDashboardFragment`

- Se agrega método privado `configurarGraficas()` llamado desde `onViewCreated` después de `observeViewModel()`
- `configurarGraficas()` invoca 3 helpers privados:
  - `configurarDonutEntregas(PieChart chart)`
  - `configurarBarrasGrupos(BarChart chart)`
  - `configurarProgresoTareas()`
- Los datos se calculan inline con los adapters/datasets existentes — no se necesita ViewModel nuevo
- `EntradaAnimator.animar()` incluye las 3 cards nuevas junto con las existentes

---

## Dashboard Admin (`AdminDashboardFragment`)

### Layout final — orden de secciones

1. **Header** — sin cambios (título "Panel de control", avatar admin)
2. **KPI grid 2×2** — sin cambios (Usuarios, Pendientes, Grupos, Tareas)
3. **`cardDonutUsuarios`** _(nuevo)_ — PieChart donut: distribución de usuarios por rol
4. **`cardBarrasActividades`** _(nuevo)_ — BarChart: actividades por estado

**Eliminado:** sección "Actividades recientes" (3 ítems estáticos) — reemplazada por las gráficas.

### Gráfica 1 — `cardDonutUsuarios` (PieChart)

- **Título:** "Usuarios por rol"
- **Tipo:** `PieChart` con `holeRadius = 58f`
- **Segmentos:**
  | Segmento | Color | Dato |
  |---|---|---|
  | Admin | `#7B1FA2` (`colorChipAdmin`) | Usuarios con rol ADMIN |
  | Docente | `#1976D2` (`colorChipDocente`) | Usuarios con rol DOCENTE |
  | Estudiante | `#388E3C` (`colorChipEstudiante`) | Usuarios con rol ESTUDIANTE |
  | Pendiente | `#F57C00` (`colorChipPendiente`) | Usuarios con rol PENDIENTE |
- **Centro del donut:** total de usuarios (número)
- **Leyenda:** lateral, con nombre y conteo
- **Animación:** `animateY(800, Easing.EaseInOutQuad)`
- **Datos:** `UsuarioAdminAdapter` — dataset de 7 usuarios estáticos

### Gráfica 2 — `cardBarrasActividades` (BarChart)

- **Título:** "Actividades por estado"
- **Tipo:** `BarChart`
- **Barras:** 4 barras (una por estado)
  | Estado | Color |
  |---|---|
  | En Curso | `#1976D2` |
  | Pendiente | `#F57C00` |
  | Completada | `#4CAF50` |
  | Vencida | `#CF6679` (`colorError`) |
- **Eje X:** etiquetas con nombre del estado
- **Eje Y:** cantidad de actividades, escala automática
- **Animación:** `animateY(700, Easing.EaseInOutQuad)`
- **Datos:** `ActividadAdminAdapter` — dataset de 8 actividades, agrupadas por estado

### Java — `AdminDashboardFragment`

- Se agrega `@AndroidEntryPoint` (actualmente no lo tiene — se agrega para consistencia con el resto del módulo admin)
- Se agrega import de `EntradaAnimator`
- Se **elimina** el listener `binding.tvVerTodoActividad` (el elemento se quita del layout junto con la sección "Actividad Reciente")
- Se agrega método privado `configurarGraficas()` llamado desde `onViewCreated`
- `configurarGraficas()` invoca:
  - `configurarDonutUsuarios(PieChart chart)`
  - `configurarBarrasActividades(BarChart chart)`
- `EntradaAnimator.animar()` anima el grid de KPIs y las 2 cards nuevas

---

## Armonía visual — reglas para ambas gráficas

- **Fondo:** transparente (mismo que `colorSurface`)
- **Textos:** `colorOnSurface` (`#1A1A1A`) para títulos, `colorOnSurfaceVariant` (`#5A5A6A`) para etiquetas
- **Sin descripción:** `chart.getDescription().setEnabled(false)`
- **Sin borde:** `chart.setDrawBorders(false)`
- **Touch deshabilitado** en el dashboard (solo visual): `chart.setTouchEnabled(false)`
- **Colores exclusivamente** de `colors.xml` — cero hex literals en Java
- **Altura de las cards:** donut = `200dp`, barras = `180dp`

---

## Archivos a modificar

| Archivo | Cambio |
|---|---|
| `settings.gradle.kts` | Agregar JitPack |
| `app/build.gradle.kts` | Agregar dependencia MPAndroidChart |
| `res/layout/fragment_docente_dashboard.xml` | Reorganizar layout, agregar 3 cards nuevas |
| `res/layout/fragment_admin_dashboard.xml` | Agregar 2 cards nuevas, quitar actividades recientes |
| `ui/docente/dashboard/DocenteDashboardFragment.java` | Agregar `configurarGraficas()` y helpers |
| `ui/admin/dashboard/AdminDashboardFragment.java` | Agregar `configurarGraficas()` y helpers |

**Archivos nuevos:** ninguno.
