package com.ernestojurado.misgastos.util;

import com.ernestojurado.misgastos.model.Gasto;

import java.util.ArrayList;
import java.util.List;

/**
 * Operaciones matemáticas sobre la lista de gastos (suma, promedio, mayor y porcentaje).
 */
public final class CalculadoraGastos {

    private CalculadoraGastos() {
    }

    public static List<Gasto> filtrar(List<Gasto> gastos, String categoria) {
        if (categoria == null || categoria.equals(Categorias.TODAS)) {
            return new ArrayList<>(gastos);
        }
        List<Gasto> resultado = new ArrayList<>();
        for (Gasto gasto : gastos) {
            if (gasto.getCategoria().equals(categoria)) {
                resultado.add(gasto);
            }
        }
        return resultado;
    }

    public static double total(List<Gasto> gastos) {
        double suma = 0;
        for (Gasto gasto : gastos) {
            suma += gasto.getMonto();
        }
        return suma;
    }

    public static double promedio(List<Gasto> gastos) {
        if (gastos.isEmpty()) {
            return 0;
        }
        return total(gastos) / gastos.size();
    }

    public static double mayor(List<Gasto> gastos) {
        double mayor = 0;
        for (Gasto gasto : gastos) {
            if (gasto.getMonto() > mayor) {
                mayor = gasto.getMonto();
            }
        }
        return mayor;
    }

    /** Porcentaje que representa {@code parte} dentro de {@code total}. Evita dividir entre cero. */
    public static double porcentaje(double parte, double total) {
        if (total == 0) {
            return 0;
        }
        return parte * 100 / total;
    }
}
