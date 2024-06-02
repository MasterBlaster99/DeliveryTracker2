package com.example.deliverytracker.utils

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.LifecycleOwner
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.controls.Facing

class CustomCameraView(context: Context, attrs: AttributeSet) : CameraView(context, attrs) {
    private var listener: CameraFailureListener? = null
    fun setFailureListener(listener: CameraFailureListener?) {
        this.listener = listener
    }
    override fun setLifecycleOwner(owner: LifecycleOwner?) {
        runInTry { super.setLifecycleOwner(owner) }
    }
    override fun takePicture() {
        runInTry { super.takePicture() }
    }
    override fun open() {
        runInTry { super.open() }
    }
    override fun close() {
        runInTry { super.close() }
    }
    override fun addCameraListener(cameraListener: CameraListener) {
        runInTry { super.addCameraListener(cameraListener) }
    }
    override fun setFacing(facing: Facing) {
        runInTry { super.setFacing(facing) }
    }
    private fun runInTry(function: () -> Unit) {
        try {
            function()
        } catch (e: Exception) {
            context.let {
                listener?.onCameraFailure()
            }
        }
    }
}
interface CameraFailureListener {
    fun onCameraFailure()
}