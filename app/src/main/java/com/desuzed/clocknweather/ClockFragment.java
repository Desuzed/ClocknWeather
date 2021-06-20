package com.desuzed.clocknweather;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.desuzed.clocknweather.mvvm.CheckBoxStates;
import com.desuzed.clocknweather.mvvm.CheckBoxViewModel;
import com.desuzed.clocknweather.util.ArrowImageView;
import com.desuzed.clocknweather.util.CheckBoxManager;
import com.desuzed.clocknweather.util.ClockApp;
import com.desuzed.clocknweather.util.MusicPlayer;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ClockFragment extends Fragment {
    public static final String TAG = "ClockFragment";
    private ImageView watchesImage;
    private ArrowImageView  arrowMinutes, arrowHours, arrowSeconds;
    private TextView tvTopClock, tvHeader, tvBottomClock;
    private CheckBox checkBoxMin, checkBox15min, checkBoxHour;
    private Observable<Long> emitter;
    private Observer<Long> analogClockObserver, bottomClockObserver;
    private CheckBoxViewModel viewModel;
    private CheckBoxManager mCheckBoxManager;
    private ClockApp clock;
    private MusicPlayer musicPlayer;

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
        init(view);
        viewModel.getCheckBoxLiveData().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<CheckBoxStates>() {
            @Override
            public void onChanged(CheckBoxStates checkBoxStates) {
                mCheckBoxManager.updateStates(checkBoxStates);
                Log.i(TAG, "onChanged: min " + checkBoxStates.getStateMinute() + "; 15 min " + checkBoxStates.getState15min() + "; 1 hour " + checkBoxStates.getStateHour());
            }
        });

        //Листенер получения размеров вьюх, чтобы изменить шрифт текста, ибо при попытке получения размеров в onViewCreated получаешь 0
        tvHeader.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int textViewHeight = tvHeader.getHeight();
                float textSize = (float) 0.7 * textViewHeight;
                tvHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tvBottomClock.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tvTopClock.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                //  watchesImage.setMaxWidth(watchesImage.getHeight());
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }

    private void tikTakBottomClock() {
        tvBottomClock.setText(clock.setTimeBottomClock());
        tvTopClock.setText(clock.setTimeTopClock());

    }

    private void tikTakAnalogClock() {
        clock.rotateAnalogClock();
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
//                Log.i(TAG, "onSubscribe: " + Thread.currentThread().getName());
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


        Predicate<Long> filterSeconds = new Predicate<Long>() {
            @Override
            public boolean test(Long aLong) throws Throwable {
                // return aLong.toString().endsWith("0");
                return aLong % 10 == 0;
            }
        };
        analogClockObserver = new Observer<Long>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                Log.i(TAG, "onSubscribe: " + Thread.currentThread().getName());
            }

            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull Long aLong) {
//                Log.i(TAG, "onNext: " + Thread.currentThread().getName());
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
                .filter(filterSeconds)
                .subscribe(analogClockObserver);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init(View view) {
        tvHeader = view.findViewById(R.id.tvHeader);
        watchesImage = view.findViewById(R.id.watchesImage);
        tvTopClock = view.findViewById(R.id.tvTopClock);
        tvBottomClock = view.findViewById(R.id.tvBottomClock);
        arrowSeconds = view.findViewById(R.id.arrow_seconds);
        arrowMinutes = view.findViewById(R.id.arrow_min);
        arrowHours = view.findViewById(R.id.arrow_hours);
        checkBoxMin = view.findViewById(R.id.checkbox1);
        checkBox15min = view.findViewById(R.id.checkbox15);
        checkBoxHour = view.findViewById(R.id.checkbox60);
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(CheckBoxViewModel.class);
        mCheckBoxManager = new CheckBoxManager(checkBoxMin, checkBox15min, checkBoxHour);
        mCheckBoxManager.setOnCheckedChangeListeners(viewModel);
        emitter = Observable.interval(100, TimeUnit.MILLISECONDS);
        musicPlayer = new MusicPlayer(mCheckBoxManager);
        clock = new ClockApp(arrowSeconds, arrowMinutes, arrowHours, musicPlayer);

        Button btn = view.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}
