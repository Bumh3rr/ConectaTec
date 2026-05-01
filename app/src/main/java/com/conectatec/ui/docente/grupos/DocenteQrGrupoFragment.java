package com.conectatec.ui.docente.grupos;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.conectatec.databinding.FragmentDocenteQrGrupoBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.OutputStream;
import java.util.EnumMap;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DocenteQrGrupoFragment extends Fragment {

    private FragmentDocenteQrGrupoBinding binding;
    private Bitmap qrBitmap;
    private String codigoUnion = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDocenteQrGrupoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        codigoUnion  = args != null ? args.getString("codigoUnion", "")  : "";
        String nombre  = args != null ? args.getString("nombreGrupo", "")  : "";
        String materia = args != null ? args.getString("materiaGrupo", "") : "";
        int alumnos    = args != null ? args.getInt("totalAlumnos", 0)     : 0;
        String fecha   = args != null ? args.getString("fechaCreacion", "") : "";

        poblarVistas(nombre, materia, alumnos, fecha);
        generarYMostrarQr(codigoUnion);
        animarEntrada();

        binding.btnBackQrGrupo.setOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());

        binding.btnDescargarQr.setOnClickListener(v -> guardarQrEnGaleria());
    }

    private void poblarVistas(String nombre, String materia, int alumnos, String fecha) {
        binding.tvNombreQrCard.setText(nombre);
        binding.tvMateriaQrCard.setText(materia);
        binding.tvInfoNombreQr.setText(nombre);
        binding.tvInfoMateriaQr.setText(materia);
        binding.tvInfoAlumnosQr.setText(alumnos + " alumnos");
        binding.tvInfoFechaQr.setText(fecha);
    }

    private void generarYMostrarQr(String contenido) {
        if (contenido.isEmpty()) return;
        try {
            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.MARGIN, 1);
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(contenido, BarcodeFormat.QR_CODE, 512, 512, hints);

            int w = matrix.getWidth();
            int h = matrix.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    pixels[y * w + x] = matrix.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }
            qrBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            qrBitmap.setPixels(pixels, 0, w, 0, 0, w, h);
            binding.ivQrCode.setImageBitmap(qrBitmap);
            binding.tvCodigoQrCard.setText(contenido);
        } catch (WriterException e) {
            Snackbar.make(binding.getRoot(), "No se pudo generar el QR", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void guardarQrEnGaleria() {
        if (qrBitmap == null) {
            Snackbar.make(binding.getRoot(), "QR no disponible", Snackbar.LENGTH_SHORT).show();
            return;
        }
        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME,
                    "QR_ConectaTec_" + codigoUnion + ".png");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.put(MediaStore.Images.Media.RELATIVE_PATH,
                        Environment.DIRECTORY_PICTURES + "/ConectaTec");
            }
            Uri uri = requireActivity().getContentResolver()
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (uri != null) {
                try (OutputStream os = requireActivity().getContentResolver().openOutputStream(uri)) {
                    if (os != null && qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                        Snackbar.make(binding.getRoot(),
                                "QR guardado en Galería", Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
            Snackbar.make(binding.getRoot(),
                    "No se pudo guardar el QR", Snackbar.LENGTH_SHORT).show();
        } catch (Exception e) {
            Snackbar.make(binding.getRoot(),
                    "Error al guardar el QR", Snackbar.LENGTH_SHORT).show();
        }
    }

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
