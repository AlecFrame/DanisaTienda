package com.alecframe.danisatienda.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alecframe.danisatienda.R;
import com.alecframe.danisatienda.model.Categoria;
import com.alecframe.danisatienda.model.Producto;
import com.alecframe.danisatienda.request.ApiClient;
import com.alecframe.danisatienda.request.ReporteProducto;
import com.alecframe.danisatienda.utils.Iconos;
import com.alecframe.danisatienda.utils.UtilsD;
import com.bumptech.glide.Glide;

import java.util.List;

public class ReporteProductoAdapter extends RecyclerView.Adapter<ReporteProductoAdapter.ReporteProductoCardHolder>{
    private List<ReporteProducto> reporteProductos;
    private Context context;
    private LayoutInflater layoutInflater;

    public ReporteProductoAdapter(List<ReporteProducto> reporteProductos, Context context, LayoutInflater layoutInflater) {
        this.reporteProductos = reporteProductos;
        this.context = context;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public ReporteProductoAdapter.ReporteProductoCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_reporte_producto, parent, false);
        return new ReporteProductoCardHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ReporteProductoAdapter.ReporteProductoCardHolder holder, int position) {
        ReporteProducto reporte = reporteProductos.get(position);
        Producto producto = reporte.getProducto();
        Categoria categoria = producto.getCategoria();

        holder.top.setText("#"+(position+1));
        holder.nombre.setText(producto.getNombre());
        if (producto.getUnidad().equals("Gramo")) {
            holder.unidades.setText(reporte.getCantidadVendida()+ ((reporte.getCantidadVendida()==1)? " gramo":" gramos") );
        }else
         holder.unidades.setText(reporte.getCantidadVendida()+ ((reporte.getCantidadVendida()==1)? " unidad":" unidades") );

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
    }

    @Override
    public int getItemCount() {
        return reporteProductos.size();
    }

    public static class ReporteProductoCardHolder extends RecyclerView.ViewHolder{
        ImageView foto;
        TextView nombre;
        TextView top;
        TextView unidades;
        CardView card;
        public ReporteProductoCardHolder(@NonNull View itemView) {
            super(itemView);
            foto =  itemView.findViewById(R.id.ivCardReporteProductoFoto);
            nombre = itemView.findViewById(R.id.tvCardReporteProductoNombre);
            top = itemView.findViewById(R.id.tvCardReportProductoTop);
            unidades =  itemView.findViewById(R.id.tvCardReporteProductoUnidades);
            card = itemView.findViewById(R.id.materialCardViewReporte);
        }
    }
}
