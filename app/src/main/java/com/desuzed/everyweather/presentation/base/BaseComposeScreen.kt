package com.desuzed.everyweather.presentation.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.desuzed.everyweather.ui.extensions.CollectSideEffect
import com.desuzed.everyweather.ui.extensions.collectAsStateWithLifecycle

abstract class BaseComposeScreen<
        S : State,
        E : SideEffect,
        A : Action,
        VM : BaseViewModel<S, E, A>>(private val initialState: S) {

    abstract val route: String

    @Composable
    protected fun ComposeScreen(
        viewModel: VM,
        onEffect: (E) -> Unit,
        content: @Composable (S) -> Unit,
    ) {
        val state by viewModel.state.collectAsStateWithLifecycle(initialState)

        CollectSideEffect(
            source = viewModel.sideEffect,
            consumer = { onEffect(it) },
        )
        content(state)
        //TODO onLifecycleEvent с КММ, где в параметры функции будут передаваться енамы лайфсайклов, соответствующие платформе
//        BackHandler {
//            //TODO null? Или сделать наследника, но тогда все экшены должны лежать в одном пакете
//            //  viewModel.onUserInteraction(null)
//        }
    }

    @Composable
    abstract fun ProvideScreenForNavGraph(
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry,
    )

}


