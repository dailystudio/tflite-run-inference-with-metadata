package com.dailystudio.tflite.example.taskapi.fragment

import android.content.Context
import com.dailystudio.devbricksx.settings.AbsSetting
import com.dailystudio.tensorflow.lite.viewer.ui.InferenceSettingsFragment

class SimpleInferenceSettingsFragment: InferenceSettingsFragment() {

    override fun createSettings(context: Context): Array<AbsSetting> {
        val oldSettings = super.createSettings(context)

        return arrayOf(oldSettings[1])
    }

}