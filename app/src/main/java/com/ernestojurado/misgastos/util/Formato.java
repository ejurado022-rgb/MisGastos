package com.ernestojurado.misgastos.util;

import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utilidades de formato (moneda y fecha) y ajuste de márgenes de la pantalla.
 */
public final class Formato {

    private static final Locale ESPANOL = Locale.forLanguageTag("es-PA");

    private Formato() {
    }

    public static String moneda(double monto) {
        return String.format(Locale.US, "$%,.2f", monto);
    }

    public static String porcentaje(double valor) {
        return String.format(Locale.US, "%.1f%%", valor);
    }

    public static String fecha(long milisegundos) {
        return new SimpleDateFormat("dd MMM yyyy, hh:mm a", ESPANOL).format(new Date(milisegundos));
    }

    public static String fechaCorta(long milisegundos) {
        return new SimpleDateFormat("dd MMM", ESPANOL).format(new Date(milisegundos));
    }

    /**
     * Aplica los márgenes de las barras del sistema (EdgeToEdge):
     * arriba y a los lados en {@code raiz}, abajo en {@code contenido}.
     */
    public static void aplicarInsets(View raiz, View contenido) {
        final int inferiorOriginal = contenido.getPaddingBottom();
        ViewCompat.setOnApplyWindowInsetsListener(raiz, (v, windowInsets) -> {
            Insets barras = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(barras.left, barras.top, barras.right, 0);
            contenido.setPadding(contenido.getPaddingLeft(), contenido.getPaddingTop(),
                    contenido.getPaddingRight(), inferiorOriginal + barras.bottom);
            return windowInsets;
        });
    }
}
