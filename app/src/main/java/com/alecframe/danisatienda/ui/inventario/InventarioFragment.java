package com.alecframe.danisatienda.ui.inventario;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import static com.alecframe.danisatienda.utils.Iconos.ICONOS;

import androidx.lifecycle.ViewModelProvider;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.alecframe.danisatienda.R;
import com.alecframe.danisatienda.databinding.FragmentInventarioBinding;
import com.alecframe.danisatienda.model.Categoria;
import com.alecframe.danisatienda.ui.adapters.CategoriaAdapter;
import com.alecframe.danisatienda.ui.adapters.ProductoAdapter;
import com.alecframe.danisatienda.ui.adapters.SpinnerCategoriaAdapter;
import com.alecframe.danisatienda.ui.adapters.SpinnerIconoAdapter;
import com.alecframe.danisatienda.ui.adapters.VentaAdapter;
import com.alecframe.danisatienda.utils.Icono;

import java.util.List;

public class InventarioFragment extends Fragment {
    private InventarioViewModel vm;
    private FragmentInventarioBinding b;
    private int estado = 1;
    private String stockOrden = null;
    private int spinnerCategoriasIndex = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(InventarioViewModel.class);
        b = FragmentInventarioBinding.inflate(getLayoutInflater());

        vm.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        vm.getLista().observe(getViewLifecycleOwner(), list -> {
            ProductoAdapter adapter = new ProductoAdapter(list, getContext(), getLayoutInflater());
            GridLayoutManager glm = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
            if (!list.isEmpty()) {
                b.tvCargandoProductos.setVisibility(INVISIBLE);
                b.tvCargandoProductos.setText("Cargando Categorias...");
            } else {
                b.tvCargandoProductos.setVisibility(VISIBLE);
                b.tvCargandoProductos.setText("No hay Categorias cargadas");
            }
            b.rvProductos.setLayoutManager(glm);
            b.rvProductos.setAdapter(adapter);
        });

        vm.getListaCategorias().observe(getViewLifecycleOwner(), this::spinnerCategoria);

        vm.getIdCategoria().observe(getViewLifecycleOwner(), id -> {
            cargandoLista();
            vm.cargarLista(b.etInventarioBuscador.getEditText().getText().toString());
        });

        vm.getStockOrden().observe(getViewLifecycleOwner(), so -> {
            stockOrden = so;
            btOrdenActualizar();
            cargandoLista();
            vm.cargarLista(b.etInventarioBuscador.getEditText().getText().toString());
        });

        vm.getEstado().observe(getViewLifecycleOwner(), e -> {
            if (estado!=e) {
                estado = e;
                if (estado == 1) {
                    b.btInventarioActivos.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_very_light)));
                    b.btInventarioActivos.setTextColor(getResources().getColor(R.color.blue));
                    b.btInventarioActivos.setText("Inactivos");
                } else {
                    b.btInventarioActivos.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                    b.btInventarioActivos.setTextColor(getResources().getColor(R.color.blue_very_light));
                    b.btInventarioActivos.setText("Activos");
                }
                cargandoLista();
                vm.cargarLista(b.etInventarioBuscador.getEditText().getText().toString());
            }
        });

        vm.cargarLista(b.etInventarioBuscador.getEditText().getText().toString());
        vm.cargarSpinnerCategorias();

        btOrdenActualizar();

        b.btInventarioBuscarNombre.setOnClickListener(v -> {
            cargandoLista();
            vm.cargarLista(b.etInventarioBuscador.getEditText().getText().toString());
        });

        b.fabAgregarProducto.setOnClickListener(v -> {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main)
                    .navigate(R.id.action_inventarioFragment_to_productoNuevoFragment);
        });

        b.btInventarioActivos.setOnClickListener(v -> {
            if (estado==1) {
                filtroInactivar();
            }else {
                filtroActivar();
            }
        });

        b.cbInventarioStock.setOnClickListener(v -> {
            btOrdenActualizar();
        });

        b.btInventarioOrdenIgual.setOnClickListener(v -> {
            vm.cambiarStockOrden(null);
        });

        b.btInventarioOrdenAscendente.setOnClickListener(v -> {
            vm.cambiarStockOrden("asc");
        });

        b.btInventarioOrdenDescendente.setOnClickListener(v -> {
            vm.cambiarStockOrden("desc");
        });

        return b.getRoot();
    }

    private void filtroActivar() {
        b.btInventarioActivos.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_very_light)));
        b.btInventarioActivos.setTextColor(getResources().getColor(R.color.blue));
        b.btInventarioActivos.setText("Inactivos");
        vm.cambiarEstado(1);
    }
    private void filtroInactivar() {
        b.btInventarioActivos.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
        b.btInventarioActivos.setTextColor(getResources().getColor(R.color.blue_very_light));
        b.btInventarioActivos.setText("Activos");
        vm.cambiarEstado(0);
    }
    private void btOrdenActualizar() {
        if (b.cbInventarioStock.isChecked()) {
            b.cbInventarioStock.setTextColor(getResources().getColor(R.color.blue));
            b.btInventarioOrdenIgual.setEnabled(true);
            b.btInventarioOrdenAscendente.setEnabled(true);
            b.btInventarioOrdenDescendente.setEnabled(true);
            if (stockOrden==null) {
                b.btInventarioOrdenIgual.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                b.btInventarioOrdenIgual.setTextColor(getResources().getColor(R.color.white));
                b.btInventarioOrdenAscendente.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_very_light)));
                b.btInventarioOrdenAscendente.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                b.btInventarioOrdenDescendente.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_very_light)));
                b.btInventarioOrdenDescendente.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
            }else if (stockOrden.equals("asc")) {
                b.btInventarioOrdenAscendente.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                b.btInventarioOrdenAscendente.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                b.btInventarioOrdenDescendente.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_very_light)));
                b.btInventarioOrdenDescendente.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                b.btInventarioOrdenIgual.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_very_light)));
                b.btInventarioOrdenIgual.setTextColor(getResources().getColor(R.color.blue));
            }else if (stockOrden.equals("desc")) {
                b.btInventarioOrdenDescendente.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                b.btInventarioOrdenDescendente.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                b.btInventarioOrdenAscendente.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_very_light)));
                b.btInventarioOrdenAscendente.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                b.btInventarioOrdenIgual.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_very_light)));
                b.btInventarioOrdenIgual.setTextColor(getResources().getColor(R.color.blue));
            }
        }else {
            b.cbInventarioStock.setTextColor(getResources().getColor(R.color.gray_light1));
            b.btInventarioOrdenIgual.setEnabled(false);
            b.btInventarioOrdenIgual.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray_light)));
            b.btInventarioOrdenIgual.setTextColor(getResources().getColor(R.color.white));
            b.btInventarioOrdenAscendente.setEnabled(false);
            b.btInventarioOrdenAscendente.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray_light)));
            b.btInventarioOrdenAscendente.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            b.btInventarioOrdenDescendente.setEnabled(false);
            b.btInventarioOrdenDescendente.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray_light)));
            b.btInventarioOrdenDescendente.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        }
    }
    private void spinnerCategoria(List<Categoria> categorias) {
        SpinnerCategoriaAdapter adapter = new SpinnerCategoriaAdapter(getContext(), categorias);
        b.spInventarioCategorias.setAdapter(adapter);

        b.spInventarioCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerCategoriasIndex!=position) {
                    spinnerCategoriasIndex = position;
                    Categoria categoria = (Categoria) parent.getItemAtPosition(position);
                    vm.cambiarCategoriaId(categoria.getIdCategoria());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void cargandoLista() {
        b.tvCargandoProductos.setVisibility(VISIBLE);
        b.tvCargandoProductos.setText("Cargando Productos...");

        VentaAdapter adapter = new VentaAdapter(List.of(), getContext(), getLayoutInflater());
        b.rvProductos.setAdapter(adapter);
    }
}