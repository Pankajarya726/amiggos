package com.tekzee.amiggoss.ui.homescreen_new.homefragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tekzee.amiggoss.R;
import com.tekzee.amiggoss.ui.homescreen_new.homefragment.model.HomeApiResponse;

import java.util.List;

public class AutoCompleteAdapter extends ArrayAdapter<HomeApiResponse.Data.NearestClub> {

    public AutoCompleteAdapter(Context context, List<HomeApiResponse.Data.NearestClub> data) {
        super(context, 0, data);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        HomeApiResponse.Data.NearestClub nearestClub = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.text_title);
        tvName.setText(nearestClub.getClubName());
        return convertView;
    }
}
