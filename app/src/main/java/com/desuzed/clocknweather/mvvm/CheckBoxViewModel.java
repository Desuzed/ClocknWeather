package com.desuzed.clocknweather.mvvm;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CheckBoxViewModel extends AndroidViewModel {
    private Repository repo;

    public CheckBoxViewModel(Application app) {
        super(app);
        repo = new Repository(app);
    }

    public void setState (CheckBoxStates checkBoxStates){
        repo.setState(checkBoxStates);
    }

    public LiveData<CheckBoxStates> getCheckBoxLiveData() {
        return repo.getCheckBoxLiveData();
    }
}
