package com.desuzed.clocknweather.util;

import android.widget.CheckBox;

import com.desuzed.clocknweather.mvvm.CheckBoxStates;
import com.desuzed.clocknweather.mvvm.ClockViewModel;

public class CheckBoxManager {
    private CheckBoxStates mCheckBoxStates = new CheckBoxStates();
    private final CheckBox checkBoxMin, checkBox15min, checkBoxHour;

    public CheckBoxManager(CheckBox checkBoxMin, CheckBox checkBox15min, CheckBox checkBoxHour) {
        this.checkBoxMin = checkBoxMin;
        this.checkBox15min = checkBox15min;
        this.checkBoxHour = checkBoxHour;
    }

    public void updateStates (CheckBoxStates checkBoxStates){
        mCheckBoxStates = checkBoxStates;
        checkBoxMin.setChecked(checkBoxStates.getStateMinute());
        checkBox15min.setChecked(checkBoxStates.getState15min());
        checkBoxHour.setChecked(checkBoxStates.getStateHour());
    }

    public void setOnCheckedChangeListeners (ClockViewModel viewModel){
        checkBoxMin.setOnCheckedChangeListener((compoundButton, b) -> {
            mCheckBoxStates.setStateMinute(b);
            viewModel.setState(mCheckBoxStates);
        });

        checkBox15min.setOnCheckedChangeListener((compoundButton, b) -> {
            mCheckBoxStates.setState15min(b);
            viewModel.setState(mCheckBoxStates);
        });

        checkBoxHour.setOnCheckedChangeListener((compoundButton, b) -> {
            mCheckBoxStates.setStateHour(b);
            viewModel.setState(mCheckBoxStates);
        });
    }

    public CheckBoxStates getCheckBoxStates() {
        return mCheckBoxStates;
    }
}
