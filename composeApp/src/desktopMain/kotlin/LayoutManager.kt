import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.RecipeItem
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontStyle
import data.RecipeRepository

@Composable
actual fun detailsLayout(repository: RecipeRepository, recipe: RecipeItem, onClose: (RecipeItem?) -> Unit, onDelete: () -> Unit) {
    var showDeleteConfirmationDialog  by remember { mutableStateOf(false) }
    var edit  by remember { mutableStateOf(false) }
    var editedRecipe  by remember { mutableStateOf(recipe) }
    var modifiedRecipe  by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDD1)),
    ) {
        if (edit) {
            editRecipe(recipe, { edit = false }) { updatedRecipe ->
                repository.update(updatedRecipe)
                editedRecipe = updatedRecipe
                modifiedRecipe = true
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp),
            ) {

                Column(
                    modifier = Modifier.weight(0.6f).fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if(editedRecipe.image != null) {
                        editedRecipe.image?.let { imageData ->
                            Image(
                                bitmap = toImageBitmap(imageData),
                                contentDescription = null,
                                modifier = Modifier
                                    .width(200.dp)
                                    .height(200.dp)
                            )
                        }
                    } else {
                        Image(
                            painter = getDefaultImage(),
                            contentDescription = null,
                            modifier = Modifier
                                .width(200.dp)
                                .height(200.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = editedRecipe.name,
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    PrepTimeIcon(editedRecipe, 24.dp, MaterialTheme.typography.body1)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row() {
                        Text(
                            text = "Category: ",
                            style = MaterialTheme.typography.body1,
                            fontStyle = FontStyle.Italic,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = editedRecipe.category,
                            style = MaterialTheme.typography.body1,
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = { onClose(if (modifiedRecipe) editedRecipe else null) },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFD67D))
                    ) {
                        Text("Close")
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {

                        Button(
                            onClick = { edit = true },
                            modifier = Modifier.width(100.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFD67D)),
                            border = BorderStroke(2.dp, Color(0xFFD2A4DB)),
                        ) {
                            Text("Edit")
                        }
                        Button(
                            onClick = { showDeleteConfirmationDialog = true },
                            modifier = Modifier.width(100.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFD67D)),
                            border = BorderStroke(2.dp, Color(0xFFF28080))
                        ) {
                            Text("Delete")
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(Color.Black)
                )
                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1.4f).verticalScroll(rememberScrollState())) {
                    Details(editedRecipe)
                }
            }

            if (showDeleteConfirmationDialog) {
                showDeleteConfirmationDialog = showAlertDialog(onDelete)
            }
        }
    }
}