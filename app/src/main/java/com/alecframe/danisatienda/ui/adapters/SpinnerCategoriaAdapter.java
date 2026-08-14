package com.alecframe.danisatienda.ui.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alecframe.danisatienda.R;
import com.alecframe.danisatienda.model.Categoria;
import com.alecframe.danisatienda.utils.Icono;
import com.alecframe.danisatienda.utils.Iconos;

import java.util.List;

public class SpinnerCategoriaAdapter extends ArrayAdapter<Categoria> {
    public SpinnerCategoriaAdapter(Context context, List<Categoria> categorias) {
        super(context, 0, categorias);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return crearVista(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return crearVista(position, convertView, parent);
    }

    private View crearVista(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_spinner_icono, parent, false);
        }

        View layout = convertView.findViewById(R.id.layoutItemSpinnerIcono);
        ImageView img = convertView.findViewById(R.id.imgIcono);
        TextView txt = convertView.findViewById(R.id.txtNombre);

        Categoria categoria = getItem(position);

        if (categoria != null) {
            layout.setBackgroundResource(R.drawable.button_white_transparent);
            layout.setBackgroundTintList(ColorStateList.valueOf(categoria.getColor()));
            img.setImageResource(Iconos.getIdByName(categoria.getDrawable()));
            txt.setText(categoria.getNombre());
        }

        return convertView;
    }
}
