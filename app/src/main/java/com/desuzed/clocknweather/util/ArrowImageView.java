package com.desuzed.clocknweather.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class ArrowImageView extends ImageView {
    private OnRotationChangedListener listener;

    public void setListener(OnRotationChangedListener listener) {
        this.listener = listener;
    }

    public ArrowImageView(Context context) {
        super(context);
    }

    public ArrowImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ArrowImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ArrowImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setRotation(float rotation, int arrowIndex) {
        float oldRotation = this.getRotation();
        if (oldRotation != rotation) {
            listener.rotationChanged(rotation, arrowIndex);
        }
        super.setRotation(rotation);
    }

    public interface OnRotationChangedListener {
        void rotationChanged(float rotation, int arrowIndex);
    }
}
