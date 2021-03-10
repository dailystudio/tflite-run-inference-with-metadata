package com.dailystudio.tflite.example.metadata.fragment

import android.content.Context
import android.graphics.Bitmap
import com.dailystudio.devbricksx.utils.ImageUtils
import com.dailystudio.devbricksx.utils.MatrixUtils
import com.dailystudio.tensorflow.lite.viewer.image.AbsTFLiteCameraFragment
import com.dailystudio.tensorflow.lite.viewer.image.AbsTFLiteImageAnalyzer
import com.dailystudio.tensorflow.lite.viewer.image.ImageInferenceInfo
import com.dailystudio.tensorflow.lite.viewer.ui.InferenceSettingsPrefs
import com.dailystudio.tensorflow.lite.viewer.utils.ModelUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import org.tensorflow.lite.task.vision.classifier.ImageClassifier.ImageClassifierOptions
import java.nio.MappedByteBuffer


class ImageClassifierAnalyzer(rotation: Int, lensFacing: Int)
    : AbsTFLiteImageAnalyzer<ImageInferenceInfo, List<Category>>(rotation, lensFacing) {

    companion object {

        private const val TF_LITE_MODEL_FILE = "lite-model_aiy_vision_classifier_insects_V1_3.tflite"
        private const val MAX_RESULTS = 10
    }

    private var classifier: ImageClassifier? = null
    private var lock = Object()

    private fun createModel(context: Context): ImageClassifier {
        val numOfThreads = InferenceSettingsPrefs.instance.numberOfThreads

        val builder: ImageClassifierOptions.Builder =
            ImageClassifierOptions.builder()
                .setMaxResults(MAX_RESULTS)
                .setNumThreads(numOfThreads)
                .setDisplayNamesLocale("en")

        val modelFile: MappedByteBuffer? = ModelUtils.loadModelFile(
            context.assets, TF_LITE_MODEL_FILE
        )

        return ImageClassifier.createFromBufferAndOptions(modelFile, builder.build())
    }

    override fun analyzeFrame(
        context: Context,
        inferenceBitmap: Bitmap,
        info: ImageInferenceInfo
    ): List<Category> {
        val tImage = TensorImage.fromBitmap(inferenceBitmap)

        val categories: MutableList<Category> = mutableListOf()

        synchronized(lock) {
            classifier = classifier ?: createModel(context)

            val start = System.currentTimeMillis()

            val classifications = classifier?.classify(tImage)

            classifications?.let {
                categories.addAll(it[0].categories)

                categories.sortByDescending { category ->
                    category.score
                }
            }

            val end = System.currentTimeMillis()

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
                GlobalScope.launch(Dispatchers.IO) {
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