package com.alecframe.danisatienda.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alecframe.danisatienda.R;
import com.alecframe.danisatienda.utils.Icono;

import java.util.List;

public class SpinnerIconoAdapter extends ArrayAdapter<Icono> {
    public SpinnerIconoAdapter(Context context, List<Icono> iconos) {
        super(context, 0, iconos);
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

        ImageView img = convertView.findViewById(R.id.imgIcono);
        TextView txt = convertView.findViewById(R.id.txtNombre);

        Icono icono = getItem(position);

        if (icono != null) {
            img.setImageResource(icono.getDrawable());
            txt.setText(icono.getNombre());
        }

        return convertView;
    }
}
