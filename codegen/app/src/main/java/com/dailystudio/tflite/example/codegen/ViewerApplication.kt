package com.dailystudio.tflite.example.codegen

import com.dailystudio.tensorflow.lite.viewer.BaseTFLiteModelViewApplication

class ViewerApplication : BaseTFLiteModelViewApplication() {

    override fun isDebugBuild(): Boolean {
        return BuildConfig.DEBUG
    }

}
