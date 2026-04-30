package com.conectatec.ui.admin.usuarios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.conectatec.R;
import com.conectatec.databinding.FragmentAdminUsuarioDetalleBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

/**
 * Detalle de un usuario: foto circular, info de cuenta, grupos donde participa,
 * y botones para cambiar rol, activar/desactivar y eliminar.
 * Datos hardcodeados para validación visual.
 */
public class AdminUsuarioDetalleFragment extends Fragment {

    private FragmentAdminUsuarioDetalleBinding binding;

    // Datos dummy del usuario mostrado
    private static final String NOMBRE_DUMMY = "Carlos Bautista";
    private static final String CORREO_DUMMY = "carlos.bautista@tec.mx";
    private static final String ROL_DUMMY    = "DOCENTE";
    private boolean cuentaActiva = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminUsuarioDetalleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cargarDatosDummy();
        setupListeners();
    }

    private void cargarDatosDummy() {
        // Avatar con iniciales
        binding.ivAvatarDetalle.setVisibility(View.GONE);
        binding.tvInicialesDetalle.setVisibility(View.VISIBLE);
        binding.tvInicialesDetalle.setText("CB");

        binding.tvNombreDetalle.setText(NOMBRE_DUMMY);
        binding.tvCorreoDetalle.setText(CORREO_DUMMY);

        // Chip de rol con color
        binding.tvChipRolDetalle.setText(ROL_DUMMY);
        binding.tvChipRolDetalle.setBackgroundResource(R.drawable.bg_chip_docente);

        // Info de cuenta
        binding.tvFechaRegistro.setText("12/03/2025");
        binding.tvEstadoCuenta.setText(cuentaActiva ? "Activa" : "Desactivada");
        binding.tvEstadoCuenta.setTextColor(requireContext().getColor(
                cuentaActiva ? R.color.colorSuccess : R.color.colorError));
        binding.tvFotoVerificada.setText("Sí");

        // Grupos dummy
        cargarGruposDummy();
    }

    private void cargarGruposDummy() {
        binding.containerGrupos.removeAllViews();
        String[] grupos = { "Ingeniería de Software – 6A", "Bases de Datos – 4B" };
        for (String g : grupos) {
            TextView tv = new TextView(requireContext());
            tv.setText(g);
            tv.setTextColor(requireContext().getColor(R.color.colorOnSurfaceVariant));
            tv.setTextSize(13f);
            tv.setPadding(0, 0, 0, 8);
            binding.containerGrupos.addView(tv);
        }
        binding.tvSinGrupos.setVisibility(View.GONE);
    }

    private void setupListeners() {
        binding.btnVolver.setOnClickListener(v -> requireActivity().onBackPressed());

        binding.btnCambiarRol.setOnClickListener(v -> {
            AsignarRolBottomSheet sheet = AsignarRolBottomSheet.newInstance(ROL_DUMMY);
            sheet.setListener(rol -> Snackbar.make(binding.getRoot(),
                    "Rol cambiado a: " + rol, Snackbar.LENGTH_SHORT).show());
            sheet.show(getChildFragmentManager(), AsignarRolBottomSheet.TAG);
        });

        binding.btnToggleActivo.setOnClickListener(v -> {
            cuentaActiva = !cuentaActiva;
            sincronizarBotonToggle();
            Snackbar.make(binding.getRoot(),
                    cuentaActiva ? "Cuenta activada" : "Cuenta desactivada",
                    Snackbar.LENGTH_SHORT).show();
        });

        binding.btnEliminarUsuario.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.msg_confirmar_titulo)
                    .setMessage(R.string.msg_confirmar_eliminar_usuario)
                    .setNegativeButton(R.string.btn_cancelar_dialog, null)
                    .setPositiveButton(R.string.btn_eliminar_usuario, (d, w) ->
                            Snackbar.make(binding.getRoot(), "Usuario eliminado",
                                    Snackbar.LENGTH_SHORT).show())
                    .show();
        });

        binding.tvVerTodosGrupos.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("docenteNombre", NOMBRE_DUMMY);
            androidx.navigation.NavController nav =
                    Navigation.findNavController(requireView());
            NavOptions opts = new NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setRestoreState(true)
                    .setPopUpTo(nav.getGraph().getStartDestinationId(), false, true)
                    .build();
            nav.navigate(R.id.adminGruposFragment, args, opts);
        });
    }

    private void sincronizarBotonToggle() {
        binding.btnToggleActivo.setText(cuentaActiva
                ? getString(R.string.btn_desactivar_cuenta)
                : getString(R.string.btn_activar_cuenta));
        int color = cuentaActiva
                ? requireContext().getColor(R.color.colorWarning)
                : requireContext().getColor(R.color.colorSuccess);
        binding.btnToggleActivo.setTextColor(color);
        binding.btnToggleActivo.setStrokeColor(
                android.content.res.ColorStateList.valueOf(color));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
