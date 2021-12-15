/*
 * Copyright 2021 Md. Mahmudul Hasan Shohag
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ------------------------------------------------------------------------
 *
 * Project: Why Not Compose!
 * Developed by: @ImaginativeShohag
 *
 * Md. Mahmudul Hasan Shohag
 * imaginativeshohag@gmail.com
 *
 * Source: https://github.com/ImaginativeShohag/Why-Not-Compose
 */

package org.imaginativeworld.whynotcompose.ui.screens.tutorial.captureimageandcrop

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import org.imaginativeworld.whynotcompose.R
import org.imaginativeworld.whynotcompose.ui.screens.AppComponent
import org.imaginativeworld.whynotcompose.ui.theme.AppTheme
import org.imaginativeworld.whynotcompose.utils.SquireCropImage
import org.imaginativeworld.whynotcompose.utils.composeutils.rememberImagePainter
import org.imaginativeworld.whynotcompose.utils.extensions.createImageFile
import org.imaginativeworld.whynotcompose.utils.extensions.getUriForFile
import org.imaginativeworld.whynotcompose.utils.extensions.toast
import java.io.File
import java.util.Date

@Composable
fun CaptureImageAndCropScreen(
    viewModel: CaptureImageAndCropViewModel
) {
    val context = LocalContext.current

    // ----------------------------------------------------------------

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val uCropLauncher = rememberLauncherForActivityResult(SquireCropImage()) { uri ->
        imageUri = uri

        uri?.apply {
            viewModel.uploadPhoto(
                context = context,
                imageUri = uri,
            )
        }
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                imageUri?.let { uri ->
                    uCropLauncher.launch(
                        Pair(
                            first = uri,
                            second = Uri.fromFile(
                                File(context.cacheDir, "temp_image_file_${Date().time}")
                            )
                        )
                    )
                }
            } else {
                context.toast("Cannot save the image!")
            }
        }

    // ----------------------------------------------------------------

    CaptureImageAndCropScreenSkeleton(
        imagePath = imageUri,
        onChooseImageClicked = {
            val newPhotoUri = context.createImageFile().getUriForFile(context)

            imageUri = newPhotoUri

            cameraLauncher.launch(newPhotoUri)
        }
    )
}

@Preview
@Composable
fun CaptureImageAndCropScreenSkeletonPreview() {
    AppTheme {
        CaptureImageAndCropScreenSkeleton()
    }
}

@Preview
@Composable
fun CaptureImageAndCropScreenSkeletonPreviewDark() {
    AppTheme(darkTheme = true) {
        CaptureImageAndCropScreenSkeleton()
    }
}

@Composable
fun CaptureImageAndCropScreenSkeleton(
    imagePath: Uri? = null,
    onChooseImageClicked: () -> Unit = {},
) {
    Scaffold(
        Modifier
            .navigationBarsWithImePadding()
            .statusBarsPadding()
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            AppComponent.Header("Capture Image and Crop for Upload")

            // ----------------------------------------------------------------
            // ----------------------------------------------------------------

            Divider()

            // ----------------------------------------------------------------

            Image(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 32.dp, top = 32.dp, end = 32.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp)),
                painter = rememberImagePainter(
                    data = imagePath,
                    placeholder = R.drawable.default_placeholder
                ),
                contentDescription = "Image",
                contentScale = ContentScale.Crop,
            )

            Button(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 32.dp),
                onClick = {
                    onChooseImageClicked()
                }
            ) {
                Text("Capture Image")
            }

            // ----------------------------------------------------------------
            // ----------------------------------------------------------------

            AppComponent.BigSpacer()
        }
    }
}