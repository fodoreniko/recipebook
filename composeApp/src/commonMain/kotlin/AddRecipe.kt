import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import data.RecipeItem
import kotlinx.coroutines.launch
import java.io.IOException


@Composable
fun AddRecipe(imageHandler: ImageHandler, onRecipeAdded: (RecipeItem) -> Unit) {
    var name by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var preptime by remember { mutableStateOf("") }
    var selectedImage by remember { mutableStateOf<ByteArray?>(null) }
    var showFilePicker by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var imageAdded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = instructions,
            onValueChange = { instructions = it },
            label = { Text("Instruction") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = ingredients,
            onValueChange = { ingredients = it },
            label = { Text("Ingredients") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = preptime,
            onValueChange = { preptime = it },
            label = { Text("Preparation time") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                showFilePicker = true
            }
        ) {
            Text("Upload picture")
        }

        if(imageAdded)  {
            selectedImage?.let { imageData ->
                Image(
                    bitmap = imageHandler.toImageBitmap(imageData),
                    contentDescription = null,
                    modifier = Modifier
                        .width(72.dp)
                        .fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val newRecipe = RecipeItem(
                    id = 0,
                    name = name,
                    instructions = instructions,
                    ingredients = ingredients,
                    category = category,
                    saved = 0,
                    preptime = preptime,
                    image = selectedImage
                )
                onRecipeAdded(newRecipe)
            }
        ) {
            Text("Add recipe")
        }

        if (showFilePicker) {
            val fileType = listOf("jpg", "png")

            FilePicker(show = showFilePicker, fileExtensions = fileType) { platformFile ->
                showFilePicker = false

                platformFile?.let {
                    coroutineScope.launch {
                        try {
                            val bytes = imageHandler.getFileByteArray(platformFile)
                            selectedImage = imageHandler.resizeImage(bytes, 1000)
                            imageAdded = true
                        } catch (e: IOException) {
                            throw e
                        }
                    }
                }
            }
        }
    }
}