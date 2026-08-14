package com.alecframe.danisatienda.ui.registroVentas;

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
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.alecframe.danisatienda.R;
import com.alecframe.danisatienda.databinding.FragmentRegistroVentasBinding;
import com.alecframe.danisatienda.request.Resumen;
import com.alecframe.danisatienda.ui.adapters.ReporteProductoAdapter;
import com.alecframe.danisatienda.ui.adapters.VentaAdapter;
import com.alecframe.danisatienda.utils.UtilsD;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RegistroVentasFragment extends Fragment {
    private RegistroVentasViewModel vm;
    private FragmentRegistroVentasBinding b;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(RegistroVentasViewModel.class);
        b = FragmentRegistroVentasBinding.inflate(getLayoutInflater());

        vm.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        vm.getReporte().observe(getViewLifecycleOwner(), reporte -> {
            if (reporte!=null) {
                Resumen resumen = reporte.getResumen();

                if (resumen!=null) {
                    b.tvReporteCardVentaPrecio.setText("$"+UtilsD.decimalFormat(resumen.getTotalVentas()));
                    b.tvReporteCardGastosPrecio.setText("$"+UtilsD.decimalFormat(resumen.getTotalGastos()));
                    b.tvReporteCardGananciasPrecio.setText("$"+UtilsD.decimalFormat(resumen.getGanancia()));
                    b.tvReporteCardNumerosPrecio.setText(String.valueOf(resumen.getCantidadVentas()));
                }else {
                    b.tvReporteCardVentaPrecio.setText("$-");
                    b.tvReporteCardGastosPrecio.setText("$-");
                    b.tvReporteCardGananciasPrecio.setText("$-");
                    b.tvReporteCardNumerosPrecio.setText("-");
                }
                if (reporte.getProductosMasVendidos()!=null) {
                    vm.cargarLista(reporte.getProductosMasVendidos());
                }
            }
        });

        vm.getLista().observe(getViewLifecycleOwner(), list -> {
            ReporteProductoAdapter adapter = new ReporteProductoAdapter(list, getContext(), getLayoutInflater());
            GridLayoutManager glm = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
            if (!list.isEmpty()) {
                b.tvReporteCargandoProducto.setVisibility(INVISIBLE);
                b.tvReporteCargandoProducto.setText("Cargando productos...");
            }else {
                b.tvReporteCargandoProducto.setVisibility(VISIBLE);
                b.tvReporteCargandoProducto.setText("No hay productos comprados");
            }
            b.rvReporteProducto.setLayoutManager(glm);
            b.rvReporteProducto.setAdapter(adapter);
        });

        aplicarHoy();
        aplicarCalendario();

        return b.getRoot();
    }
    private void aplicarHoy() {
        LocalDate dateDesde = LocalDate.now();
        LocalDate dateHasta = dateDesde.plusDays(1);

        vm.cambiarFechaDesde(dateDesde);
        vm.cambiarFechaHasta(dateHasta);
        b.etReporteFechaDesdeInput.setText(dateDesde.format(formatter));
        b.etReporteFechaHastaInput.setText(dateHasta.format(formatter));
        clickFechaButton(0);
    }
    @SuppressLint("SetTextI18n")
    private void aplicarCalendario() {
        b.btReporteFechaHoy.setOnClickListener(v -> {
            LocalDate dateDesde = LocalDate.now();
            LocalDate dateHasta = dateDesde.plusDays(1);

            vm.cambiarFechaDesde(dateDesde);
            vm.cambiarFechaHasta(dateHasta);
            b.etReporteFechaDesdeInput.setText(dateDesde.format(formatter));
            b.etReporteFechaHastaInput.setText(dateHasta.format(formatter));
            clickFechaButton(0);
        });
        b.btReporteFechaSemana.setOnClickListener(v -> {
            LocalDate dateDesde = LocalDate.now().minusWeeks(1);
            LocalDate dateHasta = LocalDate.now().plusDays(1);

            vm.cambiarFechaDesde(dateDesde);
            vm.cambiarFechaHasta(dateHasta);
            b.etReporteFechaDesdeInput.setText(dateDesde.format(formatter));
            b.etReporteFechaHastaInput.setText(dateHasta.format(formatter));
            clickFechaButton(1);
        });
        b.btReporteFechaMes.setOnClickListener(v -> {
            LocalDate dateDesde = LocalDate.now().minusMonths(1);
            LocalDate dateHasta = LocalDate.now().plusDays(1);

            vm.cambiarFechaDesde(dateDesde);
            vm.cambiarFechaHasta(dateHasta);
            b.etReporteFechaDesdeInput.setText(dateDesde.format(formatter));
            b.etReporteFechaHastaInput.setText(dateHasta.format(formatter));
            clickFechaButton(2);
        });
        b.btReporteFechaTodo.setOnClickListener(v -> {
            vm.cambiarFechaDesde(null);
            vm.cambiarFechaHasta(null);
            b.etReporteFechaDesdeInput.setText("--/--");
            b.etReporteFechaHastaInput.setText("--/--");
            clickFechaButton(3);
        });

        b.etReporteFechaDesde.setOnClickListener(v -> {
            LocalDate hoy = LocalDate.now();

            new DatePickerDialog(
                    getContext(),
                    (view, year, month, dayOfMonth) -> {
                        LocalDate date = LocalDate.of(year, month + 1, dayOfMonth );

                        vm.cambiarFechaDesde(date);
                        b.etReporteFechaDesdeInput.setText(date.format(formatter));
                        clickFechaButton(4);
                    },
                    hoy.getYear(),
                    hoy.getMonthValue() - 1,
                    hoy.getDayOfMonth()
            ).show();
        });

        b.etReporteFechaHasta.setOnClickListener(v -> {
            LocalDate hoy = LocalDate.now();

            new DatePickerDialog(
                    getContext(),
                    (view, year, month, dayOfMonth) -> {
                        LocalDate date = LocalDate.of(year, month + 1, dayOfMonth );

                        vm.cambiarFechaHasta(date);
                        b.etReporteFechaHastaInput.setText(date.format(formatter));
                        clickFechaButton(4);
                    },
                    hoy.getYear(),
                    hoy.getMonthValue() - 1,
                    hoy.getDayOfMonth()
            ).show();
        });
    }
    private void clickFechaButton(int i) {
        if (i==0) {
            clickButton(b.btReporteFechaHoy);
            noClickButton(b.btReporteFechaSemana);
            noClickButton(b.btReporteFechaMes);
            noClickButton(b.btReporteFechaTodo);
        }else if (i==1) {
            noClickButton(b.btReporteFechaHoy);
            clickButton(b.btReporteFechaSemana);
            noClickButton(b.btReporteFechaMes);
            noClickButton(b.btReporteFechaTodo);
        }else if (i==2) {
            noClickButton(b.btReporteFechaHoy);
            noClickButton(b.btReporteFechaSemana);
            clickButton(b.btReporteFechaMes);
            noClickButton(b.btReporteFechaTodo);
        }else if (i==3) {
            noClickButton(b.btReporteFechaHoy);
            noClickButton(b.btReporteFechaSemana);
            noClickButton(b.btReporteFechaMes);
            clickButton(b.btReporteFechaTodo);
        }else {
            noClickButton(b.btReporteFechaHoy);
            noClickButton(b.btReporteFechaSemana);
            noClickButton(b.btReporteFechaMes);
            noClickButton(b.btReporteFechaTodo);
        }

        vm.cargarReporte();
        cargandoLista();
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
    private void cargandoLista() {
        b.tvReporteCargandoProducto.setVisibility(VISIBLE);
        b.tvReporteCargandoProducto.setText("Cargando productos...");

        VentaAdapter adapter = new VentaAdapter(List.of(), getContext(), getLayoutInflater());
        b.rvReporteProducto.setAdapter(adapter);
    }
}