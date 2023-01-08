package com.desuzed.everyweather.util

import android.content.Context
import android.content.res.Resources.getSystem
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_SLIDE
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.Flow
import java.util.*

fun Fragment.navigate(directions: Int, bundle: Bundle? = null) {
    val controller = findNavController()
    if (isTargetDestination()) {
        if (bundle == null) {
            controller.navigate(directions)
        } else controller.navigate(directions, bundle)
    }
}

fun Fragment.isTargetDestination(): Boolean {
    val controller = findNavController()
    val currentDestination =
        (controller.currentDestination as? FragmentNavigator.Destination)?.className
            ?: (controller.currentDestination as? DialogFragmentNavigator.Destination)?.className
    return currentDestination == this.javaClass.name
}

fun Fragment.addOnBackPressedCallback() {
    val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onBackClick()
        }
    }
    requireActivity().onBackPressedDispatcher.addCallback(
        viewLifecycleOwner,
        onBackPressedCallback
    )
}

fun Fragment.onBackClick() {
    if (isTargetDestination()) {
        findNavController().navigateUp()
    }
}

inline fun <T> Fragment.collect(
    source: Flow<T>,
    crossinline consumer: suspend (T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        source.collect {
            consumer(it)
        }
    }
}

inline fun <T> AppCompatActivity.collect(
    source: Flow<T>,
    crossinline consumer: suspend (T) -> Unit
) {
    lifecycleScope.launchWhenCreated {
        source.collect {
            consumer(it)
        }
    }
}

fun AppCompatActivity.snackbar(
    text: String,
    root: View,
    @StringRes actionStringId: Int,
    onActionClick: () -> Unit,
) {
    Snackbar.make(this, root, text, Snackbar.LENGTH_LONG).apply {
        setAction(actionStringId) {
            onActionClick()
            dismiss()
        }
        animationMode = ANIMATION_MODE_SLIDE
        show()
    }
}

fun setAppLocaleAndReturnContext(language: String, newBase: Context): Context {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val res = newBase.resources
    val config = res.configuration
    config.setLocale(locale)
    config.setLayoutDirection(locale)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
        newBase.createConfigurationContext(config)
    } else {
        res.updateConfiguration(config, res.displayMetrics)
        newBase
    }
}

val Int.toIntDp: Int get() = (this / getSystem().displayMetrics.density).toInt()
//fun Activity.makeStatusBarTransparent() {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//        window.apply {
//            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                decorView.systemUiVisibility =
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//               // or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            } else {
//                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//            }
//            statusBarColor = Color.TRANSPARENT
//        }
//    }
//
//
//}
//
//fun View.setMarginTop(marginTop: Int) {
//    val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
//    menuLayoutParams.setMargins(0, marginTop, 0, 0)
//    this.layoutParams = menuLayoutParams
//}