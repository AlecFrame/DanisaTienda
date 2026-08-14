package com.alecframe.danisatienda.ui.ventas;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.alecframe.danisatienda.databinding.FragmentCarritoProductoBinding;
import com.alecframe.danisatienda.model.Categoria;
import com.alecframe.danisatienda.model.Producto;
import com.alecframe.danisatienda.ui.adapters.CarritoProductoAdapter;
import com.alecframe.danisatienda.ui.adapters.SpinnerCategoriaAdapter;
import com.alecframe.danisatienda.ui.adapters.VentaAdapter;

import java.util.ArrayList;
import java.util.List;

public class CarritoProductoFragment extends Fragment {
    private CarritoProductoViewModel vm;
    private CarritoViewModel vmCarrito;
    private FragmentCarritoProductoBinding b;
    private int spinnerCategoriasIndex = 0;
    private List<Producto> productosEnList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(CarritoProductoViewModel.class);
        vmCarrito = new ViewModelProvider(requireActivity()).get(CarritoViewModel.class);
        b = FragmentCarritoProductoBinding.inflate(getLayoutInflater());

        vm.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        vm.getListaProductos().observe(getViewLifecycleOwner(), list -> {
            CarritoProductoAdapter adapter = new CarritoProductoAdapter(list, getContext(), getLayoutInflater());
            GridLayoutManager glm = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
            if (!list.isEmpty()) {
                b.tvCargandoCarritoProductos.setVisibility(INVISIBLE);
                b.tvCargandoCarritoProductos.setText("Cargando Categorias...");
            } else {
                b.tvCargandoCarritoProductos.setVisibility(VISIBLE);
                b.tvCargandoCarritoProductos.setText("No hay Categorias cargadas");
            }
            b.rvCarritoProductos.setLayoutManager(glm);
            b.rvCarritoProductos.setAdapter(adapter);
        });

        vmCarrito.getListaProductosEnCarrito().observe(getViewLifecycleOwner(), list -> {
            productosEnList = list;
            vm.cargarListaProductos(b.etCarritoProductoBuscador.getEditText().getText().toString(), list);
        });

        vm.getListaCategorias().observe(getViewLifecycleOwner(), this::spinnerCategoria);

        vm.getIdCategoria().observe(getViewLifecycleOwner(), id -> {
            cargandoLista();
            vm.cargarListaProductos(b.etCarritoProductoBuscador.getEditText().getText().toString(), productosEnList);
        });

        vm.cargarSpinnerCategorias();

        b.btCarritoProductoBuscarNombre.setOnClickListener(v -> {
            cargandoLista();
            vm.cargarListaProductos(b.etCarritoProductoBuscador.getEditText().getText().toString(), productosEnList);
        });

        return b.getRoot();
    }

    private void spinnerCategoria(List<Categoria> categorias) {
        SpinnerCategoriaAdapter adapter = new SpinnerCategoriaAdapter(getContext(), categorias);
        b.spCarritoProductoCategorias.setAdapter(adapter);

        b.spCarritoProductoCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerCategoriasIndex!=position) {
                    spinnerCategoriasIndex=position;
                    Categoria categoria = (Categoria) parent.getItemAtPosition(position);
                    vm.cambiarCategoriaId(categoria.getIdCategoria());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void cargandoLista() {
        b.tvCargandoCarritoProductos.setVisibility(VISIBLE);
        b.tvCargandoCarritoProductos.setText("Cargando Productos...");

        VentaAdapter adapter = new VentaAdapter(List.of(), getContext(), getLayoutInflater());
        b.rvCarritoProductos.setAdapter(adapter);
    }
}