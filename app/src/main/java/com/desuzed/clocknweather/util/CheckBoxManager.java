package com.desuzed.clocknweather.util;

import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.desuzed.clocknweather.mvvm.CheckBoxStates;
import com.desuzed.clocknweather.mvvm.CheckBoxViewModel;

public class CheckBoxManager {
    private CheckBoxStates mCheckBoxStates = new CheckBoxStates();
    private CheckBox checkBoxMin, checkBox15min, checkBoxHour;

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

       // mCheckBoxStates.setStates(checkBoxStates.getStateMinute(), checkBoxStates.getState15min(), checkBoxStates.getStateHour());
    }

    public void setOnCheckedChangeListeners (CheckBoxViewModel viewModel){
        checkBoxMin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCheckBoxStates.setStateMinute(b);
                viewModel.setState(mCheckBoxStates);

                Log.i("TAG", "onCheckedChanged: min:" + b);
            }
        });

        checkBox15min.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCheckBoxStates.setState15min(b);
                viewModel.setState(mCheckBoxStates);
                Log.i("TAG", "onCheckedChanged: 15min:" + b);
            }
        });

        checkBoxHour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCheckBoxStates.setStateHour(b);
                viewModel.setState(mCheckBoxStates);
                Log.i("TAG", "onCheckedChanged: hour:" + b);
            }
        });
    }

    public CheckBoxStates getCheckBoxStates() {
        return mCheckBoxStates;
    }
}
