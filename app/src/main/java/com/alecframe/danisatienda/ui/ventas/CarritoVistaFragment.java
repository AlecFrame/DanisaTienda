package com.alecframe.danisatienda.ui.ventas;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alecframe.danisatienda.R;
import com.alecframe.danisatienda.databinding.FragmentCarritoVistaBinding;
import com.alecframe.danisatienda.model.CarritoDetalle;
import com.alecframe.danisatienda.ui.adapters.CarritoVistaAdapter;
import com.alecframe.danisatienda.utils.UtilsD;

import java.util.List;

public class CarritoVistaFragment extends Fragment {
    private CarritoVistaViewModel vm;
    private FragmentCarritoVistaBinding b;
    private int estado = 1;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(CarritoVistaViewModel.class);
        b = FragmentCarritoVistaBinding.inflate(getLayoutInflater());

        vm.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        vm.getVenta().observe(getViewLifecycleOwner(), venta -> {
            if (venta==null) {
                Toast.makeText(getContext(), "Venta no cargada correctamente", Toast.LENGTH_LONG).show(); return;
            }
            if (venta.getCarrito()==null) {
                Toast.makeText(getContext(), "Carrito no cargada correctamente", Toast.LENGTH_LONG).show(); return;
            }

            b.tvCarritoVistaId.setText("#"+venta.getIdVenta());
            b.tvCarritoVistaFecha.setText("Fecha: "+UtilsD.getFecha(venta.getFecha()));
            b.tvCarritoVistaHora.setText("Hora: "+UtilsD.getTime(venta.getFecha()));

            b.tvCarritoVistaTipoPago.setText(venta.getTipoPago());
            if (venta.getTipoPago().equals("Efectivo")) {
                b.tvCarritoVistaTipoPago.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_dark)));
            }else {
                b.tvCarritoVistaTipoPago.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
            }

            if (venta.getAlias()!=null) {
                b.tvCarritoVistaAlias.setText("Alias: " + venta.getAlias().getValor());
            }else {
                b.tvCarritoVistaAlias.setVisibility(INVISIBLE);
            }
            b.tvCarritoVistaMontoTotal.setText("Monto Total: $ "+ UtilsD.decimalFormat(venta.getCarrito().getMontoTotal()));

            vm.cargarDetalles();

            if (venta.getEstado()==0) {
                b.tvCarritoVistaActivado.setVisibility(VISIBLE);
                b.tvCarritoVistaActivado.setVisibility(VISIBLE);
                b.btCarritoVistaDesactivar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_dark)));
                b.btCarritoVistaDesactivar.setText("Activar");
            }else {
                b.tvCarritoVistaActivado.setVisibility(INVISIBLE);
                b.btCarritoVistaDesactivar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red_dark)));
                b.btCarritoVistaDesactivar.setText("Desactivar");
            }
            estado = venta.getEstado();
        });

        vm.getListaDetalle().observe(getViewLifecycleOwner(), detalles -> {
            if (detalles!=null) {
                CarritoVistaAdapter adapter = new CarritoVistaAdapter(detalles, getContext(), getLayoutInflater());
                GridLayoutManager glm = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
                if (!detalles.isEmpty()) {
                    b.tvCargandoCarritoVista.setVisibility(INVISIBLE);
                    b.tvCargandoCarritoVista.setText("Cargando Detalles...");
                } else {
                    b.tvCargandoCarritoVista.setVisibility(VISIBLE);
                    b.tvCargandoCarritoVista.setText("No hay Detalles cargados");
                }
                b.rvCarritoVista.setLayoutManager(glm);
                b.rvCarritoVista.setAdapter(adapter);
            }else {
                b.tvCargandoCarritoVista.setVisibility(VISIBLE);
                b.tvCargandoCarritoVista.setText("No hay Detalles cargados");
            }
        });

        vm.cargarVenta(getArguments());

        vm.getEstado().observe(getViewLifecycleOwner(), e -> {
            estado = e;
            procesoCambioEnTerminado();
            if (e==1) {
                b.tvCarritoVistaActivado.setVisibility(INVISIBLE);
                b.btCarritoVistaDesactivar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red_dark)));
                b.btCarritoVistaDesactivar.setText("Desactivar");
            }else {
                b.tvCarritoVistaActivado.setVisibility(VISIBLE);
                b.btCarritoVistaDesactivar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_dark)));
                b.btCarritoVistaDesactivar.setText("Activar");
            }
        });

        vm.getProcesoTerminado().observe(getViewLifecycleOwner(), procesoTerminado -> {
            procesoCambioEnTerminado();
        });

        b.btCarritoVistaDesactivar.setOnClickListener(v -> {
            procesoCambioEnCurso();
            if (estado==1) {
                vm.desactivarVenta();
            }else {
                vm.activarVenta();
            }
        });

        return b.getRoot();
    }

    private void procesoCambioEnCurso() {
        b.btCarritoVistaDesactivar.setEnabled(false);
    }
    private void procesoCambioEnTerminado() {
        b.btCarritoVistaDesactivar.setEnabled(true);
    }
}