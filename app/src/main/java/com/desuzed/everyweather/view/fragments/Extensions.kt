package com.desuzed.everyweather.view.fragments

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController


fun Fragment.navigate(directions: Int, bundle: Bundle? = null) {
    val controller = findNavController()
    val currentDestination =
        (controller.currentDestination as? FragmentNavigator.Destination)?.className
            ?: (controller.currentDestination as? DialogFragmentNavigator.Destination)?.className
    if (currentDestination == this.javaClass.name) {
        if (bundle == null) {
            controller.navigate(directions)
        } else controller.navigate(directions, bundle)
    }
}

fun Fragment.toast(message: String) {
    if (message.isEmpty()) {
        return
    }
    Toast.makeText(
        requireContext(),
        message,
        Toast.LENGTH_LONG
    )
        .show()
}

fun Fragment.addOnBackPressedCallback() {
    val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().navigateUp()
        }
    }
    requireActivity().onBackPressedDispatcher.addCallback(
        viewLifecycleOwner,
        onBackPressedCallback
    )
}

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