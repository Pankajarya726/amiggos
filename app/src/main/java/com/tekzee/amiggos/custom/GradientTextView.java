package com.tekzee.amiggos.custom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by rahulchowdhury on 04/08/16.
 */
 
public class GradientTextView extends AppCompatTextView {

    public GradientTextView(Context context) {
        super(context);
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GradientTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        //Setting the gradient if layout is changed
        if (changed) {
            getPaint().setShader(new LinearGradient(0, 0, getWidth(), getHeight(),
                    new int[]{
                            Color.parseColor("#a17226"),
                            Color.parseColor("#664206"),
                            Color.parseColor("#664206"),
                            Color.parseColor("#664206"),
                            Color.parseColor("#664206"),
                    }, null,
                    Shader.TileMode.CLAMP));
        }
    }
}