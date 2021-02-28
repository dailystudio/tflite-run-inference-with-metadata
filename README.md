# Run TensorFlow Lite models with metadata

This repostiory illustrates three approches of using TensorFlow Lite models with metadata.

## TensorFlow Lite inference with metadata
TensorFlow Lite metadata contains a rich description of what the model does and how to use the model. It can empower code generators to automatically generate the inference code for you. Plus, It can also be used to configure your custom inference pipeline.

According to different complexity and requirement, TensorFlow Lite provides serveral ways to leverage powerful metadata to accelerate on-deive maching learning:

- **Android Studio ML Model Binding** is tooling available within Android Studio to import TensorFlow Lite model through a graphical interface. Android Studio will automatically configure settings for the project and generate wrapper classes based on the model metadata.

- **TensorFlow Lite Code Generator** is an executable that generates model interface automatically based on the metadata. It currently supports Android with Java. The wrapper code removes the need to interact directly with ByteBuffer. Instead, developers can interact with the TensorFlow Lite model with typed objects such as Bitmap and Rect. Android Studio users can also get access to the codegen feature through Android Studio ML Binding.

- **TensorFlow Lite Task Library** provides optimized ready-to-use model interfaces for popular machine learning tasks, such as image classification, question and answer, etc. The model interfaces are specifically designed for each task to achieve the best performance and usability. Task Library works cross-platform and is supported on Java, C++, and Swift.

- **TensorFlow Lite Support Library** is a cross-platform library that helps to customize model interface and build inference pipelines. It contains varieties of util methods and data structures to perform pre/post processing and data conversion. It is also designed to match the behavior of TensorFlow modules, such as TF.Image and TF.Text, ensuring consistency from training to inferencing.
