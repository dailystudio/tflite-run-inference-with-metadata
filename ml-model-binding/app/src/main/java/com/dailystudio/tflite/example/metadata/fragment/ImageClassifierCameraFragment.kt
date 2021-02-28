package com.dailystudio.tflite.example.metadata.fragment

import android.content.Context
import android.graphics.Bitmap
import com.dailystudio.devbricksx.development.Logger
import com.dailystudio.devbricksx.utils.ImageUtils
import com.dailystudio.devbricksx.utils.MatrixUtils
import com.dailystudio.tensorflow.lite.viewer.image.AbsTFLiteCameraFragment
import com.dailystudio.tensorflow.lite.viewer.image.AbsTFLiteImageAnalyzer
import com.dailystudio.tensorflow.lite.viewer.image.ImageInferenceInfo
import com.dailystudio.tensorflow.lite.viewer.ui.InferenceSettingsPrefs
import com.dailystudio.tflite.example.metadata.ml.LiteModelAiyVisionClassifierBirdsV13
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.support.model.Model
import java.lang.Exception


class ImageClassifierAnalyzer(rotation: Int, lensFacing: Int)
    : AbsTFLiteImageAnalyzer<ImageInferenceInfo, List<Category>>(rotation, lensFacing) {

    private var classifier: LiteModelAiyVisionClassifierBirdsV13? = null
    private var lock = Object()

    private fun getModelOptions(): Model.Options {
        val deviceStr = InferenceSettingsPrefs.instance.device
        val numOfThreads = InferenceSettingsPrefs.instance.numberOfThreads

        val device = try {
            Model.Device.valueOf(deviceStr)
        } catch (e: Exception) {
            Logger.error("failed to parse device from [${deviceStr}]: $e")
            Model.Device.CPU
        }

        val builder = Model.Options.Builder()
            .setDevice(device)
        if (device != Model.Device.GPU) {
            builder.setNumThreads(numOfThreads)
        }

        Logger.debug("model options: device = $device, numOfThreads = $numOfThreads")

        return builder.build()
    }

    override fun analyzeFrame(context: Context,
                              inferenceBitmap: Bitmap,
                              info: ImageInferenceInfo): List<Category>? {
        val tImage = TensorImage.fromBitmap(inferenceBitmap)

        var categories: MutableList<Category>? = null

        synchronized(lock) {
            classifier = classifier ?:
                    LiteModelAiyVisionClassifierBirdsV13.newInstance(
                        context, getModelOptions())

            categories = classifier?.process(tImage)?.probabilityAsCategoryList?.apply {
                sortByDescending {
                    it.score
                }
            }
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
                        classifier?.close()

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