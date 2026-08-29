package com.alecframe.danisatienda;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import com.alecframe.danisatienda.databinding.ActivityMainBinding;
import com.alecframe.danisatienda.request.ApiClient;
import com.alecframe.danisatienda.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding b;
    private MainViewModel vm;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO // MODE_NIGHT_FOLLOW_SYSTEM
        );

        setSupportActionBar(b.appBarMain.toolbar);

        initNavigation();
        initDrawerMenu();

        // ----- Cosas del ViewModel ----- //
        vm = new ViewModelProvider(this).get(MainViewModel.class);

        vm.getToastMessage().observe(this, message -> {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });

        vm.getAlias().observe(this, alias -> {
            if (alias!=null) {
                TextView tv = b.navView.getHeaderView(0).findViewById(R.id.tvHeaderAlias);
                tv.setText("alias: "+alias.getValor());
            }
        });

        ApiClient.aplicarConfiguracion(getApplication());
    }

    private void initNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        if (navHostFragment == null) return;
        navController = navHostFragment.getNavController();

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.inicioFragment,
                R.id.inventarioFragment,
                R.id.ventasFragment,
                R.id.registroVentasFragment,
                R.id.categoriasFragment,
                R.id.aliasFragment,
                R.id.gastosFragment,
                R.id.actividadFragment,
                R.id.configuracionFragment
        )
                .setOpenableLayout(b.drawerLayout)
                .build();

        NavigationUI.setupActionBarWithNavController(
                this,
                navController,
                appBarConfiguration
        );
    }

    private void initDrawerMenu() {
        b.drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                // Acciones al abrir el drawer
            }
        });

        b.navView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_logout) {
                showLogoutDialog();
                return true;
            }
            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            if (handled) {
                b.drawerLayout.closeDrawers();
            }
            return handled;
        });
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("¿Estás seguro que querés salir de la sesión?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    logout();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();

        b.drawerLayout.closeDrawers();
    }
    private void logout() {
        ApiClient.eliminarCredenciales(getApplication());
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}