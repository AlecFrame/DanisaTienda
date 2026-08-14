package com.alecframe.danisatienda.ui.categorias;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import static com.alecframe.danisatienda.utils.Iconos.ICONOS;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.alecframe.danisatienda.R;
import com.alecframe.danisatienda.databinding.FragmentCategoriaDetalleBinding;
import com.alecframe.danisatienda.model.Categoria;
import com.alecframe.danisatienda.request.ApiClient;
import com.alecframe.danisatienda.ui.adapters.SpinnerIconoAdapter;
import com.alecframe.danisatienda.utils.Icono;
import com.alecframe.danisatienda.utils.Iconos;
import com.alecframe.danisatienda.utils.UtilsD;
import com.bumptech.glide.Glide;

import java.util.Objects;

import yuku.ambilwarna.AmbilWarnaDialog;

public class CategoriaDetalleFragment extends Fragment {
    private CategoriaDetalleViewModel vm;
    private FragmentCategoriaDetalleBinding b;
    private String viewMode = "ver";
    private int colorDefault;
    private ActivityResultLauncher<Intent> selector;
    private Intent intentGaleria;
    private int estado = 1;
    private Categoria categoriaActual;
    private String drawableNameActual = "remove_24px";
    private Uri fotoUri = null;
    private int spinnerItemIndex = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(CategoriaDetalleViewModel.class);
        b = FragmentCategoriaDetalleBinding.inflate(getLayoutInflater());
        colorDefault = getResources().getColor(R.color.gray_light);

        viewModelSettings();
        spinnerCategoria();
        cargarGaleria();

        b.btCategoriaCambiarImagen.setOnClickListener(v -> {
            selector.launch(intentGaleria);
        });

        b.btCategoriaCambiarColor.setOnClickListener(v -> {
            openColorPicker();
        });

        b.btCategoriaEditar.setOnClickListener(v -> {
            vm.setViewMode("editar");
            comprobarCambios();
        });

        b.btCategoriaCancelar.setOnClickListener(v -> {
            if (Objects.equals(viewMode, "editar")) {
                vm.setViewMode("ver");
                cargarCategoria(categoriaActual);
            }else {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main)
                        .popBackStack();
            }
        });

        b.btCategoriaGuardar.setOnClickListener(v -> {
            procesoCambioEnCurso();
            if (viewMode.equals("crear")) {
                vm.crearCategoria(
                        ICONOS.get(b.spCategoriaIcono.getSelectedItemPosition()).getDrawableNombre(),
                        colorDefault,
                        b.etCategoriaNombre.getEditText().getText().toString(),
                        b.etCategoriaEjemplos.getEditText().getText().toString()
                );
            }else {
            vm.actualizarCategoria(
                    ICONOS.get(b.spCategoriaIcono.getSelectedItemPosition()).getDrawableNombre(),
                    colorDefault,
                    b.etCategoriaNombre.getEditText().getText().toString(),
                    b.etCategoriaEjemplos.getEditText().getText().toString()
                );
            }
        });

        b.btCategoriaActivar.setOnClickListener(v -> {
            procesoCambioEnCurso();
            if (estado==1) {
                vm.desactivarCategoria();
            }else {
                vm.activarCategoria();
            }
        });

        procesoCambioEnCurso();

        return b.getRoot();
    }
    private void viewModelSettings() {
        vm.getCategoria().observe(getViewLifecycleOwner(), categoria -> {
            procesoCambioEnTerminado();
            categoriaActual = categoria;

            cargarCategoria(categoria);
            agregarWacher();
        });

        vm.getViewMode().observe(getViewLifecycleOwner(), viewMode2 -> {
            viewMode = viewMode2;
            procesoCambioEnTerminado();
            if (viewMode.equals("crear") | viewMode.equals("editar")) {
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

        vm.getBack().observe(getViewLifecycleOwner(), result -> {
            vm.limpiarFotoUri();
            fotoUri = null;
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main)
                    .popBackStack();
        });

        vm.getColor().observe(getViewLifecycleOwner(), c -> {
            colorDefault = c;
            b.btCategoriaCambiarColor.setBackgroundColor(c);
            b.ivCategoriaFotoEditar.setBackgroundTintList(ColorStateList.valueOf(c));
        });

        vm.getFotoUri().observe(getViewLifecycleOwner(), uri -> {
            fotoUri = uri;
            if (uri!=null) {
                b.ivCategoriaFotoEditar.setImageURI(uri);
            }
        });

        vm.getEstado().observe(getViewLifecycleOwner(), e -> {
            estado = e;
            procesoCambioEnTerminado();
            if (e==1) {
                b.tvCategoriaDetalleActivado.setVisibility(INVISIBLE);
                b.btCategoriaActivar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red_dark)));
                b.btCategoriaActivar.setText("Desactivar");
            }else {
                b.tvCategoriaDetalleActivado.setVisibility(VISIBLE);
                b.btCategoriaActivar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_dark)));
                b.btCategoriaActivar.setText("Activar");
            }
        });

        vm.recuperarDatos(getArguments());
    }
    private void cargarCategoria(Categoria categoria) {
        b.etCategoriaNombreInput.setText(categoria.getNombre());
        if (!categoria.getEjemplos().equals("...")) {
            b.etCategoriaEjemplosInput.setText(categoria.getEjemplos());
        }
        colorDefault = categoria.getColor();
        b.btCategoriaCambiarColor.setBackgroundTintList(ColorStateList.valueOf(colorDefault));

        b.spCategoriaIcono.setSelection(Iconos.getIndexByName(categoria.getDrawable()));

        if (categoria.getFoto()==null) {
            b.ivCategoriaFotoVer.setBackgroundTintList(ColorStateList.valueOf(categoria.getColor()));
            b.ivCategoriaFotoVer.setImageResource(Iconos.getIdByName(categoria.getDrawable()));
            vm.limpiarFotoUri();
        }else {
            Glide.with(getContext())
                    .load(UtilsD.getURLImagen("categorias",categoria.getFoto()))
                    .placeholder(R.drawable.remove_24px)
                    .error(R.drawable.block_24px)
                    .into(b.ivCategoriaFotoVer);
        }
    }
    private void cargarGaleria(){
        intentGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        selector = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), resultado -> {
                    vm.recibirFoto(resultado);
                    Log.d("galeria","onActivityResult"+resultado);
                });
    }
    private void openColorPicker() {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(getContext(), colorDefault, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {}
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                colorDefault = color;
                vm.setColor(color);
                comprobarCambios();
            }
        });
        dialog.show();
    }
    private void spinnerCategoria() {
        SpinnerIconoAdapter adapter = new SpinnerIconoAdapter(getContext(), ICONOS);
        b.spCategoriaIcono.setAdapter(adapter);

        b.spCategoriaIcono.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerItemIndex!=position) {
                    spinnerItemIndex = position;
                    Icono icono = (Icono) parent.getItemAtPosition(position);
                    setCategoriaIcono(icono.getDrawable());
                    drawableNameActual = icono.getDrawableNombre();
                    comprobarCambios();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    private void setCategoriaIcono(int drawable) {
        if (categoriaActual!=null) {
            if (categoriaActual.getFoto() == null && fotoUri==null) {
                b.ivCategoriaFotoEditar.setImageResource(drawable);
            }
        }else {
            if (fotoUri==null) {
                b.ivCategoriaFotoEditar.setImageResource(drawable);
            }
        }
    }
    private void setEditMode() {
        b.clEditarCategoriaSuperior.setVisibility(VISIBLE);
        b.ivCategoriaFotoVer.setVisibility(INVISIBLE);
        b.etCategoriaNombre.setEnabled(true);
        b.etCategoriaEjemplos.setEnabled(true);
        b.llCategoriaInferiorEditar.setVisibility(VISIBLE);
        b.llCategoriaInferiorVer.setVisibility(INVISIBLE);
    }
    private void setVerMode() {
        b.clEditarCategoriaSuperior.setVisibility(INVISIBLE);
        b.ivCategoriaFotoVer.setVisibility(VISIBLE);
        b.etCategoriaNombre.setEnabled(false);
        b.etCategoriaEjemplos.setEnabled(false);
        b.llCategoriaInferiorEditar.setVisibility(INVISIBLE);
        b.llCategoriaInferiorVer.setVisibility(VISIBLE);
    }
    private void procesoCambioEnCurso() {
        b.btCategoriaGuardar.setEnabled(false);
        b.btCategoriaCancelar.setEnabled(false);
        b.btCategoriaActivar.setEnabled(false);
        b.btCategoriaEditar.setEnabled(false);
    }
    private void procesoCambioEnTerminado() {
        comprobarCambios();
        b.btCategoriaCancelar.setEnabled(true);
        b.btCategoriaActivar.setEnabled(true);
        b.btCategoriaEditar.setEnabled(true);
    }
    private void agregarWacher() {
        if (categoriaActual!=null) {
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

            b.etCategoriaNombreInput.addTextChangedListener(watcher);
            b.etCategoriaEjemplosInput.addTextChangedListener(watcher);
        }
    }
    public void comprobarCambios() {
        if (viewMode.equals("editar")) {
            if (categoriaActual != null) {
            /*
            Log.d("COMPROBAR CAMBIOS CATEGORIA", "==================================================");
            Log.d("COMPROBAR CAMBIOS CATEGORIA", "Actual Nombre: "+categoriaActual.getNombre()+
                    ", Cambio: "+b.etCategoriaNombre.getEditText().getText().toString());
            Log.d("COMPROBAR CAMBIOS CATEGORIA", "Actual Ejemplos: "+categoriaActual.getEjemplos()+
                    ", Cambio: "+b.etCategoriaEjemplos.getEditText().getText().toString());
            Log.d("COMPROBAR CAMBIOS CATEGORIA", "Actual Color: "+categoriaActual.getColor()+
                    ", Cambio: "+colorDefault);
            Log.d("COMPROBAR CAMBIOS CATEGORIA", "Actual Drawable: "+categoriaActual.getDrawable()+
                    ", Cambio: "+drawableNameActual);
             */
                boolean noHayCambio =
                        b.etCategoriaNombre.getEditText().getText().toString().equals(categoriaActual.getNombre()) &
                                (b.etCategoriaEjemplos.getEditText().getText().toString().equals(categoriaActual.getEjemplos())
                                        | (categoriaActual.getEjemplos().equals("...") & b.etCategoriaEjemplos.getEditText().getText().toString().isBlank())) &
                                colorDefault == categoriaActual.getColor() & drawableNameActual.equals(categoriaActual.getDrawable()) &
                                fotoUri == null;

                if (noHayCambio) {
                    b.btCategoriaGuardar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray_light1)));
                    b.btCategoriaGuardar.setEnabled(false);
                } else {
                    b.btCategoriaGuardar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_dark)));
                    b.btCategoriaGuardar.setEnabled(true);
                }
            }
        }else {
            b.btCategoriaGuardar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_dark)));
            b.btCategoriaGuardar.setEnabled(true);
        }
    }
}