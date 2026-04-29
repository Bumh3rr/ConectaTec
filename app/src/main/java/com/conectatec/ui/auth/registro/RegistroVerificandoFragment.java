package com.conectatec.ui.auth.registro;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.conectatec.R;
import com.conectatec.databinding.FragmentRegistroVerificandoBinding;

import kotlinx.coroutines.Delay;

/**
 * Paso 3 del flujo de registro.
 * Muestra el progreso mientras el backend procesa el registro,
 * sube la foto a Firebase Storage y llama a Google Cloud Vision.
 * Navega a exitoso o fallido según la respuesta.
 */
public class RegistroVerificandoFragment extends Fragment {
    private FragmentRegistroVerificandoBinding binding;
    private RegistroViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRegistroVerificandoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(RegistroViewModel.class);

        // Un pequeño delay para que el usuario vea los spinners en acción antes de navegar.
        new Handler().postDelayed(this::iniciarRegistro, 1000);
    }

    private void iniciarRegistro() {
        // TODO: cuando el ViewModel tenga LiveData conectado al backend,
        // observar el resultado aquí y navegar según corresponda.

        // Ejemplo de cómo se verá cuando esté conectado:
        // viewModel.registroResult.observe(getViewLifecycleOwner(), result -> {
        //     if (result.isExitoso()) {
        //         marcarPasoCompletado(1);
        //         marcarPasoCompletado(2);
        //         marcarPasoCompletado(3);
        //         Navigation.findNavController(requireView())
        //             .navigate(R.id.action_verificando_to_exitoso);
        //     } else {
        //         Navigation.findNavController(requireView())
        //             .navigate(R.id.action_verificando_to_fallido);
        //     }
        // });

        // viewModel.registro(
        //     viewModel.getNombre(),
        //     viewModel.getCorreo(),
        //     viewModel.getContrasena(),
        //     viewModel.getFotoUri()
        // );

        Navigation.findNavController(requireView())
                .navigate(R.id.action_verificando_to_exitoso);
    }

    /**
     * Marca un paso como completado — cambia el spinner por un check verde.
     *
     * @param paso 1, 2 o 3
     */
    private void marcarPasoCompletado(int paso) {
        switch (paso) {
            case 1:
                binding.progressPaso1.setVisibility(View.GONE);
                binding.tvPaso1.setText("✓  Datos guardados");
                binding.tvPaso1.setTextColor(requireContext().getColor(R.color.colorSuccess));
                break;
            case 2:
                binding.progressPaso2.setVisibility(View.GONE);
                binding.tvPaso2.setText("✓  Foto subida");
                binding.tvPaso2.setTextColor(requireContext().getColor(R.color.colorSuccess));
                break;
            case 3:
                binding.progressPaso3.setVisibility(View.GONE);
                binding.tvPaso3.setText("✓  Foto verificada");
                binding.tvPaso3.setTextColor(requireContext().getColor(R.color.colorSuccess));
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}