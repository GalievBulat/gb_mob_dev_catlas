package com.kakadurf.catlas.presentation.map.view.managing

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import javax.inject.Inject

class AnimationManaging @Inject constructor() {
    fun startLoading(
        textView1: TextView,
        textView2: TextView,
        textView3: TextView,
        textView4: TextView,
        background: ImageView
    ) {
        textView1.visibility = View.VISIBLE
        textView2.visibility = View.VISIBLE
        textView3.visibility = View.VISIBLE
        textView4.visibility = View.VISIBLE
        background.visibility = View.VISIBLE
        animator1 = anim(textView1, textView2)
        animator2 = anim(textView3, textView4, reverse = true)
    }

    private lateinit var animator1: Animator
    private lateinit var animator2: Animator
    fun stopLoading(
        textView1: TextView,
        textView2: TextView,
        textView3: TextView,
        textView4: TextView,
        background: ImageView
    ) {
        animator1.cancel()
        animator2.cancel()
        textView1.visibility = View.GONE
        textView2.visibility = View.GONE
        textView3.visibility = View.GONE
        textView4.visibility = View.GONE
        background.visibility = View.GONE
    }

    private fun anim(first: TextView, second: TextView, reverse: Boolean = false): Animator {
        val animator = if (reverse) ValueAnimator.ofFloat(1.0f, 0.0f) else
            ValueAnimator.ofFloat(0.0f, 1.0f)
        animator.repeatCount = ValueAnimator.INFINITE
        animator.interpolator = LinearInterpolator()
        animator.duration = 9000L
        animator.addUpdateListener {
            val progress = it.animatedValue
            val width = first.width
            val translationX = width * progress as Float
            first.translationX = translationX
            second.translationX = (translationX - width)
        }
        animator.start()
        return animator
    }
}
