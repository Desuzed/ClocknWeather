package com.desuzed.everyweather.presentation.features.in_app_update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import com.desuzed.everyweather.domain.model.app_update.InAppUpdateStatus
import com.desuzed.everyweather.presentation.features.in_app_update.ui.InAppUpdateContent
import com.desuzed.everyweather.presentation.features.shared.SharedViewModel
import com.desuzed.everyweather.util.collect
import com.desuzed.everyweather.util.collectAsStateWithLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class InAppUpdateBottomSheet : BottomSheetDialogFragment() {
    private val viewModel by viewModel<InAppUpdateViewModel>()
    private val sharedViewModel by viewModel<SharedViewModel>()
    private val status: InAppUpdateStatus by lazy {
        arguments?.getParcelable(IN_APP_UPDATE_STATUS_KEY)
            ?: InAppUpdateStatus.READY_TO_LAUNCH_UPDATE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle(InAppUpdateState())
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
        viewModel.resolveStatus(status)
    }

    fun setUpdateStatus(status: InAppUpdateStatus) {
        arguments = Bundle().apply {
            putParcelable(IN_APP_UPDATE_STATUS_KEY, status)
        }
    }

    private fun onNewAction(action: InAppUpdateAction) {
        when (action) {
            InAppUpdateAction.Dismiss -> dismiss()
            InAppUpdateAction.InstallUpdate -> installUpdate()
            InAppUpdateAction.UpdateApplication -> startDownloadingUpdate()
        }
    }

    private fun startDownloadingUpdate() {
        sharedViewModel.startUpdate(requireActivity())
        dismiss()
    }

    private fun installUpdate() {
        sharedViewModel.completeUpdate()
        dismiss()
    }

    companion object {
        private const val IN_APP_UPDATE_STATUS_KEY = "IN_APP_UPDATE_STATUS_KEY"
    }

}