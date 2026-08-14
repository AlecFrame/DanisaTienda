package com.alecframe.danisatienda.ui.configuracion;

import androidx.lifecycle.ViewModelProvider;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alecframe.danisatienda.R;
import com.alecframe.danisatienda.databinding.FragmentConfiguracionBinding;
import com.alecframe.danisatienda.request.ApiClient;
import com.alecframe.danisatienda.utils.Configuracion;

public class ConfiguracionFragment extends Fragment {

    private ConfiguracionViewModel vm;
    private FragmentConfiguracionBinding b;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(ConfiguracionViewModel.class);
        b = FragmentConfiguracionBinding.inflate(getLayoutInflater());

        vm.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        vm.getConexionServidor().observe(getViewLifecycleOwner(), result -> {
            if (result==0) {
                b.tvConfigConexion.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                b.tvConfigConexion.setText("Servidor No Encontrado");
            }else
            if (result==1) {
                b.tvConfigConexion.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                b.tvConfigConexion.setText("Servidor Conectado");
            }else
            if (result==2) {
                b.tvConfigConexion.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray_light1)));
                b.tvConfigConexion.setText("Procesando Conexion");
            }else
            if (result==3) {
                b.tvConfigConexion.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange_advertence)));
                b.tvConfigConexion.setText("Fallo del servidor");
            }else  {
                b.tvConfigConexion.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                b.tvConfigConexion.setText("Resultado extraño");
            }
        });

        aplicarCambios();

        b.btConfigProbarCambios.setOnClickListener(v -> {
            ApiClient.guardarConfiguracion(getContext(), new Configuracion(
                    b.etConfigUrlServidor.getEditText().getText().toString(),
                    b.etConfigUsuario.getEditText().getText().toString()
            ));
            vm.probarServidor();
        });

        vm.probarServidor();

        return b.getRoot();
    }

    private void aplicarCambios() {
        b.etConfigUrlServidorInput.setText(ApiClient.BASE_URL);
        b.etConfigUsuarioInput.setText(ApiClient.USUARIO);
    }

}