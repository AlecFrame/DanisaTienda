package com.alecframe.danisatienda.ui.adapters;

import static android.view.View.INVISIBLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.alecframe.danisatienda.R;
import com.alecframe.danisatienda.model.Alias;
import com.alecframe.danisatienda.model.Carrito;
import com.alecframe.danisatienda.model.Venta;
import com.alecframe.danisatienda.utils.UtilsD;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class VentaAdapter extends RecyclerView.Adapter<VentaAdapter.VentaCardHolder>{
    private final List<Venta> ventas;
    private final Context context;
    private final LayoutInflater layoutInflater;

    public VentaAdapter(List<Venta> ventas, Context context, LayoutInflater layoutInflater) {
        this.ventas = ventas;
        this.context = context;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public VentaAdapter.VentaCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_venta, parent, false);
        return new VentaCardHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull VentaAdapter.VentaCardHolder holder, int position) {
        Venta venta = ventas.get(position);
        Carrito carrito = venta.getCarrito();
        Alias alias = venta.getAlias();

        holder.id.setText("#"+venta.getIdVenta());
        holder.fecha.setText("fecha: "+UtilsD.getFecha(venta.getFecha()));
        holder.hora.setText("hora: "+UtilsD.getTime(venta.getFecha()));

        holder.tipoPago.setText(String.valueOf(venta.getTipoPago()));
        if (venta.getTipoPago().equals("Transferencia")) {
            holder.tipoPago.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.blue)));
        }else {
            holder.tipoPago.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green_dark)));
            holder.alias.setVisibility(INVISIBLE);
        }

        if (alias!=null) { holder.alias.setText("Alias: "+alias.getValor()); }
        holder.precio.setText("$ "+ UtilsD.decimalFormat(carrito.getMontoTotal()));

        if (venta.getEstado()==1) {
            holder.estado.setVisibility(INVISIBLE);
        }

        holder.card.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("venta", venta);
            Navigation.findNavController(v)
                    .navigate(R.id.action_ventasFragment_to_carritoVistaFragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return ventas.size();
    }

    public static class VentaCardHolder extends RecyclerView.ViewHolder{
        TextView id;
        TextView fecha;
        TextView hora;
        TextView tipoPago;
        TextView alias;
        TextView precio;
        TextView estado;
        CardView card;
        public VentaCardHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.tvCardVentaId);
            fecha = itemView.findViewById(R.id.tvCardVentaFecha);
            hora =  itemView.findViewById(R.id.tvCardVentaHora);
            tipoPago =  itemView.findViewById(R.id.tvCardVentaTipoPago);
            alias = itemView.findViewById(R.id.tvCardVentaAlias);
            precio = itemView.findViewById(R.id.tvCardVentaPrecio);
            estado = itemView.findViewById(R.id.tvCardVentaActivo);
            card = itemView.findViewById(R.id.cardVentaAdapter);
        }
    }
}
