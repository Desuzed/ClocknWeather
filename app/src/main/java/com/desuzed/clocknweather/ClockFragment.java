package com.desuzed.clocknweather;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import com.desuzed.clocknweather.mvvm.ClockViewModel;
import com.desuzed.clocknweather.util.ArrowImageView;
import com.desuzed.clocknweather.util.CheckBoxManager;
import com.desuzed.clocknweather.util.MusicPlayer;
import com.desuzed.clocknweather.util.TimeGetter;

import java.text.SimpleDateFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ClockFragment extends Fragment {
    private ImageView watchesImage;
    private ArrowImageView arrowSeconds;
    private ArrowImageView arrowMin;
    private ArrowImageView arrowHours;
    private TextView tvTopClock, tvHeader, tvBottomClock;
    private ClockViewModel viewModel;
    private CheckBoxManager mCheckBoxManager;
   // private ClockApp clock;
    private FragmentClockBinding fragmentClockBinding;
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat sdfBottomClock = new SimpleDateFormat("hh:mm:ss.S");
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat sdfTopClock = new SimpleDateFormat("hh:mm");
    private MusicPlayer musicPlayer;

    public static ClockFragment newInstance() {
        return new ClockFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentClockBinding = FragmentClockBinding.inflate(inflater, container, false);
        return fragmentClockBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
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
        observeLiveData();
    }
    private void init() {
        Button b = fragmentClockBinding.button;
        b.setOnClickListener(view1 -> {
            throw new RuntimeException("Test Crash");
        });
        tvHeader = fragmentClockBinding.tvHeader;
        watchesImage = fragmentClockBinding.watchesImage;
        tvTopClock = fragmentClockBinding.tvTopClock;
        tvBottomClock = fragmentClockBinding.tvBottomClock;
        arrowSeconds = fragmentClockBinding.arrowSeconds;
        arrowMin = fragmentClockBinding.arrowMin;
        arrowHours = fragmentClockBinding.arrowHours;
        arrowRotations();
        CheckBox checkBoxMin = fragmentClockBinding.checkboxMin;
        CheckBox checkBox15min = fragmentClockBinding.checkbox15min;
        CheckBox checkBoxHour = fragmentClockBinding.checkboxHour;
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(ClockViewModel.class);
        mCheckBoxManager = new CheckBoxManager(checkBoxMin, checkBox15min, checkBoxHour);
        mCheckBoxManager.setOnCheckedChangeListeners(viewModel);
        musicPlayer = new MusicPlayer(mCheckBoxManager, getContext());
        //  clock = new ClockApp(arrowSeconds, arrowMin, arrowHours, musicPlayer);
        initObservers();
    }

    private void observeLiveData() {
        viewModel.getCheckBoxLiveData().observe(getViewLifecycleOwner(), checkBoxStates -> mCheckBoxManager.updateStates(checkBoxStates));
        viewModel.getHourLiveData().observe(getViewLifecycleOwner(), this::turnHourArrow);
        viewModel.getMinLiveData().observe(getViewLifecycleOwner(), min -> {
            turnMinuteArrow(min);
            setTextTopClock(sdfTopClock.format(System.currentTimeMillis()));
        });
        viewModel.getSecLiveData().observe(getViewLifecycleOwner(), this::turnSecondArrow);
        viewModel.getMSecLiveData().observe(getViewLifecycleOwner(), mSec -> setTextBotClock(sdfBottomClock.format(System.currentTimeMillis())));
    }



    private void arrowRotations() {
        arrowHours.setRotation(30 * (new TimeGetter().getHour()));
        arrowMin.setRotation(6 * (new TimeGetter().getMinute()));
        arrowSeconds.setRotation(6 * (new TimeGetter().getSec()));
    }

    private void initObservers() {
//        AnalogClockObserver analogClockObserver = new AnalogClockObserver(clock);
//        BottomClockObserver bottomClockObserver = new BottomClockObserver(tvBottomClock, tvTopClock, clock);
//        viewModel.getEmitter()
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(bottomClockObserver);
//        viewModel.getEmitter()
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .filter(viewModel.getFilterSeconds())
//                .subscribe(analogClockObserver);

        //_______________----------________________
        viewModel.getEmitter()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(viewModel.getHourObserver());
        viewModel.getEmitter()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(viewModel.getMinuteObserver());
        viewModel.getEmitter()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(viewModel.getSecObserver());
        viewModel.getEmitter()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(viewModel.getMSecObserver());

    }

    private void turnHourArrow(Integer hour) {
        float rotation = 30 * hour;
        boolean rotChanged = arrowHours.rotateArrow(rotation);
        if (rotChanged) {
            viewModel.playMusic(rotation, ClockViewModel.ARROW_HOUR, musicPlayer);
        }
    }

    private void turnSecondArrow(Integer sec) {
        arrowSeconds.rotateArrow(6 * sec);
    }

    private void turnMinuteArrow(Integer min) {
        float rotation = 6 * min;
        boolean rotChanged = arrowMin.rotateArrow(rotation);
        if (rotChanged) {
            viewModel.playMusic(rotation, ClockViewModel.ARROW_MIN, musicPlayer);
        }
    }

    private void setTextTopClock(String time) {
        tvTopClock.setText(time);
    }


    private void setTextBotClock(String time) {
        tvBottomClock.setText(time);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentClockBinding = null;
    }
}
