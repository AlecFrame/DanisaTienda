package com.alecframe.danisatienda.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.alecframe.danisatienda.R;
import com.alecframe.danisatienda.model.CarritoDetalle;
import com.alecframe.danisatienda.model.Categoria;
import com.alecframe.danisatienda.model.Producto;
import com.alecframe.danisatienda.request.ApiClient;
import com.alecframe.danisatienda.utils.Iconos;
import com.alecframe.danisatienda.utils.UtilsD;
import com.bumptech.glide.Glide;

import java.util.List;

public class CarritoVistaAdapter extends RecyclerView.Adapter<CarritoVistaAdapter.CarritoDetalleCardHolder>{
    private final List<CarritoDetalle> carritoDetalles;
    private final Context context;
    private final LayoutInflater layoutInflater;

    public CarritoVistaAdapter(List<CarritoDetalle> carritoDetalles, Context context, LayoutInflater layoutInflater) {
        this.carritoDetalles = carritoDetalles;
        this.context = context;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public CarritoVistaAdapter.CarritoDetalleCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_carrito_detalle, parent, false);
        return new CarritoDetalleCardHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CarritoVistaAdapter.CarritoDetalleCardHolder holder, int position) {
        CarritoDetalle carritoDetalle = carritoDetalles.get(position);
        carritoDetalle.setIdCarritoDetalle(position);

        Producto producto = carritoDetalle.getProducto();
        Categoria categoria = producto.getCategoria();

        holder.nombreProducto.setText(producto.getNombre());
        holder.precioUnitario.setText("Precio Unitario: $"+UtilsD.decimalFormat(producto.getPrecio()));

        if (producto.getUnidad().equals("Gramo")) {
            holder.cantidad.setText(carritoDetalle.getCantidad()+"g");
        }else
            holder.cantidad.setText(String.valueOf(carritoDetalle.getCantidad()));

        holder.subTotal.setText("$"+UtilsD.decimalFormat(carritoDetalle.getSubtotal()));

        if (producto.getFoto() != null) {
            Glide.with(context)
                    .load(ApiClient.BASE_URL + producto.getFoto())
                    .into(holder.foto
                    );
        } else {
            if (categoria != null) {
                holder.foto.setBackgroundTintList(ColorStateList.valueOf(categoria.getColor()));
                holder.foto.setImageResource(Iconos.getIdByName(categoria.getDrawable()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return carritoDetalles.size();
    }

    public static class CarritoDetalleCardHolder extends RecyclerView.ViewHolder{
        ImageView foto;
        TextView nombreProducto;
        TextView precioUnitario;
        TextView cantidad;
        TextView subTotal;
        CardView card;
        public CarritoDetalleCardHolder(@NonNull View itemView) {
            super(itemView);
            foto =  itemView.findViewById(R.id.ivCardCarritoDetalleFoto);
            nombreProducto = itemView.findViewById(R.id.tvCardCarritoDetalleNombre);
            precioUnitario = itemView.findViewById(R.id.tvCardCarritoDetallePrecioUnitario);
            cantidad =  itemView.findViewById(R.id.tvCardCarritoDetalleCantidad2);
            subTotal =  itemView.findViewById(R.id.tvCardCarritoDetalleSubTotal);
            card = itemView.findViewById(R.id.cardCarritoDetalleAdapter);
        }
    }
}
