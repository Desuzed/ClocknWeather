package com.desuzed.everyweather.presentation.features.in_app_update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import com.desuzed.everyweather.presentation.features.weather_main.WeatherMainFragment
import com.desuzed.everyweather.util.collect
import com.desuzed.everyweather.util.navigateBackWithParameter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class InAppUpdateBottomSheet : BottomSheetDialogFragment() {
    private val viewModel by viewModel<InAppUpdateViewModel>()
    private val isReadyToUpdate: Boolean by lazy {
        arguments?.getBoolean(IS_UPDATE_READY_TAG) ?: false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            val state by viewModel.state.collectAsState()
            InAppUpdateContent(
                state = state,
                onUserInteraction = viewModel::onUserInteraction,
            )
        }
        isNestedScrollingEnabled = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collect(viewModel.action, ::onNewAction)
        viewModel.initState(isReadyToUpdate)
    }

    private fun onNewAction(action: InAppUpdateAction) {
        when (action) {
            InAppUpdateAction.Dismiss -> dismiss()
            InAppUpdateAction.UpdateApplication -> navigateBackWithParameter(
                WeatherMainFragment.IN_APP_UPDATE,
                WeatherMainFragment.START_UPDATE,
            )
            InAppUpdateAction.InstallUpdate -> navigateBackWithParameter(
                WeatherMainFragment.IN_APP_UPDATE,
                WeatherMainFragment.INSTALL_UPDATE,
            )
        }
    }

    companion object {
        const val IS_UPDATE_READY_TAG = "IS_UPDATE_READY_TAG"
    }
}