package com.conectatec.ui.common;

import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Aplica un efecto scroll-driven de fade + scale a los ítems visibles de un RecyclerView.
 *
 * Uso:
 *   ScrollRevealAnimator animator = new ScrollRevealAnimator(recyclerView);
 *   animator.triggerInicial();          // primer render o tras cambio de datos
 */
public class ScrollRevealAnimator {

    private static final float MIN_ALPHA  = 0.3f;
    private static final float MIN_SCALE  = 0.88f;
    /** Zona (en dp) desde el borde superior/inferior donde empieza el fade. */
    private static final int   EDGE_DP    = 120;
    private static final long  ANIM_MS    = 260;
    private static final long  STAGGER_MS = 55;

    private final RecyclerView rv;
    private final float        edgePx;
    private final DecelerateInterpolator interpolator = new DecelerateInterpolator(1.6f);

    public ScrollRevealAnimator(@NonNull RecyclerView recyclerView) {
        this.rv     = recyclerView;
        this.edgePx = EDGE_DP * recyclerView.getContext()
                .getResources().getDisplayMetrics().density;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
                // Asignación directa durante el scroll: sin animación para máxima fluidez.
                aplicarInstante();
            }
        });
    }

    /**
     * Anima la aparición de los ítems visibles con fade-in escalonado.
     * Llámalo tras cargar o filtrar datos (usa rv.post para esperar el layout).
     */
    public void triggerInicial() {
        rv.post(() -> {
            for (int i = 0; i < rv.getChildCount(); i++) {
                View child = rv.getChildAt(i);
                float tAlpha = calcAlpha(child);
                float tScale = calcScale(child);

                // Estado de partida ligeramente por debajo del destino
                child.setAlpha(tAlpha * 0.4f);
                child.setScaleX(MIN_SCALE);
                child.setScaleY(MIN_SCALE);
                child.setTranslationY(16f);

                child.animate()
                        .alpha(tAlpha)
                        .scaleX(tScale)
                        .scaleY(tScale)
                        .translationY(0f)
                        .setDuration(ANIM_MS)
                        .setStartDelay(i * STAGGER_MS)
                        .setInterpolator(interpolator)
                        .start();
            }
        });
    }

    // ── privados ──────────────────────────────────────────────────────────

    /** Aplica las propiedades calculadas sin animación (para uso durante el scroll). */
    private void aplicarInstante() {
        for (int i = 0; i < rv.getChildCount(); i++) {
            View child = rv.getChildAt(i);
            float a = calcAlpha(child);
            float s = calcScale(child);
            child.setAlpha(a);
            child.setScaleX(s);
            child.setScaleY(s);
        }
    }

    /**
     * Ratio 0..1 basado solo en la distancia al borde inferior.
     * Ítems completamente visibles (arriba o al centro) siempre retornan 1.
     */
    private float ratio(View child) {
        int rvH = rv.getHeight();
        if (rvH == 0) return 1f;
        float center      = (child.getTop() + child.getBottom()) / 2f;
        float distFromBot = rvH - center;
        return Math.min(1f, Math.max(0f, distFromBot / edgePx));
    }

    private float calcAlpha(View child) {
        return MIN_ALPHA + ratio(child) * (1f - MIN_ALPHA);
    }

    private float calcScale(View child) {
        return MIN_SCALE + ratio(child) * (1f - MIN_SCALE);
    }
}
