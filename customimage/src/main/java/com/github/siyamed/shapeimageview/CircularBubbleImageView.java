package com.github.siyamed.shapeimageview;

import android.content.Context;
import android.util.AttributeSet;

import com.github.siyamed.shapeimageview.shader.ShaderHelper;
import com.github.siyamed.shapeimageview.shader.SvgShader;

public class CircularBubbleImageView extends ShaderImageView {

    public CircularBubbleImageView(Context context) {
        super(context);

    }


    public CircularBubbleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public CircularBubbleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    public ShaderHelper createImageViewHelper() {
        return new SvgShader(R.raw.rectanglenew);
    }
}
