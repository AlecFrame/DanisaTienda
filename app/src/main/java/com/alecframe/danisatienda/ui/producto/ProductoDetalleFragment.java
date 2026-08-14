package com.alecframe.danisatienda.ui.producto;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

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

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.alecframe.danisatienda.R;
import com.alecframe.danisatienda.databinding.FragmentProductoDetalleBinding;
import com.alecframe.danisatienda.model.Categoria;
import com.alecframe.danisatienda.model.Producto;
import com.alecframe.danisatienda.request.ApiClient;
import com.alecframe.danisatienda.ui.adapters.SpinnerCategoriaAdapter;
import com.alecframe.danisatienda.ui.adapters.SpinnerUnidadAdapter;
import com.alecframe.danisatienda.utils.DListas;
import com.alecframe.danisatienda.utils.Iconos;
import com.alecframe.danisatienda.utils.UtilsD;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Objects;

public class ProductoDetalleFragment extends Fragment {
    private ProductoDetalleViewModel vm;
    private FragmentProductoDetalleBinding b;
    private Intent intentGaleria;
    private ActivityResultLauncher<Intent> selector;
    private Categoria categoria = null;
    private Uri fotoUri = null;
    private String viewMode = "ver";
    private Producto productoActual = null;
    private int estado = 1;
    private String unidad = "Unidad";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(ProductoDetalleViewModel.class);
        b = FragmentProductoDetalleBinding.inflate(getLayoutInflater());

        vm.cargarSpinnerCategorias();

        viewModelSettings();
        cargarGaleria();

        b.fotoProductoDetalle.setOnClickListener(v -> {
            selector.launch(intentGaleria);
        });

        b.btProductoActivar.setOnClickListener(v -> {
            procesoCambioEnCurso();
            if (estado==1) {
                vm.desactivarCategoria();
            }else {
                vm.activarCategoria();
            }
        });

        b.btProductoGuardar.setOnClickListener(v -> {
            procesoCambioEnCurso();
            vm.actualizarProducto(
                    b.etProductoNombre.getEditText().getText().toString(),
                    b.etProductoDescripcion.getEditText().getText().toString(),
                    ((categoria==null)? 0: categoria.getIdCategoria()),
                    unidad,
                    b.etProductoPrecio.getEditText().getText().toString(),
                    b.etProductoCostoCompra.getEditText().getText().toString(),
                    b.etProductoStock.getEditText().getText().toString(),
                    b.etProductoStockMinimo.getEditText().getText().toString()
            );
        });

        b.btProductoCancelar.setOnClickListener(v -> {
            vm.setViewMode("ver");
            cargarProducto(productoActual);
        });

        b.btProductoEditar.setOnClickListener(v -> {
            vm.setViewMode("editar");
            comprobarCambios();
        });

        return b.getRoot();
    }

    private void viewModelSettings() {
        vm.getProducto().observe(getViewLifecycleOwner(), producto -> {
            procesoCambioEnTerminado();
            productoActual = producto;

            cargarProducto(producto);
            agregarWacher();
        });

        vm.getViewMode().observe(getViewLifecycleOwner(), viewMode2 -> {
            viewMode = viewMode2;
            procesoCambioEnTerminado();
            if (viewMode.equals("editar")) {
                setEditMode();
            }else {
                setVerMode();
            }
        });

        vm.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        vm.getProcesoTerminado().observe(getViewLifecycleOwner(), procesoTerminado -> {
            procesoCambioEnTerminado();
        });

        vm.getListaCategorias().observe(getViewLifecycleOwner(), this::spinnerCategoria);
        spinnerUnidad();

        vm.getFotoUri().observe(getViewLifecycleOwner(), uri -> {
            fotoUri = uri;
            if (uri!=null) {
                b.fotoProductoDetalle.setImageURI(uri);
            }
        });

        vm.getEstado().observe(getViewLifecycleOwner(), e -> {
            estado = e;
            procesoCambioEnTerminado();
            if (e==1) {
                b.tvProductoActivado.setVisibility(INVISIBLE);
                b.btProductoActivar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_very_light)));
                b.btProductoActivar.setTextColor(getResources().getColor(R.color.blue));
                b.btProductoActivar.setText("Desactivar");
            }else {
                b.tvProductoActivado.setVisibility(VISIBLE);
                b.btProductoActivar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                b.btProductoActivar.setTextColor(getResources().getColor(R.color.white));
                b.btProductoActivar.setText("Activar");
            }
        });

        vm.recuperarDatos(getArguments());
    }

    private void cargarGaleria(){
        intentGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        selector = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), resultado -> {
            vm.recibirFoto(resultado);
            Log.d("galeria","onActivityResult"+resultado);
        });
    }
    private void cargarProducto(Producto producto) {
        categoria = producto.getCategoria();
        unidad = producto.getUnidad();
        if (categoria==null) {
            Toast.makeText(getContext(), "Categoria del producto no cargada", Toast.LENGTH_SHORT).show();
            return;
        }

        b.etProductoNombreInput.setText(producto.getNombre());
        if (!producto.getDescripcion().equals("...")) {
            b.etProductoDescripcionInput.setText(producto.getDescripcion());
        }
        b.etProductoPrecioInput.setText(UtilsD.decimalFormat(producto.getPrecio()));
        b.etProductoCostoCompraInput.setText(UtilsD.decimalFormat(producto.getCostoCompra()));
        b.etProductoStockInput.setText(String.valueOf(producto.getStock()));
        b.etProductoStockMinimoInput.setText(String.valueOf(producto.getStockBajo()));

        if (producto.getFoto()==null) {
            b.fotoProductoDetalleVer.setBackgroundTintList(ColorStateList.valueOf(categoria.getColor()));
            b.fotoProductoDetalleVer.setImageResource(Iconos.getIdByName(categoria.getDrawable()));
            vm.limpiarFotoUri();
        }else {
            Glide.with(getContext())
                    .load(ApiClient.BASE_URL + categoria.getFoto())
                    .placeholder(R.drawable.remove_24px)
                    .error(R.drawable.block_24px)
                    .into(b.fotoProductoDetalleVer);
        }

        b.tvProductoCategoria.setText(categoria.getNombre());
        b.tvProductoCategoria.setBackgroundTintList(ColorStateList.valueOf(categoria.getColor()));

        if (unidad.equals("Gramo")) {
            b.etProductoStock.setHint("Stock en gramos");
            b.etProductoStockMinimo.setHint("Stock mínimo en gramos");
        }else {
            b.etProductoStock.setHint("Stock actual");
            b.etProductoStockMinimo.setHint("Stock mínimo");
        }

        b.tvProductoUnidad.setText(unidad);
    }
    private void spinnerCategoria(List<Categoria> categorias) {
        SpinnerCategoriaAdapter adapter = new SpinnerCategoriaAdapter(getContext(), categorias);
        b.spProductoCategorias.setAdapter(adapter);

        b.spProductoCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoria = (Categoria) parent.getItemAtPosition(position);
                if (productoActual.getFoto() == null && fotoUri==null) {
                    b.fotoProductoDetalle.setImageResource(Iconos.getIdByName(categoria.getDrawable()));
                    b.fotoProductoDetalle.setBackgroundTintList(ColorStateList.valueOf(categoria.getColor()));
                }
                comprobarCambios();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    private void spinnerUnidad() {
        SpinnerUnidadAdapter adapter = new SpinnerUnidadAdapter(getContext(), DListas.TIPOS_UNDIADES);
        b.spProductoUnidades.setAdapter(adapter);

        b.spProductoUnidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unidad = (String) parent.getItemAtPosition(position);
                if (unidad.equals("Gramo")) {
                    b.etProductoStock.setHint("Stock en gramos");
                    b.etProductoStockMinimo.setHint("Stock mínimo en gramos");
                    Log.d("ProductoDetalle", "Gramo, hint: "+b.etProductoStockInput.getHint());
                }else {
                    b.etProductoStock.setHint("Stock actual");
                    b.etProductoStockMinimo.setHint("Stock mínimo");
                    Log.d("ProductoDetalle", "Unidad, hint: "+b.etProductoStockInput.getHint());
                }
                comprobarCambios();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    private void setEditMode() {
        b.etProductoNombre.setEnabled(true);
        b.spProductoCategorias.setEnabled(true);
        b.etProductoDescripcion.setEnabled(true);
        b.etProductoPrecio.setEnabled(true);
        b.etProductoCostoCompra.setEnabled(true);
        b.etProductoStock.setEnabled(true);
        b.etProductoStockMinimo.setEnabled(true);
        b.fotoProductoDetalle.setVisibility(VISIBLE);
        b.fotoProductoDetalleVer.setVisibility(INVISIBLE);
        b.llProductoInferiorEditar.setVisibility(VISIBLE);
        b.llProductoInferiorVer.setVisibility(INVISIBLE);
        b.tvProductoCategoria.setVisibility(INVISIBLE);
        b.spProductoCategorias.setVisibility(VISIBLE);
        b.tvProductoUnidad.setVisibility(INVISIBLE);
        b.spProductoUnidades.setVisibility(VISIBLE);
    }
    private void setVerMode() {
        b.etProductoNombre.setEnabled(false);
        b.spProductoCategorias.setEnabled(false);
        b.etProductoDescripcion.setEnabled(false);
        b.etProductoPrecio.setEnabled(false);
        b.etProductoCostoCompra.setEnabled(false);
        b.etProductoStock.setEnabled(false);
        b.etProductoStockMinimo.setEnabled(false);
        b.fotoProductoDetalle.setVisibility(INVISIBLE);
        b.fotoProductoDetalleVer.setVisibility(VISIBLE);
        b.llProductoInferiorEditar.setVisibility(INVISIBLE);
        b.llProductoInferiorVer.setVisibility(VISIBLE);
        b.tvProductoCategoria.setVisibility(VISIBLE);
        b.spProductoCategorias.setVisibility(INVISIBLE);
        b.tvProductoUnidad.setVisibility(VISIBLE);
        b.spProductoUnidades.setVisibility(INVISIBLE);
    }
    private void procesoCambioEnCurso() {
        b.btProductoGuardar.setEnabled(false);
        b.btProductoCancelar.setEnabled(false);
        b.btProductoActivar.setEnabled(false);
    }
    private void procesoCambioEnTerminado() {
        comprobarCambios();
        b.btProductoCancelar.setEnabled(true);
        b.btProductoActivar.setEnabled(true);
    }
    private void agregarWacher() {
        if (productoActual!=null) {
            TextWatcher watcher = new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    if (viewMode.equals("editar")) {
                        comprobarCambios();
                    }
                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
            };

            b.etProductoNombreInput.addTextChangedListener(watcher);
            b.etProductoDescripcionInput.addTextChangedListener(watcher);
            b.etProductoPrecioInput.addTextChangedListener(watcher);
            b.etProductoCostoCompraInput.addTextChangedListener(watcher);
            b.etProductoStockInput.addTextChangedListener(watcher);
            b.etProductoStockMinimoInput.addTextChangedListener(watcher);
        }
    }
    public void comprobarCambios() {
        if (viewMode.equals("editar")) {
            if (productoActual != null) {
                boolean noHayCambio =
                        b.etProductoNombre.getEditText().getText().toString().equals(productoActual.getNombre()) &
                        (b.etProductoDescripcion.getEditText().getText().toString().equals(productoActual.getDescripcion())
                                | (productoActual.getDescripcion().equals("...") & b.etProductoDescripcion.getEditText().getText().toString().isBlank())) &
                        categoria.getIdCategoria()==productoActual.getIdCategoria() &
                        b.etProductoPrecio.getEditText().getText().toString().equals(String.valueOf(productoActual.getPrecio())) &
                        b.etProductoCostoCompra.getEditText().getText().toString().equals(String.valueOf(productoActual.getCostoCompra())) &
                        Objects.equals(unidad, productoActual.getUnidad()) &
                        b.etProductoStock.getEditText().getText().toString().equals(String.valueOf(productoActual.getStock())) &
                        b.etProductoStockMinimo.getEditText().getText().toString().equals(String.valueOf(productoActual.getStockBajo())) &
                        fotoUri == null;
                if (noHayCambio) {
                    b.btProductoGuardar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray_light1)));
                    b.btProductoGuardar.setEnabled(false);
                } else {
                    b.btProductoGuardar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_dark)));
                    b.btProductoGuardar.setEnabled(true);
                }
            }
        }else {
            b.btProductoGuardar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_dark)));
            b.btProductoGuardar.setEnabled(true);
        }
    }
}