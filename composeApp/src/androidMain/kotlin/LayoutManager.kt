import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.List
import androidx.compose.material.DropdownMenu
import data.RecipeItem
import data.RecipeRepository

@Composable
actual fun detailsLayout(repository: RecipeRepository, recipe: RecipeItem, onClose: (RecipeItem?) -> Unit, onDelete: () -> Unit) {
    var showDeleteConfirmationDialog  by remember { mutableStateOf(false) }
    var edit  by remember { mutableStateOf(false) }
    var editedRecipe  by remember { mutableStateOf(recipe) }
    var modifiedRecipe  by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

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
            Column {
                TopAppBar(
                    title = { Text(editedRecipe.name) },
                    navigationIcon = {
                        IconButton(onClick = { onClose(if (modifiedRecipe) editedRecipe else null) }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        Box(
                            contentAlignment = Alignment.TopEnd
                        ) {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(
                                    imageVector = FeatherIcons.List,
                                    contentDescription = "Options"
                                )
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                            ) {
                                DropdownMenuItem(onClick = { edit = true }) {
                                    Text("Edit")
                                }
                                DropdownMenuItem(onClick = {
                                    showDeleteConfirmationDialog = true
                                }) {
                                    Text("Delete")
                                }
                            }
                        }
                    },
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
                    ) {
                        if(editedRecipe.image != null) {
                            editedRecipe.image?.let { imageData ->
                                Image(
                                    bitmap = toImageBitmap(imageData),
                                    contentDescription = null,
                                )
                            }
                        } else {
                            Image(
                                painter = getDefaultImage(),
                                contentDescription = null,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    PrepTimeIcon(editedRecipe, 24.dp, MaterialTheme.typography.body1)
                    Spacer(modifier = Modifier.height(16.dp))
                    Details(editedRecipe)
                }
            }

            if (showDeleteConfirmationDialog) {
                showDeleteConfirmationDialog = showAlertDialog(onDelete)
            }
        }
    }
}