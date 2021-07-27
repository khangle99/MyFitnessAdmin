package com.khangle.myfitnessadmin.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun String.toBitmap(context: Context, coroutineScope: CoroutineScope,handler: (Bitmap?) -> Unit) {
    coroutineScope.launch(Dispatchers.IO) {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(this@toBitmap)
            .allowHardware(false) // Disable hardware bitmaps.
            .build()

        val result = (loader.execute(request) as? SuccessResult)?.drawable
        val bitmap = (result as? BitmapDrawable)?.bitmap
        withContext(Dispatchers.Main) {
            handler(bitmap)
        }
    }

}