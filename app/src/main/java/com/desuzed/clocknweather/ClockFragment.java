package com.desuzed.clocknweather;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.math.BigDecimal;

public class ClockFragment extends Fragment {
    public static final String TAG = "ClockFragment";
   //private LinearLayout linearLayout,;
    private ImageView watchesImage, arrowSeconds, arrowMinutes, arrowHours;
    private TextView tvUpperClock, tvHeader, tvBottomClock;

    public static ClockFragment newInstance() {
        return new ClockFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_clock, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvHeader = view.findViewById(R.id.tvHeader);
        tvUpperClock = view.findViewById(R.id.tvTopClock);
        tvBottomClock = view.findViewById(R.id.tvBottomClock);
      //  watchesImage = view.findViewById(R.id.watchesImage);
        arrowSeconds = view.findViewById(R.id.arrow_seconds);
        arrowMinutes = view.findViewById(R.id.arrow_min);
        arrowHours = view.findViewById(R.id.arrow_hours);
        tvHeader.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int textViewHeight = tvHeader.getHeight();
                float textSize = (float)0.7 * textViewHeight;
                tvHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX , textSize);
                tvBottomClock.setTextSize(TypedValue.COMPLEX_UNIT_PX , textSize);
                tvUpperClock.setTextSize(TypedValue.COMPLEX_UNIT_PX , textSize);
                Log.i(TAG, "onGlobalLayout: " + textSize);
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        double secondsStep = 360/60;
        double minutesStep = (double)360/(double)3600;
        double hoursStep = (double)360/(double)21600;
        Button btn = view.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // BigDecimal sumMinutesRotation = new BigDecimal((float)360/(float)3600);
                double sumSecondsRot = arrowSeconds.getRotation() + secondsStep;
                double sumMinutesRot = arrowMinutes.getRotation() + minutesStep;
                double sumHoursRot = arrowHours.getRotation() + hoursStep;

                arrowSeconds.setRotation((float)sumSecondsRot);
                arrowMinutes.setRotation((float)sumMinutesRot);
                arrowHours.setRotation((float)sumHoursRot);
                if (sumSecondsRot == 360) {
                    arrowSeconds.setRotation(0);
                }
                Log.i("TAG", "onClick: sumMinRot:" + sumMinutesRot + " sumHoursRot: " + sumHoursRot);
            }
        });
    }


}
