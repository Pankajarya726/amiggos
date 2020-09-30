package com.tekzee.amiggos.ui.memories.ourmemories.custom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.tekzee.amiggos.R;

import ru.alexbykov.nopaginate.item.LoadingItem;

public class CustomLoadingItem implements LoadingItem {
   
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading_item, parent, false);
            return new RecyclerView.ViewHolder(view) {
            };
        }
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
   
        }
   
}