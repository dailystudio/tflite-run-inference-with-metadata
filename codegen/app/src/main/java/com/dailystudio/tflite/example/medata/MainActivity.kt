package com.dailystudio.tflite.example.medata

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dailystudio.tensorflow.lite.viewer.AbsTFLiteModelViewerActivity
import com.dailystudio.tensorflow.lite.viewer.image.ImageInferenceInfo
import com.dailystudio.tflite.example.medata.fragment.ImageClassifierCameraFragment
import org.tensorflow.lite.support.label.Category
import kotlin.math.min

class MainActivity : AbsTFLiteModelViewerActivity<ImageInferenceInfo, List<Category>>() {

    companion object {
        const val REPRESENTED_ITEMS_COUNT = 3
    }

    private var detectItemViews: Array<TextView?> =
        Array(REPRESENTED_ITEMS_COUNT) {null}
    private var detectItemValueViews: Array<TextView?> =
        Array(REPRESENTED_ITEMS_COUNT) {null}

    override fun createBaseFragment(): Fragment {
        return ImageClassifierCameraFragment()
    }

    override fun createResultsView(): View? {
        val resultsView = LayoutInflater.from(this).inflate(
                R.layout.layout_results, null)

        resultsView?.let {
            setupResultView(it)
        }

        return resultsView
    }

    private fun setupResultView(resultsView: View) {
        for (i in 0 until REPRESENTED_ITEMS_COUNT) {
            detectItemViews[i] = resultsView.findViewById(
                resources.getIdentifier("detected_item${i + 1}", "id", packageName)
            )

            detectItemValueViews[i] = resultsView.findViewById(
                resources.getIdentifier("detected_item${i + 1}_value", "id", packageName)
            )
        }
    }

    override fun onResultsUpdated(results: List<Category>) {
        val itemCount = min(results.size, REPRESENTED_ITEMS_COUNT)

        for (i in 0 until itemCount) {
            detectItemViews[i]?.text = results[i].displayName
            detectItemValueViews[i]?.text = "%.1f%%".format(results[i].score * 100)
        }
    }

    override fun getViewerAppName(): CharSequence? {
        return getString(R.string.app_name)
    }

    override fun getAboutIconResource(): Int {
        return R.mipmap.ic_launcher
    }

    override fun getAboutThumbResource(): Int {
        return R.drawable.app_thumb
    }

    override fun getViewerAppDesc(): CharSequence? {
        return getString(R.string.app_desc)
    }

}
