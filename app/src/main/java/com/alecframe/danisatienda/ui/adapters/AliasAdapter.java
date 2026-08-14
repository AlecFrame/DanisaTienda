package com.alecframe.danisatienda.ui.adapters;

import static android.view.View.INVISIBLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.alecframe.danisatienda.R;
import com.alecframe.danisatienda.model.Alias;
import java.util.List;

public class AliasAdapter extends RecyclerView.Adapter<AliasAdapter.AliasCardViewHolder>{
    private List<Alias> aliasList;
    private Context context;
    private LayoutInflater layoutInflater;

    public AliasAdapter(List<Alias> aliasList, Context context, LayoutInflater layoutInflater) {
        this.aliasList = aliasList;
        this.context = context;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public AliasAdapter.AliasCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_alias, parent, false);
        return new AliasAdapter.AliasCardViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AliasAdapter.AliasCardViewHolder holder, int position) {
        Alias alias = aliasList.get(position);
        holder.valor.setText("Alias: "+alias.getValor());
        holder.banco.setText("Banco: "+alias.getBanco());
        holder.propietario.setText("Propietario: "+alias.getPropietario());

        if (alias.getEstado()==1) {
            holder.inactivo.setVisibility(INVISIBLE);
        }

        holder.card.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("alias", alias);
            Navigation.findNavController(v)
                    .navigate(R.id.action_aliasFragment_to_aliasDetalleFragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return aliasList.size();
    }

    public class AliasCardViewHolder extends RecyclerView.ViewHolder{
        TextView valor;
        TextView banco;
        TextView propietario;
        TextView inactivo;
        ConstraintLayout card;
        public AliasCardViewHolder(@NonNull View itemView) {
            super(itemView);
            valor = itemView.findViewById(R.id.tvCardAliasValor);
            banco =  itemView.findViewById(R.id.tvCardAliasBanco);
            propietario =  itemView.findViewById(R.id.tvCardAliasPropietario);
            inactivo = itemView.findViewById(R.id.tvCardAliasActivo);
            card = itemView.findViewById(R.id.cardAlias);
        }
    }
}
