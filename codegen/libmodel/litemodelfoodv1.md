# LiteModelFoodV1 Usage

```
import com.dailystudio.tflite.example.codegen.LiteModelFoodV1;

// 1. Initialize the Model
LiteModelFoodV1 model = null;

try {
    model = new LiteModelFoodV1(context);  // android.content.Context
    // Create the input container.
    LiteModelFoodV1.Inputs inputs = model.createInputs();
} catch (IOException e) {
    e.printStackTrace();
}

if (model != null) {

    // 2. Set the inputs
    // Load input tensor "image" from a Bitmap with ARGB_8888 format.
    Bitmap bitmap = ...;
    inputs.loadImage(bitmap);
    // Alternatively, load the input tensor "image" from a TensorImage.
    // Check out TensorImage documentation to load other image data structures.
    // TensorImage tensorImage = ...;
    // inputs.loadImage(tensorImage);

    // 3. Run the model
    LiteModelFoodV1.Outputs outputs = model.run(inputs);

    // 4. Retrieve the results
    Map<String, Float> probability = outputs.getProbability();
}
```
