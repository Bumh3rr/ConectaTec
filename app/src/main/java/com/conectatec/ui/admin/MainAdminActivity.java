package com.conectatec.ui.admin;

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
import com.conectatec.databinding.ActivityMainAdminBinding;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity principal del rol Administrador.
 * Maneja el NavController y el pill bottom nav custom (sin Toolbar).
 * TODO: quitar el intent-filter del manifest cuando el login enrute por rol.
 */
@AndroidEntryPoint
public class MainAdminActivity extends AppCompatActivity {

    private ActivityMainAdminBinding binding;
    private NavController navController;
    private NavItem[] items;

    /** Destinos del nav graph en el mismo orden que el array items. */
    private static final int[] DESTINATIONS = {
            R.id.adminDashboardFragment,
            R.id.adminUsuariosFragment,
            R.id.adminGruposFragment,
            R.id.adminActividadesFragment,
            R.id.adminPerfilFragment
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
        binding = ActivityMainAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navHostAdmin);
        if (navHost == null) return;

        navController = navHost.getNavController();
        setupPillNav();
    }

    private void setupPillNav() {
        items = new NavItem[]{
                new NavItem(binding.itemDashboard,    binding.circleDashboard,
                        binding.iconDashboard,    binding.labelDashboard),
                new NavItem(binding.itemUsuarios,     binding.circleUsuarios,
                        binding.iconUsuarios,     binding.labelUsuarios),
                new NavItem(binding.itemGrupos,       binding.circleGrupos,
                        binding.iconGrupos,       binding.labelGrupos),
                new NavItem(binding.itemActividades,  binding.circleActividades,
                        binding.iconActividades,  binding.labelActividades),
                new NavItem(binding.itemPerfil,       binding.circlePerfil,
                        binding.iconPerfil,       binding.labelPerfil)
        };

        // Click → navegar al destino correspondiente
        for (int i = 0; i < items.length; i++) {
            final int destId = DESTINATIONS[i];
            items[i].container.setOnClickListener(v -> {
                if (navController.getCurrentDestination() != null
                        && navController.getCurrentDestination().getId() == destId) {
                    return; // ya estás en esa pestaña, no hagas nada
                }

                NavOptions options = new NavOptions.Builder()
                        .setLaunchSingleTop(true)        // no apilar copias del mismo destino
                        .setRestoreState(true)           // restaura el estado guardado de la pestaña
                        .setPopUpTo(
                                navController.getGraph().getStartDestinationId(),
                                false,                   // no incluir el start destination en el pop
                                true                     // saveState: guardar estado del back stack
                        )
                        .build();

                navController.navigate(destId, null, options);
            });
        }

        // Cambio de destino → actualizar UI del pill
        // El listener se dispara también con el destino inicial, así que
        // no hace falta seleccionar manualmente la pestaña al arranque.
        navController.addOnDestinationChangedListener((controller, dest, args) -> {
            int id = dest.getId();

            // Intentar encontrar el ítem raíz exacto
            for (int i = 0; i < DESTINATIONS.length; i++) {
                if (DESTINATIONS[i] == id) {
                    selectItem(i);
                    return;
                }
            }

            // Para sub-destinos (detalles), resaltar la pestaña padre
            if (id == R.id.adminUsuarioDetalleFragment) {
                selectItem(1); // índice Usuarios
            } else if (id == R.id.adminGrupoDetalleFragment) {
                selectItem(2); // índice Grupos
            } else if (id == R.id.adminActividadDetalleFragment) {
                selectItem(3); // índice Actividades
            }
            // adminMiembrosGrupoFragment: no cambia la pestaña activa
            // El pill siempre permanece visible
        });
    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}