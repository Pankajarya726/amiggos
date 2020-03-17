package com.tekzee.amiggos.ui.stripepayment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tekzee.amiggos.databinding.PaymentRowDataBinding;
import com.tekzee.amiggos.ui.stripepayment.PaymentClick;
import com.tekzee.amiggos.ui.stripepayment.model.CardListResponse;
import com.tekzee.amiggos.ui.stripepayment.model.CardListResponse.Cards.Card;

import java.util.ArrayList;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.MyViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<CardListResponse.Cards.Card> data;
    private PaymentClick listener;

    public PaymentAdapter(ArrayList<CardListResponse.Cards.Card> data, PaymentClick listener) {
        this.data = data;
        this.listener = listener;
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
        CardListResponse.Cards.Card model = data.get(position);
        holder.bindConnection(model);
        final PaymentRowDataBinding dataBinding = holder.getBinding();

        holder.getBinding().tvCardNumber.setText(model.getBrand() + " ending in " + model.getLast4());

        if (model.isDefault()==1) {
            holder.getBinding().ivDefault.setVisibility(View.VISIBLE);

        } else {
            holder.getBinding().ivDefault.setVisibility(View.GONE);
        }


        holder.getBinding().mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onRowClick(model);

            }
        });
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

        public void bindConnection(CardListResponse.Cards.Card model) {
            binding.setPaymentRowResponse(model);
        }
    }


}
