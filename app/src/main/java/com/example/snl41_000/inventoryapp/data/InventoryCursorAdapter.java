package com.example.snl41_000.inventoryapp.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.snl41_000.inventoryapp.EditorActivity;
import com.example.snl41_000.inventoryapp.R;

public class InventoryCursorAdapter extends CursorAdapter{

    EditorActivity editorActivity;
    long id;
    private double price;
    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView nameTextView = view.findViewById(R.id.name);
        TextView priceTextView = view.findViewById(R.id.price);
        TextView summaryTextView = view.findViewById(R.id.summary);
        final Button saleB = view.findViewById(R.id.sale);
        saleB.setFocusable(false);
        saleB.setFocusableInTouchMode(false);

        // Find the columns of pet attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRICE);
        editorActivity = new EditorActivity();
        id = cursor.getLong(cursor.getColumnIndex(InventoryContract.InventoryEntry._ID));
        // Read the pet attributes from the Cursor for the current pet
        String Name = cursor.getString(nameColumnIndex);
        final int quantity = cursor.getInt(quantityColumnIndex);
        price = cursor.getDouble(priceColumnIndex);
        if (TextUtils.isEmpty(Name)) {
            Name = context.getString(R.string.unknown_name);
        }

        saleB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentResolver resolver = context.getContentResolver();


                ContentValues values = new ContentValues();


                if (quantity > 0) {


                    // Create a new uri for this product ( ListItem)
                    Uri CurrentUri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI, id);

                    // Present a new variable to send the reduced quantity to database
                    int currentAvailableQuantity = quantity;
                    currentAvailableQuantity -= 1;


                    values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, currentAvailableQuantity);

                    resolver.update(CurrentUri, values, null, null);

                    context.getContentResolver().notifyChange(CurrentUri, null);
                } else {

                    Toast.makeText(v.getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Update the TextViews with the attributes for the current pet
        nameTextView.setText(Name);
        summaryTextView.setText(Integer.toString(quantity));
        priceTextView.setText("$" + String.format("%.2f", price));

    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }
}
