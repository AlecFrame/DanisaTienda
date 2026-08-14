package com.alecframe.danisatienda.ui.actividad;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.alecframe.danisatienda.R;
import com.alecframe.danisatienda.databinding.FragmentActividadBinding;
import com.alecframe.danisatienda.ui.adapters.HistorialAdapter;
import com.alecframe.danisatienda.ui.adapters.SpinnerSimpleAdapter;
import com.alecframe.danisatienda.ui.adapters.VentaAdapter;
import com.alecframe.danisatienda.utils.DListas;
import com.alecframe.danisatienda.utils.SpinnerNDC;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ActividadFragment extends Fragment {
    private ActividadViewModel vm;
    private FragmentActividadBinding b;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
    private int spinnerEntidadIndex = 0;
    private int spinnerAccionIndex = 0;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(ActividadViewModel.class);
        b = FragmentActividadBinding.inflate(getLayoutInflater());

        vm.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        vm.getLista().observe(getViewLifecycleOwner(), list -> {
            HistorialAdapter adapter = new HistorialAdapter(list, getContext(), getLayoutInflater());
            GridLayoutManager glm = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
            if (!list.isEmpty()) {
                b.tvCargandoHistorial.setVisibility(INVISIBLE);
                b.tvCargandoHistorial.setText("Cargando Ventas...");
            }else {
                b.tvCargandoHistorial.setVisibility(VISIBLE);
                b.tvCargandoHistorial.setText("No hay Ventas cargadas");
            }
            b.rvHistorial.setLayoutManager(glm);
            b.rvHistorial.setAdapter(adapter);
        });

        vm.getFechaDesde().observe(getViewLifecycleOwner(), fechaDesde -> {
            cargandoLista();
            vm.cargarLista(b.etHistorialUsuario.getEditText().getText().toString());
        });

        vm.getFechaHasta().observe(getViewLifecycleOwner(), fechaHasta -> {
            cargandoLista();
            vm.cargarLista(b.etHistorialUsuario.getEditText().getText().toString());
        });

        vm.cargarLista(b.etHistorialUsuario.getEditText().getText().toString());
        spinnerEntidad();
        spinnerAccion();

        vm.getFechaMode().observe(getViewLifecycleOwner(), this::aplicarFechaMode);

        b.cbHistorialEntreFechas.setOnCheckedChangeListener((buttonView, isChecked) -> checkedClick(isChecked));

        b.btHistorialFiltrar.setOnClickListener(v -> {
            vm.cargarLista(b.etHistorialUsuario.getEditText().getText().toString());
        });

        aplicarCalendario();

        return b.getRoot();
    }

    @SuppressLint("SetTextI18n")
    private void aplicarCalendario() {
        b.btHistorialFechaTodos.setOnClickListener(v -> {
            vm.cambiarFechaMode(0);
            vm.cambiarFechaDesde(null);
            vm.cambiarFechaHasta(null);
            b.btHistorialFecha.setText("Específica");
            b.btHistorialFechaDesde.setText("Desde");
            b.btHistorialFechaHasta.setText("Hasta");
        });

        b.btHistorialFecha.setOnClickListener(v -> {
            LocalDate hoy = LocalDate.now();
            vm.cambiarFechaMode(1);

            new DatePickerDialog(
                    getContext(),
                    (view, year, month, dayOfMonth) -> {
                        LocalDate date = LocalDate.of(year, month + 1, dayOfMonth );

                        vm.cambiarFechaDesde(date);
                        vm.cambiarFechaHasta(date.plusDays(1));
                        b.btHistorialFecha.setText("Específica: "+date.format(formatter));
                    },
                    hoy.getYear(),
                    hoy.getMonthValue() - 1,
                    hoy.getDayOfMonth()
            ).show();
        });

        b.btHistorialFechaDesde.setOnClickListener(v -> {
            LocalDate hoy = LocalDate.now();
            vm.cambiarFechaMode(2);

            new DatePickerDialog(
                    getContext(),
                    (view, year, month, dayOfMonth) -> {
                        LocalDate date = LocalDate.of(year, month + 1, dayOfMonth );

                        vm.cambiarFechaDesde(date);
                        b.btHistorialFechaDesde.setText(date.format(formatter));
                    },
                    hoy.getYear(),
                    hoy.getMonthValue() - 1,
                    hoy.getDayOfMonth()
            ).show();
        });

        b.btHistorialFechaHasta.setOnClickListener(v -> {
            LocalDate hoy = LocalDate.now();
            vm.cambiarFechaMode(2);

            new DatePickerDialog(
                    getContext(),
                    (view, year, month, dayOfMonth) -> {
                        LocalDate date = LocalDate.of(year, month + 1, dayOfMonth );

                        vm.cambiarFechaHasta(date);
                        b.btHistorialFechaHasta.setText(date.format(formatter));
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
            noClickButton(b.btHistorialFechaTodos);
            clickButton(b.btHistorialFecha);
            noClickButton(b.btHistorialFechaDesde);
            noClickButton(b.btHistorialFechaHasta);
        }else if (fechaMode==2) {
            noClickButton(b.btHistorialFechaTodos);
            noClickButton(b.btHistorialFecha);
            clickButton(b.btHistorialFechaDesde);
            clickButton(b.btHistorialFechaHasta);
        }else {
            clickButton(b.btHistorialFechaTodos);
            noClickButton(b.btHistorialFecha);
            noClickButton(b.btHistorialFechaDesde);
            noClickButton(b.btHistorialFechaHasta);
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
    private void spinnerEntidad() {
        SpinnerSimpleAdapter adapter = new SpinnerSimpleAdapter(getContext(), DListas.ENTIDADES);
        b.spHistorialEntidad.setAdapter(adapter);

        b.spHistorialEntidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerNDC entidad = (SpinnerNDC) parent.getItemAtPosition(position);
                if (spinnerEntidadIndex!=position) {
                    spinnerEntidadIndex = position;
                    cargandoLista();
                    vm.setEntidad(entidad.getNombre());
                    vm.cargarLista(b.etHistorialUsuario.getEditText().getText().toString());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    private void spinnerAccion() {
        SpinnerSimpleAdapter adapter = new SpinnerSimpleAdapter(getContext(), DListas.ACCIONES);
        b.spHistorialAccion.setAdapter(adapter);

        b.spHistorialAccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerNDC accion = (SpinnerNDC) parent.getItemAtPosition(position);
                if (spinnerAccionIndex!=position) {
                    spinnerAccionIndex = position;
                    cargandoLista();
                    vm.setAccion(accion.getNombre());
                    vm.cargarLista(b.etHistorialUsuario.getEditText().getText().toString());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    @SuppressLint("SetTextI18n")
    private void checkedClick(boolean checked) {
        if (checked) {
            b.btHistorialFecha.setVisibility(INVISIBLE);
            b.llEntreFechas.setVisibility(VISIBLE);
        }else {
            b.btHistorialFecha.setVisibility(VISIBLE);
            b.llEntreFechas.setVisibility(INVISIBLE);
        }
        vm.cambiarFechaMode(0);
        vm.cambiarFechaDesde(null);
        vm.cambiarFechaHasta(null);
        b.btHistorialFecha.setText("Específica");
        b.btHistorialFechaDesde.setText("Desde");
        b.btHistorialFechaHasta.setText("Hasta");
    }
    @SuppressLint("SetTextI18n")
    private void cargandoLista() {
        b.tvCargandoHistorial.setVisibility(VISIBLE);
        b.tvCargandoHistorial.setText("Cargando Ventas...");

        VentaAdapter adapter = new VentaAdapter(List.of(), getContext(), getLayoutInflater());
        b.rvHistorial.setAdapter(adapter);
    }
}