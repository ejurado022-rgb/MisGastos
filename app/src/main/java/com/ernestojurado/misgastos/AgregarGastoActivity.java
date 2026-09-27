package com.ernestojurado.misgastos;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.ernestojurado.misgastos.data.GastoRepository;
import com.ernestojurado.misgastos.model.Gasto;
import com.ernestojurado.misgastos.util.Categorias;
import com.ernestojurado.misgastos.util.Formato;
import com.google.android.material.appbar.MaterialToolbar;

/**
 * Formulario para registrar un gasto nuevo.
 */
public class AgregarGastoActivity extends AppCompatActivity {

    private EditText etDescripcion;
    private EditText etMonto;
    private Spinner spCategoria;
    private Button btnGuardar;
    private FrameLayout layoutGuardado;
    private LottieAnimationView lottieGuardado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_gasto);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        etDescripcion = findViewById(R.id.etDescripcion);
        etMonto = findViewById(R.id.etMonto);
        spCategoria = findViewById(R.id.spCategoria);
        btnGuardar = findViewById(R.id.btnGuardar);
        layoutGuardado = findViewById(R.id.layoutGuardado);
        lottieGuardado = findViewById(R.id.lottieGuardado);

        Formato.aplicarInsets(findViewById(R.id.raiz), findViewById(R.id.contenido));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.item_spinner, Categorias.conSeleccionar());
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spCategoria.setAdapter(adapter);
    }

    // Llamado desde el XML con android:onClick="guardar"
    public void guardar(View view) {
        String descripcion = etDescripcion.getText().toString().trim();
        String textoMonto = etMonto.getText().toString().trim().replace(",", ".");

        if (descripcion.isEmpty()) {
            etDescripcion.setError(getString(R.string.error_descripcion));
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(textoMonto);
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.error_monto_invalido, Toast.LENGTH_SHORT).show();
            return;
        }
        if (monto <= 0) {
            Toast.makeText(this, R.string.error_monto_cero, Toast.LENGTH_SHORT).show();
            return;
        }

        if (spCategoria.getSelectedItemPosition() == 0) {
            Toast.makeText(this, R.string.error_categoria, Toast.LENGTH_SHORT).show();
            return;
        }
        String categoria = (String) spCategoria.getSelectedItem();

        long ahora = System.currentTimeMillis();
        new GastoRepository(this).agregar(new Gasto(ahora, descripcion, monto, categoria, ahora));

        mostrarAnimacionGuardado();
    }

    // Llamado desde el XML con android:onClick="limpiar"
    public void limpiar(View view) {
        etDescripcion.setText("");
        etMonto.setText("");
        spCategoria.setSelection(0);
        etDescripcion.requestFocus();
    }

    private void mostrarAnimacionGuardado() {
        btnGuardar.setEnabled(false);
        layoutGuardado.setVisibility(View.VISIBLE);
        lottieGuardado.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setResult(RESULT_OK);
                finish();
            }
        });
        lottieGuardado.playAnimation();
    }
}
