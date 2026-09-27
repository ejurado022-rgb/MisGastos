package com.ernestojurado.misgastos.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Representa un gasto registrado por el usuario.
 */
public class Gasto {

    private final long id;
    private final String descripcion;
    private final double monto;
    private final String categoria;
    private final long fecha;

    public Gasto(long id, String descripcion, double monto, String categoria, long fecha) {
        this.id = id;
        this.descripcion = descripcion;
        this.monto = monto;
        this.categoria = categoria;
        this.fecha = fecha;
    }

    public long getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getMonto() {
        return monto;
    }

    public String getCategoria() {
        return categoria;
    }

    public long getFecha() {
        return fecha;
    }

    // Conversión a JSON para guardarlo en SharedPreferences
    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("descripcion", descripcion);
        json.put("monto", monto);
        json.put("categoria", categoria);
        json.put("fecha", fecha);
        return json;
    }

    public static Gasto fromJson(JSONObject json) throws JSONException {
        return new Gasto(
                json.getLong("id"),
                json.getString("descripcion"),
                json.getDouble("monto"),
                json.getString("categoria"),
                json.getLong("fecha"));
    }
}
