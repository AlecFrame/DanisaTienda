package com.alecframe.danisatienda.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alecframe.danisatienda.R;
import com.alecframe.danisatienda.model.Gasto;
import com.alecframe.danisatienda.utils.UtilsD;

import java.util.List;

public class GastoAdapter extends RecyclerView.Adapter<GastoAdapter.GastoCardHolder>{
    private final List<Gasto> gastos;
    private final LayoutInflater layoutInflater;

    public GastoAdapter(List<Gasto> gastos, Context context, LayoutInflater layoutInflater) {
        this.gastos = gastos;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public GastoAdapter.GastoCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_gasto, parent, false);
        return new GastoCardHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull GastoAdapter.GastoCardHolder holder, int position) {
        Gasto gasto = gastos.get(position);

        holder.id.setText("Gasto #"+gasto.getIdGasto()+" - "+gasto.getCategoria());
        holder.monto.setText("monto: "+UtilsD.decimalFormat(gasto.getMonto()));
        holder.descripcion.setText(gasto.getDescripcion());
        holder.fecha.setText(UtilsD.getFechaEntera(gasto.getFecha()));
    }

    @Override
    public int getItemCount() {
        return gastos.size();
    }

    public static class GastoCardHolder extends RecyclerView.ViewHolder{
        ImageView foto;
        TextView id;
        TextView monto;
        TextView descripcion;
        TextView fecha;
        CardView card;
        public GastoCardHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.ivCardGastoFoto);
            id = itemView.findViewById(R.id.tvCardGastoId);
            monto = itemView.findViewById(R.id.tvCardGastoMonto);
            descripcion = itemView.findViewById(R.id.tvCardGastoDescripcion);
            fecha = itemView.findViewById(R.id.tvCardGastoFecha);
            card = itemView.findViewById(R.id.cardGastoAdapter);
        }
    }
}
