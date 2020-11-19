package com.tekzee.amiggos.ui.homescreen_new;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.tekzee.amiggos.R;
import com.tekzee.amiggos.ui.homescreen_new.homefragment.model.HomeResponse;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Activity context;

    public CustomInfoWindowAdapter(Activity context){
        this.context = context;

    }

    @Override
    public View getInfoWindow(Marker marker) {
        HomeResponse.Data.Venue venueData = (HomeResponse.Data.Venue) marker.getTag();
        View view = context.getLayoutInflater().inflate(R.layout.custom_info_window, null);
        TextView custom_name = (TextView) view.findViewById(R.id.custom_name);
        TextView custom_city_state = (TextView) view.findViewById(R.id.custom_city_state);
        TextView custom_type = (TextView) view.findViewById(R.id.custom_type);
        Button custom_view_details = (Button) view.findViewById(R.id.custom_view_details);
        custom_name.setText(venueData.getName());
        custom_city_state.setText(venueData.getCity()+", "+venueData.getState());
        custom_type.setText(venueData.getType());



        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {

//        HomeResponse.Data.Venue venueData = (HomeResponse.Data.Venue) marker.getTag();
//        View view = context.getLayoutInflater().inflate(R.layout.custom_info_window, null);
//        TextView custom_name = (TextView) view.findViewById(R.id.custom_name);
//        TextView custom_city_state = (TextView) view.findViewById(R.id.custom_city_state);
//        TextView custom_type = (TextView) view.findViewById(R.id.custom_type);
//        Button custom_view_details = (Button) view.findViewById(R.id.custom_view_details);
//        custom_name.setText(venueData.getName());
//        custom_city_state.setText(venueData.getName());
//        custom_type.setText(venueData.getType());
//
//        return view;
        return null;
    }
}