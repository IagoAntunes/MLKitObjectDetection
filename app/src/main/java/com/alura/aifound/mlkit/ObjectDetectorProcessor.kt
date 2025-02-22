package com.alura.aifound.mlkit

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions

class ObjectDetectorProcessor {
    private lateinit var objectDetector: ObjectDetector

    init {
        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
            .enableClassification()
            .build()

        objectDetector = ObjectDetection.getClient(options)
    }

    fun setCustomModel(modelName: String) {
        val localModel = LocalModel.Builder()
            .setAssetFilePath("$modelName.tflite")
            .build()

        val customObjectDetectorOptions =
            CustomObjectDetectorOptions.Builder(localModel)
                .setDetectorMode(CustomObjectDetectorOptions.STREAM_MODE)
                .enableClassification()
                .setClassificationConfidenceThreshold(0.5f)
                .setMaxPerObjectLabelCount(3)
                .build()

        objectDetector = ObjectDetection.getClient(customObjectDetectorOptions)

    }

    @ExperimentalGetImage
    fun processImage(
        imageProxy: ImageProxy,
        onSuccess: (List<DetectedObject>) -> Unit,
        onError: (Exception) -> Unit = {},
    ) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            objectDetector.process(image)
                .addOnSuccessListener { objectList: List<DetectedObject> ->
                    onSuccess(objectList)
                }
                .addOnFailureListener { e ->
                    onError(e)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
            onError(Exception("Image is null"))
        }
    }

}