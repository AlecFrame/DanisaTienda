package com.alecframe.danisatienda.ui.categorias;

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
import com.alecframe.danisatienda.databinding.FragmentCategoriasBinding;
import com.alecframe.danisatienda.ui.adapters.CategoriaAdapter;
import com.alecframe.danisatienda.ui.adapters.VentaAdapter;

import java.util.List;

public class CategoriasFragment extends Fragment {
    private CategoriasViewModel vm;
    private FragmentCategoriasBinding b;
    private int estado = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(CategoriasViewModel.class);
        b = FragmentCategoriasBinding.inflate(getLayoutInflater());
        Bundle bundle = new Bundle();

        b.fabAgregarCategoria.setOnClickListener(v -> {
            bundle.putString("viewMode", "crear");
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main)
                    .navigate(R.id.action_categoriasFragment_to_categoriaDetalleFragment, bundle);
        });

        vm.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        vm.getLista().observe(getViewLifecycleOwner(), list -> {
            CategoriaAdapter adapter = new CategoriaAdapter(list, getContext(), getLayoutInflater());
            GridLayoutManager glm = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
            if (!list.isEmpty()) {

                b.tvCargandoCategorias.setVisibility(INVISIBLE);
                b.tvCargandoCategorias.setText("Cargando Categorias...");
            }else {

                b.tvCargandoCategorias.setVisibility(VISIBLE);
                b.tvCargandoCategorias.setText("No hay Categorias cargadas");
            }
            b.rvCategorias.setLayoutManager(glm);
            b.rvCategorias.setAdapter(adapter);
        });

        vm.getEstado().observe(getViewLifecycleOwner(), e -> {
            if (estado!=e) {
                estado = e;
                if (estado == 1) {
                    b.btCategoriasActivos.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_very_light)));
                    b.btCategoriasActivos.setTextColor(getResources().getColor(R.color.blue));
                } else {
                    b.btCategoriasActivos.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                    b.btCategoriasActivos.setTextColor(getResources().getColor(R.color.blue_very_light));
                }
                cargandoLista();
                vm.cargarLista();
            }
        });

        vm.cargarLista();

        b.btCategoriasActivos.setOnClickListener(v -> {
            if (estado==1) {
                filtroInactivar();
            }else {
                filtroActivar();
            }
        });

        return b.getRoot();
    }

    private void filtroActivar() {
        b.btCategoriasActivos.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_very_light)));
        b.btCategoriasActivos.setTextColor(getResources().getColor(R.color.blue));
        b.btCategoriasActivos.setText("Ver Desactivados");
        vm.cambiarEstado(1);
    }

    private void filtroInactivar() {
        b.btCategoriasActivos.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
        b.btCategoriasActivos.setTextColor(getResources().getColor(R.color.blue_very_light));
        b.btCategoriasActivos.setText("Ver Activados");
        vm.cambiarEstado(0);
    }

    private void cargandoLista() {
        b.tvCargandoCategorias.setVisibility(VISIBLE);
        b.tvCargandoCategorias.setText("Cargando Categorias...");

        VentaAdapter adapter = new VentaAdapter(List.of(), getContext(), getLayoutInflater());
        b.rvCategorias.setAdapter(adapter);
    }
}