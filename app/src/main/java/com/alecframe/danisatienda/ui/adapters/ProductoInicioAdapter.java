package com.alecframe.danisatienda.ui.adapters;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.alecframe.danisatienda.R;
import com.alecframe.danisatienda.model.Categoria;
import com.alecframe.danisatienda.model.Producto;
import com.alecframe.danisatienda.request.ApiClient;
import com.alecframe.danisatienda.utils.Iconos;
import com.alecframe.danisatienda.utils.UtilsD;
import com.bumptech.glide.Glide;

import java.util.List;

public class ProductoInicioAdapter extends RecyclerView.Adapter<ProductoInicioAdapter.ProductoCardHolder>{
    private List<Producto> productos;
    private Context context;
    private LayoutInflater layoutInflater;

    public ProductoInicioAdapter(List<Producto> productos, Context context, LayoutInflater layoutInflater) {
        this.productos = productos;
        this.context = context;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public ProductoInicioAdapter.ProductoCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_producto, parent, false);
        return new ProductoInicioAdapter.ProductoCardHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductoInicioAdapter.ProductoCardHolder holder, int position) {
        Producto producto = productos.get(position);
        Categoria categoria = producto.getCategoria();

        holder.nombre.setText(producto.getNombre());
        holder.descripcion.setText(producto.getDescripcion());
        holder.precio.setText(UtilsD.decimalFormat(producto.getPrecio()));
        if (producto.getUnidad().equals("Gramo")) {
            holder.stock.setText(producto.getStock() +"g");
        }else
            holder.stock.setText(String.valueOf(producto.getStock()));

        if (producto.getStock()==0) {
            holder.advertencia.setText("Sin stock");
            holder.advertencia.setVisibility(VISIBLE);
            holder.advertencia.setTextColor(context.getResources().getColor(R.color.red_dark));
            holder.advertencia.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.red_dark)));
        }else
        if (producto.getStock()<=producto.getStockBajo()) {
            holder.advertencia.setText("Stock bajo");
            holder.advertencia.setVisibility(VISIBLE);
            holder.advertencia.setTextColor(context.getResources().getColor(R.color.orange_advertence));
            holder.advertencia.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.orange_advertence)));
        }else{
            holder.advertencia.setVisibility(INVISIBLE);
        }

        if (producto.getFoto()!=null) {
            Glide.with(context)
                    .load(UtilsD.getURLImagen("productos",producto.getFoto()))
                    .into(holder.foto);
        } else {
            if (categoria!=null) {
                holder.foto.setBackgroundTintList(ColorStateList.valueOf(categoria.getColor()));
                holder.foto.setImageResource(Iconos.getIdByName(categoria.getDrawable()));
            }
        }

        if (categoria!=null) {
            holder.categoria.setText(categoria.getNombre());
            holder.categoria.setBackgroundTintList(ColorStateList.valueOf(categoria.getColor()));
        }

        if (producto.getEstado()==1) {
            holder.estado.setVisibility(INVISIBLE);
        }

        holder.card.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("producto", producto);
            Navigation.findNavController(v)
                    .navigate(R.id.action_inicioFragment_to_productoDetalleFragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public class ProductoCardHolder extends RecyclerView.ViewHolder{
        ImageView foto;
        TextView nombre;
        TextView descripcion;
        TextView categoria;
        TextView precio;
        TextView stock;
        TextView advertencia;
        TextView estado;
        CardView card;
        public ProductoCardHolder(@NonNull View itemView) {
            super(itemView);
            foto =  itemView.findViewById(R.id.ivCardProductoFoto);
            nombre = itemView.findViewById(R.id.tvCardProductoNombre);
            descripcion = itemView.findViewById(R.id.tvCardProductoDescripcion);
            categoria =  itemView.findViewById(R.id.tvCardProductoCategoria);
            precio =  itemView.findViewById(R.id.tvCardProductoPrecio);
            stock = itemView.findViewById(R.id.tvCardProductoStock);
            advertencia = itemView.findViewById(R.id.tvCardProductoSinStockAdvertencia);
            estado = itemView.findViewById(R.id.tvCardProductoActivo);
            card = itemView.findViewById(R.id.cardProductoAdapter);
        }
    }
}
