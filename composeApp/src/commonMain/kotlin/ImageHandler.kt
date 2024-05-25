import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import com.darkrockstudios.libraries.mpfilepicker.MPFile


expect suspend fun resizeImage(fileBytes: ByteArray, size: Int): ByteArray
expect suspend fun getFileByteArray(platformFile: MPFile<Any>): ByteArray
expect fun toImageBitmap(bytearray: ByteArray): ImageBitmap
@Composable
expect fun getDefaultImage(): Painter
