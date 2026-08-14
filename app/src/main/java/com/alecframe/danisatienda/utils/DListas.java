package com.alecframe.danisatienda.utils;

import com.alecframe.danisatienda.R;

import java.util.List;

public class DListas {
    public static final List<SpinnerNDC> TIPO_PAGOS = List.of(
            new SpinnerNDC("Todos", R.drawable.add_24px, 0xE0E2E9),
            new SpinnerNDC("Efectivo", R.drawable.payments_24px, 0x24AD50),
            new SpinnerNDC("Transferencia", R.drawable.credit_card_24px, 0xFF3636E2)
    );

    public static final List<SpinnerNDC> ENTIDADES = List.of(
            new SpinnerNDC("Todos", R.drawable.remove_24px, 0xEBEBEB),
            new SpinnerNDC("Producto", R.drawable.shopping_bag_24px, 0xAC55FA),
            new SpinnerNDC("Categoría", R.drawable.sell_24px, 0xFAE454),
            new SpinnerNDC("Alias", R.drawable.multiple_stop_24px, 0x579BFA),
            new SpinnerNDC("Venta", R.drawable.receipt_long_24px, 0x56FA5D),
            new SpinnerNDC("Gasto", R.drawable.account_balance_wallet_24px, 0xFA5D5D)
    );

    public static final List<SpinnerNDC> ACCIONES = List.of(
            new SpinnerNDC("Todas", R.drawable.remove_24px, 0xEBEBEB),
            new SpinnerNDC("CREAR", R.drawable.add_24px, 0x56FA5D),
            new SpinnerNDC("MODIFICAR", R.drawable.edit_24px, 0xAC55FA),
            new SpinnerNDC("ACTIVAR", R.drawable.keyboard_arrow_up_24px, 0x579BFA),
            new SpinnerNDC("DESACTIVAR", R.drawable.keyboard_arrow_down_24px, 0xFA5D5D)
    );

    public static final List<String> TIPOS_UNDIADES = List.of(
            "Unidad",
            "Gramo"
    );

    public static final List<String> GASTOS = List.of(
            "Alquiler",
            "Servicios",
            "Mercadería",
            "Sueldos",
            "Mantenimiento",
            "Transporte",
            "Impuestos",
            "Publicidad",
            "Insumos",
            "Comisiones",
            "Otros"
    );

    public static final List<String> GASTOS2 = List.of(
            "Todos",
            "Alquiler",
            "Servicios",
            "Mercadería",
            "Sueldos",
            "Mantenimiento",
            "Transporte",
            "Impuestos",
            "Publicidad",
            "Insumos",
            "Comisiones",
            "Otros"
    );
}
