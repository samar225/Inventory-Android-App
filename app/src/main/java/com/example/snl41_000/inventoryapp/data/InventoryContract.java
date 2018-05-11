package com.example.snl41_000.inventoryapp.data;

import android.provider.BaseColumns;

public final class InventoryContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private InventoryContract() {}

    /**
     * Inner class that defines constant values for the inventory database table.
     * Each entry in the table represents a single inventory.
     */
    public static final class InventoryEntry implements BaseColumns {

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
        public final static String COLUMN_PRICE = "Price" ;

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

