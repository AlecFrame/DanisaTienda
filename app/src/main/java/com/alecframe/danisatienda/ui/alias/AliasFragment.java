package com.alecframe.danisatienda.ui.alias;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.lifecycle.ViewModelProvider;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alecframe.danisatienda.R;
import com.alecframe.danisatienda.databinding.FragmentAliasBinding;
import com.alecframe.danisatienda.ui.adapters.AliasAdapter;
import com.alecframe.danisatienda.ui.adapters.CategoriaAdapter;
import com.alecframe.danisatienda.ui.adapters.VentaAdapter;

import java.util.List;

public class AliasFragment extends Fragment {
    private AliasViewModel vm;
    private FragmentAliasBinding b;
    private int estado = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(AliasViewModel.class);
        b = FragmentAliasBinding.inflate(getLayoutInflater());

        Bundle bundle = new Bundle();

        b.fabAgregarAlias.setOnClickListener(v -> {
            bundle.putString("viewMode", "crear");
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main)
                    .navigate(R.id.action_aliasFragment_to_aliasDetalleFragment, bundle);
        });

        vm.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        vm.getLista().observe(getViewLifecycleOwner(), list -> {
            AliasAdapter adapter = new AliasAdapter(list, getContext(), getLayoutInflater());
            GridLayoutManager glm = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
            if (!list.isEmpty()) {

                b.tvAliasCargando.setVisibility(INVISIBLE);
                b.tvAliasCargando.setText("Cargando Alias...");
            }else {

                b.tvAliasCargando.setVisibility(VISIBLE);
                b.tvAliasCargando.setText("No hay Alias cargadas");
            }
            b.rvAlias.setLayoutManager(glm);
            b.rvAlias.setAdapter(adapter);
        });

        vm.getEstado().observe(getViewLifecycleOwner(), e -> {
            if (estado!=e) {
                estado = e;
                if (estado == 1) {
                    b.btAliasActivos.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_very_light)));
                    b.btAliasActivos.setTextColor(getResources().getColor(R.color.blue));
                } else {
                    b.btAliasActivos.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                    b.btAliasActivos.setTextColor(getResources().getColor(R.color.blue_very_light));
                }
                cargandoLista();
                vm.cargarLista();
            }
        });

        vm.cargarLista();

        b.btAliasActivos.setOnClickListener(v -> {
            if (estado==1) {
                filtroInactivar();
            }else {
                filtroActivar();
            }
        });

        return b.getRoot();
    }

    private void filtroActivar() {
        b.btAliasActivos.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_very_light)));
        b.btAliasActivos.setTextColor(getResources().getColor(R.color.blue));
        b.btAliasActivos.setText("Ver Desactivados");
        vm.cambiarEstado(1);
    }

    private void filtroInactivar() {
        b.btAliasActivos.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
        b.btAliasActivos.setTextColor(getResources().getColor(R.color.blue_very_light));
        b.btAliasActivos.setText("Ver Activados");
        vm.cambiarEstado(0);
    }

    private void cargandoLista() {
        b.tvAliasCargando.setVisibility(VISIBLE);
        b.tvAliasCargando.setText("Cargando Alias...");

        VentaAdapter adapter = new VentaAdapter(List.of(), getContext(), getLayoutInflater());
        b.rvAlias.setAdapter(adapter);
    }
}