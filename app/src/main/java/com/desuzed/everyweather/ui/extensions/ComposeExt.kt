package com.desuzed.everyweather.ui.extensions

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.desuzed.everyweather.presentation.features.main_activity.EdgeToEdgeInset
import com.desuzed.everyweather.presentation.features.main_activity.LocalEdgeToEdgeInset

/**
 * Порядок модификатора важен!
 * Использовать паддинг перед стандартными модификаторами паддингов
 * */
@Composable
fun Modifier.topEdgeToEdgePadding(): Modifier {
    val hasTopInset = LocalEdgeToEdgeInset.current.contains(EdgeToEdgeInset.Top)

    return this then if (hasTopInset) this else Modifier.statusBarsPadding()
}

/**
 * Порядок модификатора важен!
 * Использовать паддинг перед стандартными модификаторами паддингов
 * */
@Composable
fun Modifier.bottomEdgeToEdgePadding(): Modifier {
    val hasBottomInset = LocalEdgeToEdgeInset.current.contains(EdgeToEdgeInset.Bottom)

    return this then if (hasBottomInset) this else Modifier.navigationBarsPadding()
}

/**
 * Порядок модификатора важен!
 * Использовать паддинг перед стандартными модификаторами паддингов
 * */
@Composable
fun Modifier.bottomEdgeToEdgeImePadding(): Modifier {
    val hasBottomInset = LocalEdgeToEdgeInset.current.contains(EdgeToEdgeInset.Bottom)

    return this then if (hasBottomInset) this else Modifier.imePadding()
}