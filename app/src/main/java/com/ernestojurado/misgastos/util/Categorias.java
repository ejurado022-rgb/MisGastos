package com.ernestojurado.misgastos.util;

import com.ernestojurado.misgastos.R;

/**
 * Categorías disponibles con su ícono y color.
 */
public final class Categorias {

    public static final String TODAS = "Todas";
    public static final String SELECCIONAR = "Seleccionar categoría";

    public static final String[] NOMBRES = {
            "Comida", "Transporte", "Ocio", "Servicios", "Salud", "Otros"
    };

    private Categorias() {
    }

    /** Opciones para el Spinner de filtro: "Todas" + las categorías. */
    public static String[] conTodas() {
        return agregarAlInicio(TODAS);
    }

    /** Opciones para el Spinner del formulario: "Seleccionar categoría" + las categorías. */
    public static String[] conSeleccionar() {
        return agregarAlInicio(SELECCIONAR);
    }

    private static String[] agregarAlInicio(String primera) {
        String[] opciones = new String[NOMBRES.length + 1];
        opciones[0] = primera;
        System.arraycopy(NOMBRES, 0, opciones, 1, NOMBRES.length);
        return opciones;
    }

    public static int icono(String categoria) {
        switch (categoria) {
            case "Comida":
                return R.drawable.ic_comida;
            case "Transporte":
                return R.drawable.ic_transporte;
            case "Ocio":
                return R.drawable.ic_ocio;
            case "Servicios":
                return R.drawable.ic_servicios;
            case "Salud":
                return R.drawable.ic_salud;
            default:
                return R.drawable.ic_otros;
        }
    }

    public static int color(String categoria) {
        switch (categoria) {
            case "Comida":
                return R.color.cat_comida;
            case "Transporte":
                return R.color.cat_transporte;
            case "Ocio":
                return R.color.cat_ocio;
            case "Servicios":
                return R.color.cat_servicios;
            case "Salud":
                return R.color.cat_salud;
            default:
                return R.color.cat_otros;
        }
    }
}
