import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import compose.icons.FeatherIcons
import compose.icons.feathericons.Clock
import compose.icons.feathericons.Image
import data.RecipeItem
import kotlinx.coroutines.launch
import java.io.IOException


@Composable
fun AddRecipe(imageHandler: ImageHandler, onRecipeAdded: (RecipeItem) -> Unit, onCancel: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var preptime by remember { mutableStateOf("") }
    var selectedImage by remember { mutableStateOf<ByteArray?>(null) }
    var showFilePicker by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var imageAdded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDD1)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomTextField(
                value = name,
                onValueChange = { name = it },
                label = "Name"
            )
            CustomTextField(
                value = instructions,
                onValueChange = { instructions = it },
                label = "Instruction"
            )
            CustomTextField(
                value = ingredients,
                onValueChange = { ingredients = it },
                label = "Ingredients"
            )
            CustomTextField(
                value = category,
                onValueChange = { category = it },
                label = "Category"
            )

            CustomTextField(
                value = preptime,
                onValueChange = { preptime = it },
                label = "Preparation time",
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFD67D)),
                onClick = {
                    showFilePicker = true
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = FeatherIcons.Image,
                        contentDescription = "image",
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Upload picture")
                }
            }

            if (imageAdded) {
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

            Row(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
                horizontalArrangement  =  Arrangement.SpaceEvenly,
            ) {
                Button(
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFD67D))
                ) {
                    Text("Cancel")
                }

                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFD67D)),
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
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
) {
    var isFocused by remember { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                color = if (isFocused) Color(0xFFFFD67D) else Color.DarkGray
            )
        },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = Color(0xFFFFD67D),
            unfocusedIndicatorColor = Color.Gray,
            cursorColor = Color(0xFFFFD67D),
            textColor = Color.DarkGray
        )
    )
}