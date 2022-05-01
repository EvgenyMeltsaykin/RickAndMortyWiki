package com.wiki.cf_ui.extensions

import androidx.constraintlayout.motion.widget.MotionLayout

fun MotionLayout.progressChangeListener(listener: (Float) -> Unit) {
    addTransitionListener(
        object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

            override fun onTransitionChange(p0: MotionLayout?, startId: Int, endId: Int, progress: Float) {
                listener(progress)
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {}

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}

        }
    )
}

fun MotionLayout.onTransitionCompeteListener(listener: (Int) -> Unit) {
    addTransitionListener(
        object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}
            override fun onTransitionChange(p0: MotionLayout?, startId: Int, endId: Int, progress: Float) {}
            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                listener(p1)
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
        }
    )
}