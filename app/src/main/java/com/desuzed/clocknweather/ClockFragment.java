package com.desuzed.clocknweather;

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

import com.desuzed.clocknweather.databinding.FragmentClockBinding;
import com.desuzed.clocknweather.mvvm.CheckBoxStates;
import com.desuzed.clocknweather.mvvm.CheckBoxViewModel;
import com.desuzed.clocknweather.util.ArrowImageView;
import com.desuzed.clocknweather.util.CheckBoxManager;
import com.desuzed.clocknweather.util.ClockApp;
import com.desuzed.clocknweather.util.MusicPlayer;

import java.util.Objects;
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
    private TextView tvTopClock, tvHeader, tvBottomClock;
    private Observable<Long> emitter;
    private Observer<Long> analogClockObserver, bottomClockObserver;
    private CheckBoxViewModel viewModel;
    private CheckBoxManager mCheckBoxManager;
    private ClockApp clock;
    private FragmentClockBinding fragmentClockBinding;


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
        fragmentClockBinding = FragmentClockBinding.inflate(inflater, container, false);
        return fragmentClockBinding.getRoot();
  //      return inflater.inflate(R.layout.fragment_clock, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        //Листенер получения размеров вьюх, чтобы изменить шрифт текста, ибо при попытке получения размеров в onViewCreated получаешь 0
        tvHeader.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int textViewHeight = tvHeader.getHeight();
                float textSize = (float) 0.7 * textViewHeight;
                tvHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tvBottomClock.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tvTopClock.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                watchesImage.setMaxWidth(watchesImage.getHeight());
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        viewModel.getCheckBoxLiveData().observe(getViewLifecycleOwner(), checkBoxStates -> {
            mCheckBoxManager.updateStates(checkBoxStates);
          //  Log.i(TAG, "onChanged: min " + checkBoxStates.getStateMinute() + "; 15 min " + checkBoxStates.getState15min() + "; 1 hour " + checkBoxStates.getStateHour());
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

        Predicate<Long> filterSeconds = aLong -> {
            // return aLong.toString().endsWith("0");
            return aLong % 10 == 0;
        };
        analogClockObserver = new Observer<Long>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                Log.i(TAG, "onSubscribe: " + Thread.currentThread().getName());
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
                .filter(filterSeconds)
                .subscribe(analogClockObserver);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init(View view) {
        tvHeader = fragmentClockBinding.tvHeader;
        Button b = fragmentClockBinding.button;
        b.setOnClickListener(view1 -> {
            throw new RuntimeException("Test Crash");
        });
        watchesImage = fragmentClockBinding.watchesImage;
        tvTopClock = fragmentClockBinding.tvTopClock;
        tvBottomClock = fragmentClockBinding.tvBottomClock;
        tvBottomClock = view.findViewById(R.id.tvBottomClock);
        ArrowImageView arrowSeconds = fragmentClockBinding.arrowSeconds;
        ArrowImageView arrowMin = fragmentClockBinding.arrowMin;
        ArrowImageView arrowHours = fragmentClockBinding.arrowHours;
        CheckBox checkBoxMin = fragmentClockBinding.checkboxMin;
        CheckBox checkBox15min = fragmentClockBinding.checkbox15min;
        CheckBox checkBoxHour = fragmentClockBinding.checkboxHour;
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(CheckBoxViewModel.class);
        mCheckBoxManager = new CheckBoxManager(checkBoxMin, checkBox15min, checkBoxHour);
        mCheckBoxManager.setOnCheckedChangeListeners(viewModel);
        emitter = Observable.interval(100, TimeUnit.MILLISECONDS);
        MusicPlayer musicPlayer = new MusicPlayer(mCheckBoxManager, getContext());
        clock = new ClockApp(arrowSeconds, arrowMin, arrowHours, musicPlayer);
    }
}
