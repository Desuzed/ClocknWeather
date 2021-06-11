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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ClockFragment extends Fragment {
    public static final String TAG = "ClockFragment";
    private ImageView watchesImage, arrowSeconds, arrowMinutes, arrowHours;
    private TextView tvTopClock, tvHeader, tvBottomClock;
    private Observable<Long> emitter;
    //    double secondsStep = 360 / 60;
//    double minutesStep = (double) 360 / (double) 3600;
//    double hoursStep = (double) 360 / (double) 21600;
    private SimpleDateFormat sdfBottomClock = new SimpleDateFormat("hh:mm:ss.S");
    private SimpleDateFormat sdfTopClock = new SimpleDateFormat("hh:mm");
    private Observer<Long> analogClockObserver, bottomClockObserver;

    public static ClockFragment newInstance() {
//        Bundle b = new Bundle();
//        b.putString(TAG, name);
//        ClockFragment f = new ClockFragment();
//        f.setArguments(b);
//        return f;
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
        bind(view);
        //Листенер получения размеров вьюх, чтобы изменить шрифт текста, ибо при попытке получения размеров в onViewCreated получаешь 0
        tvHeader.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int textViewHeight = tvHeader.getHeight();
                float textSize = (float) 0.7 * textViewHeight;
                tvHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tvBottomClock.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tvTopClock.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                Log.i(TAG, "onGlobalLayout: " + textSize);
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        emitter = Observable.interval(100, TimeUnit.MILLISECONDS);
    }

    private void tikTakBottomClock() {
        tvBottomClock.setText(sdfBottomClock.format(System.currentTimeMillis()));
        tvTopClock.setText(sdfTopClock.format(System.currentTimeMillis()));

    }

    private void tikTakAnalogClock() {
        GregorianCalendar calendar = new GregorianCalendar();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        arrowHours.setRotation(30 * hour);
        arrowMinutes.setRotation(6 * minute);
        arrowSeconds.setRotation(6 * seconds);

//        double sumSecondsRot = arrowSeconds.getRotation() + secondsStep;
//        double sumMinutesRot = arrowMinutes.getRotation() + minutesStep;
//        double sumHoursRot = arrowHours.getRotation() + hoursStep;
//        arrowSeconds.setRotation((float) sumSecondsRot);
//        arrowMinutes.setRotation((float) sumMinutesRot);
//        arrowHours.setRotation((float) sumHoursRot);
//        if (sumSecondsRot == 360) {
//            arrowSeconds.setRotation(0);
//        }
        //   tvBottomClock.setText(simpleDateFormat.format(System.currentTimeMillis()));
        // Log.i("TAG", "onClick: sumMinRot:" + sumMinutesRot + " sumHoursRot: " + sumHoursRot);
        //  Log.i("TAG", "onClick: sumSecRot:" + sumSecondsRot);
    }

    @Override
    public void onResume() {
        super.onResume();
        initObservers();

    }

    private void initObservers() {
        bottomClockObserver = new Observer<Long>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
            }

            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull Long aLong) {
                tikTakBottomClock();
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                Log.i(TAG, "onError: " + e.getLocalizedMessage());
            }

            @Override
            public void onComplete() {
            }
        };


        Predicate<Long> filter = new Predicate<Long>() {
            @Override
            public boolean test(Long aLong) throws Throwable {
                return aLong.toString().endsWith("0");
            }
        };
        analogClockObserver = new Observer<Long>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
            }

            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull Long aLong) {
                tikTakAnalogClock();
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                Log.i(TAG, "onError: " + e.getLocalizedMessage());
            }

            @Override
            public void onComplete() {
            }
        };

        emitter
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bottomClockObserver);

        emitter
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(filter)
                .subscribe(analogClockObserver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void bind(View view) {
        tvHeader = view.findViewById(R.id.tvHeader);
        tvTopClock = view.findViewById(R.id.tvTopClock);
        tvBottomClock = view.findViewById(R.id.tvBottomClock);
        arrowSeconds = view.findViewById(R.id.arrow_seconds);
        arrowMinutes = view.findViewById(R.id.arrow_min);
        arrowHours = view.findViewById(R.id.arrow_hours);
        Button btn = view.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}
