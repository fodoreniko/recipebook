import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import data.RecipeItem


@Composable
fun editRecipe(recipe: RecipeItem, imageHandler: ImageHandler, onClose: () -> Unit, onSave: (RecipeItem) -> Unit) {
    var editedName by remember { mutableStateOf(recipe.name) }
    var editedIngredients by remember { mutableStateOf(recipe.ingredients) }
    var editedInstructions by remember { mutableStateOf(recipe.instructions) }
    var editedPrepTime by remember { mutableStateOf(recipe.preptime) }
    var editedCategory by remember { mutableStateOf(recipe.category) }
    var selectedImage by remember { mutableStateOf(recipe.image) }
    var showFilePicker by remember { mutableStateOf(false) }

    Column {
        TopAppBar(
            title = { Text("") },
            backgroundColor = Color(0xFFFFD67D)
        )
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable { showFilePicker = true }
            ) {
                selectedImage?.let { imageData ->
                    Image(
                        bitmap = imageHandler.toImageBitmap(imageData),
                        contentDescription = null,
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp)
                    )
                }

            }
            CustomTextField(editedName, { editedName = it }, "Name")
            Spacer(modifier = Modifier.height(6.dp))
            CustomTextField(editedPrepTime, { editedPrepTime = it }, "Preparation time")
            Spacer(modifier = Modifier.height(6.dp))
            CustomTextField(editedCategory, { editedCategory = it }, "Category")
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(editedIngredients, { editedIngredients = it }, "Ingredients")
            CustomTextField(editedInstructions, { editedInstructions = it }, "Instructions")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Button(
                    onClick = {
                        val updatedRecipe = RecipeItem(
                            id = recipe.id,
                            name = editedName,
                            instructions = editedInstructions,
                            ingredients = editedIngredients,
                            category = editedCategory,
                            saved = recipe.saved,
                            preptime = editedPrepTime,
                            image = selectedImage
                        )
                        onSave(updatedRecipe)
                        onClose()
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFD67D)),
                    border = BorderStroke(2.dp, Color(0xFF89D9A5)),
                ) {
                    Text("Confirm")
                }

                Button(
                    onClick = onClose,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFD67D)),
                    border = BorderStroke(2.dp, Color(0xFFF28080)),
                ) {
                    Text("Discard")
                }
            }
        }

        if(showFilePicker)  {
            selectedImage = addImage(imageHandler)
        }
    }
}