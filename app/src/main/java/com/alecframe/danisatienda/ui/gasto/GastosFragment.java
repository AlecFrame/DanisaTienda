package com.alecframe.danisatienda.ui.gasto;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.Toast;

import com.alecframe.danisatienda.R;
import com.alecframe.danisatienda.databinding.FragmentGastosBinding;
import com.alecframe.danisatienda.ui.adapters.GastoAdapter;
import com.alecframe.danisatienda.ui.adapters.SpinnerStringAdapter;
import com.alecframe.danisatienda.ui.adapters.VentaAdapter;
import com.alecframe.danisatienda.utils.DListas;

import java.util.List;

public class GastosFragment extends Fragment {
    private GastosViewModel vm;
    private FragmentGastosBinding b;
    private int estado = 1;
    private int spinnerCategoriasIndex = 0;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(GastosViewModel.class);
        b = FragmentGastosBinding.inflate(getLayoutInflater());

        vm.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        vm.getLista().observe(getViewLifecycleOwner(), list -> {
            GastoAdapter adapter = new GastoAdapter(list, getContext(), getLayoutInflater());
            GridLayoutManager glm = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
            if (!list.isEmpty()) {
                b.tvCargandoGastos.setVisibility(INVISIBLE);
                b.tvCargandoGastos.setText("Cargando Gastos...");
            } else {
                b.tvCargandoGastos.setVisibility(VISIBLE);
                b.tvCargandoGastos.setText("No hay Gastos cargados");
            }
            b.rvGastos.setLayoutManager(glm);
            b.rvGastos.setAdapter(adapter);
        });

        vm.getCategoria().observe(getViewLifecycleOwner(), categoria -> {
            cargandoLista();
            vm.cargarLista(b.etGastosBuscador.getEditText().getText().toString());
        });

        vm.getEstado().observe(getViewLifecycleOwner(), e -> {
            if (estado!=e) {
                estado = e;
                if (estado == 1) {
                    b.btGastosActivos.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_very_light)));
                    b.btGastosActivos.setTextColor(getResources().getColor(R.color.blue));
                } else {
                    b.btGastosActivos.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                    b.btGastosActivos.setTextColor(getResources().getColor(R.color.blue_very_light));
                }
                cargandoLista();
                vm.cargarLista(b.etGastosBuscador.getEditText().getText().toString());
            }
        });

        vm.cargarLista(b.etGastosBuscador.getEditText().getText().toString());
        spinnerCategoria();

        b.btGastosBuscarDescripcion.setOnClickListener(v -> {
            cargandoLista();
            vm.cargarLista(b.etGastosBuscador.getEditText().getText().toString());
        });

        b.fabGastosVenta.setOnClickListener(v -> {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main)
                    .navigate(R.id.action_gastosFragment_to_gastoDetalleFragment);
        });

        b.btGastosActivos.setOnClickListener(v -> {
            if (estado==1) {
                filtroInactivar();
            }else {
                filtroActivar();
            }
        });

        return b.getRoot();
    }

    @SuppressLint("SetTextI18n")
    private void filtroActivar() {
        b.btGastosActivos.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_very_light)));
        b.btGastosActivos.setTextColor(getResources().getColor(R.color.blue));
        b.btGastosActivos.setText("Inactivos");
        vm.cambiarEstado(1);
    }
    @SuppressLint("SetTextI18n")
    private void filtroInactivar() {
        b.btGastosActivos.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
        b.btGastosActivos.setTextColor(getResources().getColor(R.color.blue_very_light));
        b.btGastosActivos.setText("Activos");
        vm.cambiarEstado(0);
    }
    private void spinnerCategoria() {
        SpinnerStringAdapter adapter = new SpinnerStringAdapter(getContext(), DListas.GASTOS2);
        b.spInventarioCategorias.setAdapter(adapter);

        b.spInventarioCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerCategoriasIndex!=position) {
                    spinnerCategoriasIndex = position;
                    String categoria = (String) parent.getItemAtPosition(position);
                    vm.cambiarCategoria(categoria);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @SuppressLint("SetTextI18n")
    private void cargandoLista() {
        b.tvCargandoGastos.setVisibility(VISIBLE);
        b.tvCargandoGastos.setText("Cargando Gastos...");

        VentaAdapter adapter = new VentaAdapter(List.of(), getContext(), getLayoutInflater());
        b.rvGastos.setAdapter(adapter);
    }
}