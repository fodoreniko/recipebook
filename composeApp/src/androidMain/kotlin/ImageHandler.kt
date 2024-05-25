import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import java.io.IOException
import androidx.core.net.toUri
import org.example.project.R
import java.io.ByteArrayOutputStream
import kotlin.math.min

var applicationContext: Any = object {}

fun setA(a: Any) {
    applicationContext = a
}

actual suspend fun resizeImage(fileBytes: ByteArray, size: Int): ByteArray {

    val bitmap = BitmapFactory.decodeByteArray(fileBytes, 0, fileBytes.size)

    val squareBitmap = cropToSquare(bitmap)

    val scaledBitmap = scaleBitmap(squareBitmap, size)

    return bitmapToByteArray(scaledBitmap)
}
private fun cropToSquare(bitmap: Bitmap): Bitmap {
    val size = min(bitmap.width, bitmap.height)
    val x = (bitmap.width - size) / 2
    val y = (bitmap.height - size) / 2
    return Bitmap.createBitmap(bitmap, x, y, size, size)
}

private fun scaleBitmap(bitmap: Bitmap, size: Int): Bitmap {
    val ratio = size.toFloat() / bitmap.width.toFloat()
    val matrix = Matrix()
    matrix.postScale(ratio, ratio)
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)
}

private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return outputStream.toByteArray()
}
actual fun toImageBitmap(bytearray: ByteArray): ImageBitmap {
    return BitmapFactory.decodeByteArray(bytearray, 0, bytearray.size).asImageBitmap()
}

actual suspend fun getFileByteArray(platformFile: MPFile<Any>): ByteArray {
    println("heheh")
    val contentResolver: ContentResolver = (applicationContext as android.content.Context).contentResolver
    val inputStream = contentResolver.openInputStream(platformFile.path.toUri())
    val bytes = inputStream?.readBytes()
    inputStream?.close()
    return bytes ?: throw IOException("Failed to read file")
}
@Composable
actual fun getDefaultImage(): Painter {
    return painterResource(R.drawable.default_image)
}




