import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import org.jetbrains.skia.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO


actual class ImageHandler {
    actual suspend fun getFileByteArray(platformFile: MPFile<Any>): ByteArray {
        return platformFile.getFileByteArray()
    }
    actual suspend fun resizeImage(fileBytes: ByteArray, size: Int): ByteArray {
        val inputImage = ImageIO.read(ByteArrayInputStream(fileBytes))
        val imageSize = minOf(inputImage.width, inputImage.height)

        val croppedImage = cropImage(inputImage, imageSize)
        val resizedImage = resizeImage(croppedImage, size)

        val outputStream = ByteArrayOutputStream()
        ImageIO.write(resizedImage, "jpg", outputStream)

        return outputStream.toByteArray()
    }
    private fun cropImage(inputImage: BufferedImage, size: Int): BufferedImage {
        val startX = (inputImage.width - size) / 2
        val startY = (inputImage.height - size) / 2

        return inputImage.getSubimage(startX, startY, size, size)
    }

    private fun resizeImage(inputImage: BufferedImage, size: Int): BufferedImage {
        val outputImage = BufferedImage(size, size, BufferedImage.TYPE_INT_RGB)
        val graphics = outputImage.createGraphics()
        graphics.drawImage(inputImage, 0, 0, size, size, null)
        graphics.dispose()

        return outputImage
    }
    actual fun toImageBitmap(bytearray: ByteArray): ImageBitmap {
        return Image.makeFromEncoded(bytearray).toComposeImageBitmap()
    }
}

