package com.ernestojurado.misgastos;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.ernestojurado.misgastos.data.GastoRepository;
import com.ernestojurado.misgastos.util.Categorias;
import com.ernestojurado.misgastos.util.Formato;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/**
 * Muestra el detalle de un gasto recibido por Intent y permite eliminarlo.
 */
public class DetalleGastoActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_DESCRIPCION = "extra_descripcion";
    public static final String EXTRA_MONTO = "extra_monto";
    public static final String EXTRA_CATEGORIA = "extra_categoria";
    public static final String EXTRA_FECHA = "extra_fecha";

    private long idGasto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle_gasto);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        Formato.aplicarInsets(findViewById(R.id.raiz), findViewById(R.id.contenido));

        // Datos recibidos desde MainActivity
        Intent intent = getIntent();
        idGasto = intent.getLongExtra(EXTRA_ID, -1);
        String descripcion = intent.getStringExtra(EXTRA_DESCRIPCION);
        double monto = intent.getDoubleExtra(EXTRA_MONTO, 0);
        String categoria = intent.getStringExtra(EXTRA_CATEGORIA);
        long fecha = intent.getLongExtra(EXTRA_FECHA, 0);
        if (categoria == null) {
            categoria = Categorias.NOMBRES[Categorias.NOMBRES.length - 1];
        }

        ImageView ivIcono = findViewById(R.id.ivIcono);
        TextView tvMonto = findViewById(R.id.tvMonto);
        TextView tvDescripcion = findViewById(R.id.tvDescripcion);
        TextView tvCategoria = findViewById(R.id.tvCategoria);
        TextView tvFecha = findViewById(R.id.tvFecha);
        Button btnEliminar = findViewById(R.id.btnEliminar);

        int color = ContextCompat.getColor(this, Categorias.color(categoria));
        ivIcono.setImageResource(Categorias.icono(categoria));
        ivIcono.setBackgroundTintList(ColorStateList.valueOf(color));
        tvMonto.setText(Formato.moneda(monto));
        tvDescripcion.setText(descripcion);
        tvCategoria.setText(categoria);
        tvFecha.setText(Formato.fecha(fecha));

        btnEliminar.setOnClickListener(v -> confirmarEliminar());
    }

    private void confirmarEliminar() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.eliminar_titulo)
                .setMessage(R.string.eliminar_mensaje)
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.eliminar, (dialog, which) -> {
                    new GastoRepository(this).eliminar(idGasto);
                    setResult(RESULT_OK);
                    finish();
                })
                .show();
    }
}
