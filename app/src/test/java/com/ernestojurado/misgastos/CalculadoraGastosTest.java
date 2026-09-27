package com.ernestojurado.misgastos;

import static org.junit.Assert.assertEquals;

import com.ernestojurado.misgastos.model.Gasto;
import com.ernestojurado.misgastos.util.CalculadoraGastos;
import com.ernestojurado.misgastos.util.Categorias;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Pruebas unitarias de las operaciones matemáticas de los gastos.
 */
public class CalculadoraGastosTest {

    private static final double DELTA = 0.001;

    private final List<Gasto> gastos = Arrays.asList(
            new Gasto(1, "Almuerzo", 8.50, "Comida", 1),
            new Gasto(2, "Pasaje", 1.50, "Transporte", 2),
            new Gasto(3, "Cena", 20.00, "Comida", 3),
            new Gasto(4, "Cine", 10.00, "Ocio", 4));

    @Test
    public void total_sumaTodosLosMontos() {
        assertEquals(40.00, CalculadoraGastos.total(gastos), DELTA);
    }

    @Test
    public void promedio_divideEntreLaCantidad() {
        assertEquals(10.00, CalculadoraGastos.promedio(gastos), DELTA);
    }

    @Test
    public void promedio_listaVaciaEsCero() {
        assertEquals(0, CalculadoraGastos.promedio(new ArrayList<>()), DELTA);
    }

    @Test
    public void mayor_devuelveElMontoMasAlto() {
        assertEquals(20.00, CalculadoraGastos.mayor(gastos), DELTA);
    }

    @Test
    public void filtrar_porCategoria() {
        List<Gasto> comida = CalculadoraGastos.filtrar(gastos, "Comida");
        assertEquals(2, comida.size());
        assertEquals(28.50, CalculadoraGastos.total(comida), DELTA);
    }

    @Test
    public void filtrar_todasDevuelveLaListaCompleta() {
        assertEquals(4, CalculadoraGastos.filtrar(gastos, Categorias.TODAS).size());
    }

    @Test
    public void porcentaje_calculaSobreElTotal() {
        assertEquals(25.0, CalculadoraGastos.porcentaje(10, 40), DELTA);
    }

    @Test
    public void porcentaje_totalCeroNoDivide() {
        assertEquals(0, CalculadoraGastos.porcentaje(10, 0), DELTA);
    }
}
