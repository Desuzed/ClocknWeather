package com.desuzed.everyweather.presentation.features.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.desuzed.everyweather.domain.model.app_update.InAppUpdateStatus
import com.desuzed.everyweather.presentation.features.main_activity.MainActivity
import com.desuzed.everyweather.util.addOnBackPressedCallback
import com.desuzed.everyweather.util.collect
import com.desuzed.everyweather.util.onBackClick
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val state by viewModel.state.collectAsState()
                SettingsContent(
                    state = state,
                    onUserInteraction = viewModel::onUserInteraction,
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addOnBackPressedCallback()
        collect(viewModel.action, ::onNewAction)
    }

    private fun onNewAction(action: SettingsAction) {
        when (action) {
            SettingsAction.NavigateBack -> onBackClick()
            is SettingsAction.ShowReadyToInstallDialog -> showReadyToInstallDialog(action.status)
            is SettingsAction.ShowUpdateDialog -> showReadyToInstallDialog(action.status)
        }
    }

    private fun showReadyToInstallDialog(status: InAppUpdateStatus) {
        (activity as MainActivity).showUpdateDialog(status)
    }

}