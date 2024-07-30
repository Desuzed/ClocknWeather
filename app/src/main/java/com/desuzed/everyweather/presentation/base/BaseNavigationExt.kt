package com.desuzed.everyweather.presentation.base

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

fun <S, A, I, VM, Screen : BaseComposeScreen<S, A, I, VM>> NavGraphBuilder.composeScreenDestination(
    screen: Screen,
    navController: NavHostController,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
) {
    composable(
        route = screen.route,
        arguments = arguments,
        deepLinks = deepLinks,
//TODO animations
        content = { navEntry ->//TODO scope, arguments?
            screen.ProvideScreenForNavGraph(navController, navEntry)
        }
    )
}

fun <S, A, I, VM, Screen : BaseComposeScreen<S, A, I, VM>> NavController.navigate(
    screen: Screen,
) {
    this.navigate(screen.route)
}