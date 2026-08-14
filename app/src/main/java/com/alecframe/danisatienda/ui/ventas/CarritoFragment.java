package com.alecframe.danisatienda.ui.ventas;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.alecframe.danisatienda.R;
import com.alecframe.danisatienda.databinding.FragmentCarritoBinding;
import com.alecframe.danisatienda.model.Alias;
import com.alecframe.danisatienda.ui.adapters.CarritoDetalleAdapter;
import com.alecframe.danisatienda.ui.adapters.SpinnerAliasAdapter;
import com.alecframe.danisatienda.utils.UtilsD;

import java.util.List;

public class CarritoFragment extends Fragment {
    private CarritoViewModel vm;
    private FragmentCarritoBinding b;
    private String tipoPago = "Efectivo";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(requireActivity()).get(CarritoViewModel.class);
        b = FragmentCarritoBinding.inflate(getLayoutInflater());

        vm.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        vm.getListaDetalles().observe(getViewLifecycleOwner(), list -> {
            CarritoDetalleAdapter adapter = new CarritoDetalleAdapter(list, getContext(), getLayoutInflater());
            GridLayoutManager glm = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
            if (!list.isEmpty()) {
                b.tvCargandoCarritoDetalle.setVisibility(INVISIBLE);
                b.tvCargandoCarritoDetalle.setText("Cargando Detalles...");
            }else {
                b.tvCargandoCarritoDetalle.setVisibility(VISIBLE);
                b.tvCargandoCarritoDetalle.setText("No hay Detalles cargados");
            }
            b.rvCarritoDetalle.setLayoutManager(glm);
            b.rvCarritoDetalle.setAdapter(adapter);
            vm.calcularMontoTota();
        });

        vm.getListaAlias().observe(getViewLifecycleOwner(), this::spinnerIdAlias);
        vm.getTipoPago().observe(getViewLifecycleOwner(), tipoPago2 ->  tipoPago=tipoPago2);
        vm.getMontoTotal().observe(getViewLifecycleOwner(), montoTotal -> {
            b.etCarritoMontoTotalInput.setText(UtilsD.decimalFormat(montoTotal));
        });
        vm.cargarSpinnerAlias();

        vm.getBackCarrito().observe(getViewLifecycleOwner(), back -> {
            if (back) {
                vm.setBackCarrito(false);
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main)
                        .popBackStack(R.id.ventasFragment, false);
            }
        });

        b.rbEfectivo.setOnClickListener(v -> {
            aplicarEfectivo();
        });

        b.rbTransferencia.setOnClickListener(v -> {
            aplicarTransferencia();
        });

        b.btCarritoAgregarProducto.setOnClickListener(v -> {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main)
                    .navigate(R.id.action_carritoFragment_to_carritoProductoFragment);
        });

        b.btCarritoCancelar.setOnClickListener(v -> {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main)
                    .popBackStack();
        });

        b.btCarritoCalcular.setOnClickListener(v -> {
            vm.calcularMontoTota();
        });

        b.btCarritoConfirmar.setOnClickListener(v -> {
            vm.registrarVenta(
                    b.etCarritoMontoTotal.getEditText().getText().toString()
            );
        });

        aplicarWatcher();

        return b.getRoot();
    }
    private void aplicarWatcher() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                calcularVuelto();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        };

        b.etCarritoMontoTotalInput.addTextChangedListener(watcher);
        b.etCarritoVueltoInput.addTextChangedListener(watcher);
    }
    private void calcularVuelto() {
        if (tipoPago.equals("Efectivo")) {
            String montoTotalS = b.etCarritoMontoTotal.getEditText().getText().toString();
            String vueltoS = b.etCarritoVuelto.getEditText().getText().toString();

            if (!montoTotalS.isBlank() & !vueltoS.isBlank()) {
                double montoTotal = Double.parseDouble(montoTotalS);
                double vuelto = Double.parseDouble(vueltoS);

                b.etCarritoVuelto2Input.setText(UtilsD.decimalFormat(vuelto - montoTotal));
            } else {
                b.etCarritoVuelto2Input.setText("");
            }
        }
    }
    private void aplicarEfectivo() {
        vm.cambiarTipoPago("Efectivo");
        b.llCarritoVuelto.setVisibility(VISIBLE);
        b.llCarritoTransferencia.setVisibility(INVISIBLE);
    }
    private void aplicarTransferencia() {
        vm.cambiarTipoPago("Transferencia");
        b.llCarritoVuelto.setVisibility(INVISIBLE);
        b.llCarritoTransferencia.setVisibility(VISIBLE);
    }
    private void spinnerIdAlias(List<Alias> list) {
        SpinnerAliasAdapter adapter = new SpinnerAliasAdapter(getContext(), list);
        b.spCarritoAlias.setAdapter(adapter);

        b.spCarritoAlias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Alias alias = (Alias) parent.getItemAtPosition(position);
                vm.cambiarIdAlias(alias.getIdAlias());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}