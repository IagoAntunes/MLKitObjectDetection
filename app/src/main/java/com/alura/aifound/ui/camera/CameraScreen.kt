package com.alura.aifound.ui.camera

import android.util.Log
import android.util.Size
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.alura.aifound.data.Product
import com.alura.aifound.extensions.dpToPx
import com.alura.aifound.extensions.pxToDp
import com.alura.aifound.mlkit.ObjectDetectorProcessor
import com.google.mlkit.vision.objects.DetectedObject


@OptIn(ExperimentalGetImage::class)
@Composable
fun CameraScreen(
    onNewProductDetected: (Product) -> Unit
) {
    val viewModel = hiltViewModel<CameraViewModel>()
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current.applicationContext

    LaunchedEffect(state.detectedProduct) {
        state.detectedProduct?.let {
            onNewProductDetected(it.product)
        } ?: run {
            onNewProductDetected(Product())
        }
    }

    val objectDetector = remember {
        ObjectDetectorProcessor().apply {
            setCustomModel("model_products")
        }
    }


    val cameraAnalyzer = remember {
        CameraAnalyzer { imageProxy ->
            Log.d("CameraAnalyzer", "Image received: ${state.imageWidth}x${state.imageHeight}")

            viewModel.setImageSize(imageProxy.width, imageProxy.height)

            objectDetector.processImage(
                imageProxy,
                onSuccess = { detectedObjects: List<DetectedObject> ->
                    Log.e("DETECTED", "Detected objects: ${detectedObjects.size}")
                    detectedObjects.firstOrNull()?.let { detectedObject ->
                        viewModel.setObjectDetected(detectedObject)
                    } ?: run {
                        viewModel.resetDetectedProduct()
                    }
                },
                onError = {
                    imageProxy.close()
                }
            )
        }
    }

    // 1 Camera Controller
    val cameraController = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
            setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(context),
                cameraAnalyzer
            )
        }
    }

    // 2 Camera Preview
    CameraPreview(cameraController = cameraController)

    // 3 Overlay
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.2f))
    ) {
        viewModel.setScreenSize(maxWidth.dpToPx().toInt(), maxHeight.dpToPx().toInt())

        state.detectedProduct?.let {
            ObjectOverlay(
                boundsObject = it.boundingBox,
                nameObject = it.product.name,
                coordinateX = it.coordinateX.pxToDp(),
                coordinateY = it.coordinateY.pxToDp()
            )
        }


    }
}


@Composable
private fun CameraPreview(
    modifier: Modifier = Modifier,
    cameraController: LifecycleCameraController,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        factory = { context ->
            PreviewView(context).apply {
                this.controller = cameraController
                cameraController.bindToLifecycle(lifecycleOwner)
            }
        },
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun ObjectOverlay(
    boundsObject: Rect,
    nameObject: String,
    coordinateX: Dp,
    coordinateY: Dp
) {
    Column(
        modifier = Modifier
            .offset(x = coordinateX, y = coordinateY)
            .size(boundsObject.width.dp, boundsObject.height.dp + boundsObject.height.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(boundsObject.width.dp, boundsObject.height.dp)
                .border(width = 5.dp, color = Color.White, shape = RoundedCornerShape(15.dp)),
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (nameObject.isNotEmpty()) {
            Text(
                text = nameObject,
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.White, shape = RoundedCornerShape(5.dp)
                    )
                    .padding(8.dp)
            )
        }
    }
}