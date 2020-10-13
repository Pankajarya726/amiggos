package com.tekzee.amiggos.ui.stripepayment.paymentactivity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tekzee.amiggos.R;
import com.tekzee.amiggos.constant.ConstantLib;
import com.tekzee.amiggos.databinding.PaymentActivityRowDataBinding;
import com.tekzee.amiggos.ui.stripepayment.model.CardListResponse;
import com.tekzee.amiggos.ui.stripepayment.paymentactivity.PaymentActivityClick;

import java.util.ArrayList;

public class PaymentActivityAdapter extends RecyclerView.Adapter<PaymentActivityAdapter.MyViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<CardListResponse.Data.Card> data;
    private PaymentActivityClick listener;
    private String from;
    public PaymentActivityAdapter(ArrayList<CardListResponse.Data.Card> data, String from, PaymentActivityClick listener) {
        this.data = data;
        this.listener = listener;
        this.from = from;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (layoutInflater == null) {
            layoutInflater = layoutInflater.from(parent.getContext());
        }
        PaymentActivityRowDataBinding binding = PaymentActivityRowDataBinding.inflate(layoutInflater, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CardListResponse.Data.Card model = data.get(position);
        holder.bindConnection(model);
        final PaymentActivityRowDataBinding dataBinding = holder.getBinding();

        holder.getBinding().tvCardNumber.setText(model.getBrand() + " ********* " + model.getLast4());

        if(model.isSelected()){
            holder.getBinding().delete.setVisibility(View.VISIBLE);
        }else{
            holder.getBinding().delete.setVisibility(View.GONE);
        }
        Glide.with(context).load(model.getIcon()).placeholder(R.drawable.noimage).into(holder.getBinding().ivCard);
       
        holder.getBinding().mainlayout.setOnClickListener((View.OnClickListener) view -> {
            listener.onRowClick(model,position);
        });
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private PaymentActivityRowDataBinding binding;

        public MyViewHolder(PaymentActivityRowDataBinding _binding) {
            super(_binding.getRoot());
            binding = _binding;
        }

        public PaymentActivityRowDataBinding getBinding() {
            return binding;
        }

        public void bindConnection(CardListResponse.Data.Card model) {
            binding.setPaymentRowResponse(model);
        }
    }


}
