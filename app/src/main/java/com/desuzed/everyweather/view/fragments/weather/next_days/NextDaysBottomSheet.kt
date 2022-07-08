package com.desuzed.everyweather.view.fragments.weather.next_days

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewModelProvider
import com.desuzed.everyweather.App
import com.desuzed.everyweather.view.AppViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NextDaysBottomSheet : BottomSheetDialogFragment() {
    private val weatherViewModel: NextDaysViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            AppViewModelFactory(App.instance.getRepo())
        )
            .get(NextDaysViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            val state by weatherViewModel.state.collectAsState()
            NextDaysBottomSheetContent(state = state)
        }
        isNestedScrollingEnabled = true
    }
}