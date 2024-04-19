
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import data.RecipeItem
import data.RecipeRepository
import org.jetbrains.compose.resources.ExperimentalResourceApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Clock

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App(recipes: List<RecipeItem>, repository: RecipeRepository) {
    var selectedRecipe by remember { mutableStateOf<RecipeItem?>(null) }
    var addingRecipe by remember { mutableStateOf(false) }
    var showFavourites by remember { mutableStateOf(false) }
    var currentRecipes by remember { mutableStateOf(recipes.toMutableList()) }

    MaterialTheme {
        if (addingRecipe) {
            AddRecipe(
                onRecipeAdded = { newRecipe ->
                    addingRecipe = false
                    repository.insertRecipe(newRecipe)
                    currentRecipes += repository.getLastRecipe()
                    selectedRecipe = null
                },
                onCancel = {addingRecipe = false})
        } else if (selectedRecipe == null) {

            Column(
                modifier = Modifier.fillMaxSize() .background(Color(0xFFFFFDD1)), //0xFFFFFDD1 0xFFFFFEE8
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Button(
                        onClick = { showFavourites = !showFavourites },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFD67D)),
                    ) {
                        if (showFavourites)
                            Text("Show all recipes")
                        else
                            Text("Show favourites")
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                }

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp)

                ) {
                    if(!showFavourites) {
                    items(currentRecipes) { recipe ->
                        RecipeListItem(recipe = recipe, repository) {
                            selectedRecipe = recipe
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }} else {
                        items(repository.getSavedRecipes().toMutableList()) { recipe ->
                            RecipeListItem(recipe = recipe, repository) {
                                selectedRecipe = recipe
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
                Button(
                    onClick = { addingRecipe = true },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFD67D)),
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                ) {
                    Text("Add Recipe")
                }
            }
        } else {
            var onDelete = {
                if (selectedRecipe != null) {
                    repository.delete(selectedRecipe!!)
                    currentRecipes  -= selectedRecipe!!
                    selectedRecipe = null
                }
            }
            detailsLayout(repository, recipe = selectedRecipe!!,
                onClose = { updatedRecipe ->
                    if (updatedRecipe != null) {
                        val index = currentRecipes.indexOfFirst { it.id == updatedRecipe.id }
                        if (index != -1) {
                            currentRecipes[index] = updatedRecipe
                        }
                    }
                    selectedRecipe = null
                },
                onDelete)
        }
    }
}

@Composable
fun RecipeListItem(recipe: RecipeItem, repository: RecipeRepository, onClick: () -> Unit) { //0xFFFFF3B0
    Card(elevation = 10.dp, modifier = Modifier.padding(5.dp), backgroundColor = Color.White) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(recipe.image != null) {
                recipe.image?.let { imageData ->
                    val imageBitmap = toImageBitmap(imageData)
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = null,
                        modifier = Modifier
                            .width(72.dp)
                            .fillMaxWidth()
                            .padding(6.dp, 0.dp)
                    )
                }
            } else {
                Image(
                    painter = getDefaultImage(),
                    contentDescription = null,
                    modifier = Modifier
                        .width(72.dp)
                        .fillMaxWidth()
                        .padding(6.dp, 0.dp)
                )
            }
            Row {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                        .clickable { onClick() }
                ) {
                    Text(
                        text = recipe.name,
                        style = MaterialTheme.typography.h5,
                    )
                    Text(
                        text = "Category: ${recipe.category}",
                        style = MaterialTheme.typography.body1,
                    )
                    PrepTimeIcon(recipe, 12.dp, MaterialTheme.typography.body2)
                }
                StarButton(recipe, repository)
            }
        }
    }
}

@Composable
fun Details(recipe: RecipeItem) {
    Text(
        text = "Ingredients",
        style = MaterialTheme.typography.h6,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    Text(
        text = buildAnnotatedString {
            recipe.ingredients.split("\n").forEachIndexed { index, ingredient ->
                val bulletPoint = if (index == 0) "• " else "\n• "
                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                    append(bulletPoint)
                }
                append(ingredient.trim())
            }
        },
        style = MaterialTheme.typography.body1
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "Instructions",
        style = MaterialTheme.typography.h6,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    Text(
        text = recipe.instructions,
        style = MaterialTheme.typography.body1
    )
}

@Composable
fun PrepTimeIcon(recipe: RecipeItem, iconSize: Dp, textStyle: TextStyle) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = FeatherIcons.Clock,
            contentDescription = "Preparation Time Icon",
            modifier = Modifier.size(iconSize)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = recipe.preptime,
            style = textStyle,
        )
    }
}

@Composable
fun showAlertDialog(onDelete: () -> Unit): Boolean {
    var showDeleteConfirmationDialog by remember { mutableStateOf(true) }
    if(showDeleteConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmationDialog = false },
            title = { Text("Delete Recipe") },
            text = { Text("Are you sure you want to delete this recipe?") },
            dismissButton = {
                Button(
                    onClick = { showDeleteConfirmationDialog = false },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFD67D)),
                ) {
                    Text("No")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete()
                        showDeleteConfirmationDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFD67D)),
                ) {
                    Text("Yes")
                }
            },
        )
    }
    return showDeleteConfirmationDialog
}

@Composable
fun StarButton(recipe: RecipeItem, repository: RecipeRepository) {
    var r = repository.getRecipe(recipe)

    var isFavourite by remember { mutableStateOf(false) }

    isFavourite = r.saved == 1L

    IconButton(
        onClick = {
            isFavourite = !isFavourite

            var updatedRecipe = r.copy(saved = if (isFavourite) 1L else 0L)

            repository.update(updatedRecipe)
        }
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Star",
            tint = if (isFavourite) Color(0xFFf5dd42) else Color.LightGray
        )
    }
}