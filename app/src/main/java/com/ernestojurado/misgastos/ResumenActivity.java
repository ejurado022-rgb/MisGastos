package com.ernestojurado.misgastos;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;

import com.ernestojurado.misgastos.data.GastoRepository;
import com.ernestojurado.misgastos.model.Gasto;
import com.ernestojurado.misgastos.util.CalculadoraGastos;
import com.ernestojurado.misgastos.util.Categorias;
import com.ernestojurado.misgastos.util.Formato;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.List;

/**
 * Resumen con operaciones matemáticas: total, promedio, gasto mayor y porcentaje por categoría.
 */
public class ResumenActivity extends AppCompatActivity {

    private List<Gasto> todos;
    private TextView tvTotal;
    private TextView tvPromedio;
    private TextView tvMayor;
    private TextView tvPorcentaje;
    private TextView tvCantidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this, SystemBarStyle.dark(Color.TRANSPARENT));
        setContentView(R.layout.activity_resumen);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        Formato.aplicarInsets(findViewById(R.id.raiz), findViewById(R.id.contenido));

        todos = new GastoRepository(this).listar();

        tvTotal = findViewById(R.id.tvTotal);
        tvPromedio = findViewById(R.id.tvPromedio);
        tvMayor = findViewById(R.id.tvMayor);
        tvPorcentaje = findViewById(R.id.tvPorcentaje);
        tvCantidad = findViewById(R.id.tvCantidad);

        Spinner spCategoria = findViewById(R.id.spCategoria);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.item_spinner, Categorias.conTodas());
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spCategoria.setAdapter(adapter);
        spCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calcular((String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mostrarBarras();
    }

    private void calcular(String categoria) {
        List<Gasto> filtrados = CalculadoraGastos.filtrar(todos, categoria);
        double total = CalculadoraGastos.total(filtrados);

        tvTotal.setText(Formato.moneda(total));
        tvPromedio.setText(Formato.moneda(CalculadoraGastos.promedio(filtrados)));
        tvMayor.setText(Formato.moneda(CalculadoraGastos.mayor(filtrados)));
        tvPorcentaje.setText(Formato.porcentaje(
                CalculadoraGastos.porcentaje(total, CalculadoraGastos.total(todos))));
        tvCantidad.setText(String.valueOf(filtrados.size()));
    }

    // Una barra de progreso por categoría con su porcentaje del total
    private void mostrarBarras() {
        LinearLayout contenedor = findViewById(R.id.contenedorBarras);
        LayoutInflater inflater = LayoutInflater.from(this);
        double totalGeneral = CalculadoraGastos.total(todos);

        for (String categoria : Categorias.NOMBRES) {
            double totalCategoria = CalculadoraGastos.total(CalculadoraGastos.filtrar(todos, categoria));
            double porcentaje = CalculadoraGastos.porcentaje(totalCategoria, totalGeneral);

            View fila = inflater.inflate(R.layout.item_barra_categoria, contenedor, false);
            TextView tvNombre = fila.findViewById(R.id.tvNombre);
            TextView tvValor = fila.findViewById(R.id.tvValor);
            LinearProgressIndicator barra = fila.findViewById(R.id.barra);

            int color = ContextCompat.getColor(this, Categorias.color(categoria));
            tvNombre.setText(categoria);
            tvValor.setText(getString(R.string.barra_valor,
                    Formato.moneda(totalCategoria), Formato.porcentaje(porcentaje)));
            barra.setIndicatorColor(color);
            barra.setTrackColor(ColorUtils.setAlphaComponent(color, 50));
            barra.setProgress((int) Math.round(porcentaje));

            contenedor.addView(fila);
        }
    }
}
