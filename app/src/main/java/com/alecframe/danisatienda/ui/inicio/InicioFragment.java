package com.alecframe.danisatienda.ui.inicio;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alecframe.danisatienda.MainViewModel;
import com.alecframe.danisatienda.R;
import com.alecframe.danisatienda.databinding.FragmentInicioBinding;
import com.alecframe.danisatienda.ui.adapters.HistorialAdapter;
import com.alecframe.danisatienda.ui.adapters.ProductoInicioAdapter;
import com.alecframe.danisatienda.utils.UtilsD;

public class InicioFragment extends Fragment {

    private InicioViewModel vm;
    private MainViewModel vmMain;
    private FragmentInicioBinding b;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(InicioViewModel.class);
        vmMain = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        b = FragmentInicioBinding.inflate(getLayoutInflater());

        vm.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        vmMain.getAlias().observe(getViewLifecycleOwner(), alias -> {
            if (alias!=null) {
                b.tvInicioAlias.setText("alias: "+alias.getValor());
            }
        });

        vm.getResumen().observe(getViewLifecycleOwner(), resumenVentas -> {
            if (resumenVentas!=null) {
                b.tvInicioCardHoyPrecio.setText("$"+ UtilsD.decimalFormat(resumenVentas.getTotalVentasHoy()));
                b.tvInicioCardSemanaPrecio.setText("$"+UtilsD.decimalFormat(resumenVentas.getTotalVentasSemana()));
            }
        });

        vm.getListaProductos().observe(getViewLifecycleOwner(), list -> {
            ProductoInicioAdapter adapter = new ProductoInicioAdapter(list, getContext(), getLayoutInflater());
            GridLayoutManager glm = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
            if (!list.isEmpty()) {
                b.tvStockBajoCargando.setVisibility(INVISIBLE);
                b.tvStockBajoCargando.setText("Cargando productos con stock bajo...");
            } else {
                b.tvStockBajoCargando.setVisibility(VISIBLE);
                b.tvStockBajoCargando.setText("No hay productos con stock bajo");
            }
            b.rvStockBajo.setLayoutManager(glm);
            b.rvStockBajo.setAdapter(adapter);
        });

        vm.getListaAcciones().observe(getViewLifecycleOwner(), list -> {
            HistorialAdapter adapter = new HistorialAdapter(list, getContext(), getLayoutInflater());
            GridLayoutManager glm = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
            if (!list.isEmpty()) {
                b.tvRecientesCargando.setVisibility(INVISIBLE);
                b.tvRecientesCargando.setText("Cargando Acciones recientes...");
            } else {
                b.tvRecientesCargando.setVisibility(VISIBLE);
                b.tvRecientesCargando.setText("No hay acciones recientes");
            }
            b.rvRecientes.setLayoutManager(glm);
            b.rvRecientes.setAdapter(adapter);
        });

        vmMain.cargarAlias();
        vm.cargarResumen();
        vm.cargarListaProductos();
        vm.cargarListaAcciones();

        return b.getRoot();
    }

}