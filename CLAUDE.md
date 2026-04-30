# TecConnect — Android App

Plataforma académica Android para gestión de grupos, tareas y comunicación entre docentes y estudiantes.

## Stack técnico

- **Lenguaje:** Java 11
- **Min SDK:** 24 | **Target SDK:** 36
- **Package:** `com.conectatec`
- **DI:** Hilt (`@AndroidEntryPoint` en Activities y Fragments)
- **UI:** ViewBinding (nunca `findViewById`)
- **Nav:** Navigation Component (`nav_admin.xml`, `nav_docente.xml`, `nav_registro.xml`)
- **Build:** Gradle KTS (`app/build.gradle.kts`)

## Arquitectura por roles

El app tiene dos módulos principales de UI, cada uno con su propia Activity + nav graph:

### Módulo Admin (`ui/admin/`)
- Entry: `MainAdminActivity` → `nav_admin.xml`
- 5 tabs: Dashboard, Grupos, Actividades, Usuarios, Perfil
- Fragments: `AdminDashboard`, `AdminGrupos`, `AdminGrupoDetalle`, `AdminMiembrosGrupo`, `AdminActividades`, `AdminActividadDetalle`, `AdminAlumnoDetalle`, `AdminUsuarios`, `AdminUsuarioDetalle`, `AdminPendientes`, `AdminPerfil`
- Adapters: `GrupoAdminAdapter`, `MiembrosGrupoAdapter`, `ActividadAdminAdapter`, `UsuarioAdminAdapter`, `UsuarioPendienteAdapter`

### Módulo Docente (`ui/docente/`) — rama `vista-docente`
Implementado en 5 planes (Plans 1–5). Entry: `MainDocenteActivity` → `nav_docente.xml`.
5 tabs: Dashboard, Grupos, Tareas, Chat, Perfil.

| Plan | Módulo | Archivos clave |
|------|--------|----------------|
| 1 (infra) | Activity + nav graph + placeholders | `MainDocenteActivity`, `nav_docente.xml`, `activity_main_docente.xml` |
| 2 | Dashboard + Perfil | `DocenteDashboardFragment`, `DocentePerfilFragment` |
| 3 | Grupos | `DocenteGruposFragment`, `DocenteGrupoDetalleFragment`, `DocenteCrearGrupoFragment`, `DocenteMiembrosGrupoFragment` |
| 4 | Tareas | `DocenteTareasFragment`, `DocenteBloquesFragment`, `DocenteTareasBloqueFragment`, `DocenteCrearTareaFragment`, `DocenteEntregasFragment`, `DocenteCalificarEntregaFragment` |
| 5 | Chat | `DocenteChatFragment`, `DocenteConversacionFragment` |

#### Drill-down del módulo Tareas (Plan 4)
`DocenteTareasFragment` (lista grupos) → `DocenteBloquesFragment` (3 bloques) → `DocenteTareasBloqueFragment` (tareas con filtros tipo+estado) → `DocenteEntregasFragment` (entregas con progreso) → `DocenteCalificarEntregaFragment` (calificar). También: `DocenteTareasBloqueFragment` → `DocenteCrearTareaFragment`.

#### Adapters del módulo Docente
- `GrupoDocenteAdapter`, `MiembroGrupoDocenteAdapter` (Plan 3)
- `GrupoTareasDocenteAdapter`, `BloqueDocenteAdapter`, `TareaDocenteAdapter`, `EntregaDocenteAdapter` (Plan 4)
- `SalaDocenteAdapter`, `MensajeDocenteAdapter` (Plan 5)

#### Dataset dummy (Plan 4 — coordinación crítica)
Los 3 grupos del docente deben coincidir exactamente en `id`, `nombre`, `materia`, `totalAlumnos` y `fechaCreacion` entre `GrupoDocenteAdapter` (Plan 3) y `GrupoTareasDocenteAdapter` (Plan 4):

| id | nombre | materia | totalAlumnos | fechaCreacion |
|----|--------|---------|-------------|--------------|
| 1 | Programación Móvil 6A | Programación Móvil | 18 | 01/02/2026 |
| 2 | Bases de Datos 4B | Bases de Datos | 16 | 15/01/2026 |
| 3 | Cálculo Integral 2A | Cálculo Integral | 13 | 20/01/2026 |

`TareaDocenteAdapter` contiene 8 tareas dummy estáticas en `DATASET` (grupoId 1–3, bloqueId 1–3).

### Flujo de autenticación (`ui/auth/`)
- Entry: `SplashActivity` (LAUNCHER) → `LoginActivity` → por rol
- `LoginActivity` tiene **selector provisional de rol** (MODO DEMO): botón "Admin" → `MainAdminActivity`, botón "Docente" → `MainDocenteActivity`
- Registro: `RegistroActivity` con nav graph propio (`nav_registro.xml`) y 5 fragments de flujo
- **TODO:** integrar `LoginViewModel` con JWT y enrutar por rol automáticamente (eliminar selector provisional)

## Convenciones de layout XML

Ver skill `tecconnect-android-layouts` para reglas completas. Resumen:

- **Cero hex literals** — solo `@color/colorXxx` y `@drawable/bg_*`
- **No `BottomNavigationView`** — pill nav custom (`LinearLayout` horizontal, items 56dp)
- **MaterialCardView:** `cornerRadius=12dp`, `elevation=0dp`, `strokeWidth=0dp`
- **Chips de estado:** `TextView` con `@drawable/bg_chip_*` (docente=azul, estudiante=verde, pendiente=naranja, admin=morado)
- **Empty state:** `<include layout="@layout/layout_empty_state"/>` — nunca duplicar
- **RecyclerView con pill nav:** `paddingBottom="88dp"` + `clipToPadding="false"`
- **Chips de filtro** (checked state): `com.google.android.material.chip.Chip style="@style/Widget.Material3.Chip.Filter"` dentro de `HorizontalScrollView`

## Convenciones Java

Ver skill `tecconnect-android-java` para reglas completas. Resumen:

- `@AndroidEntryPoint` en toda Activity y Fragment
- ViewBinding: inflar en `onCreateView`, nulificar en `onDestroyView` (`binding = null`)
- Datos dummy: clase interna estática `*DummyXxx` dentro del adapter, método `cargarDatosDummy()` o similar
- TODOs de backend en español: `// TODO: llamar a XxxService.metodo()`
- Navegación con args: `Navigation.findNavController(requireView()).navigate(R.id.action_xxx, args)`
- NavOptions solo en tabs raíz (no en sub-destinos)

## Recursos importantes

- `app/src/main/res/values/colors.xml` — paleta completa
- `app/src/main/res/values/strings.xml` — todos los strings (organizados por módulo con comentarios)
- `app/src/main/res/drawable/` — `bg_chip_*`, `bg_avatar_placeholder`, `bg_card`, `bg_input`, `bg_accent_bar`, `bg_nav_container`, `ic_*`
- `app/src/main/res/navigation/nav_docente.xml` — todas las acciones del módulo docente declaradas
- `docs/superpowers/plans/` — planes de implementación originales (referencia histórica)
- `docs/superpowers/specs/2026-04-30-docente-module-design.md` — spec completa del módulo docente
