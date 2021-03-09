# Run TensorFlow Lite models with metadata on Android

This repository illustrates three approaches to use TensorFlow Lite models with metadata on Android platforms.

## TensorFlow Lite inference with metadata
TensorFlow Lite metadata contains a rich description of what the model does and how to use the model. It can empower code generators to automatically generate the inference code for you. Plus, It can also be used to configure your custom inference pipeline.

According to different complexity and requirement, TensorFlow Lite provides several ways to leverage powerful metadata to accelerate on-deive machine learning:

- **Android Studio ML Model Binding** is tooling available within Android Studio to import the TensorFlow Lite model through a graphical interface. Android Studio will automatically configure settings for the project and generate wrapper classes based on the model metadata.

- **TensorFlow Lite Code Generator** is an executable that generates a model interface automatically based on the metadata. It currently supports Android with Java. The wrapper code removes the need to interact directly with ByteBuffer. Instead, developers can interact with the TensorFlow Lite model with typed objects such as Bitmap and Rect. Android Studio users can also get access to the codegen feature through Android Studio ML Binding.

- **TensorFlow Lite Task Library** provides optimized ready-to-use model interfaces for popular machine learning tasks, such as image classification, question, and answer, etc. The model interfaces are specifically designed for each task to achieve the best performance and usability. Task Library works cross-platform and is supported on Java, C++, and Swift.

- **TensorFlow Lite Support Library** is a cross-platform library that helps to customize the model interface and build inference pipelines. It contains varieties of util methods and data structures to perform pre/post-processing and data conversion. It is also designed to match the behavior of TensorFlow modules, such as TF.Image and TF.Text, ensuring consistency from training to inferencing.

## Directories

- [**boilerplate**](./boilerplate) Project boilerplate. It includes a basic structure of a camera-based TensorFlow Lite application. Developers only need to focus on their specific inference codes without any knowledge about the rest parts of the app.
- [**ml-model-binding**](./ml-model-binding) Complete source codes of a bird recognition application. It demonstrates how to use ML Model Binding with TensorFlow Lite models. 
- [**codegen**](./codegen) Complete source codes of a food recognition application. It demonstrates how to use TensorFlow Lite Code Generator with TensorFlow Lite models. 
- [**tflite-task-api**](./tflite-task-api) Complete source codes of an insects recognition application. It demonstrates how to use TensorFlow Lite Task Library with TensorFlow Lite models. 

## License
    Copyright 2021 Daily Studio.

    Licensed under the GNU General Public License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

