package com.example.snl41_000.inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.snl41_000.inventoryapp.data.InventoryContract;
import com.example.snl41_000.inventoryapp.data.InventoryContract.InventoryEntry;

public class AddNewProduct extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public EditText mNameEditText;
    public int price;
    private Uri mCurrentInventoryUri;
    private EditText mPriceEditText;
    private EditText mSupplierName, mSupplierNumber;
    private TextView mQuantityEditText;
    private int quantity = 1;
    private int mquantity = 0;
    private int mprice = 0;
    private Button increament, decreament;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentInventoryUri = intent.getData();
        if (mCurrentInventoryUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_product));
            invalidateOptionsMenu();
        }
        // Find all relevant views that we will need to read user input from
        mNameEditText = findViewById(R.id.edit_inventory_name);
        mPriceEditText = findViewById(R.id.edit_inventory_price);
        mQuantityEditText = findViewById(R.id.edit_inventory_quantity);
        mSupplierName = findViewById(R.id.edit_inventory_supplier_name);
        mSupplierNumber = findViewById(R.id.edit_inventory_supplier_phone);

        increament = findViewById(R.id.increament);
        increament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increament(mQuantityEditText);
            }
        });
        decreament = findViewById(R.id.decreament);
        decreament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreament(mQuantityEditText);
            }
        });

    }

    private void saveInventory() {
        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String suppliername = mSupplierName.getText().toString().trim();
        String supplierphone = mSupplierNumber.getText().toString().trim();
        if (priceString == null) {
            priceString = "0";
        }
        if (quantityString == "1") {
            quantityString = "0";
        }
        //and check if all the fields in the editor are blank
        if (TextUtils.isEmpty(nameString) || TextUtils.isEmpty(quantityString) || TextUtils.isEmpty(suppliername) ||
               TextUtils.isEmpty(supplierphone) || mquantity == Integer.parseInt(quantityString)
                || mprice == Integer.parseInt(priceString)) {
            Toast.makeText(this, "Enter all information please, Don't leave space please, try again", Toast.LENGTH_LONG).show();
            // Since no fields were modified, we can return early without creating a new pet.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }

        // and Toto's pet attributes are the values.
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_PRODUCT_NAME, nameString);
        int price = 0;
        if (!TextUtils.isEmpty(priceString)) {
            price = Integer.parseInt(priceString);
        }
        values.put(InventoryEntry.COLUMN_PRICE, price);
        values.put(InventoryEntry.COLUMN_QUANTITY, quantityString);
        values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, suppliername);
        values.put(InventoryEntry.COLUMN_SUPPLIER_PHONE, supplierphone);

        // Determine if this is a new or existing pet by checking if mCurrentPetUri is null or not
        if (mCurrentInventoryUri == null) {
            // This is a NEW pet, so insert a new pet into the provider,
            // returning the content URI for the new pet.
            Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_inventory_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_inventory_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // Otherwise this is an EXISTING pet, so update the pet with content URI: mCurrentPetUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentPetUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(mCurrentInventoryUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_inventory_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_inventory_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void increament(View view) {
        quantity = Integer.parseInt(mQuantityEditText.getText().toString());
        quantity = quantity + 1;
        display(quantity);
    }

    public void decreament(View view) {

        quantity = Integer.parseInt(mQuantityEditText.getText().toString());
        if (quantity > 0) {
            quantity = quantity - 1;
            display(quantity);
        } else {
            return;
        }
    }

    private void display(int number) {
        mQuantityEditText.setText("" + number);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Do nothing for now
                saveInventory();
                finish();

                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the pet hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                saveInventory();
                Intent intent = new Intent(AddNewProduct.this, CatalogActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                InventoryContract.InventoryEntry._ID,
                InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_PRICE,
                InventoryEntry.COLUMN_QUANTITY,
                InventoryEntry.COLUMN_SUPPLIER_NAME,
                InventoryEntry.COLUMN_SUPPLIER_PHONE};
        return new CursorLoader(this, mCurrentInventoryUri,
                projection, null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY);
            int sunameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NAME);
            int phoneColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_PHONE);

            // Extract out the value from the Cursor for the given column index
            String nameString = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String suppliername = cursor.getString(sunameColumnIndex);
            String supplierphone = cursor.getString(phoneColumnIndex);
            // Update the views on the screen with the values from the database
            mNameEditText.setText(nameString);
            mPriceEditText.setText(Integer.toString(price));
            mQuantityEditText.setText(Integer.toString(quantity));

            mSupplierName.setText(suppliername);
            mSupplierNumber.setText(supplierphone);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText("");
        mSupplierName.setText("");
        mSupplierNumber.setText("");
    }


    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    /**
     * Prompt the user to confirm that they want to delete this pet.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deletePet();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the pet in the database.
     */
    private void deletePet() {
        // Only perform the delete if this is an existing pet.
        if (mCurrentInventoryUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentInventoryUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_inventory_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_inventory_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }
}
