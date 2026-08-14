package com.alecframe.danisatienda.ui.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alecframe.danisatienda.R;
import com.alecframe.danisatienda.utils.SpinnerNDC;

import java.util.List;

public class SpinnerSimpleAdapter extends ArrayAdapter<SpinnerNDC> {
    public  SpinnerSimpleAdapter(Context context, List<SpinnerNDC> items) {
        super(context, 0, items);
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
        View layout = convertView.findViewById(R.id.layoutItemSpinnerIcono);

        SpinnerNDC item = getItem(position);

        if (item != null) {
            if (item.getDrawable()!=0) {
                img.setImageResource(item.getDrawable());
            }
            txt.setText(item.getNombre());
            if (item.getColor()!=0) {
                layout.setBackgroundTintList(ColorStateList.valueOf(item.getColor()));
            }
        }

        return convertView;
    }
}
