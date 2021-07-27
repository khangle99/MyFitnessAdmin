package com.khangle.myfitnessadmin.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun Uri.toBitmap(context: Context, coroutineScope: CoroutineScope, handle: (Bitmap) -> Unit) {
    coroutineScope.launch(Dispatchers.Default) {
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(
                    context.contentResolver,
                    this@toBitmap
                )
            )
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, this@toBitmap)
        }
        withContext(Dispatchers.Main) {
            handle(bitmap)
        }
    }

}