package com.alecframe.danisatienda.ui.ventas;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
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
import android.widget.Button;
import android.widget.Toast;

import com.alecframe.danisatienda.R;
import com.alecframe.danisatienda.databinding.FragmentVentasBinding;
import com.alecframe.danisatienda.model.Alias;
import com.alecframe.danisatienda.ui.adapters.SpinnerAliasAdapter;
import com.alecframe.danisatienda.ui.adapters.SpinnerTipoPagoAdapter;
import com.alecframe.danisatienda.ui.adapters.VentaAdapter;
import com.alecframe.danisatienda.utils.DListas;
import com.alecframe.danisatienda.utils.SpinnerNDC;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class VentasFragment extends Fragment {
    private VentasViewModel vm;
    private FragmentVentasBinding b;
    private int estado = 1;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
    private int spinnerTipoPagoIndex = 0;
    private int spinnerAliasIndex = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(VentasViewModel.class);
        b = FragmentVentasBinding.inflate(getLayoutInflater());

        vm.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        vm.getLista().observe(getViewLifecycleOwner(), list -> {
            VentaAdapter adapter = new VentaAdapter(list, getContext(), getLayoutInflater());
            GridLayoutManager glm = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
            if (!list.isEmpty()) {
                b.tvCargandoVentas.setVisibility(INVISIBLE);
                b.tvCargandoVentas.setText("Cargando Ventas...");
            }else {
                b.tvCargandoVentas.setVisibility(VISIBLE);
                b.tvCargandoVentas.setText("No hay Ventas cargadas");
            }
            b.rvVentas.setLayoutManager(glm);
            b.rvVentas.setAdapter(adapter);
        });

        vm.getListaAlias().observe(getViewLifecycleOwner(), this::spinnerIdAlias);

        vm.getTipoPago().observe(getViewLifecycleOwner(), tipoPago -> {
            cargandoLista();
            Log.d("DOBLE CARGA","TipoPago");
            vm.cargarLista();
        });

        vm.getIdAlias().observe(getViewLifecycleOwner(), idAlias -> {
            cargandoLista();
            Log.d("DOBLE CARGA","IdAlias");
            vm.cargarLista();
        });

        vm.getEstado().observe(getViewLifecycleOwner(), e -> {
            if (estado!=e) {
                estado = e;
                if (estado == 1) {
                    b.btVentaActivos.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_very_light)));
                    b.btVentaActivos.setTextColor(getResources().getColor(R.color.blue));
                } else {
                    b.btVentaActivos.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                    b.btVentaActivos.setTextColor(getResources().getColor(R.color.blue_very_light));
                }
                cargandoLista();
                vm.cargarLista();
            }
        });

        vm.getFechaDesde().observe(getViewLifecycleOwner(), fechaDesde -> {
            cargandoLista();
            vm.cargarLista();
        });

        vm.getFechaHasta().observe(getViewLifecycleOwner(), fechaHasta -> {
            cargandoLista();
            vm.cargarLista();
        });

        vm.getListaAlias().observe(getViewLifecycleOwner(), this::spinnerIdAlias);

        vm.cargarLista();
        vm.cargarSpinnerAlias();
        spinnerTipoPago();

        vm.getFechaMode().observe(getViewLifecycleOwner(), this::aplicarFechaMode);

        b.btVentaActivos.setOnClickListener(v -> {
            if (estado==1) {
                filtroInactivar();
            }else {
                filtroActivar();
            }
        });

        b.cbVentaEntreFechas.setOnCheckedChangeListener((buttonView, isChecked) -> checkedClick(isChecked));

        b.fabAgregarVenta.setOnClickListener(v -> {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main)
                    .navigate(R.id.action_ventasFragment_to_carritoFragment);
        });

        aplicarCalendario();

        return b.getRoot();
    }
    @SuppressLint("SetTextI18n")
    private void aplicarCalendario() {
        b.btVentaFechaTodos.setOnClickListener(v -> {
            vm.cambiarFechaMode(0);
            vm.cambiarFechaDesde(null);
            vm.cambiarFechaHasta(null);
            b.btVentaFecha.setText("Específica");
            b.btVentaFechaDesde.setText("Desde");
            b.btVentaFechaHasta.setText("Hasta");
        });

        b.btVentaFecha.setOnClickListener(v -> {
            LocalDate hoy = LocalDate.now();
            vm.cambiarFechaMode(1);

            new DatePickerDialog(
                    getContext(),
                    (view, year, month, dayOfMonth) -> {
                        LocalDate date = LocalDate.of(year, month + 1, dayOfMonth );

                        vm.cambiarFechaDesde(date);
                        vm.cambiarFechaHasta(date.plusDays(1));
                        b.btVentaFecha.setText("Específica: "+date.format(formatter));
                    },
                    hoy.getYear(),
                    hoy.getMonthValue() - 1,
                    hoy.getDayOfMonth()
            ).show();
        });

        b.btVentaFechaDesde.setOnClickListener(v -> {
            LocalDate hoy = LocalDate.now();
            vm.cambiarFechaMode(2);

            new DatePickerDialog(
                    getContext(),
                    (view, year, month, dayOfMonth) -> {
                        LocalDate date = LocalDate.of(year, month + 1, dayOfMonth );

                        vm.cambiarFechaDesde(date);
                        b.btVentaFechaDesde.setText(date.format(formatter));
                    },
                    hoy.getYear(),
                    hoy.getMonthValue() - 1,
                    hoy.getDayOfMonth()
            ).show();
        });

        b.btVentaFechaHasta.setOnClickListener(v -> {
            LocalDate hoy = LocalDate.now();
            vm.cambiarFechaMode(2);

            new DatePickerDialog(
                    getContext(),
                    (view, year, month, dayOfMonth) -> {
                        LocalDate date = LocalDate.of(year, month + 1, dayOfMonth );

                        vm.cambiarFechaHasta(date);
                        b.btVentaFechaHasta.setText(date.format(formatter));
                    },
                    hoy.getYear(),
                    hoy.getMonthValue() - 1,
                    hoy.getDayOfMonth()
            ).show();
        });
    }
    @SuppressLint("UseCompatTextViewDrawableApis")
    private void aplicarFechaMode(int fechaMode) {
        if (fechaMode==1) {
            noClickButton(b.btVentaFechaTodos);
            clickButton(b.btVentaFecha);
            noClickButton(b.btVentaFechaDesde);
            noClickButton(b.btVentaFechaHasta);
        }else if (fechaMode==2) {
            noClickButton(b.btVentaFechaTodos);
            noClickButton(b.btVentaFecha);
            clickButton(b.btVentaFechaDesde);
            clickButton(b.btVentaFechaHasta);
        }else {
            clickButton(b.btVentaFechaTodos);
            noClickButton(b.btVentaFecha);
            noClickButton(b.btVentaFechaDesde);
            noClickButton(b.btVentaFechaHasta);
        }
    }
    @SuppressLint("UseCompatTextViewDrawableApis")
    private void noClickButton(Button button) {
        button.setBackgroundTintList(ColorStateList.valueOf(getContext().getColor(R.color.blue_very_light)));
        button.setTextColor(getContext().getColor(R.color.blue));
        button.setCompoundDrawableTintList(ColorStateList.valueOf(getContext().getColor(R.color.blue)));
    }
    @SuppressLint("UseCompatTextViewDrawableApis")
    private void clickButton(Button button) {
        button.setBackgroundTintList(ColorStateList.valueOf(getContext().getColor(R.color.blue)));
        button.setTextColor(getContext().getColor(R.color.white));
        button.setCompoundDrawableTintList(ColorStateList.valueOf(getContext().getColor(R.color.white)));
    }
    private void checkedClick(boolean checked) {
        if (checked) {
            b.btVentaFecha.setVisibility(INVISIBLE);
            b.llEntreFechas.setVisibility(VISIBLE);
        }else {
            b.btVentaFecha.setVisibility(VISIBLE);
            b.llEntreFechas.setVisibility(INVISIBLE);
        }
        vm.cambiarFechaMode(0);
        vm.cambiarFechaDesde(null);
        vm.cambiarFechaHasta(null);
        b.btVentaFecha.setText("Específica");
        b.btVentaFechaDesde.setText("Desde");
        b.btVentaFechaHasta.setText("Hasta");
    }
    private void cargandoLista() {
        b.tvCargandoVentas.setVisibility(VISIBLE);
        b.tvCargandoVentas.setText("Cargando Ventas...");

        VentaAdapter adapter = new VentaAdapter(List.of(), getContext(), getLayoutInflater());
        b.rvVentas.setAdapter(adapter);
    }
    private void filtroActivar() {
        b.btVentaActivos.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_very_light)));
        b.btVentaActivos.setTextColor(getResources().getColor(R.color.blue));
        b.btVentaActivos.setText("Ver Desactivados");
        vm.cambiarEstado(1);
    }
    private void filtroInactivar() {
        b.btVentaActivos.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
        b.btVentaActivos.setTextColor(getResources().getColor(R.color.blue_very_light));
        b.btVentaActivos.setText("Ver Activados");
        vm.cambiarEstado(0);
    }
    private void spinnerTipoPago() {
        SpinnerTipoPagoAdapter adapter = new SpinnerTipoPagoAdapter(getContext(), DListas.TIPO_PAGOS);
        b.spVentaTipoPago.setAdapter(adapter);

        b.spVentaTipoPago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerNDC tipoPago = (SpinnerNDC) parent.getItemAtPosition(position);
                if (spinnerTipoPagoIndex!=position) {
                    spinnerTipoPagoIndex = position;
                    if (tipoPago.getNombre().equals("Todos")) {
                        vm.cambiarTipoPago(null);
                    }else {
                        vm.cambiarTipoPago(tipoPago.getNombre());
                    }
                    cargandoLista();
                    vm.cargarLista();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    private void spinnerIdAlias(List<Alias> list) {
        SpinnerAliasAdapter adapter = new SpinnerAliasAdapter(getContext(), list);
        b.spVentaAlias.setAdapter(adapter);

        b.spVentaAlias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Alias alias = (Alias) parent.getItemAtPosition(position);
                if (spinnerAliasIndex!=position) {
                    spinnerAliasIndex = position;
                    if (alias.getValor().equals("Cualquier alias")) {
                        vm.cambiarIdAlias(null);
                    }else {
                        vm.cambiarIdAlias(alias.getIdAlias());
                    }
                    cargandoLista();
                    vm.cargarLista();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}