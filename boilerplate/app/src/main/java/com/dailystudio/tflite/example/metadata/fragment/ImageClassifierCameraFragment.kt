package com.dailystudio.tflite.example.metadata.fragment

import android.content.Context
import android.graphics.Bitmap
import com.dailystudio.devbricksx.utils.ImageUtils
import com.dailystudio.devbricksx.utils.MatrixUtils
import com.dailystudio.tensorflow.lite.viewer.image.AbsTFLiteCameraFragment
import com.dailystudio.tensorflow.lite.viewer.image.AbsTFLiteImageAnalyzer
import com.dailystudio.tensorflow.lite.viewer.image.ImageInferenceInfo
import com.dailystudio.tensorflow.lite.viewer.ui.InferenceSettingsPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.label.Category


class ImageClassifierAnalyzer(rotation: Int, lensFacing: Int)
    : AbsTFLiteImageAnalyzer<ImageInferenceInfo, List<Category>>(rotation, lensFacing) {

    private var classifier: Any? = null
    private var lock = Object()

    override fun analyzeFrame(context: Context,
                              inferenceBitmap: Bitmap,
                              info: ImageInferenceInfo): List<Category>? {
        val tImage = TensorImage.fromBitmap(inferenceBitmap)

        var categories: MutableList<Category>? = null

        synchronized(lock) {
            val start = System.currentTimeMillis()
            /**
             * TODO: add your classifier and inference implementation here
             */
            val end = System.currentTimeMillis()

            /**
             * info.inferenceTime reflects the real inference time,
             * exclude any pre-process and post-process steps. It is
             * displayed in the Performance section of the bottom sheet.
             */
            info.inferenceTime = (end - start)
        }

        return categories
    }

    override fun createInferenceInfo(): ImageInferenceInfo {
        return ImageInferenceInfo()
    }

    override fun preProcessImage(
        frameBitmap: Bitmap?,
        info: ImageInferenceInfo
    ): Bitmap? {
        if (frameBitmap == null) {
            return frameBitmap
        }

        val matrix = MatrixUtils.getTransformationMatrix(
            frameBitmap.width,
            frameBitmap.height,
            640, 480,
            info.imageRotation,
            true
        )

        return ImageUtils.createTransformedBitmap(
            frameBitmap,
            matrix
        )
    }

    override fun onInferenceSettingsChange(changePrefName: String) {
        super.onInferenceSettingsChange(changePrefName)

        when (changePrefName) {
            InferenceSettingsPrefs.PREF_DEVICE, InferenceSettingsPrefs.PREF_NUMBER_OF_THREADS -> {
                GlobalScope.launch (Dispatchers.IO) {
                    synchronized(lock) {
                        /**
                         * TODO: close your classifier here to avoid leaks
                         */

                        classifier = null
                    }
                }
            }
        }
    }

}

class ImageClassifierCameraFragment
    : AbsTFLiteCameraFragment<ImageInferenceInfo, List<Category>>() {

    override fun createAnalyzer(screenAspectRatio: Int, rotation: Int, lensFacing: Int): AbsTFLiteImageAnalyzer<ImageInferenceInfo, List<Category>> {
        return ImageClassifierAnalyzer(rotation, lensFacing)
    }

}