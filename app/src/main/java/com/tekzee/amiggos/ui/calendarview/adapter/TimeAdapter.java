package com.tekzee.amiggos.ui.calendarview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tekzee.amiggos.R;

import java.util.ArrayList;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.SingleViewHolder> {

    private Context context;
    private ArrayList<String> time;
    // if checkedPosition = -1, there is no default selection
    // if checkedPosition = 0, 1st item is selected by default
    private int checkedPosition = 0;

    public TimeAdapter(Context context, ArrayList<String> time) {
        this.context = context;
        this.time = time;
    }

    public void setEmployees(ArrayList<String> time) {
        this.time = new ArrayList<>();
        this.time = time;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SingleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_time, viewGroup, false);
        return new SingleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleViewHolder singleViewHolder, int position) {
        singleViewHolder.bind(time.get(position));
    }

    @Override
    public int getItemCount() {
        return time.size();
    }

    class SingleViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_time;

        SingleViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_time = itemView.findViewById(R.id.txt_time);

        }

        void bind(final String employee) {
            if (checkedPosition == -1) {
                txt_time.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.disabled_time_bg));
                txt_time.setTextColor(context.getResources().getColor(R.color.black));
            } else {
                if (checkedPosition == getAdapterPosition()) {
                    txt_time.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.enable_time_bg));
                    txt_time.setTextColor(context.getResources().getColor(R.color.white));
                } else {
                    txt_time.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.disabled_time_bg));
                    txt_time.setTextColor(context.getResources().getColor(R.color.black));
                }
            }
            txt_time.setText(employee);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    txt_time.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.enable_time_bg));
                    txt_time.setTextColor(context.getResources().getColor(R.color.white));
                    if (checkedPosition != getAdapterPosition()) {
                        notifyItemChanged(checkedPosition);
                        checkedPosition = getAdapterPosition();
                    }
                }
            });
        }
    }

    public String getSelected() {
        if (checkedPosition != -1) {
            return time.get(checkedPosition);
        }
        return null;
    }
}