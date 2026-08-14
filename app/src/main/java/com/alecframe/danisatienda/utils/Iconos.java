package com.alecframe.danisatienda.utils;


import com.alecframe.danisatienda.R;

import java.util.List;

public class Iconos {
    public static final List<Icono> ICONOS = List.of(
            new Icono("Ninguno", R.drawable.remove_24px, "remove_24px"),
            new Icono("Medialuna", R.drawable.bakery_dining_24px, "bakery_dining_24px"),
            new Icono("Galletas", R.drawable.cookie_24px, "cookie_24px"),
            new Icono("Cena", R.drawable.dining_24px, "dining_24px"),
            new Icono("Té", R.drawable.emoji_food_beverage_24px, "emoji_food_beverage_24px"),
            new Icono("Pastel", R.drawable.cake_full_24px, "cake_full_24px"),
            new Icono("Comida rápida", R.drawable.fastfood_24px, "fastfood_24px"),
            new Icono("Crema", R.drawable.shaved_ice_24px, "shaved_ice_24px"),
            new Icono("Hanami", R.drawable.hanami_dango_24px, "hanami_dango_24px"),
            new Icono("Huevo", R.drawable.egg_24px, "egg_24px"),
            new Icono("Huevo revuelto", R.drawable.egg_alt_24px, "egg_alt_24px"),
            new Icono("Naranja", R.drawable.nutrition_24px, "nutrition_24px"),
            new Icono("Hoja", R.drawable.nest_eco_leaf_24px, "nest_eco_leaf_24px"),
            new Icono("Mascota", R.drawable.pets_24px, "pets_24px"),
            new Icono("Vaso", R.drawable.glass_cup_24px, "glass_cup_24px"),
            new Icono("Bebidas", R.drawable.liquor_24px, "liquor_24px"),
            new Icono("Botella", R.drawable.water_bottle_24px, "water_bottle_24px"),
            new Icono("Botella grande", R.drawable.water_bottle_large_24px, "water_bottle_large_24px"),
            new Icono("Agua", R.drawable.water_drop_24px, "water_drop_24px"),
            new Icono("Pizza", R.drawable.local_pizza_24px, "local_pizza_24px"),
            new Icono("Helado", R.drawable.icecream_24px, "icecream_24px"),
            new Icono("Frío", R.drawable.snowflake_24px, "snowflake_24px"),
            new Icono("Nevera", R.drawable.kitchen_24px, "kitchen_24px"),
            new Icono("Fuego", R.drawable.local_fire_department_24px, "local_fire_department_24px"),
            new Icono("Horno", R.drawable.oven_24px, "local_fire_department_24px"),
            new Icono("Caliente", R.drawable.onsen_24px, "onsen_24px"),
            new Icono("Lapiz", R.drawable.edit_24px, "edit_24px"),
            new Icono("Pintura", R.drawable.palette_24px, "palette_24px"),
            new Icono("Spray", R.drawable.cleaning_24px, "cleaning_24px"),
            new Icono("Cuidado personal", R.drawable.clean_hands_24px, "clean_hands_24px"),
            new Icono("Diente", R.drawable.dentistry_24px, "dentistry_24px"),
            new Icono("Escoba", R.drawable.cleaning_services_24px, "cleaning_services_24px"),
            new Icono("Guardarropa", R.drawable.checkroom_24px, "checkroom_24px"),
            new Icono("Robot", R.drawable.smart_toy_24px, "smart_toy_24px"),
            new Icono("Juego", R.drawable.stadia_controller_24px, "stadia_controller_24px"),
            new Icono("Calavera", R.drawable.skull_24px, "skull_24px"),
            new Icono("Variado", R.drawable.store_24px, "store_24px"),
            new Icono("Caja", R.drawable.box_add_24px, "box_add_24px"),
            new Icono("Documento", R.drawable.description_24px, "description_24px"),
            new Icono("Estrella", R.drawable.star_24px_fill, "star_24px_fill"),
            new Icono("Vendido", R.drawable.sell_24px, "sell_24px"),
            new Icono("Regalo", R.drawable.featured_seasonal_and_gifts_24px, "featured_seasonal_and_gifts_24px"),
            new Icono("Caja", R.drawable.point_of_sale_24px, "point_of_sale_24px"),
            new Icono("Pesa", R.drawable.scale_24px, "scale_24px"),
            new Icono("Impresión", R.drawable.print_24px, "print_24px"),
            new Icono("Cerdito", R.drawable.savings_24px, "savings_24px"),
            new Icono("Médicamentos", R.drawable.medication_24px, "medication_24px"),
            new Icono("Caja médica", R.drawable.medical_services_24px, "medical_services_24px"),
            new Icono("Estetoscopio", R.drawable.stethoscope_24px, "stethoscope_24px")
    );

    public static int getIdByName(String name) {
        for (Icono i: ICONOS) {
            if (i.getDrawableNombre().equals(name)) {
                return i.getDrawable();
            }
        }
        return 0;
    }

    public static int getIndexByName(String name) {
        for (int i=0; i<ICONOS.size(); i++) {
            if (ICONOS.get(i).getDrawableNombre().equals(name)) {
                return i;
            }
        }
        return -1;
    }
}
