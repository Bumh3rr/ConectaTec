# Diseño: Modo Oscuro/Claro + Animaciones Módulo Docente

**Fecha:** 2026-05-19
**Proyecto:** TecConnect Android

---

## Contexto

El app actualmente usa un tema oscuro fijo (`Theme.Material3.Dark.NoActionBar`). No existe paleta de colores claros ni mecanismo de switching. El sistema de animaciones (`EntradaAnimator`, `ScrollRevealAnimator`) está implementado en el módulo Admin pero no aplicado en el módulo Docente (excepto `DocentePerfilFragment`).

---

## Sección 1 — Arquitectura del Tema Claro/Oscuro

### Cambio de parent theme

`values/themes.xml` y `values-night/themes.xml` cambian el parent a:

```xml
Theme.Material3.DayNight.NoActionBar
```

Android selecciona automáticamente la variante según el modo activo (`AppCompatDelegate`).

### Paleta de colores

**`values/colors.xml`** — paleta clara (nueva):

| Nombre | Valor | Uso |
|--------|-------|-----|
| `colorBackground` | `#F2F4F8` | Fondo principal |
| `colorSurface` | `#FFFFFF` | Superficie de cards |
| `colorSurfaceVariant` | `#E8EAF0` | Superficie secundaria |
| `colorOnSurface` | `#1A1A1A` | Texto principal |
| `colorOnSurfaceVariant` | `#5A5A6A` | Texto secundario |
| `colorOnPrimary` | `#FFFFFF` | Texto sobre primario |
| `colorDivider` | `#D0D4DE` | Separadores |
| `colorBorder` | `#C8CCD8` | Bordes de inputs |
| `colorNavBackground` | `#FFFFFF` | Fondo pill nav |
| `colorNavCircle` | `#E3EAF5` | Círculo activo pill nav |
| `colorNavIcon` | `#1976D2` | Ícono activo |
| `colorNavIconInactive` | `#8A8A9A` | Ícono inactivo |
| `colorNavLabel` | `#1976D2` | Label activo |
| `colorNavLabelInactive` | `#8A8A9A` | Label inactivo |

Los colores de rol (`colorDocente`, `colorEstudiante`, `colorPendiente`, `colorAdmin`), colores primarios (`colorPrimary`, `colorPrimaryDark`, `colorPrimaryLight`) y colores de estado (`colorError`, `colorSuccess`, `colorWarning`) son **iguales en ambos temas** — no requieren variante nocturna.

**`values-night/colors.xml`** — paleta oscura (valores actuales movidos aquí):

Mismos nombres de recursos pero con los valores oscuros ya existentes en el proyecto.

### ThemePreferenceManager

Nueva clase `ui/common/ThemePreferenceManager.java`:

- Guarda preferencia en `SharedPreferences` con clave `"app_theme_mode"`
- Tres modos: `MODE_NIGHT_FOLLOW_SYSTEM` (default), `MODE_NIGHT_NO`, `MODE_NIGHT_YES`
- Método estático `applyTheme(Context)` — llamado en `onCreate` de cada Activity **antes** de `super.onCreate()`
- Método estático `setThemeMode(Context, int)` — guarda y aplica el modo; `AppCompatDelegate` gestiona la recreación automática

Activities que deben llamar `applyTheme`: `SplashActivity`, `LoginActivity`, `RegistroActivity`, `WaitingApprovalActivity`, `MainAdminActivity`, `MainDocenteActivity`.

---

## Sección 2 — AjustesFragment y Navegación

### Fragment

**Ubicación:** `ui/common/ajustes/AjustesFragment.java`
**Layout:** `fragment_ajustes.xml`
**Anotación:** `@AndroidEntryPoint`
**Binding:** `FragmentAjustesBinding`

Un único fragment reutilizado desde Admin y Docente.

### Layout (estilo Teams)

```
┌──────────────────────────────────┐
│  ← Ajustes                       │  ← toolbar/header con back
├──────────────────────────────────┤
│  APARIENCIA                      │  ← TextView sección (colorPrimary)
│  ┌──────────────────────────────┐│
│  │ Tema          ○ Sistema      ││  ← MaterialCardView
│  │               ○ Claro        ││
│  │               ○ Oscuro       ││
│  └──────────────────────────────┘│
│  NOTIFICACIONES                  │
│  ┌──────────────────────────────┐│
│  │ Notificaciones       [  ●  ] ││  ← Switch placeholder (siempre ON)
│  └──────────────────────────────┘│
│  GENERAL                         │
│  ┌──────────────────────────────┐│
│  │ Idioma            Español  > ││  ← placeholder sin acción
│  └──────────────────────────────┘│
└──────────────────────────────────┘
```

**Selector de tema:** `RadioGroup` vertical con 3 `RadioButton` dentro de `MaterialCardView`. Al cambiar selección: llama `ThemePreferenceManager.setThemeMode()` — la Activity se recrea automáticamente con el nuevo tema.

**Estado inicial:** Lee `ThemePreferenceManager.getCurrentMode()` en `onViewCreated` y marca el radio correspondiente.

**Animación:** `EntradaAnimator` sobre header, card apariencia, card notificaciones, card general.

### Navegación

**`nav_admin.xml`:**
- Nuevo destino: `ajustesFragment` → `com.conectatec.ui.common.ajustes.AjustesFragment`
- Acción en `adminPerfilFragment`: `action_admin_perfil_to_ajustes`

**`nav_docente.xml`:**
- Nuevo destino: `ajustesFragment` → `com.conectatec.ui.common.ajustes.AjustesFragment`
- Acción en `docentePerfilFragment`: `action_docente_perfil_to_ajustes`

**Sin `NavOptions` de singleTop** — no es destino raíz.

### Cambios en Perfiles existentes

**`AdminPerfilFragment`** — la tarjeta de ajustes no existe actualmente; se agrega una fila "Ajustes" clickeable con ícono de engrane antes del botón Cerrar Sesión. Al hacer click: `Navigation.findNavController(...).navigate(R.id.action_admin_perfil_to_ajustes)`.

**`DocentePerfilFragment`** — la fila "Tema" en `cardConfigDocente` se convierte en el punto de entrada a Ajustes: el click navega a `AjustesFragment` en lugar de mostrar un placeholder.

---

## Sección 3 — Sistema de Animaciones para el Módulo Docente

### EntradaAnimator — 12 fragments

Llamada en `onViewCreated` con las vistas principales de cada fragment:

| Fragment | Vistas pasadas a EntradaAnimator |
|---|---|
| `DocenteDashboardFragment` | headerDashboard, cardStatGrupos, cardStatAlumnos, containerTareasRecientes, containerAvisos |
| `DocenteGruposFragment` | headerGrupos, etBuscarGrupos, rvGruposDocente |
| `DocenteGrupoDetalleFragment` | cardInfoGrupoDetalle, containerMiembrosPreview, containerAvisosPreview, layoutBotonesGrupoDetalle |
| `DocenteMiembrosGrupoFragment` | headerMiembrosDocente, rvMiembrosGrupoDocente |
| `DocenteCrearGrupoFragment` | cardFormularioCrearGrupo, btnCrearGrupo |
| `DocenteTareasFragment` | headerTareas, rvGruposTareas |
| `DocenteBloquesFragment` | headerBloques, rvBloques |
| `DocenteTareasBloqueFragment` | headerTareasBloque, hsvChipsTipo, hsvChipsEstado, rvTareasBloque |
| `DocenteCrearTareaFragment` | etTituloTareaNueva, spinnerTipoTareaNueva, etDescripcionTareaNueva, etFechaLimiteTareaNueva, btnPublicarTarea |
| `DocenteEntregasFragment` | cardStatsEntregas, hsvChipsEntregas, rvEntregas |
| `DocenteCalificarEntregaFragment` | cardAvatarAlumno, cardInfoEntrega, containerArchivoCalif, etCalificacionEntrega, btnGuardarCalificacion |
| `DocenteChatFragment` | headerChat, hsvChipsChat, rvSalasChat |

### ScrollRevealAnimator — 7 fragments

Adjuntado al `RecyclerView` en `onViewCreated` después de asignar el adapter, usando `addOnScrollListener`:

- `DocenteGruposFragment` → `rvGruposDocente`
- `DocenteMiembrosGrupoFragment` → `rvMiembrosGrupoDocente`
- `DocenteTareasFragment` → `rvGruposTareas`
- `DocenteBloquesFragment` → `rvBloques`
- `DocenteTareasBloqueFragment` → `rvTareasBloque`
- `DocenteEntregasFragment` → `rvEntregas`
- `DocenteChatFragment` → `rvSalasChat`

**`DocenteConversacionFragment` excluido** — chat con `stackFromEnd=true`; la animación de entrada rompería la posición del scroll.

---

## Archivos a crear

| Archivo | Descripción |
|---|---|
| `ui/common/ThemePreferenceManager.java` | Gestión de preferencia de tema |
| `ui/common/ajustes/AjustesFragment.java` | Fragment de ajustes compartido |
| `res/layout/fragment_ajustes.xml` | Layout del fragment de ajustes |
| `res/values-night/colors.xml` | Paleta oscura (valores actuales) |

## Archivos a modificar

| Archivo | Cambio |
|---|---|
| `res/values/colors.xml` | Agregar paleta clara |
| `res/values/themes.xml` | Cambiar parent a DayNight |
| `res/values-night/themes.xml` | Cambiar parent a DayNight |
| `res/navigation/nav_admin.xml` | Agregar destino + acción ajustes |
| `res/navigation/nav_docente.xml` | Agregar destino + acción ajustes |
| `ui/admin/perfil/AdminPerfilFragment.java` | Agregar fila Ajustes |
| `ui/docente/perfil/DocentePerfilFragment.java` | Conectar fila Tema → Ajustes |
| 6 Activities (auth + main) | Llamar `applyTheme` en `onCreate` |
| 12 fragments docente | Agregar `EntradaAnimator` |
| 7 fragments docente | Agregar `ScrollRevealAnimator` |
