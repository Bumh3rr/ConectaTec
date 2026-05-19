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
     * Precondición: todas las vistas deben tener contexto no nulo (getContext() != null).
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
