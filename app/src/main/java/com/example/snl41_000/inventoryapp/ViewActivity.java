package com.example.snl41_000.inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.snl41_000.inventoryapp.data.InventoryContract;
import com.example.snl41_000.inventoryapp.data.InventoryContract.InventoryEntry;

public class ViewActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_INVENTORY_LOADER = 0;
    public TextView mNameEditText;
    private Uri mCurrentInventoryUri;
    private TextView mPriceEditText;
    private TextView  mSupplierName, mSupplierNumber;
    private TextView mQuantityEditText;
    private boolean mHasChanged = false;
    private int quantity;
    private Button increament, decreament;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
              super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        Intent intent = getIntent();
        mCurrentInventoryUri = intent.getData();
        if (mCurrentInventoryUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_product));
            invalidateOptionsMenu();
        } else {
            setTitle("View Product");
            getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
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

        FloatingActionButton call = findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = mSupplierNumber.getText().toString().trim();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });

        Button view = findViewById(R.id.view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewActivity.this, EditorActivity.class);
                Uri uri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI, CatalogActivity.lo);
                intent.setData(uri);
                startActivity(intent);
            }
        });

    }

    private void saveInventory() {
        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String suppliername = mSupplierName.getText().toString().trim();
        String supplierphone = mSupplierNumber.getText().toString().trim();

        // and check if all the fields in the editor are blank
        if (mCurrentInventoryUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(priceString)  && TextUtils.isEmpty(suppliername) &&
                 TextUtils.isEmpty(supplierphone)) {
            // Since no fields were modified, we can return early without creating a new inventory.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }


        // and Toto's inventory attributes are the values.
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_PRODUCT_NAME, nameString);
        values.put(InventoryEntry.COLUMN_PRICE, priceString);
        values.put(InventoryEntry.COLUMN_QUANTITY, quantityString);
        values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, suppliername);
        values.put(InventoryEntry.COLUMN_SUPPLIER_PHONE, supplierphone);

        // Determine if this is a new or existing inventory by checking if mCurrentInventoryUri is null or not
        if (mCurrentInventoryUri == null) {
            // This is a NEW inventory, so insert a new inventory into the provider,
            // returning the content URI for the new inventory.
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
            // Otherwise this is an EXISTING inventory, so update the inventory with content URI: mCurrentInventoryUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentInventoryUri will already identify the correct row in the database that
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
        }}
        public void increament (View view){
            quantity = Integer.parseInt(mQuantityEditText.getText().toString());
            quantity = quantity + 1;
            display(quantity);
        }

        public void decreament (View view){

            quantity = Integer.parseInt(mQuantityEditText.getText().toString());
            if (quantity > 0) {
                quantity = quantity - 1;
                display(quantity);
            } else {
                return;
            }
        }
        private void display ( int number){
            mQuantityEditText.setText("" + number);
        }


        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu options from the res/menu/menu_editor.xml file.
            // This adds menu items to the app bar.
            getMenuInflater().inflate(R.menu.view_menu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
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
                    // If the inventory hasn't changed, continue with navigating up to parent activity
                    // which is the {@link CatalogActivity}.
                    if (!mHasChanged) {
                        NavUtils.navigateUpFromSameTask(ViewActivity.this);
                        return true;
                    }

                    // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                    // Create a click listener to handle the user confirming that
                    // changes should be discarded.
                    DialogInterface.OnClickListener discardButtonClickListener =
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // User clicked "Discard" button, navigate to parent activity.
                                    NavUtils.navigateUpFromSameTask(ViewActivity.this);
                                }
                            };

                    // Show a dialog that notifies the user they have unsaved changes
                    showUnsavedChangesDialog(discardButtonClickListener);
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }


        @Override
        public void onLoadFinished (Loader < Cursor > loader, Cursor data){
// Bail early if the cursor is null or there is less than 1 row in the cursor
            if (data == null || data.getCount() < 1) {
                return;
            }

            // Proceed with moving to the first row of the cursor and reading data from it
            // (This should be the only row in the cursor)
            if (data.moveToFirst()) {
                // Find the columns of inventory attributes that we're interested in
                int nameColumnIndex = data.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME);
                int priceColumnIndex = data.getColumnIndex(InventoryEntry.COLUMN_PRICE);
                int quantityColumnIndex = data.getColumnIndex(InventoryEntry.COLUMN_QUANTITY);
                int sunameColumnIndex = data.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NAME);
                int phoneColumnIndex = data.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_PHONE);

                // Extract out the value from the Cursor for the given column index
                String nameString = data.getString(nameColumnIndex);
                int price = data.getInt(priceColumnIndex);
                int quantity = data.getInt(quantityColumnIndex);
                String suppliername = data.getString(sunameColumnIndex);
                String supplierphone = data.getString(phoneColumnIndex);
                // Update the views on the screen with the values from the database
                mNameEditText.setText(nameString);
                mPriceEditText.setText(Integer.toString(price));
                mQuantityEditText.setText(Integer.toString(quantity));
                mSupplierName.setText(suppliername);
                mSupplierNumber.setText(supplierphone);
            }
        }

        @Override
        public Loader<Cursor> onCreateLoader ( int id, Bundle args){
            String[] projection = {
                    InventoryContract.InventoryEntry._ID,
                    InventoryEntry.COLUMN_PRODUCT_NAME,
                    InventoryEntry.COLUMN_PRICE,
                    InventoryEntry.COLUMN_QUANTITY,
                    InventoryEntry.COLUMN_SUPPLIER_NAME,
                    InventoryEntry.COLUMN_SUPPLIER_PHONE};
            return new CursorLoader(this, mCurrentInventoryUri, projection, null, null, null);

        }

        @Override
        public void onLoaderReset (Loader < Cursor > loader) {
            mNameEditText.setText("");
            mPriceEditText.setText("");
            mQuantityEditText.setText("");
            mSupplierName.setText("");
            mSupplierNumber.setText("");
        }

    @Override
    public void onBackPressed() {
        // If the inventory hasn't changed, continue with handling back button press
        if (!mHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new inventory, hide the "Delete" menu item.
        if (mCurrentInventoryUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    /**
     * Prompt the user to confirm that they want to delete this inventory.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the inventory.
                deleteInventory();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the inventory.
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
     * Perform the deletion of the inventory in the database.
     */
    private void deleteInventory() {
        // Only perform the delete if this is an existing inventory.
        if (mCurrentInventoryUri != null) {
            // Call the ContentResolver to delete the inventory at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentInventoryUri
            // content URI already identifies the inventory that we want.
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

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the inventory.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
