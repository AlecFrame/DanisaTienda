package com.alecframe.danisatienda.ui.ventas;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alecframe.danisatienda.model.Alias;
import com.alecframe.danisatienda.model.Carrito;
import com.alecframe.danisatienda.model.CarritoDetalle;
import com.alecframe.danisatienda.model.Producto;
import com.alecframe.danisatienda.model.Venta;
import com.alecframe.danisatienda.request.ApiClient;
import com.alecframe.danisatienda.request.ApiServiceAlias;
import com.alecframe.danisatienda.request.ApiServiceVentas;
import com.alecframe.danisatienda.request.CrearVentaRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarritoViewModel extends AndroidViewModel {
    private final MutableLiveData<String> mToastMessage = new MutableLiveData<>();
    private final MutableLiveData<Venta> mVenta = new MutableLiveData<>();
    private final MutableLiveData<Carrito> mCarrito = new MutableLiveData<>(new Carrito());
    private final MutableLiveData<List<CarritoDetalle>> mListaDetalles = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Alias>> mListaAlias = new MutableLiveData<>();
    private final MutableLiveData<Integer> mIdAlias = new MutableLiveData<>();
    private final MutableLiveData<String> mTipoPago = new MutableLiveData<>("Efectivo");
    private MutableLiveData<Double> mMontoTotal = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mBack = new MutableLiveData<>();


    public CarritoViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<String> getToastMessage() {
        return mToastMessage;
    }
    public LiveData<Venta> getVenta() {
        return mVenta;
    }
    public LiveData<Carrito> getCarrito() {
        return mCarrito;
    }
    public LiveData<List<CarritoDetalle>> getListaDetalles() {
        return mListaDetalles;
    }
    public LiveData<List<Alias>> getListaAlias() {
        return mListaAlias;
    }
    public LiveData<Integer> getIdAlias() {
        return mIdAlias;
    }
    public LiveData<String> getTipoPago() {
        return mTipoPago;
    }
    public LiveData<Double> getMontoTotal() {
        return mMontoTotal;
    }
    public LiveData<Boolean> getBack() {
        return mBack;
    }
    public void setBack(boolean back) {
        mBack.setValue(back);
    }
    public void cambiarIdAlias(Integer idAlias) {
        mIdAlias.setValue(idAlias);
    }
    public void cambiarTipoPago(String tipoPago) {
        mTipoPago.setValue(tipoPago);
    }
    public void calcularMontoTota() {
        double montoTotal = 0;

        if (mListaDetalles.getValue()!=null) {
            for (CarritoDetalle cd : mListaDetalles.getValue()) {
                montoTotal+=cd.getSubtotal();
            }
            mMontoTotal.setValue(montoTotal);
        }else {
            mToastMessage.setValue("Detalles no cargados para el calculo");
        }
    }
    public void cargarSpinnerAlias() {
        ApiServiceAlias servicio = ApiClient.getApiServiceAlias(getApplication());

        Call<List<Alias>> call = servicio.obtenerTodosLosAlias(1);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Alias>> call, Response<List<Alias>> response) {
                if (response.isSuccessful()) {
                    mListaAlias.postValue(response.body());
                } else {
                    mToastMessage.postValue("Error al cargar los alias");
                    Log.d("CARGAR ALIAS", "Error: "+response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Alias>> call, Throwable t) {
                mToastMessage.postValue("Error del servidor.");
                Log.d("CARGAR ALIAS ERROR", "DRAWABLE: call: "+call);
                Log.d("CARGAR ALIAS ERROR", "DRAWABLE: throwable: "+t);
            }
        });
    }


    private final MutableLiveData<List<Producto>> mListaProductosEnCarrito = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Producto>> getListaProductosEnCarrito() {
        return mListaProductosEnCarrito;
    }
    public void agregarProductoCarrito(Producto producto) {
        mListaProductosEnCarrito.getValue().add(producto);
    }
    public void removerProductoCarrito(Producto producto) {
        mListaProductosEnCarrito.getValue().remove(producto);
    }
    public void agregarCarritoDetalle(String cantidadS, String subTotalS, Producto producto) {
        if (producto!=null) {
            if (cantidadS.isBlank() | subTotalS.isBlank()) {
                mToastMessage.setValue("Faltan campos por rellenar");
                return;
            }

            int cantidad = Integer.parseInt(cantidadS);
            double subTotal = Double.parseDouble(subTotalS);

            if (mListaProductosEnCarrito.getValue().contains(producto)) {
                mToastMessage.setValue("El producto ya se encuentra en el Carrito");
                return;
            }

            CarritoDetalle carritoDetalle = new CarritoDetalle(0, 0, producto.getIdProducto(), cantidad,
                    producto.getPrecio(), producto.getCostoCompra(), subTotal, null, producto);

            mListaDetalles.getValue().add(carritoDetalle);
            agregarProductoCarrito(producto);
            mBack.setValue(true);
        }else {
            mToastMessage.setValue("Producto no detectado");
            Log.d("CARRITO VIEW MODEL", "Producto no detectado al crear el carrito detalle");
        }
    }
    public void actualizarCarritoDetalle(String cantidadS, String subTotalS, CarritoDetalle carritoDetalle, Producto producto) {
        if (carritoDetalle!=null) {
            if (producto != null) {
                if (cantidadS.isBlank() | subTotalS.isBlank()) {
                    mToastMessage.setValue("Faltan campos por rellenar");
                    return;
                }

                int cantidad = Integer.parseInt(cantidadS);
                double subTotal = Double.parseDouble(subTotalS);

                carritoDetalle.setCantidad(cantidad);
                carritoDetalle.setSubtotal(subTotal);

                mListaDetalles.getValue().set(carritoDetalle.getIdCarritoDetalle(), carritoDetalle);
                mBack.setValue(true);
            } else {
                mToastMessage.setValue("Producto no detectado");
                Log.d("CARRITO VIEW MODEL", "Producto no detectado al crear el carrito detalle");
            }
        }else {
            mToastMessage.setValue("CarritoDetalle no detectado");
            Log.d("CARRITO VIEW MODEL", "Carrito Detalle no detectado al actualizarlo");
        }
    }
    public void removerCarritoDetalle(CarritoDetalle carritoDetalle, Producto producto) {
        if (carritoDetalle!=null) {
            if (producto!=null) {
                carritoDetalle.setIdCarritoDetalle(0);

                mListaDetalles.getValue().remove(carritoDetalle);
                removerProductoCarrito(producto);
                mBack.setValue(true);
            } else {
                mToastMessage.setValue("Producto no detectado");
                Log.d("CARRITO VIEW MODEL", "Producto no detectado al remover el carrito detalle");
            }
        }else {
            mToastMessage.setValue("CarritoDetalle no detectado");
            Log.d("CARRITO VIEW MODEL", "Carrito Detalle no detectado al remover");
        }
    }

    public void registrarVenta(String montoTotalS) {
        if (montoTotalS.isBlank()) {
            mToastMessage.postValue("No se encontro el monto total"); return;
        }
        if (mTipoPago.getValue()==null) {
            mToastMessage.postValue("No se encontro el tipo de Pago"); return;
        }
        if (mTipoPago.getValue().equals("Transferencia")) {
            if (mIdAlias.getValue() == null) {
                mToastMessage.postValue("No se encontro el alias"); return;
            }
        }
        if (mListaDetalles.getValue()==null) {
            mToastMessage.postValue("No se encontraron los detalles"); return;
        }

        double montoTotal = Double.parseDouble(montoTotalS);
        Integer idAlias = mIdAlias.getValue();

        if (!mTipoPago.getValue().equals("Transferencia")) {
            idAlias = null;
        }

        ApiServiceVentas servicio = ApiClient.getApiServiceVentas(getApplication());

        CrearVentaRequest request = new CrearVentaRequest(
                mTipoPago.getValue(),
                idAlias,
                montoTotal,
                mListaDetalles.getValue()
        );

        Call<Venta> call = servicio.crearVenta(request);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Venta> call, Response<Venta> response) {
                if (response.isSuccessful()) {
                    mVenta.postValue(response.body());
                } else {
                    mToastMessage.postValue("Error al registrar la venta");
                    Log.d("CREAR VENTA", "Error: "+response.message());
                }
            }
            @Override
            public void onFailure(Call<Venta> call, Throwable t) {
                mToastMessage.postValue("Error del servidor.");
                Log.d("CREAR VENTA ERROR", "DRAWABLE: call: "+call);
                Log.d("CREAR VENTA ERROR", "DRAWABLE: throwable: "+t);
            }
        });
    }

    public void limpiarCarrito() {
        mCarrito.setValue(new Carrito());
        mVenta.setValue(null);
        mListaDetalles.setValue(new ArrayList<>());
        mListaProductosEnCarrito.setValue(new ArrayList<>());
        mTipoPago.setValue("Efectivo");
        mMontoTotal = new MutableLiveData<>();
    }
}