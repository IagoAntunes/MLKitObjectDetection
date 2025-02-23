![detecção-objetos-banner](https://github.com/IagoAntunes/MLKitObjectDetection/blob/master/app/src/main/res/drawable/github/object_detection_cover.png)

# AI Found 
The shopping app prototype that lists a series of products with image, description, price and allows you to add them to the cart. Thanks to object recognition, it is possible to search for products just by pointing the Android camera and whatever is identified will be searched for in the App's database.


## 🔨 Project features
https://github.com/git-jr/3648-Android-MLKit-Object-Detection/assets/35709152/6996f637-7b22-4ca2-a6a4-b1758096c038

### ✨ Object detection
- When opening the product search, the camera passes the frames to the detector for analysis in real time, allowing analysis by the model. 
- Each frame is analyzed for information through the model in use (standard or customized with TensorFlow Lite) 
- When a product is identified by the model, an overlay is displayed in the camera preview at the exact coordinates of the object in question. 
- The detected product is searched for in the database and displayed on the screen automatically, allowing it to be added to the shopping cart
  
### 📱Screens
- Home list: products available for purchase
- Details: information such as price and description in addition to the possibility of adding it to the cart. 
- Cart: list of all products added via details screen or camera detection.

## ✔️ Techniques and technologies used

The techniques and technologies used for this are:

- `Jetpack Compose`: Modern toolkit for creating UIs on mobile devices
- `Kotlin`: programming language
- `Gradle Version Catalogs`: new way to manage plugins and dependencies in Android projects
- `Material Design 3`: design pattern recommended by Google for creating modern UIs
- `Hilt`: dependency injection
- `Navigating with Compose`: navigation between composables and screens
- `Viewmodel, states and flow`: management of states and control of events triggered by Google model detections
- `ML Kit Object Detection`: library to detect and extract information about objects in real time using the device camera
- `TensorFlow Lite Models`: set of pre-trained and optimized models to run efficiently on mobile devices.
- `Teachable Machine`: Platform that allows you to train machine learning models easily, without the need to write code. Allows users to create custom models for object recognition, image classification and more, with an intuitive and user-friendly interface.
- `CameraX`: Jetpack library that makes it easy to integrate camera functionality into Android apps, abstracting the complexity of the Android Camera API and offering a simple interface for capturing photos and video
- `CameraAnalyzer`: component used with CameraX to analyze video frames in real time, allowing the implementation of detection features that require frame-by-frame processing.

## Social
[Linkedin](https://www.linkedin.com/in/iagoaferreira/)

[Portfólio](https://iagoferreira.web.app/)
