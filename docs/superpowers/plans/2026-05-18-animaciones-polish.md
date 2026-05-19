# Animaciones Polish — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Reemplazar todas las animaciones básicas de TecConnect por un sistema Material 3 Estándar cohesivo: curvas FastOutSlowIn, slides con fade combinado, pill nav animado, y una clase `EntradaAnimator` centralizada.

**Architecture:** Los 6 XMLs de animación se actualizan para combinar translate+alpha. Una nueva clase utilitaria `EntradaAnimator` centraliza la lógica de entrada de pantalla que hoy está copy-pasted en 5 fragments. El pill nav de Admin y Docente reemplaza `AutoTransition`+scale instantáneo por `ViewPropertyAnimator` con `OvershootInterpolator`. `ScrollRevealAnimator` ajusta constantes e interpolador.

**Tech Stack:** Java 11, Android SDK min 24, `android.view.animation.PathInterpolator` (API 21+), `android.view.animation.OvershootInterpolator`, recurso XML `@interpolator/fast_out_slow_in` (disponible vía `libs.material`).

---

## Mapa de archivos

| Acción | Archivo |
|---|---|
| Modificar | `app/src/main/res/anim/fade_in.xml` |
| Modificar | `app/src/main/res/anim/fade_out.xml` |
| Modificar | `app/src/main/res/anim/slide_in_right.xml` |
| Modificar | `app/src/main/res/anim/slide_in_left.xml` |
| Modificar | `app/src/main/res/anim/slide_out_left.xml` |
| Modificar | `app/src/main/res/anim/slide_out_right.xml` |
| Crear | `app/src/main/java/com/conectatec/ui/common/EntradaAnimator.java` |
| Modificar | `app/src/main/java/com/conectatec/ui/docente/dashboard/DocenteDashboardFragment.java` |
| Modificar | `app/src/main/java/com/conectatec/ui/docente/grupos/DocenteGrupoDetalleFragment.java` |
| Modificar | `app/src/main/java/com/conectatec/ui/docente/grupos/DocenteQrGrupoFragment.java` |
| Modificar | `app/src/main/java/com/conectatec/ui/docente/perfil/DocentePerfilFragment.java` |
| Modificar | `app/src/main/java/com/conectatec/ui/dashboard/DashboardActivity.java` |
| Modificar | `app/src/main/java/com/conectatec/ui/admin/MainAdminActivity.java` |
| Modificar | `app/src/main/java/com/conectatec/ui/docente/MainDocenteActivity.java` |
| Modificar | `app/src/main/java/com/conectatec/ui/common/ScrollRevealAnimator.java` |

---

## Task 1: Actualizar XMLs de animación

**Files:**
- Modify: `app/src/main/res/anim/fade_in.xml`
- Modify: `app/src/main/res/anim/fade_out.xml`
- Modify: `app/src/main/res/anim/slide_in_right.xml`
- Modify: `app/src/main/res/anim/slide_in_left.xml`
- Modify: `app/src/main/res/anim/slide_out_left.xml`
- Modify: `app/src/main/res/anim/slide_out_right.xml`

- [ ] **Step 1: Reemplazar `fade_in.xml`**

Contenido completo del archivo:
```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <alpha
        android:fromAlpha="0.0"
        android:toAlpha="1.0"
        android:duration="300"
        android:interpolator="@interpolator/fast_out_slow_in" />
</set>
```

- [ ] **Step 2: Reemplazar `fade_out.xml`**

```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <alpha
        android:fromAlpha="1.0"
        android:toAlpha="0.0"
        android:duration="300"
        android:interpolator="@interpolator/fast_out_slow_in" />
</set>
```

- [ ] **Step 3: Reemplazar `slide_in_right.xml`**

El slide baja de 100% a 40% y combina un fade-in. La pantalla entrante aparece suave, no empuja bruscamente:
```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:fromXDelta="40%"
        android:toXDelta="0%"
        android:duration="350"
        android:interpolator="@interpolator/fast_out_slow_in" />
    <alpha
        android:fromAlpha="0.0"
        android:toAlpha="1.0"
        android:duration="280"
        android:interpolator="@interpolator/fast_out_slow_in" />
</set>
```

- [ ] **Step 4: Reemplazar `slide_in_left.xml`**

```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:fromXDelta="-40%"
        android:toXDelta="0%"
        android:duration="350"
        android:interpolator="@interpolator/fast_out_slow_in" />
    <alpha
        android:fromAlpha="0.0"
        android:toAlpha="1.0"
        android:duration="280"
        android:interpolator="@interpolator/fast_out_slow_in" />
</set>
```

- [ ] **Step 5: Reemplazar `slide_out_left.xml`**

La pantalla saliente desaparece más rápido que la entrante aparece — da sensación de profundidad:
```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:fromXDelta="0%"
        android:toXDelta="-40%"
        android:duration="300"
        android:interpolator="@interpolator/fast_out_slow_in" />
    <alpha
        android:fromAlpha="1.0"
        android:toAlpha="0.0"
        android:duration="200"
        android:interpolator="@interpolator/fast_out_slow_in" />
</set>
```

- [ ] **Step 6: Reemplazar `slide_out_right.xml`**

```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:fromXDelta="0%"
        android:toXDelta="40%"
        android:duration="300"
        android:interpolator="@interpolator/fast_out_slow_in" />
    <alpha
        android:fromAlpha="1.0"
        android:toAlpha="0.0"
        android:duration="200"
        android:interpolator="@interpolator/fast_out_slow_in" />
</set>
```

- [ ] **Step 7: Verificar build**

```bash
./gradlew assembleDebug
```
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 8: Commit**

```bash
git add app/src/main/res/anim/
git commit -m "style: slides con fade combinado y curva FastOutSlowIn"
```

---

## Task 2: Crear `EntradaAnimator`

**Files:**
- Create: `app/src/main/java/com/conectatec/ui/common/EntradaAnimator.java`

- [ ] **Step 1: Crear el archivo**

```java
package com.conectatec.ui.common;

import android.view.View;
import android.view.animation.PathInterpolator;

public final class EntradaAnimator {

    // Cubic-bezier(0.4, 0, 0.2, 1) — Material 3 Standard easing
    private static final PathInterpolator INTERPOLATOR =
            new PathInterpolator(0.4f, 0f, 0.2f, 1f);

    static final int    DURATION_MS  = 300;
    static final int    STAGGER_MS   = 60;
    static final float  TRANSLATION_DP = 20f;

    private EntradaAnimator() {}

    /**
     * Anima la aparición de las vistas con fade + slide desde abajo.
     * Establece el estado inicial (alpha=0, translationY=20dp) antes de animar.
     * Llámalo en onViewCreated después de que el binding esté listo.
     */
    public static void animar(View... vistas) {
        if (vistas == null || vistas.length == 0) return;
        float density = vistas[0].getContext().getResources().getDisplayMetrics().density;
        float translY = TRANSLATION_DP * density;

        for (int i = 0; i < vistas.length; i++) {
            View v = vistas[i];
            v.setAlpha(0f);
            v.setTranslationY(translY);
            v.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(DURATION_MS)
                    .setStartDelay((long) i * STAGGER_MS)
                    .setInterpolator(INTERPOLATOR)
                    .start();
        }
    }
}
```

- [ ] **Step 2: Verificar build**

```bash
./gradlew assembleDebug
```
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/conectatec/ui/common/EntradaAnimator.java
git commit -m "feat: clase EntradaAnimator centralizada con Material 3 easing"
```

---

## Task 3: Refactorizar fragments — reemplazar `animarEntrada` duplicado

Los 4 fragments del módulo Docente tienen el mismo patrón copy-pasted. Se reemplaza en todos en este task.

**Files:**
- Modify: `app/src/main/java/com/conectatec/ui/docente/dashboard/DocenteDashboardFragment.java`
- Modify: `app/src/main/java/com/conectatec/ui/docente/grupos/DocenteGrupoDetalleFragment.java`
- Modify: `app/src/main/java/com/conectatec/ui/docente/grupos/DocenteQrGrupoFragment.java`
- Modify: `app/src/main/java/com/conectatec/ui/docente/perfil/DocentePerfilFragment.java`

### DocenteDashboardFragment.java

- [ ] **Step 1: Añadir import de `EntradaAnimator`**

En la sección de imports, agregar:
```java
import com.conectatec.ui.common.EntradaAnimator;
```
Eliminar el import que ya no se usa:
```java
import android.view.animation.DecelerateInterpolator;
```

- [ ] **Step 2: Eliminar los dos métodos y actualizar call sites**

Eliminar el método `prepararAnimacion()` completo (4 líneas de alpha=0) y el método `animarEntrada()` completo.

**Call sites a modificar en `onViewCreated` y `observeViewModel()`:**

1. En `onViewCreated`, línea `prepararAnimacion();` — **eliminarla** (EntradaAnimator pone el estado inicial solo).

2. En `observeViewModel()`, el bloque `UiState.Success` llama `animarEntrada()` — **reemplazar** esa línea:
```java
// Antes:
animarEntrada();
// Después:
EntradaAnimator.animar(
    binding.cardBienvenidaDocente,
    binding.layoutKpisDocente,
    binding.cardTareasRecientesDashboard,
    binding.cardActividadHoyDashboard
);
```

3. En `observeViewModel()`, el bloque `UiState.Error` también llama `animarEntrada()` — **reemplazar** igual:
```java
EntradaAnimator.animar(
    binding.cardBienvenidaDocente,
    binding.layoutKpisDocente,
    binding.cardTareasRecientesDashboard,
    binding.cardActividadHoyDashboard
);
```

### DocenteGrupoDetalleFragment.java

- [ ] **Step 3: Añadir import de `EntradaAnimator`, eliminar `DecelerateInterpolator`**

```java
import com.conectatec.ui.common.EntradaAnimator;
// Eliminar: import android.view.animation.DecelerateInterpolator;
```

- [ ] **Step 4: Eliminar los métodos y actualizar call sites**

Eliminar el método `prepararAnimacion()` completo y el método `animarEntrada()` completo.

**Call sites:**

1. En `onViewCreated`, línea `prepararAnimacion();` — **eliminarla**.

2. En `poblarVistas()`, la última línea llama `animarEntrada()` — **reemplazar**:
```java
EntradaAnimator.animar(
    binding.cardHeroGrupo,
    binding.cardAccionesRapidas,
    binding.cardMiembrosGrupo,
    binding.cardAvisosGrupo
);
```

### DocenteQrGrupoFragment.java

- [ ] **Step 5: Añadir import de `EntradaAnimator`, eliminar `DecelerateInterpolator`**

```java
import com.conectatec.ui.common.EntradaAnimator;
// Eliminar: import android.view.animation.DecelerateInterpolator;
```

- [ ] **Step 6: Eliminar `animarEntrada()` y reemplazar su llamada**

Eliminar el método (no tiene `prepararAnimacion()` separado — el estado inicial se setea dentro):
```java
private void animarEntrada() {
    float translY = 40f * getResources().getDisplayMetrics().density;
    View[] cards = {binding.cardQrDisplay, binding.cardInfoQr};
    for (int i = 0; i < cards.length; i++) {
        View card = cards[i];
        card.setAlpha(0f);
        card.setTranslationY(translY);
        card.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(i * 80L)
                .setDuration(320)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }
}
```

Reemplazar la llamada con:
```java
EntradaAnimator.animar(binding.cardQrDisplay, binding.cardInfoQr);
```

### DocentePerfilFragment.java

- [ ] **Step 7: Añadir import de `EntradaAnimator`, eliminar `DecelerateInterpolator`**

```java
import com.conectatec.ui.common.EntradaAnimator;
// Eliminar: import android.view.animation.DecelerateInterpolator;
```

- [ ] **Step 8: Eliminar los métodos y actualizar call sites**

Eliminar el método `prepararAnimacion()` completo y el método `animarEntrada()` completo.

**Call sites:** En `onViewCreated`, ambas llamadas aparecen consecutivas — eliminar `prepararAnimacion()` y reemplazar `animarEntrada()` con:
```java
EntradaAnimator.animar(
    binding.cardHeroPerfilDocente,
    binding.cardInfoCuentaDocente,
    binding.cardConfigDocente,
    binding.btnCerrarSesionDocente
);
```

- [ ] **Step 9: Verificar build**

```bash
./gradlew assembleDebug
```
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 10: Commit**

```bash
git add app/src/main/java/com/conectatec/ui/docente/dashboard/DocenteDashboardFragment.java \
        app/src/main/java/com/conectatec/ui/docente/grupos/DocenteGrupoDetalleFragment.java \
        app/src/main/java/com/conectatec/ui/docente/grupos/DocenteQrGrupoFragment.java \
        app/src/main/java/com/conectatec/ui/docente/perfil/DocentePerfilFragment.java
git commit -m "refactor: reemplazar animarEntrada duplicado con EntradaAnimator"
```

---

## Task 4: Refactorizar `DashboardActivity.animarKpiCards()`

`DashboardActivity` itera sobre hijos de un `LinearLayout` dinámicamente, por lo que no usa `EntradaAnimator` (que recibe vistas fijas por varargs). Se actualiza directamente con `ViewPropertyAnimator` + `PathInterpolator`.

**Files:**
- Modify: `app/src/main/java/com/conectatec/ui/dashboard/DashboardActivity.java`

- [ ] **Step 1: Añadir imports**

```java
import android.view.animation.PathInterpolator;
```
Eliminar si ya no se usan:
```java
// Eliminar si no se usan en otro lugar del archivo:
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
```

- [ ] **Step 2: Reemplazar el cuerpo de `animarKpiCards()`**

Localizar el método completo:
```java
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
```

Reemplazarlo por:
```java
private void animarKpiCards() {
    float density = getResources().getDisplayMetrics().density;
    float translY = 20f * density;
    PathInterpolator interp = new PathInterpolator(0.4f, 0f, 0.2f, 1f);

    for (int i = 0; i < binding.linearKpis.getChildCount(); i++) {
        View card = binding.linearKpis.getChildAt(i);
        card.setAlpha(0f);
        card.setTranslationY(translY);
        card.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(300)
                .setStartDelay((long) i * 60)
                .setInterpolator(interp)
                .start();
    }
}
```

- [ ] **Step 3: Verificar build**

```bash
./gradlew assembleDebug
```
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/conectatec/ui/dashboard/DashboardActivity.java
git commit -m "refactor: animarKpiCards usa ViewPropertyAnimator con FastOutSlowIn"
```

---

## Task 5: Animación del pill nav — `MainAdminActivity`

**Files:**
- Modify: `app/src/main/java/com/conectatec/ui/admin/MainAdminActivity.java`

- [ ] **Step 1: Actualizar imports**

Eliminar:
```java
import android.transition.AutoTransition;
import android.transition.TransitionManager;
```
Agregar:
```java
import android.view.animation.OvershootInterpolator;
import android.view.animation.PathInterpolator;
```

- [ ] **Step 2: Reemplazar el método `selectItem()` completo**

Localizar:
```java
private void selectItem(int activeIndex) {
    AutoTransition t = new AutoTransition();
    t.setDuration(180);
    TransitionManager.beginDelayedTransition(binding.bottomNavAdmin, t);

    int activeIcon   = ContextCompat.getColor(this, R.color.nav_icon_active);
    int inactiveIcon = ContextCompat.getColor(this, R.color.nav_icon_inactive);

    for (int i = 0; i < items.length; i++) {
        NavItem item = items[i];
        boolean active = (i == activeIndex);

        if (active) {
            item.container.setBackgroundResource(R.drawable.bg_nav_item_active);
            item.circle.setBackgroundResource(R.drawable.bg_icon_circle_active);
            item.circle.setScaleX(0.8f);
            item.circle.setScaleY(0.8f);
            item.label.setVisibility(View.VISIBLE);
            ImageViewCompat.setImageTintList(item.icon, ColorStateList.valueOf(activeIcon));
        } else {
            item.container.setBackground(null);
            item.circle.setBackgroundResource(R.drawable.bg_icon_circle_inactive);
            item.circle.setScaleX(1f);
            item.circle.setScaleY(1f);
            item.label.setVisibility(View.GONE);
            ImageViewCompat.setImageTintList(item.icon, ColorStateList.valueOf(inactiveIcon));
        }
    }
}
```

Reemplazar por:
```java
private void selectItem(int activeIndex) {
    int activeIcon   = ContextCompat.getColor(this, R.color.nav_icon_active);
    int inactiveIcon = ContextCompat.getColor(this, R.color.nav_icon_inactive);

    for (int i = 0; i < items.length; i++) {
        NavItem item = items[i];
        boolean active = (i == activeIndex);

        // Fondo e icono: instantáneos
        if (active) {
            item.container.setBackgroundResource(R.drawable.bg_nav_item_active);
            item.circle.setBackgroundResource(R.drawable.bg_icon_circle_active);
            ImageViewCompat.setImageTintList(item.icon, ColorStateList.valueOf(activeIcon));
        } else {
            item.container.setBackground(null);
            item.circle.setBackgroundResource(R.drawable.bg_icon_circle_inactive);
            ImageViewCompat.setImageTintList(item.icon, ColorStateList.valueOf(inactiveIcon));
        }

        // Scale del círculo con animación
        float targetScale = active ? 0.8f : 1f;
        item.circle.animate()
                .scaleX(targetScale)
                .scaleY(targetScale)
                .setDuration(200)
                .setInterpolator(active
                        ? new OvershootInterpolator(1.5f)
                        : new PathInterpolator(0.4f, 0f, 0.2f, 1f))
                .start();

        // Label: fade in/out
        if (active) {
            item.label.setAlpha(0f);
            item.label.setVisibility(View.VISIBLE);
            item.label.animate()
                    .alpha(1f)
                    .setDuration(150)
                    .setInterpolator(new PathInterpolator(0.4f, 0f, 0.2f, 1f))
                    .start();
        } else {
            item.label.animate()
                    .alpha(0f)
                    .setDuration(100)
                    .withEndAction(() -> item.label.setVisibility(View.GONE))
                    .start();
        }
    }
}
```

- [ ] **Step 3: Verificar build**

```bash
./gradlew assembleDebug
```
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/conectatec/ui/admin/MainAdminActivity.java
git commit -m "feat: animación suave del pill nav en Admin (overshoot + fade)"
```

---

## Task 6: Animación del pill nav — `MainDocenteActivity`

El cambio es idéntico al Task 5.

**Files:**
- Modify: `app/src/main/java/com/conectatec/ui/docente/MainDocenteActivity.java`

- [ ] **Step 1: Actualizar imports**

Eliminar:
```java
import android.transition.AutoTransition;
import android.transition.TransitionManager;
```
Agregar:
```java
import android.view.animation.OvershootInterpolator;
import android.view.animation.PathInterpolator;
```

- [ ] **Step 2: Reemplazar el método `selectItem()` completo**

Localizar:
```java
private void selectItem(int activeIndex) {
    AutoTransition t = new AutoTransition();
    t.setDuration(180);
    TransitionManager.beginDelayedTransition(binding.bottomNavDocente, t);

    int activeIcon   = ContextCompat.getColor(this, R.color.nav_icon_active);
    int inactiveIcon = ContextCompat.getColor(this, R.color.nav_icon_inactive);

    for (int i = 0; i < items.length; i++) {
        NavItem item = items[i];
        boolean active = (i == activeIndex);

        if (active) {
            item.container.setBackgroundResource(R.drawable.bg_nav_item_active);
            item.circle.setBackgroundResource(R.drawable.bg_icon_circle_active);
            item.circle.setScaleX(0.8f);
            item.circle.setScaleY(0.8f);
            item.label.setVisibility(View.VISIBLE);
            ImageViewCompat.setImageTintList(item.icon, ColorStateList.valueOf(activeIcon));
        } else {
            item.container.setBackground(null);
            item.circle.setBackgroundResource(R.drawable.bg_icon_circle_inactive);
            item.circle.setScaleX(1f);
            item.circle.setScaleY(1f);
            item.label.setVisibility(View.GONE);
            ImageViewCompat.setImageTintList(item.icon, ColorStateList.valueOf(inactiveIcon));
        }
    }
}
```

Reemplazar por:
```java
private void selectItem(int activeIndex) {
    int activeIcon   = ContextCompat.getColor(this, R.color.nav_icon_active);
    int inactiveIcon = ContextCompat.getColor(this, R.color.nav_icon_inactive);

    for (int i = 0; i < items.length; i++) {
        NavItem item = items[i];
        boolean active = (i == activeIndex);

        // Fondo e icono: instantáneos
        if (active) {
            item.container.setBackgroundResource(R.drawable.bg_nav_item_active);
            item.circle.setBackgroundResource(R.drawable.bg_icon_circle_active);
            ImageViewCompat.setImageTintList(item.icon, ColorStateList.valueOf(activeIcon));
        } else {
            item.container.setBackground(null);
            item.circle.setBackgroundResource(R.drawable.bg_icon_circle_inactive);
            ImageViewCompat.setImageTintList(item.icon, ColorStateList.valueOf(inactiveIcon));
        }

        // Scale del círculo con animación
        float targetScale = active ? 0.8f : 1f;
        item.circle.animate()
                .scaleX(targetScale)
                .scaleY(targetScale)
                .setDuration(200)
                .setInterpolator(active
                        ? new OvershootInterpolator(1.5f)
                        : new PathInterpolator(0.4f, 0f, 0.2f, 1f))
                .start();

        // Label: fade in/out
        if (active) {
            item.label.setAlpha(0f);
            item.label.setVisibility(View.VISIBLE);
            item.label.animate()
                    .alpha(1f)
                    .setDuration(150)
                    .setInterpolator(new PathInterpolator(0.4f, 0f, 0.2f, 1f))
                    .start();
        } else {
            item.label.animate()
                    .alpha(0f)
                    .setDuration(100)
                    .withEndAction(() -> item.label.setVisibility(View.GONE))
                    .start();
        }
    }
}
```

- [ ] **Step 3: Verificar build**

```bash
./gradlew assembleDebug
```
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/conectatec/ui/docente/MainDocenteActivity.java
git commit -m "feat: animación suave del pill nav en Docente (overshoot + fade)"
```

---

## Task 7: Ajustar `ScrollRevealAnimator`

**Files:**
- Modify: `app/src/main/java/com/conectatec/ui/common/ScrollRevealAnimator.java`

- [ ] **Step 1: Actualizar imports**

Agregar:
```java
import android.view.animation.PathInterpolator;
```
Eliminar:
```java
import android.view.animation.DecelerateInterpolator;
```

- [ ] **Step 2: Actualizar constantes y campo interpolador**

Localizar la sección de constantes y el campo del interpolador:
```java
private static final float MIN_ALPHA  = 0.3f;
private static final float MIN_SCALE  = 0.88f;
/** Zona (en dp) desde el borde superior/inferior donde empieza el fade. */
private static final int   EDGE_DP    = 120;
private static final long  ANIM_MS    = 260;
private static final long  STAGGER_MS = 55;

private final RecyclerView rv;
private final float        edgePx;
private final DecelerateInterpolator interpolator = new DecelerateInterpolator(1.6f);
```

Reemplazar por:
```java
private static final float MIN_ALPHA  = 0.0f;
private static final float MIN_SCALE  = 0.92f;
/** Zona (en dp) desde el borde superior/inferior donde empieza el fade. */
private static final int   EDGE_DP    = 120;
private static final long  ANIM_MS    = 300;
private static final long  STAGGER_MS = 50;

private final RecyclerView rv;
private final float        edgePx;
private final PathInterpolator interpolator = new PathInterpolator(0.4f, 0f, 0.2f, 1f);
```

- [ ] **Step 3: Actualizar `translationY` en `triggerInicial()`**

Localizar en el método `triggerInicial()`:
```java
child.setTranslationY(16f);
```
Reemplazar por:
```java
child.setTranslationY(24f);
```

- [ ] **Step 4: Verificar build**

```bash
./gradlew assembleDebug
```
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/conectatec/ui/common/ScrollRevealAnimator.java
git commit -m "style: ScrollRevealAnimator con valores y curva Material 3"
```
