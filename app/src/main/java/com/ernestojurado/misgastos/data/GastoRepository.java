package com.ernestojurado.misgastos.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.ernestojurado.misgastos.model.Gasto;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Guarda y lee los gastos en SharedPreferences usando formato JSON,
 * así los datos no se pierden al cerrar la aplicación.
 */
public class GastoRepository {

    private static final String PREFS = "mis_gastos";
    private static final String CLAVE_GASTOS = "gastos";

    private final SharedPreferences prefs;

    public GastoRepository(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    /** Devuelve todos los gastos, del más reciente al más antiguo. */
    public List<Gasto> listar() {
        List<Gasto> gastos = new ArrayList<>();
        String texto = prefs.getString(CLAVE_GASTOS, "[]");
        try {
            JSONArray arreglo = new JSONArray(texto);
            for (int i = 0; i < arreglo.length(); i++) {
                gastos.add(Gasto.fromJson(arreglo.getJSONObject(i)));
            }
        } catch (JSONException e) {
            // Si el JSON está dañado se empieza con la lista vacía
            gastos.clear();
        }
        Collections.sort(gastos, (a, b) -> Long.compare(b.getFecha(), a.getFecha()));
        return gastos;
    }

    public void agregar(Gasto gasto) {
        List<Gasto> gastos = listar();
        gastos.add(gasto);
        guardar(gastos);
    }

    public boolean eliminar(long id) {
        List<Gasto> gastos = listar();
        for (int i = 0; i < gastos.size(); i++) {
            if (gastos.get(i).getId() == id) {
                gastos.remove(i);
                guardar(gastos);
                return true;
            }
        }
        return false;
    }

    private void guardar(List<Gasto> gastos) {
        JSONArray arreglo = new JSONArray();
        try {
            for (Gasto gasto : gastos) {
                arreglo.put(gasto.toJson());
            }
        } catch (JSONException e) {
            return;
        }
        prefs.edit().putString(CLAVE_GASTOS, arreglo.toString()).apply();
    }
}
