package com.desuzed.clocknweather;

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
import com.desuzed.clocknweather.rx.AnalogClockObserver;
import com.desuzed.clocknweather.rx.BottomClockObserver;
import com.desuzed.clocknweather.util.ArrowImageView;
import com.desuzed.clocknweather.util.CheckBoxManager;
import com.desuzed.clocknweather.util.ClockApp;
import com.desuzed.clocknweather.util.MusicPlayer;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ClockFragment extends Fragment {
    public static final String TAG = "ClockFragment";
    private ImageView watchesImage;
    private TextView tvTopClock, tvHeader, tvBottomClock;
    private ClockViewModel viewModel;
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

    private void init(View view) {
        Button b = fragmentClockBinding.button;
        b.setOnClickListener(view1 -> {
            throw new RuntimeException("Test Crash");
        });
        tvHeader = fragmentClockBinding.tvHeader;
        watchesImage = fragmentClockBinding.watchesImage;
        tvTopClock = fragmentClockBinding.tvTopClock;
        tvBottomClock = fragmentClockBinding.tvBottomClock;
        ArrowImageView arrowSeconds = fragmentClockBinding.arrowSeconds;
        ArrowImageView arrowMin = fragmentClockBinding.arrowMin;
        ArrowImageView arrowHours = fragmentClockBinding.arrowHours;
        CheckBox checkBoxMin = fragmentClockBinding.checkboxMin;
        CheckBox checkBox15min = fragmentClockBinding.checkbox15min;
        CheckBox checkBoxHour = fragmentClockBinding.checkboxHour;
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(ClockViewModel.class);
        mCheckBoxManager = new CheckBoxManager(checkBoxMin, checkBox15min, checkBoxHour);
        mCheckBoxManager.setOnCheckedChangeListeners(viewModel);
        MusicPlayer musicPlayer = new MusicPlayer(mCheckBoxManager, getContext());
        clock = new ClockApp(arrowSeconds, arrowMin, arrowHours, musicPlayer, viewModel);
        initObservers();
    }

    private void initObservers() {
        AnalogClockObserver analogClockObserver = new AnalogClockObserver(clock);
        BottomClockObserver bottomClockObserver = new BottomClockObserver(tvBottomClock, tvTopClock, clock);

        viewModel.getEmitter()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bottomClockObserver);
        viewModel.getEmitter()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(viewModel.getFilterSeconds())
                .subscribe(analogClockObserver);
        /* todo
        Правильный MVVM делается так:
        1. Модель не имеет связи с фрагментом, фрагмент имеет на нее ссылку
        2. Модель генерит события, которые меняют (через post|set) находящую в
        ней мьютэбл лайфлдату. В данном случае повороты стрелок, изменения текста в часах
        3. Фрагмент это обсервит и показывает (лайфдату получаем геттером, или просто как поле)
        4. При клике по элементам управления фрагмент ничего с этим не делает
        - только передает новое состояние в в.модель вызовом ее метода
        5. ВМ  это отрабатывает и хранит локально
        6. Хранение постоянных изменений делается путем вызова метода модели. Модель же
        делает вызов методов в.модели через коллбэк если что случилось с данными
         и надо это обобразить - ну скажем данные с датчика пришли или с сети.
        То есть у нас обсервейбл  с фильтрами - в ВМ, обсерверы во фрагменте.
        Состояние чекбоксов - в модели, локальная копия в ВМ ( нужна для ее логики)
        там же они запрашиваются при перезапуске фрагмента и передаются по цепочке начальные
        7. музыка  - работа с аппаратурой, то есть код ее проигрывания  (сам плеер
        предъявленного ему звука)в модели, а когда играть и какую решает ВМ
        8. Рх пакет при этом не нужен, он пустой все одно почти

         */
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentClockBinding=null;
    }
}
