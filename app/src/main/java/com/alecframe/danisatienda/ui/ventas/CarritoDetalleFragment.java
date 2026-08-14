package com.alecframe.danisatienda.ui.ventas;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alecframe.danisatienda.R;
import com.alecframe.danisatienda.databinding.FragmentCarritoDetalleBinding;
import com.alecframe.danisatienda.model.CarritoDetalle;
import com.alecframe.danisatienda.model.Categoria;
import com.alecframe.danisatienda.model.Producto;
import com.alecframe.danisatienda.request.ApiClient;
import com.alecframe.danisatienda.utils.Iconos;
import com.alecframe.danisatienda.utils.UtilsD;
import com.bumptech.glide.Glide;

public class CarritoDetalleFragment extends Fragment {
    private CarritoDetalleViewModel vm;
    private CarritoViewModel vmCarrito;
    private FragmentCarritoDetalleBinding b;
    private CarritoDetalle carritoDetalleActual;
    private Producto producto;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(CarritoDetalleViewModel.class);
        vmCarrito = new ViewModelProvider(requireActivity()).get(CarritoViewModel.class);
        b = FragmentCarritoDetalleBinding.inflate(getLayoutInflater());

        vm.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        vm.getProducto().observe(getViewLifecycleOwner(), this::cargarProducto);

        vm.getCarritoDetalle().observe(getViewLifecycleOwner(), carritoDetalle -> {
            if (carritoDetalle!=null) {
                cargarProducto(carritoDetalle.getProducto());
                carritoDetalleActual = carritoDetalle;

                b.etCarritoDetalleCantidadInput.setText(String.valueOf(carritoDetalle.getCantidad()));
                b.etCarritoDetalleGramosInput.setText(String.valueOf(carritoDetalle.getCantidad()));
                b.etCarritoDetalleSubTotalInput.setText(UtilsD.decimalFormat(carritoDetalle.getSubtotal()));
            }

            b.btCarritoDetalleAgregar.setText("Actualizar");
            b.btCarritoDetalleEliminar.setVisibility(VISIBLE);
        });

        vmCarrito.getBack().observe(getViewLifecycleOwner(), back -> {
            if (back) {
                vmCarrito.setBack(false);
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main)
                        .popBackStack(R.id.carritoFragment, false);
            }
        });

        b.btCarritoDetalleCalcular.setOnClickListener(v -> {
            calcularSubTotal();
        });

        b.btCarritoDetalleAgregarCantidad.setOnClickListener(v -> {
            cambiarCantidad(1);
        });

        b.btCarritoDetalleQuitarCantidad.setOnClickListener(v -> {
            cambiarCantidad(-1);
        });

        aplicarWatcher();

        b.btCarritoDetalleCancelar.setOnClickListener(v -> {
            if (carritoDetalleActual==null) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main)
                        .popBackStack();
            }else {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main)
                        .popBackStack();
            }
        });

        vm.cargarArguments(getArguments());

        b.btCarritoDetalleAgregar.setOnClickListener(v -> {
            String cantidad = "";
            if (producto.getUnidad().equals("Unidad")) {
                cantidad = b.etCarritoDetalleCantidad.getEditText().getText().toString();
            }else {
                cantidad = b.etCarritoDetalleGramos.getEditText().getText().toString();
            }


            if (carritoDetalleActual!=null) {
                vmCarrito.actualizarCarritoDetalle(
                        cantidad,
                        b.etCarritoDetalleSubTotal.getEditText().getText().toString(),
                        carritoDetalleActual,
                        producto
                );
            }else {
                vmCarrito.agregarCarritoDetalle(
                        cantidad,
                        b.etCarritoDetalleSubTotal.getEditText().getText().toString(),
                        producto
                );
            }
        });

        b.btCarritoDetalleEliminar.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Eliminar producto")
                    .setMessage("¿Estás seguro de que quieres eliminar este producto del carrito?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        vmCarrito.removerCarritoDetalle(carritoDetalleActual, producto);
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        comprobarCambios();

        return b.getRoot();
    }
    @SuppressLint("SetTextI18n")
    private void cargarProducto(Producto p) {
        if (p!=null) {
            Categoria categoria = p.getCategoria();
            if (categoria == null) {
                Toast.makeText(getContext(), "Categoria del producto no cargada", Toast.LENGTH_SHORT).show();
                return;
            }

            b.tvCarritoDetalleNombre.setText(p.getNombre());
            if (!p.getDescripcion().equals("...")) {
                b.tvCarritoDetalleDescripcion.setText(p.getDescripcion());
            }
            b.tvCarritoDetallePrecioUnitario.setText("Precio Unitario: $"+UtilsD.decimalFormat(p.getPrecio()));

            if (p.getFoto() == null) {
                b.fotoCarritoDetalle.setBackgroundTintList(ColorStateList.valueOf(categoria.getColor()));
                b.fotoCarritoDetalle.setImageResource(Iconos.getIdByName(categoria.getDrawable()));
            } else {
                Glide.with(getContext())
                        .load(UtilsD.getURLImagen("productos", p.getFoto()))
                        .placeholder(R.drawable.remove_24px)
                        .error(R.drawable.block_24px)
                        .into(b.fotoCarritoDetalle);
            }

            b.tvCarritoProductoCategoria.setText(categoria.getNombre());
            b.tvCarritoProductoCategoria.setBackgroundTintList(ColorStateList.valueOf(categoria.getColor()));

            producto = p;

            if (p.getUnidad().equals("Gramo")) {
                b.tvCarritoDetalleStock.setText("Stock: "+p.getStock()+"g");
                b.llCarritoDetalleCantidad.setVisibility(INVISIBLE);
                b.llCarritoDetalleGramo.setVisibility(VISIBLE);
            }else {
                b.tvCarritoDetalleStock.setText("Stock: " + p.getStock());
                b.llCarritoDetalleCantidad.setVisibility(VISIBLE);
                b.llCarritoDetalleGramo.setVisibility(INVISIBLE);
            }
        }
    }
    private void calcularSubTotal() {
        if (producto!=null) {
            if (producto.getUnidad().equals("Unidad")) {
                String cantidadS = b.etCarritoDetalleCantidad.getEditText().getText().toString();

                if (!cantidadS.isBlank()) {
                    int cantidad = Integer.parseInt(cantidadS);

                    b.etCarritoDetalleSubTotalInput.setText(UtilsD.decimalFormat(cantidad * producto.getPrecio()));
                }
            }else {
                String gramoS = b.etCarritoDetalleGramos.getEditText().getText().toString();

                if (!gramoS.isBlank()) {
                    int gramo = Integer.parseInt(gramoS);

                    b.etCarritoDetalleSubTotalInput.setText(UtilsD.decimalFormat(gramo * 0.001 * producto.getPrecio()));
                }
            }
        }
    }
    private void cambiarCantidad(int i) {
        String cantidadS = b.etCarritoDetalleCantidad.getEditText().getText().toString();

        if (!cantidadS.isBlank()) {
            int cantidad = Integer.parseInt(cantidadS);

            if (cantidad==0&&i<0) { return; }
            if (cantidad==producto.getStock()&&i>0) { return; }
            b.etCarritoDetalleCantidadInput.setText(String.valueOf(cantidad+i));
        }else {
            b.etCarritoDetalleCantidadInput.setText("0");
        }
    }
    private void aplicarWatcher() {
        TextWatcher watcherSubTotal = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                calcularSubTotal();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        };

        TextWatcher watcherComprobaciones = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                comprobarCambios();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        };

        b.etCarritoDetalleCantidadInput.addTextChangedListener(watcherSubTotal);
        b.etCarritoDetalleCantidadInput.addTextChangedListener(watcherComprobaciones);
        b.etCarritoDetalleGramosInput.addTextChangedListener(watcherSubTotal);
        b.etCarritoDetalleGramosInput.addTextChangedListener(watcherComprobaciones);
        b.etCarritoDetalleSubTotalInput.addTextChangedListener(watcherComprobaciones);
    }
    private void comprobarCambios() {
        String subTotalS = b.etCarritoDetalleSubTotal.getEditText().getText().toString();

        if (producto==null) {
            desactivarAgregar();
            return;
        }

        if (!subTotalS.isBlank()) {
            double subTotal = Double.parseDouble(subTotalS);

            if (producto.getUnidad().equals("Unidad")) {
                String cantidadS = b.etCarritoDetalleCantidad.getEditText().getText().toString();

                if (!cantidadS.isBlank()) {
                    int cantidad = Integer.parseInt(cantidadS);

                    if (cantidad > 0 & subTotal > 0) {

                        if (cantidad > producto.getStock()) {
                            cantidad = producto.getStock();
                            b.etCarritoDetalleCantidadInput.setText(String.valueOf(cantidad));
                        }

                        if (carritoDetalleActual != null) {
                            boolean noHayCambio =
                                    cantidadS.equals(String.valueOf(carritoDetalleActual.getCantidad())) &
                                            subTotalS.equals(String.valueOf(carritoDetalleActual.getSubtotal()));

                            if (noHayCambio) {
                                desactivarAgregar();
                            } else {
                                activarAgregar();
                            }
                        } else { activarAgregar(); }
                    } else { desactivarAgregar(); }
                } else { desactivarAgregar(); }
            } else {
                String gramoS = b.etCarritoDetalleGramos.getEditText().getText().toString();

                if (!gramoS.isBlank()) {
                    int gramos = Integer.parseInt(gramoS);

                    if (gramos > 0 & subTotal > 0) {

                        if (gramos > producto.getStock()) {
                            gramos = producto.getStock();
                            b.etCarritoDetalleGramosInput.setText(String.valueOf(gramos));
                        }

                        if (carritoDetalleActual != null) {
                            boolean noHayCambio =
                                    gramoS.equals(String.valueOf(carritoDetalleActual.getCantidad())) &
                                            subTotalS.equals(String.valueOf(carritoDetalleActual.getSubtotal()));

                            if (noHayCambio) {
                                desactivarAgregar();
                            } else {
                                activarAgregar();
                            }
                        } else { activarAgregar(); }
                    } else { desactivarAgregar(); }
                } else { desactivarAgregar(); }
            }
        }else {
            desactivarAgregar();
        }
    }

    public void activarAgregar() {
        b.btCarritoDetalleAgregar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_dark)));
        b.btCarritoDetalleAgregar.setEnabled(true);
    }

    public void desactivarAgregar() {
        b.btCarritoDetalleAgregar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray_light1)));
        b.btCarritoDetalleAgregar.setEnabled(false);
    }
}