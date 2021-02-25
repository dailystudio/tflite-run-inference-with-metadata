package com.dailystudio.tflite.example.mlmodelbinding.fragment

import android.graphics.Bitmap
import com.dailystudio.devbricksx.GlobalContextWrapper
import com.dailystudio.devbricksx.utils.ImageUtils
import com.dailystudio.devbricksx.utils.MatrixUtils
import com.dailystudio.tensorflow.lite.viewer.image.AbsTFLiteCameraFragment
import com.dailystudio.tensorflow.lite.viewer.image.AbsTFLiteImageAnalyzer
import com.dailystudio.tensorflow.lite.viewer.image.ImageInferenceInfo
import com.dailystudio.tflite.example.mlmodelbinding.ml.LiteModelAiyVisionClassifierBirdsV13
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.label.Category


class ImageClassifierAnalyzer(rotation: Int, lensFacing: Int)
    : AbsTFLiteImageAnalyzer<ImageInferenceInfo, List<Category>>(rotation, lensFacing) {

    private val classifier: LiteModelAiyVisionClassifierBirdsV13? by lazy {
        GlobalContextWrapper.context?.let {
            LiteModelAiyVisionClassifierBirdsV13.newInstance(it)
        }
    }

    override fun analyzeFrame(inferenceBitmap: Bitmap,
                              info: ImageInferenceInfo): List<Category>? {
        val tImage = TensorImage.fromBitmap(inferenceBitmap)

        val categories = classifier?.process(tImage)?.probabilityAsCategoryList

        categories?.sortByDescending {
            it.score
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
            frameBitmap.height, 640, 480, info.imageRotation, true
        )

        return ImageUtils.createTransformedBitmap(
            frameBitmap,
            matrix
        )
    }

}

class ImageClassifierCameraFragment
    : AbsTFLiteCameraFragment<ImageInferenceInfo, List<Category>>() {

    override fun createAnalyzer(screenAspectRatio: Int, rotation: Int, lensFacing: Int): AbsTFLiteImageAnalyzer<ImageInferenceInfo, List<Category>> {
        return ImageClassifierAnalyzer(rotation, lensFacing)
    }

}