import androidx.compose.ui.graphics.ImageBitmap
import com.darkrockstudios.libraries.mpfilepicker.MPFile

expect class ImageHandler {
    suspend fun getFileByteArray(platformFile: MPFile<Any>): ByteArray
    suspend fun resizeImage(fileBytes: ByteArray, size: Int): ByteArray
    fun toImageBitmap(bytearray: ByteArray): ImageBitmap
}

