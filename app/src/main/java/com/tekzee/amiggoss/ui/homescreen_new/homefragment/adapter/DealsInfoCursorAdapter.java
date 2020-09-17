package com.tekzee.amiggoss.ui.homescreen_new.homefragment.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import com.tekzee.amiggoss.R;

public class DealsInfoCursorAdapter extends CursorAdapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private SearchView searchView;

    public DealsInfoCursorAdapter(Context context, Cursor cursor, SearchView sv) {
        super(context, cursor, false);
        mContext = context;
        searchView = sv;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = mLayoutInflater.inflate(R.layout.list_row, parent, false);
        return v;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        String deal = cursor.getString(cursor.getColumnIndexOrThrow("deal"));
        String cashback = cursor.getString(cursor.getColumnIndexOrThrow("cashback"));

        TextView dealsTv = (TextView) view.findViewById(R.id.text_title);
        dealsTv.setText(deal);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //take next action based user selected item
                TextView dealText = (TextView) view.findViewById(R.id.text_title);
                searchView.setIconified(true);
                Toast.makeText(context, "Selected suggestion "+dealText.getText(),
                        Toast.LENGTH_LONG).show();

            }
        });

    }
}