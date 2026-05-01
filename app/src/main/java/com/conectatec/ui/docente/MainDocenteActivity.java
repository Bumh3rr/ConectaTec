package com.conectatec.ui.docente;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.conectatec.R;
import com.conectatec.databinding.ActivityMainDocenteBinding;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity principal del rol Docente.
 * Maneja el NavController y el pill bottom nav custom (sin Toolbar).
 * TODO: quitar el intent-filter del manifest cuando el login enrute por rol.
 */
@AndroidEntryPoint
public class MainDocenteActivity extends AppCompatActivity {

    private ActivityMainDocenteBinding binding;
    private NavController navController;
    private NavItem[] items;

    /** Destinos del nav graph en el mismo orden que el array items. */
    private static final int[] DESTINATIONS = {
            R.id.docenteDashboardFragment,
            R.id.docenteGruposFragment,
            R.id.docenteTareasFragment,
            R.id.docenteChatFragment,
            R.id.docentePerfilFragment
    };

    private static class NavItem {
        final LinearLayout container;
        final FrameLayout circle;
        final ImageView icon;
        final TextView label;

        NavItem(LinearLayout c, FrameLayout fc, ImageView i, TextView l) {
            container = c; circle = fc; icon = i; label = l;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainDocenteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navHostDocente);
        if (navHost == null) return;

        navController = navHost.getNavController();
        setupPillNav();
    }

    private void setupPillNav() {
        items = new NavItem[]{
                new NavItem(binding.itemDashboard, binding.circleDashboard,
                        binding.iconDashboard, binding.labelDashboard),
                new NavItem(binding.itemGrupos,    binding.circleGrupos,
                        binding.iconGrupos,    binding.labelGrupos),
                new NavItem(binding.itemTareas,    binding.circleTareas,
                        binding.iconTareas,    binding.labelTareas),
                new NavItem(binding.itemChat,      binding.circleChat,
                        binding.iconChat,      binding.labelChat),
                new NavItem(binding.itemPerfil,    binding.circlePerfil,
                        binding.iconPerfil,    binding.labelPerfil)
        };

        for (int i = 0; i < items.length; i++) {
            final int destId = DESTINATIONS[i];
            items[i].container.setOnClickListener(v -> {
                if (navController.getCurrentDestination() != null
                        && navController.getCurrentDestination().getId() == destId) {
                    return;
                }

                NavOptions options = new NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .setRestoreState(true)
                        .setPopUpTo(
                                navController.getGraph().getStartDestinationId(),
                                false,
                                true
                        )
                        .build();

                navController.navigate(destId, null, options);
            });
        }

        navController.addOnDestinationChangedListener((controller, dest, args) -> {
            int id = dest.getId();

            // Pill nav solo en los 5 destinos raíz
            boolean esRaiz = (id == R.id.docenteDashboardFragment
                    || id == R.id.docenteGruposFragment
                    || id == R.id.docenteTareasFragment
                    || id == R.id.docenteChatFragment
                    || id == R.id.docentePerfilFragment);
            binding.bottomNavDocente.setVisibility(esRaiz ? View.VISIBLE : View.GONE);

            for (int i = 0; i < DESTINATIONS.length; i++) {
                if (DESTINATIONS[i] == id) {
                    selectItem(i);
                    return;
                }
            }

            // Sub-destinos: resaltar la pestaña padre
            if (id == R.id.docenteCrearGrupoFragment
                    || id == R.id.docenteGrupoDetalleFragment
                    || id == R.id.docenteMiembrosGrupoFragment
                    || id == R.id.docenteAlumnoPerfilFragment
                    || id == R.id.docenteQrGrupoFragment) {
                selectItem(1); // Grupos
            } else if (id == R.id.docenteBloquesFragment
                    || id == R.id.docenteCrearBloqueFragment
                    || id == R.id.docenteTareasBloqueFragment
                    || id == R.id.docenteCrearTareaFragment
                    || id == R.id.docenteEntregasFragment
                    || id == R.id.docenteCalificarEntregaFragment) {
                selectItem(2); // Tareas
            } else if (id == R.id.docenteConversacionFragment) {
                selectItem(3); // Chat
            }
        });
    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
