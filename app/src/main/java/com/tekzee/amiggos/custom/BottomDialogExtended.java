package com.tekzee.amiggos.custom;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.tekzee.amiggos.R;
import com.tekzee.amiggos.util.AnimationUtils;


public class BottomDialogExtended extends DialogFragment {

    public static BottomDialogExtended newInstance(String titleText, String[] items) {
        return newInstance(titleText,null,items);
    }

    public static BottomDialogExtended newInstance(String titleText, String cancelText, String[] items) {

        Bundle args = new Bundle();
        args.putString("title", titleText);
        args.putString("cancel",cancelText);
        args.putStringArray("items", items);
        BottomDialogExtended fragment = new BottomDialogExtended();
        fragment.setArguments(args);
        return fragment;
    }

    private boolean isAnimation = false;
    private String mTitle;
    private String mCancel;
    private String[] items;
    private View mRootView;
    private OnClickListener mListener;

    public void setListener(OnClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    dismiss();
                }
                return true;
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        initData();
        mRootView = inflater.inflate(R.layout.bottom_lib_layout_extended, container, false);
        AnimationUtils.slideToUp(mRootView);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = (ListView) view.findViewById(R.id.bottom_lib_listView);
        TextView titleView = (TextView) view.findViewById(R.id.title);
        if (TextUtils.isEmpty(mTitle)) {
            view.findViewById(R.id.titleLayout).setVisibility(View.GONE);
        }else {
            titleView.setText(mTitle);
        }


        listView.setAdapter(new ArrayAdapter(getContext(),R.layout.bottom_lib_item,R.id.item,items));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null) {
                    mListener.click(position);
                }

                dismiss();
            }
        });


        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        cancel.setText(mCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initData() {
        mTitle = getArguments().getString("title");
        items = getArguments().getStringArray("items");
        mCancel = getArguments().getString("cancel");
        if (TextUtils.isEmpty(mCancel)) {
            mCancel = getResources().getString(R.string.bottom_dialog_lib_cancel);
        }
    }

    @Override
    public void dismiss() {
        if (isAnimation) {
            return;
        }
        isAnimation = true;
        AnimationUtils.slideToDown(mRootView, new AnimationUtils.AnimationListener() {
            @Override
            public void onFinish() {
                isAnimation = false;
                BottomDialogExtended.super.dismiss();
            }
        });
    }

    public interface OnClickListener {
        void click(int position);
    }
}