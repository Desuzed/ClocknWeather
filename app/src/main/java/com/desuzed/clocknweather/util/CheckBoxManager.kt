package com.desuzed.clocknweather.util

import android.widget.CheckBox
import android.widget.CompoundButton
import com.desuzed.clocknweather.mvvm.CheckBoxStates
import com.desuzed.clocknweather.mvvm.ClockViewModel

class CheckBoxManager(
    val checkBoxMin: CheckBox,
    val checkBox15min: CheckBox,
    val checkBoxHour: CheckBox
) {
    var checkBoxStates = CheckBoxStates()

    fun updateStates(checkBoxStates: CheckBoxStates) {
        this.checkBoxStates = checkBoxStates
        checkBoxMin.isChecked = checkBoxStates.stateMinute
        checkBox15min.isChecked = checkBoxStates.state15min
        checkBoxHour.isChecked = checkBoxStates.stateHour
    }

    fun setOnCheckedChangeListeners(viewModel: ClockViewModel) {
        checkBoxMin.setOnCheckedChangeListener { compoundButton: CompoundButton?, b: Boolean ->
            checkBoxStates.stateMinute = b
            viewModel.setState(checkBoxStates)
        }
        checkBox15min.setOnCheckedChangeListener { compoundButton: CompoundButton?, b: Boolean ->
            checkBoxStates.state15min = b
            viewModel.setState(checkBoxStates)
        }
        checkBoxHour.setOnCheckedChangeListener { compoundButton: CompoundButton?, b: Boolean ->
            checkBoxStates.stateHour = b
            viewModel.setState(checkBoxStates)
        }
    }
}