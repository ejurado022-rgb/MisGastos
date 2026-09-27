package com.ernestojurado.misgastos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.ernestojurado.misgastos.data.GastoRepository;
import com.ernestojurado.misgastos.model.Gasto;
import com.ernestojurado.misgastos.ui.GastoAdapter;
import com.ernestojurado.misgastos.util.CalculadoraGastos;
import com.ernestojurado.misgastos.util.Categorias;
import com.ernestojurado.misgastos.util.Formato;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/**
 * Pantalla principal: total gastado (MotionLayout), filtro por categoría (Spinner)
 * y la lista de gastos (RecyclerView).
 */
public class MainActivity extends AppCompatActivity {

    private GastoRepository repositorio;
    private GastoAdapter adapter;

    private View raiz;
    private TextView tvEtiquetaTotal;
    private TextView tvTotal;
    private TextView tvCantidad;
    private Spinner spFiltro;
    private RecyclerView rvGastos;
    private LinearLayout layoutVacio;
    private LottieAnimationView lottieVacio;

    // Recibe el resultado de AgregarGastoActivity y DetalleGastoActivity
    private final ActivityResultLauncher<Intent> agregarLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), resultado -> {
                if (resultado.getResultCode() == Activity.RESULT_OK) {
                    cargarGastos();
                    Snackbar.make(raiz, R.string.msg_gasto_agregado, Snackbar.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Intent> detalleLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), resultado -> {
                if (resultado.getResultCode() == Activity.RESULT_OK) {
                    cargarGastos();
                    Snackbar.make(raiz, R.string.msg_gasto_eliminado, Snackbar.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        repositorio = new GastoRepository(this);

        raiz = findViewById(R.id.main);
        tvEtiquetaTotal = findViewById(R.id.tvEtiquetaTotal);
        tvTotal = findViewById(R.id.tvTotal);
        tvCantidad = findViewById(R.id.tvCantidad);
        spFiltro = findViewById(R.id.spFiltro);
        rvGastos = findViewById(R.id.rvGastos);
        layoutVacio = findViewById(R.id.layoutVacio);
        lottieVacio = findViewById(R.id.lottieVacio);
        ImageButton btnResumen = findViewById(R.id.btnResumen);
        FloatingActionButton fabAgregar = findViewById(R.id.fabAgregar);

        Formato.aplicarInsets(raiz, rvGastos);
        // El botón flotante se separa de la barra de navegación del sistema
        final int margenFab = ((ViewGroup.MarginLayoutParams) fabAgregar.getLayoutParams()).bottomMargin;
        ViewCompat.setOnApplyWindowInsetsListener(fabAgregar, (v, insets) -> {
            int inferior = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            params.bottomMargin = margenFab + inferior;
            v.setLayoutParams(params);
            return insets;
        });

        adapter = new GastoAdapter(this::abrirDetalle);
        rvGastos.setLayoutManager(new LinearLayoutManager(this));
        rvGastos.setAdapter(adapter);

        configurarFiltro();

        fabAgregar.setOnClickListener(v ->
                agregarLauncher.launch(new Intent(this, AgregarGastoActivity.class)));
        btnResumen.setOnClickListener(v ->
                startActivity(new Intent(this, ResumenActivity.class)));

        // Al tocar la animación se pausa o reanuda (como en el taller de Lottie)
        lottieVacio.setOnClickListener(v -> {
            if (lottieVacio.isAnimating()) {
                lottieVacio.pauseAnimation();
            } else {
                lottieVacio.resumeAnimation();
            }
        });

        cargarGastos();
    }

    private void configurarFiltro() {
        ArrayAdapter<String> adapterFiltro = new ArrayAdapter<>(this,
                R.layout.item_spinner, Categorias.conTodas());
        adapterFiltro.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spFiltro.setAdapter(adapterFiltro);
        spFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cargarGastos();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void cargarGastos() {
        String categoria = (String) spFiltro.getSelectedItem();
        List<Gasto> gastos = CalculadoraGastos.filtrar(repositorio.listar(), categoria);
        adapter.setGastos(gastos);

        if (Categorias.TODAS.equals(categoria)) {
            tvEtiquetaTotal.setText(R.string.total_gastado);
        } else {
            tvEtiquetaTotal.setText(getString(R.string.total_en, categoria));
        }
        tvTotal.setText(Formato.moneda(CalculadoraGastos.total(gastos)));
        tvCantidad.setText(getResources().getQuantityString(
                R.plurals.cantidad_gastos, gastos.size(), gastos.size()));

        boolean vacio = gastos.isEmpty();
        layoutVacio.setVisibility(vacio ? View.VISIBLE : View.GONE);
        if (vacio) {
            lottieVacio.playAnimation();
        } else {
            lottieVacio.cancelAnimation();
        }
    }

    private void abrirDetalle(Gasto gasto) {
        // Se envían los datos del gasto a la otra Activity con putExtra
        Intent intent = new Intent(this, DetalleGastoActivity.class);
        intent.putExtra(DetalleGastoActivity.EXTRA_ID, gasto.getId());
        intent.putExtra(DetalleGastoActivity.EXTRA_DESCRIPCION, gasto.getDescripcion());
        intent.putExtra(DetalleGastoActivity.EXTRA_MONTO, gasto.getMonto());
        intent.putExtra(DetalleGastoActivity.EXTRA_CATEGORIA, gasto.getCategoria());
        intent.putExtra(DetalleGastoActivity.EXTRA_FECHA, gasto.getFecha());
        detalleLauncher.launch(intent);
    }
}
