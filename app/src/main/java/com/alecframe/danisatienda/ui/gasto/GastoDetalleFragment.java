package com.alecframe.danisatienda.ui.gasto;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.alecframe.danisatienda.R;
import com.alecframe.danisatienda.databinding.FragmentGastoDetalleBinding;
import com.alecframe.danisatienda.ui.adapters.SpinnerUnidadAdapter;
import com.alecframe.danisatienda.utils.DListas;

public class GastoDetalleFragment extends Fragment {
    private GastoDetalleViewModel vm;
    private FragmentGastoDetalleBinding b;
    private String categoria = "Otros";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(GastoDetalleViewModel.class);
        b = FragmentGastoDetalleBinding.inflate(getLayoutInflater());

        vm.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        vm.getProcesoTerminado().observe(getViewLifecycleOwner(), procesoTerminado -> {
            procesoCambioEnTerminado();
        });

        spinnerCategoria();

        vm.getBack().observe(getViewLifecycleOwner(), result -> {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main)
                    .popBackStack();
        });

        b.btGastoCancelar.setOnClickListener(v -> {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main)
                    .popBackStack();
        });

        b.btGastoGuardar.setOnClickListener(v -> {
            procesoCambioEnCurso();
            vm.crearGasto(
                    b.etGastoDescripcion.getEditText().getText().toString(),
                    categoria,
                    b.etGastoMonto.getEditText().getText().toString()
            );
        });

        return b.getRoot();
    }
    private void spinnerCategoria() {
        SpinnerUnidadAdapter adapter = new SpinnerUnidadAdapter(getContext(), DListas.GASTOS);
        b.spGastoNuevoCategorias.setAdapter(adapter);

        b.spGastoNuevoCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoria = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    private void procesoCambioEnCurso() {
        b.btGastoGuardar.setEnabled(false);
        b.btGastoCancelar.setEnabled(false);
    }
    private void procesoCambioEnTerminado() {
        b.btGastoGuardar.setEnabled(true);
        b.btGastoCancelar.setEnabled(true);
    }
}