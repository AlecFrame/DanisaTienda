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
import com.alecframe.danisatienda.model.Auditoria;
import com.alecframe.danisatienda.utils.UtilsD;

import java.util.List;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.HistorialCardHolder>{
    private final List<Auditoria> auditorias;
    private final LayoutInflater layoutInflater;
    private final Context context;

    public HistorialAdapter(List<Auditoria> auditorias, Context context, LayoutInflater layoutInflater) {
        this.auditorias = auditorias;
        this.context = context;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public HistorialAdapter.HistorialCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_historial, parent, false);
        return new HistorialCardHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HistorialAdapter.HistorialCardHolder holder, int position) {
        Auditoria auditoria = auditorias.get(position);

        if (auditoria.getEntidad().equals("Alias")) {
            holder.foto.setBackgroundTintList(ColorStateList.valueOf(0xFF579BFA));
            holder.foto.setImageTintList(ColorStateList.valueOf(0xFF3636E2));
        }else
        if (auditoria.getEntidad().equals("Categoria")) {
            holder.foto.setBackgroundTintList(ColorStateList.valueOf(0xFFFAE454));
            holder.foto.setImageTintList(ColorStateList.valueOf(0xFFE29830));
        }else
        if (auditoria.getEntidad().equals("Producto")) {
            holder.foto.setBackgroundTintList(ColorStateList.valueOf(0xFFAC55FA));
            holder.foto.setImageTintList(ColorStateList.valueOf(0xFF8434E2));
        }else
        if (auditoria.getEntidad().equals("Venta")) {
            holder.foto.setBackgroundTintList(ColorStateList.valueOf(0xFF56FA5D));
            holder.foto.setImageTintList(ColorStateList.valueOf(0xFF2FCF54));
        }else
        if (auditoria.getEntidad().equals("Gasto")) {
            holder.foto.setBackgroundTintList(ColorStateList.valueOf(0xFFFA5D5D));
            holder.foto.setImageTintList(ColorStateList.valueOf(0xFFCF2D2D));
        }

        holder.id.setText("Auditoría #"+auditoria.getIdAuditoria()+" - "+
                auditoria.getEntidad()+" #"+auditoria.getIdEntidad());
        holder.usuario.setText("usuario: "+auditoria.getUsuario().getNombre());
        holder.accion.setText("accion: "+auditoria.getAccion()+"_"+auditoria.getEntidad().toUpperCase());
        holder.descripcion.setText(auditoria.getDescripcion());
        holder.fecha.setText(UtilsD.getFechaEntera(auditoria.getFecha()));
    }

    @Override
    public int getItemCount() {
        return auditorias.size();
    }

    public static class HistorialCardHolder extends RecyclerView.ViewHolder{
        ImageView foto;
        TextView id;
        TextView usuario;
        TextView accion;
        TextView descripcion;
        TextView fecha;
        CardView card;
        public HistorialCardHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.ivCardHistorialFoto);
            id = itemView.findViewById(R.id.tvCardHistorialId);
            usuario = itemView.findViewById(R.id.tvCardHistorialUsuario);
            accion =  itemView.findViewById(R.id.tvCardHistorialAccion);
            descripcion = itemView.findViewById(R.id.tvCardHistorialDescripcion);
            fecha = itemView.findViewById(R.id.tvCardHistorialFecha);
            card = itemView.findViewById(R.id.cardHistorialAdapter);
        }
    }
}
