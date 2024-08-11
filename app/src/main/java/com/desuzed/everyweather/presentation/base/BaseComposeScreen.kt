package com.desuzed.everyweather.presentation.base

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.desuzed.everyweather.domain.model.result.QueryResult
import com.desuzed.everyweather.domain.repository.provider.ActionResultProvider
import com.desuzed.everyweather.ui.elements.AppSnackbar
import com.desuzed.everyweather.ui.elements.CollectSnackbar
import com.desuzed.everyweather.ui.extensions.CollectSideEffect
import com.desuzed.everyweather.ui.extensions.collectAsStateWithLifecycle
import com.desuzed.everyweather.ui.navigation.Destination
import kotlin.reflect.KClass

abstract class BaseComposeScreen<
        S : State,
        E : SideEffect,
        A : Action,
        VM : BaseViewModel<S, E, A>>(private val initialState: S) {

    abstract val destination: Destination

    @Composable
    protected fun ComposeScreen(
        viewModel: VM,
        backAction: A? = null,
        onEffect: (E) -> Unit,
        content: @Composable (S) -> Unit,
    ) {
        val state by viewModel.state.collectAsStateWithLifecycle(initialState)

        CollectSideEffect(
            source = viewModel.sideEffect,
            consumer = { onEffect(it) },
        )
        content(state)
        if (backAction != null) {
            BackHandler {
                viewModel.onAction(backAction)
            }
        }
        //TODO onLifecycleEvent с КММ, где в параметры функции будут передаваться енамы лайфсайклов, соответствующие платформе
    }

    @Composable
    protected fun <P : ActionResultProvider, B : SnackBarEffect> ComposeScreen(
        viewModel: VM,
        snackBarParams: SnackBarParams<P, B>,
        backAction: A? = null,
        onEffect: (E) -> Unit,
        content: @Composable (S) -> Unit,
    ) {
        val state by viewModel.state.collectAsStateWithLifecycle(initialState)
        val snackBarHostState = remember { SnackbarHostState() }
        var snackData: QueryResult? by remember { mutableStateOf(null) }

        CollectSnackbar(
            queryResult = snackData,
            snackbarState = snackBarHostState,
            providerClass = snackBarParams.snackBarProviderClass,
            onRetryClick = {
                viewModel.onAction(snackBarParams.snackBarRetryAction)
            },
        )

        CollectSideEffect(
            source = viewModel.sideEffect,
            consumer = {
                if (it is SnackBarEffect && it::class == snackBarParams.snackBarEffectClass) {
                    snackData = it.data
                }
                onEffect(it)
            },
        )
        Box(modifier = Modifier.fillMaxSize()) {
            content(state)
            AppSnackbar(
                snackbarState = snackBarHostState,
            )
        }
        if (backAction != null) {
            BackHandler {
                viewModel.onAction(backAction)
            }
        }
    }

    @Composable
    abstract fun ProvideScreenForNavGraph(
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry,
    )

    /**
     * Параметры и контракт для снекбара
     *
     * @param snackBarProviderClass - наследник класса ActionResultProvider, который достаёт нужные
     * строки в зависимости от кода ошибки. Не забыть добавить в ActionResultProviderFactory
     * @param snackBarRetryAction - экшен кнопки "повторить"
     * @param snackBarEffectClass - контракт для показа снекбара, обязывающий наследовать эффект от
     * интерфейса SnackBarEffect
     * */
    inner class SnackBarParams<P : ActionResultProvider, B : SnackBarEffect>(
        val snackBarProviderClass: KClass<P>,
        val snackBarRetryAction: A,
        val snackBarEffectClass: KClass<B>,
    )

}
