package com.example.snl41_000.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class InventoryContract {
    //Possible path (appended to base content URI for possible URI's)
    public static final String PATH_INVENTORY = "inventory";
    public static final String CONTENT_AUTHORITY = "com.example.snl41_000.inventoryapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private InventoryContract() {
    }

    /**
     * Inner class that defines constant values for the inventory database table.
     * Each entry in the table represents a single inventory.
     */
    public static final class InventoryEntry implements BaseColumns {

       // The MIME type of the {@link #CONTENT_URI} for a list of Inventories.
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;
        // The MIME type of the {@link #CONTENT_URI} for a single Inventory.
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);

        /**
         * Name of database table for inventory
         */
        public final static String TABLE_NAME = "inventory";

        /**
         * Unique ID number for the inventory (only for use in the database table).
         * <p>
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Product Name Price of the inventory.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_PRODUCT_NAME = "name";

        /**
         * Price of the inventory.
         * <p>
         * Type: INTEGER
         */
        public final static String COLUMN_PRICE = "Price";

        /**
         * Supplier Phone Number of the inventory.
         * <p>
         * Type: INTEGER
         */

        public final static String COLUMN_SUPPLIER_PHONE = "Supplier_Phone_Number";

        /**
         * Quantity of the inventory.
         * <p>
         * Type: INTEGER
         */
        public final static String COLUMN_QUANTITY = "Quantity";

        /**
         * Supplier Name Price of the inventory.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_SUPPLIER_NAME = "Supplier_name";
    }
}

