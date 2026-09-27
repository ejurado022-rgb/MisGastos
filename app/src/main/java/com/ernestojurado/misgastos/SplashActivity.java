package com.ernestojurado.misgastos;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Pantalla de bienvenida con una animación Lottie.
 */
public class SplashActivity extends AppCompatActivity {

    private static final long DURACION_MS = 2300;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable irAlInicio = () -> {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        handler.postDelayed(irAlInicio, DURACION_MS);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(irAlInicio);
        super.onDestroy();
    }
}
