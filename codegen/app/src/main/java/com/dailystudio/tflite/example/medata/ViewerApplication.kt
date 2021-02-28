package com.dailystudio.tflite.example.medata

import com.dailystudio.tensorflow.lite.viewer.BaseTFLiteModelViewApplication

class ViewerApplication : BaseTFLiteModelViewApplication() {

    override fun isDebugBuild(): Boolean {
        return BuildConfig.DEBUG
    }

}
