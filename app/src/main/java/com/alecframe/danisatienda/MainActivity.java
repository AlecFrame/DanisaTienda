package com.alecframe.danisatienda;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            if (handled) {
                b.drawerLayout.closeDrawers();
            }
            return handled;
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}