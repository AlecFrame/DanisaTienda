package com.alecframe.danisatienda.ui.alias;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.lifecycle.ViewModelProvider;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alecframe.danisatienda.R;
import com.alecframe.danisatienda.databinding.FragmentAliasDetalleBinding;
import com.alecframe.danisatienda.model.Alias;

import java.util.Objects;

public class AliasDetalleFragment extends Fragment {
    private AliasDetalleViewModel vm;
    private FragmentAliasDetalleBinding b;
    private String viewMode = "ver";
    private int estado = 1;
    private Alias aliasActual;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(AliasDetalleViewModel.class);
        b = FragmentAliasDetalleBinding.inflate(getLayoutInflater());

        viewModelSettings();

        b.btAliasEditar.setOnClickListener(v -> {
            vm.setViewMode("editar");
            comprobarCambios();
        });

        b.btAliasCancelar.setOnClickListener(v -> {
            if (Objects.equals(viewMode, "editar")) {
                vm.setViewMode("ver");
                cargarAlias(aliasActual);
            }else {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main)
                        .popBackStack();
            }
        });

        b.btAliasGuardar.setOnClickListener(v -> {
            procesoCambioEnCurso();
            if (viewMode.equals("crear")) {
                vm.crearAlias(
                        b.etAliasValor.getEditText().getText().toString(),
                        b.etAliasBanco.getEditText().getText().toString(),
                        b.etAliasPropietario.getEditText().getText().toString()
                );
            }else {
                vm.actualizarAlias(
                        b.etAliasValor.getEditText().getText().toString(),
                        b.etAliasBanco.getEditText().getText().toString(),
                        b.etAliasPropietario.getEditText().getText().toString()
                );
            }
        });

        b.btAliasActivar.setOnClickListener(v -> {
            procesoCambioEnCurso();
            if (estado==1) {
                vm.desactivarAlias();
            }else {
                vm.activarAlias();
            }
        });

        procesoCambioEnCurso();

        return b.getRoot();
    }
    private void viewModelSettings() {
        vm.getAlias().observe(getViewLifecycleOwner(), alias -> {
            procesoCambioEnTerminado();
            aliasActual = alias;

            cargarAlias(alias);
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
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main)
                    .popBackStack();
        });

        vm.getEstado().observe(getViewLifecycleOwner(), e -> {
            estado = e;
            procesoCambioEnTerminado();
            if (e==1) {
                b.tvAliasDetalleActivado.setVisibility(INVISIBLE);
                b.btAliasActivar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red_dark)));
                b.btAliasActivar.setText("Desactivar");
            }else {
                b.tvAliasDetalleActivado.setVisibility(VISIBLE);
                b.btAliasActivar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_dark)));
                b.btAliasActivar.setText("Activar");
            }
        });

        vm.recuperarDatos(getArguments());
    }
    private void cargarAlias(Alias alias) {
        b.etAliasValorInput.setText(alias.getValor());
        b.etAliasBancoInput.setText(alias.getBanco());
        b.etAliasPropietarioInput.setText(alias.getPropietario());
    }
    private void setEditMode() {
        b.etAliasValor.setEnabled(true);
        b.etAliasBanco.setEnabled(true);
        b.etAliasPropietario.setEnabled(true);
        b.llAliasInferiorEditar.setVisibility(VISIBLE);
        b.llAliasInferiorVer.setVisibility(INVISIBLE);
    }
    private void setVerMode() {
        b.etAliasValor.setEnabled(false);
        b.etAliasBanco.setEnabled(false);
        b.etAliasPropietario.setEnabled(false);
        b.llAliasInferiorEditar.setVisibility(INVISIBLE);
        b.llAliasInferiorVer.setVisibility(VISIBLE);
    }
    private void procesoCambioEnCurso() {
        b.btAliasGuardar.setEnabled(false);
        b.btAliasCancelar.setEnabled(false);
        b.btAliasActivar.setEnabled(false);
        b.btAliasEditar.setEnabled(false);
    }
    private void procesoCambioEnTerminado() {
        comprobarCambios();
        b.btAliasCancelar.setEnabled(true);
        b.btAliasActivar.setEnabled(true);
        b.btAliasEditar.setEnabled(true);
    }
    private void agregarWacher() {
        if (aliasActual!=null) {
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

            b.etAliasValorInput.addTextChangedListener(watcher);
            b.etAliasBancoInput.addTextChangedListener(watcher);
            b.etAliasPropietarioInput.addTextChangedListener(watcher);
        }
    }
    public void comprobarCambios() {
        if (viewMode.equals("editar")) {
            if (aliasActual != null) {
                boolean noHayCambio =
                        b.etAliasValor.getEditText().getText().toString().equals(aliasActual.getValor()) &
                        b.etAliasBanco.getEditText().getText().toString().equals(aliasActual.getBanco()) &
                        b.etAliasPropietario.getEditText().getText().toString().equals(aliasActual.getPropietario());

                if (noHayCambio) {
                    b.btAliasGuardar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray_light1)));
                    b.btAliasGuardar.setEnabled(false);
                } else {
                    b.btAliasGuardar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_dark)));
                    b.btAliasGuardar.setEnabled(true);
                }
            }
        }else {
            b.btAliasGuardar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_dark)));
            b.btAliasGuardar.setEnabled(true);
        }
    }
}