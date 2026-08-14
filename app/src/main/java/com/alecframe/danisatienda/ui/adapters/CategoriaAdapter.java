package com.alecframe.danisatienda.ui.adapters;

import static android.view.View.VISIBLE;

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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.alecframe.danisatienda.R;
import com.alecframe.danisatienda.model.Categoria;
import com.alecframe.danisatienda.request.ApiClient;
import com.alecframe.danisatienda.utils.Iconos;
import com.alecframe.danisatienda.utils.UtilsD;
import com.bumptech.glide.Glide;

import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaCardViewHolder>{
    private List<Categoria> categorias;
    private Context context;
    private LayoutInflater layoutInflater;

    public CategoriaAdapter(List<Categoria> categorias, Context context, LayoutInflater layoutInflater) {
        this.categorias = categorias;
        this.context = context;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public CategoriaCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_categoria, parent, false);
        return new CategoriaCardViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CategoriaCardViewHolder holder, int position) {
        Categoria categoria = categorias.get(position);
        holder.nombre.setText(categoria.getNombre());
        holder.ejemplos.setText(categoria.getEjemplos());

        if (categoria.getFoto()!=null) {
            Glide.with(context)
                    .load(UtilsD.getURLImagen("categorias",categoria.getFoto()))
                    .into(holder.foto);
        } else {
            holder.foto.setImageResource(Iconos.getIdByName(categoria.getDrawable()));
            holder.foto.setBackgroundTintList(ColorStateList.valueOf(categoria.getColor()));
        }

        if (categoria.getEstado()==0) {
            holder.inactivo.setVisibility(VISIBLE);
        }

        holder.card.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("categoria", categoria);
            Navigation.findNavController(v)
                    .navigate(R.id.action_categoriasFragment_to_categoriaDetalleFragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    public class CategoriaCardViewHolder extends RecyclerView.ViewHolder{
        TextView nombre;
        TextView ejemplos;
        ImageView foto;
        TextView inactivo;
        ConstraintLayout card;
        public CategoriaCardViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvCardCategoriaNombre);
            ejemplos =  itemView.findViewById(R.id.tvCardCategoriaEjemplos);
            foto =  itemView.findViewById(R.id.ivCardCategoriaFoto);
            inactivo = itemView.findViewById(R.id.tvCardCategoriaActivo);
            card = itemView.findViewById(R.id.cardCategoria);
        }
    }
}
