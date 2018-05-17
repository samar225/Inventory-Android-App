package com.example.snl41_000.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.snl41_000.inventoryapp.data.InventoryContract;
import com.example.snl41_000.inventoryapp.data.InventoryContract.InventoryEntry;
import com.example.snl41_000.inventoryapp.data.InventoryCursorAdapter;


public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int INVENTORY_LOADER = 0;
    public static long lo;
    InventoryCursorAdapter inventoryCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, AddNewProduct.class);
                startActivity(intent);
            }
        });
        // Find the ListView which will be populated with the inventory data
        ListView listView = findViewById(R.id.list);


        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        inventoryCursorAdapter = new InventoryCursorAdapter(this, null);
        listView.setAdapter(inventoryCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CatalogActivity.this, ViewActivity.class);
                Uri uri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI, l);
                lo = l;
                intent.setData(uri);
                startActivity(intent);

            }
        });

        getLoaderManager().initLoader(INVENTORY_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.cd ..
        getMenuInflater().inflate(R.menu.catalog_menu, menu);
        return true;
    }

    /**
     * Helper method to insert hardcoded inventory data into the database. For debugging purposes only.
     */
    private void insertInventory() {

        // Gets the database in write mode
        // Create a ContentValues object where column names are the keys,
        //  attributes are the values.
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_PRODUCT_NAME, "strawberry cake");
        values.put(InventoryEntry.COLUMN_PRICE, 10);
        values.put(InventoryEntry.COLUMN_QUANTITY, 2);
        values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, "Youssef");
        values.put(InventoryEntry.COLUMN_SUPPLIER_PHONE, 0000000);
        getContentResolver().insert(InventoryEntry.CONTENT_URI, values);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertInventory();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllInventories();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                InventoryContract.InventoryEntry._ID,
                InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_PRICE,
                InventoryEntry.COLUMN_QUANTITY,
                InventoryEntry.COLUMN_SUPPLIER_NAME,
                InventoryEntry.COLUMN_SUPPLIER_PHONE};
        return new CursorLoader(this, InventoryContract.InventoryEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        inventoryCursorAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        inventoryCursorAdapter.swapCursor(null);

    }

    /**
     * Helper method to delete all inventories in the database.
     */
    private void deleteAllInventories() {
        int rowsDeleted = getContentResolver().delete(InventoryEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from products database");
    }
}