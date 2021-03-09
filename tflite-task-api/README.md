# TensorFlow Lite Task Library

TensorFlow Lite Task Library contains a set of powerful and easy-to-use task-specific libraries for app developers to create ML experiences with TFLite. It provides optimized out-of-box model interfaces for popular machine learning tasks, such as image classification, question and answer, etc. The model interfaces are specifically designed for each task to achieve the best performance and usability. 

## Preparation
Before getting started, you need to assure the following things prerequisites are ready:

- Android Studio 4.1 or later
- An Android device (mobile phones are better) with Android 5.0+ (API Level 21)
- TensorFlow Lite model - [insects_v1](https://tfhub.dev/google/aiy/vision/classifier/insects_V1/1)

> In the following steps, we assume that 
>
> - you clone the repository into this specific directory, **/Volumes/Workspace/gitrepos/dailystudio/tflite-run-inference-with-metadata/** 
> - you put models in the download directory, **/Volumes/Workspace/sandbox/tflite-example/models/** 
> - your target working directory is **/Volumes/Workspace/sandbox/tflite-example/**

## Let's go

### Initialize the project from the boilerplate

Copy the boilerplate project into your destination and import it into Android Studio.

``` Shell
$ cd /Volumes/Workspace/sandbox/tflite-example/
$ cp -a /Volumes/Workspace/gitrepos/dailystudio/tflite-run-inference-with-metadata/boilerplate tflite-task-api
```

### Add dependency of TensorFlow Lite Task Library
Adding Task Library dependency in **app/build.gradle**.

```Groovy
implementation 'org.tensorflow:tensorflow-lite-task-vision:0.1.0'
```

### Edit the code

Modify the implementation of **ImageClassifierAnalyzer** to run the inference of the model.

Changing classifier declaration to your specific type:

```Kotlin
private var classifier: ImageClassifier? = null
```

In **analyzeFrame()**, creating an instance of classifier

```Kotlin
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
```

And call classify() to do the inference.

```Kotlin
...
    val categories: MutableList<Category> = mutableListOf()

    synchronized(lock) {
        classifier = classifier ?: createModel(context)

        val classifications = classifier?.classify(tImage)

        classifications?.let {
            categories.addAll(it[0].categories)

            categories.sortByDescending { category ->
                category.score
            }
        }
    }
...
```

### Launch the app

Launch the app and you will see the results,
![](../.github/task_api_1.png)

### Remove useless settings
By default, **InferenceSettingsFragment** includes two settings options:

- Inference Device
- Number of threads

Since TensorFlow Lite Task Library does not support the GPU delegate, we need to remove useless settings from the default project template. We remove **Inference Device** and keep **Number of threads**.

Create a new class, which is named **SimpleInferenceSettingsFragment**, and add the following implementation:

```Kotlin
class SimpleInferenceSettingsFragment: InferenceSettingsFragment() {

    override fun createSettings(context: Context): Array<AbsSetting> {
        val oldSettings = super.createSettings(context)

        return arrayOf(oldSettings[1])
    }

}
```

Re-launch the app, you will see:

![](../.github/task_api_2.png)

## Tips

There are a few tips when you should notice:

- TensorFlow Lite Task Library does not a simple wrapper of TensorFlow Lite Support Library. Each function in Task Library has a relative JNI implementation underline. That means **Task Library does not support GPU delegate**. But, you needn't worry about its performance. Its data processing would take no more than a few milliseconds.
- If you want your own Lite model to fit the Task Library well. You have to **check the compatibility of your model**. There is a model compatibility requirements section of each Task Library's documentation.
