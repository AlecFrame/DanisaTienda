package com.alecframe.danisatienda.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alecframe.danisatienda.R;

import java.util.List;

public class SpinnerUnidadAdapter extends ArrayAdapter<String> {
    public SpinnerUnidadAdapter(Context context, List<String> list) {
        super(context, 0, list);
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

    @SuppressLint("SetTextI18n")
    private View crearVista(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_spinner_icono, parent, false);
        }

        TextView txt = convertView.findViewById(R.id.txtNombre);

        String unidad = getItem(position);

        if (unidad != null) {
            txt.setText(unidad);
        }

        return convertView;
    }
}
