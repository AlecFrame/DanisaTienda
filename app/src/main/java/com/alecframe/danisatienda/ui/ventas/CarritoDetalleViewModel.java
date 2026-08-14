package com.alecframe.danisatienda.ui.ventas;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alecframe.danisatienda.model.CarritoDetalle;
import com.alecframe.danisatienda.model.Producto;

public class CarritoDetalleViewModel extends AndroidViewModel {
    private final MutableLiveData<String> mToastMessage = new MutableLiveData<>();
    private final MutableLiveData<CarritoDetalle> mCarritoDetalle = new MutableLiveData<>();
    private final MutableLiveData<Producto> mProducto = new MutableLiveData<>();

    public CarritoDetalleViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<String> getToastMessage() {
        return mToastMessage;
    }

    public LiveData<Producto> getProducto() {
        return mProducto;
    }
    public LiveData<CarritoDetalle> getCarritoDetalle() {
        return mCarritoDetalle;
    }

    public void cargarArguments(Bundle bundle) {
        Producto p = bundle.getSerializable("producto", Producto.class);
        CarritoDetalle cd = bundle.getSerializable("carritoDetalle", CarritoDetalle.class);
        if (p!=null) { mProducto.setValue(p); }
        if (cd!=null) { mCarritoDetalle.setValue(cd); }
    }
}
