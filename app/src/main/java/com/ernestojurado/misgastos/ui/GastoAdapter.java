package com.ernestojurado.misgastos.ui;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ernestojurado.misgastos.R;
import com.ernestojurado.misgastos.model.Gasto;
import com.ernestojurado.misgastos.util.Categorias;
import com.ernestojurado.misgastos.util.Formato;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter del RecyclerView que muestra la lista de gastos.
 */
public class GastoAdapter extends RecyclerView.Adapter<GastoAdapter.GastoViewHolder> {

    public interface OnGastoClickListener {
        void onGastoClick(Gasto gasto);
    }

    private final List<Gasto> gastos = new ArrayList<>();
    private final OnGastoClickListener listener;

    public GastoAdapter(OnGastoClickListener listener) {
        this.listener = listener;
    }

    public void setGastos(List<Gasto> nuevos) {
        gastos.clear();
        gastos.addAll(nuevos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GastoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gasto, parent, false);
        return new GastoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull GastoViewHolder holder, int position) {
        holder.mostrar(gastos.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return gastos.size();
    }

    static class GastoViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivIcono;
        private final TextView tvDescripcion;
        private final TextView tvSubtitulo;
        private final TextView tvMonto;

        GastoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcono = itemView.findViewById(R.id.ivIcono);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvSubtitulo = itemView.findViewById(R.id.tvSubtitulo);
            tvMonto = itemView.findViewById(R.id.tvMonto);
        }

        void mostrar(Gasto gasto, OnGastoClickListener listener) {
            int color = ContextCompat.getColor(itemView.getContext(), Categorias.color(gasto.getCategoria()));
            ivIcono.setImageResource(Categorias.icono(gasto.getCategoria()));
            ivIcono.setBackgroundTintList(ColorStateList.valueOf(color));
            tvDescripcion.setText(gasto.getDescripcion());
            tvSubtitulo.setText(itemView.getContext().getString(R.string.item_subtitulo,
                    gasto.getCategoria(), Formato.fechaCorta(gasto.getFecha())));
            tvMonto.setText(Formato.moneda(gasto.getMonto()));
            itemView.setOnClickListener(v -> listener.onGastoClick(gasto));
        }
    }
}
