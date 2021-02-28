package com.dailystudio.tflite.example.taskapi

import com.dailystudio.tensorflow.lite.viewer.BaseTFLiteModelViewApplication
import com.dailystudio.tensorflow.lite.viewer.ui.InferenceSettingsPrefs
import org.tensorflow.lite.support.model.Model

class ViewerApplication : BaseTFLiteModelViewApplication() {

    override fun onCreate() {
        super.onCreate()

        InferenceSettingsPrefs.instance.device = Model.Device.CPU.toString()
    }

    override fun isDebugBuild(): Boolean {
        return BuildConfig.DEBUG
    }

}
