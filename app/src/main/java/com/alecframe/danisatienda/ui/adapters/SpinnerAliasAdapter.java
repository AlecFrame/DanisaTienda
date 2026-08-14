package com.alecframe.danisatienda.ui.adapters;

import android.annotation.SuppressLint;
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
import com.alecframe.danisatienda.model.Alias;

import java.util.List;

public class SpinnerAliasAdapter extends ArrayAdapter<Alias> {
    public SpinnerAliasAdapter(Context context, List<Alias> list) {
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

        View layout = convertView.findViewById(R.id.layoutItemSpinnerIcono);
        ImageView img = convertView.findViewById(R.id.imgIcono);
        TextView txt = convertView.findViewById(R.id.txtNombre);

        Alias alias = getItem(position);

        if (alias != null) {
            img.setImageResource(R.drawable.account_balance_24px);
            txt.setText(alias.getValor()+": "+alias.getPropietario());
        }

        return convertView;
    }
}
