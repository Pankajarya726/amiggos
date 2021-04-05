package com.tekzee.amiggos.ui.stripepayment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tekzee.amiggos.R;
import com.tekzee.amiggos.constant.ConstantLib;
import com.tekzee.amiggos.databinding.PaymentRowDataBinding;
import com.tekzee.amiggos.ui.stripepayment.PaymentClick;
import com.tekzee.amiggos.ui.stripepayment.model.CardListResponse;


import java.util.ArrayList;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.MyViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<CardListResponse.Data.Card> data;
    private PaymentClick listener;
    private String from;
    public PaymentAdapter(ArrayList<CardListResponse.Data.Card> data,String from, PaymentClick listener) {
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
        PaymentRowDataBinding binding = PaymentRowDataBinding.inflate(layoutInflater, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CardListResponse.Data.Card model = data.get(position);
        holder.bindConnection(model);
        final PaymentRowDataBinding dataBinding = holder.getBinding();

        holder.getBinding().tvCardNumber.setText(model.getBrand() + " ********* " + model.getLast4());

        Glide.with(context).load(model.getIcon()).placeholder(R.drawable.noimage).into(holder.getBinding().ivCard);

//        if (model.isDefault()==1) {
//            holder.getBinding().ivDefault.setVisibility(View.GONE);
//
//        } else {
//            holder.getBinding().ivDefault.setVisibility(View.GONE);
//        }

        if(from.equals(ConstantLib.FINALBASKET)){
            holder.getBinding().delete.setVisibility(View.GONE);
        }else{
            holder.getBinding().delete.setVisibility(View.VISIBLE);
        }
        holder.getBinding().delete.setOnClickListener((View.OnClickListener) view -> {
            listener.onDeleteCard(model);
        });

//        holder.getBinding().mainlayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                listenerFragment.onRowClick(model);
//
//            }
//        });
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private PaymentRowDataBinding binding;

        public MyViewHolder(PaymentRowDataBinding _binding) {
            super(_binding.getRoot());
            binding = _binding;
        }

        public PaymentRowDataBinding getBinding() {
            return binding;
        }

        public void bindConnection(CardListResponse.Data.Card model) {
            binding.setPaymentRowResponse(model);
        }
    }


}
