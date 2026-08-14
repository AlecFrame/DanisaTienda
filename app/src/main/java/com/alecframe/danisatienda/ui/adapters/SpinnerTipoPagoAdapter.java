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
import com.alecframe.danisatienda.utils.SpinnerNDC;

import java.util.List;

public class SpinnerTipoPagoAdapter extends ArrayAdapter<SpinnerNDC> {
    public SpinnerTipoPagoAdapter(Context context, List<SpinnerNDC> list) {
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

    private View crearVista(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_spinner_icono, parent, false);
        }

        View layout = convertView.findViewById(R.id.layoutItemSpinnerIcono);
        ImageView img = convertView.findViewById(R.id.imgIcono);
        TextView txt = convertView.findViewById(R.id.txtNombre);

        SpinnerNDC tipoPago = getItem(position);

        if (tipoPago != null) {
            img.setImageResource(tipoPago.getDrawable());
            txt.setText(tipoPago.getNombre());
        }

        return convertView;
    }
}
