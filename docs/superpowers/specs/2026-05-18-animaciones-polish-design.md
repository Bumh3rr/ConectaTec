# Spec: Polish de Animaciones — TecConnect Android

**Fecha:** 2026-05-18  
**Enfoque aprobado:** B — Pulir + Animación del Pill Nav  
**Estilo:** Material 3 Estándar (FastOutSlowIn)

---

## Objetivo

Hacer las animaciones de TecConnect más fluidas y profesionales sin agregar dependencias externas. El resultado debe sentirse contenido e institucional — no llamativo.

---

## 1. Sistema de timing y curvas

Un set único de constantes de referencia. Se definen como `static final` dentro de `EntradaAnimator` y se referencian desde cualquier lugar que las necesite.

| Token | Valor | Uso |
|---|---|---|
| `DURATION_SHORT` | 200ms | Feedback del pill nav, label fade |
| `DURATION_STANDARD` | 300ms | Entradas de pantalla, fade de navegación |
| `DURATION_LONG` | 350ms | Slides de navegación entre fragments |
| Interpolador principal | `FastOutSlowInInterpolator` | Entradas de pantalla, slides |
| Interpolador pill | `OvershootInterpolator(1.5f)` | Scale del círculo activo en el nav |

---

## 2. Archivos XML de animación (`res/anim/`)

### `fade_in.xml` / `fade_out.xml`
- Duración: `300ms` (sin cambio)
- Interpolador: `@interpolator/fast_out_slow_in` (reemplaza `decelerate_cubic`)

### `slide_in_right.xml` / `slide_in_left.xml`
Combinan translate + alpha para que la pantalla entrante no "empuje" bruscamente:

```xml
<set>
  <translate fromXDelta="40%" toXDelta="0%" duration="350"
             interpolator="@interpolator/fast_out_slow_in" />
  <alpha fromAlpha="0.0" toAlpha="1.0" duration="280"
         interpolator="@interpolator/fast_out_slow_in" />
</set>
```

El desplazamiento baja de `100%` a `40%` — es suficiente para dar dirección sin ser un "wipe" completo.

### `slide_out_left.xml` / `slide_out_right.xml`
Equivalente inverso con fade-out:

```xml
<set>
  <translate fromXDelta="0%" toXDelta="-40%" duration="300"
             interpolator="@interpolator/fast_out_slow_in" />
  <alpha fromAlpha="1.0" toAlpha="0.0" duration="200"
         interpolator="@interpolator/fast_out_slow_in" />
</set>
```

La pantalla saliente desaparece más rápido que la entrante aparece — crea sensación de profundidad.

---

## 3. `EntradaAnimator` — utilidad centralizada

**Archivo:** `app/src/main/java/com/conectatec/ui/common/EntradaAnimator.java`

Clase utilitaria estática que reemplaza el código `prepararAnimacion()` + `animarEntrada()` duplicado en al menos 5 fragments.

### API pública

```java
// Anima un conjunto de vistas con el patrón estándar
EntradaAnimator.animar(View... vistas);
```

### Comportamiento interno

1. Para cada vista, establece estado inicial: `alpha=0f`, `translationY=20dp`
2. Encadena animación con `ViewPropertyAnimator`:
   - `alpha(1f)`
   - `translationY(0f)`
   - Duración: `300ms`
   - Stagger: `60ms × índice`
   - Interpolador: `FastOutSlowInInterpolator`
3. Usa `rv.post()` si se llama desde dentro de un `RecyclerView`; si no, directamente

### Fragments a refactorizar

Eliminar `prepararAnimacion()` y `animarEntrada()` y reemplazar con `EntradaAnimator.animar(...)` en:

| Fragment | Vistas animadas |
|---|---|
| `DocenteDashboardFragment` | cardBienvenida, layoutKpis, cardTareasRecientes, cardActividadHoy |
| `DocenteGrupoDetalleFragment` | cardHeroGrupo, cardAccionesRapidas, cardMiembrosGrupo, cardAvisosGrupo |
| `DocenteQrGrupoFragment` | cards en el layout QR |
| `DocentePerfilFragment` | cardHeroPerfil, cardInfoCuenta, cardConfig, btnCerrarSesion |
| `DashboardActivity` | cards del dashboard admin |

---

## 4. Animación del Pill Nav

**Archivos:** `MainAdminActivity.java` y `MainDocenteActivity.java`  
**Método:** `actualizarPill(int activeIndex)`

### Comportamiento actual (sin animación)
`setScaleX(0.8f)` / `setScaleY(1f)` instantáneo al cambiar de tab.

### Comportamiento nuevo

Al activar un tab:
```java
// Círculo: press + rebote
item.circle.animate()
    .scaleX(1.0f).scaleY(1.0f)
    .setDuration(200)
    .setInterpolator(new OvershootInterpolator(1.5f))
    .start();

// Label: fade-in
item.label.setAlpha(0f);
item.label.setVisibility(View.VISIBLE);
item.label.animate()
    .alpha(1f)
    .setDuration(150)
    .setInterpolator(new FastOutSlowInInterpolator())
    .start();
```

Al desactivar un tab:
```java
// Label: fade-out rápido antes de ocultar
item.label.animate()
    .alpha(0f)
    .setDuration(100)
    .withEndAction(() -> item.label.setVisibility(View.GONE))
    .start();

// Círculo: vuelve a escala neutral
item.circle.animate()
    .scaleX(1f).scaleY(1f)
    .setDuration(150)
    .start();
```

**Lo que NO cambia:** El fondo `bg_nav_item_active` sigue aplicándose instantáneo vía `setBackgroundResource` — animarlo requeriría refactor del layout y no vale la complejidad.

---

## 5. `ScrollRevealAnimator` — ajustes puntuales

**Archivo:** `ScrollRevealAnimator.java`

| Constante | Valor actual | Valor nuevo | Razón |
|---|---|---|---|
| `MIN_ALPHA` | `0.3f` | `0.0f` | Efecto de borde más limpio |
| `MIN_SCALE` | `0.88f` | `0.92f` | Scale menos agresivo |
| `ANIM_MS` | `260` | `300` | Consistente con el sistema |
| `STAGGER_MS` | `55` | `50` | Más ajustado |
| translationY inicial | `16f` | `24f` | Más presencia al entrar |
| Interpolador | `DecelerateInterpolator(1.6f)` | `FastOutSlowInInterpolator` | Consistencia |

El comportamiento durante scroll (`aplicarInstante()`) no cambia — sigue siendo asignación directa sin animación.

---

## Archivos modificados

| Archivo | Tipo de cambio |
|---|---|
| `res/anim/fade_in.xml` | Interpolador |
| `res/anim/fade_out.xml` | Interpolador |
| `res/anim/slide_in_right.xml` | Duración + fade combinado |
| `res/anim/slide_in_left.xml` | Duración + fade combinado |
| `res/anim/slide_out_left.xml` | Duración + fade combinado |
| `res/anim/slide_out_right.xml` | Duración + fade combinado |
| `ui/common/EntradaAnimator.java` | **Nuevo** |
| `ui/common/ScrollRevealAnimator.java` | Ajuste de constantes e interpolador |
| `ui/admin/MainAdminActivity.java` | Animación de pill nav |
| `ui/docente/MainDocenteActivity.java` | Animación de pill nav |
| `ui/docente/dashboard/DocenteDashboardFragment.java` | Usar EntradaAnimator |
| `ui/docente/grupos/DocenteGrupoDetalleFragment.java` | Usar EntradaAnimator |
| `ui/docente/grupos/DocenteQrGrupoFragment.java` | Usar EntradaAnimator |
| `ui/docente/perfil/DocentePerfilFragment.java` | Usar EntradaAnimator |
| `ui/dashboard/DashboardActivity.java` | Usar EntradaAnimator |

---

## Fuera de alcance

- Transiciones entre Activities (Splash → Login) — usan `android.R.anim` del sistema, aceptable
- Animaciones de Lottie en `RegistroExitosoFragment` — ya están bien
- Módulo Estudiante — no implementado
- Shared Element Transitions — complejidad no justificada para este sprint
