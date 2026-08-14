package com.alecframe.danisatienda.ui.producto;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.alecframe.danisatienda.R;
import com.alecframe.danisatienda.databinding.FragmentProductoNuevoBinding;
import com.alecframe.danisatienda.model.Categoria;
import com.alecframe.danisatienda.ui.adapters.SpinnerCategoriaAdapter;
import com.alecframe.danisatienda.ui.adapters.SpinnerUnidadAdapter;
import com.alecframe.danisatienda.utils.DListas;
import com.alecframe.danisatienda.utils.Iconos;

import java.util.List;

public class ProductoNuevoFragment extends Fragment {
    private ProductoNuevoViewModel vm;
    private FragmentProductoNuevoBinding b;
    private Intent intentGaleria;
    private ActivityResultLauncher<Intent> selector;
    private Categoria categoria = null;
    private Uri fotoUri = null;
    private String unidad = "Unidad";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(ProductoNuevoViewModel.class);
        b = FragmentProductoNuevoBinding.inflate(getLayoutInflater());

        cargarGaleria();

        vm.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        vm.getProcesoTerminado().observe(getViewLifecycleOwner(), procesoTerminado -> {
            procesoCambioEnTerminado();
        });

        vm.getListaCategorias().observe(getViewLifecycleOwner(), this::spinnerCategoria);
        spinnerUnidad();

        vm.getBack().observe(getViewLifecycleOwner(), result -> {
            vm.limpiarFotoUri();
            fotoUri = null;
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main)
                    .popBackStack();
        });

        vm.getFotoUri().observe(getViewLifecycleOwner(), uri -> {
            fotoUri = uri;
            if (uri!=null) {
                b.fotoProductoNuevoDetalle.setImageURI(uri);
            }
        });

        vm.cargarSpinnerCategorias();

        b.btProductoNuevoCancelar.setOnClickListener(v -> {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main)
                    .popBackStack();
        });

        b.btProductoNuevoGuardar.setOnClickListener(v -> {
            procesoCambioEnCurso();
            vm.crearProducto(
                b.etProductoNuevoNombre.getEditText().getText().toString(),
                b.etProductoNuevoDescripcion.getEditText().getText().toString(),
                ((categoria==null)? 0: categoria.getIdCategoria()),
                unidad,
                b.etProductoNuevoPrecio.getEditText().getText().toString(),
                b.etProductoNuevoCostoCompra.getEditText().getText().toString(),
                b.etProductoNuevoStock.getEditText().getText().toString(),
                b.etProductoNuevoStockMinimo.getEditText().getText().toString()
            );
        });

        b.fotoProductoNuevoDetalle.setOnClickListener(v -> {
            selector.launch(intentGaleria);
        });

        return b.getRoot();
    }

    private void spinnerCategoria(List<Categoria> categorias) {
        SpinnerCategoriaAdapter adapter = new SpinnerCategoriaAdapter(getContext(), categorias);
        b.spProductoNuevoCategorias.setAdapter(adapter);

        b.spProductoNuevoCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoria = (Categoria) parent.getItemAtPosition(position);
                if (fotoUri == null) {
                    b.fotoProductoNuevoDetalle.setImageResource(Iconos.getIdByName(categoria.getDrawable()));
                    b.fotoProductoNuevoDetalle.setBackgroundTintList(ColorStateList.valueOf(categoria.getColor()));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void spinnerUnidad() {
        SpinnerUnidadAdapter adapter = new SpinnerUnidadAdapter(getContext(), DListas.TIPOS_UNDIADES);
        b.spProductoNuevoUnidades.setAdapter(adapter);

        b.spProductoNuevoUnidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unidad = (String) parent.getItemAtPosition(position);
                if (unidad.equals("Gramo")) {
                    b.etProductoNuevoStock.setHint("Stock en gramos");
                    b.etProductoNuevoStockMinimo.setHint("Stock mínimo en gramos");
                }else {
                    b.etProductoNuevoStock.setHint("Stock actual");
                    b.etProductoNuevoStockMinimo.setHint("Stock mínimo");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void cargarGaleria(){
        intentGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        selector = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), resultado -> {
            vm.recibirFoto(resultado);
            Log.d("galeria","onActivityResult"+resultado);
        });
    }

    private void procesoCambioEnCurso() {
        b.btProductoNuevoGuardar.setEnabled(false);
        b.btProductoNuevoCancelar.setEnabled(false);
    }
    private void procesoCambioEnTerminado() {
        b.btProductoNuevoGuardar.setEnabled(true);
        b.btProductoNuevoCancelar.setEnabled(true);
    }

}