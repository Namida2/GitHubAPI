package com.example.testapi

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import com.google.android.material.floatingactionbutton.FloatingActionButton


class Anim {

    companion object {
        fun startAnim(item : View) {
            val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.8f, 1f)
            val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.8f, 1f)
            ObjectAnimator.ofPropertyValuesHolder(item, scaleX, scaleY).apply {
                interpolator = LinearInterpolator()
                duration = 100
            }.start()
        }

        fun showView (view : View)
        {
            val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0f, 1f)
            val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f, 1f)
            var alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
            ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY, alpha).apply {
                interpolator = OvershootInterpolator()
            }.start()
        }

        fun hideView (view : View) {
            val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 0f)
            val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 0f)
            var alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 0f)

            ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY, alpha).apply {
                interpolator = OvershootInterpolator()
            }.start()
        }
    }
}



