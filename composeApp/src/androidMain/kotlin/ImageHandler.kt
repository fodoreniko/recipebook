import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import java.io.IOException
import androidx.core.net.toUri
import java.io.ByteArrayOutputStream
import kotlin.math.min


actual class ImageHandler(private val applicationContext: Any) {

    actual suspend fun getFileByteArray(platformFile: MPFile<Any>): ByteArray {
        val contentResolver: ContentResolver = (applicationContext as android.content.Context).contentResolver
        val inputStream = contentResolver.openInputStream(platformFile.path.toUri())
        val bytes = inputStream?.readBytes()
        inputStream?.close()
        return bytes ?: throw IOException("Failed to read file")
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
}


