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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import compose.icons.AllIcons
import compose.icons.FeatherIcons
import compose.icons.feathericons.Clock

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App(recipes: List<RecipeItem>, repository: RecipeRepository, imageHandler: ImageHandler) {
    var selectedRecipe by remember { mutableStateOf<RecipeItem?>(null) }
    var addingRecipe by remember { mutableStateOf(false) }
    var currentRecipes by remember { mutableStateOf(recipes) }

    MaterialTheme {
        if (addingRecipe) {
            AddRecipe(imageHandler,
                onRecipeAdded = { newRecipe ->
                    addingRecipe = false
                    repository.insertRecipe(newRecipe)
                    currentRecipes  += newRecipe
                    selectedRecipe = null
                },
            onCancel = {addingRecipe = false})
        } else if (selectedRecipe == null) {

                Column(
                    modifier = Modifier.fillMaxSize() .background(Color(0xFFFFFDD1)), //0xFFFFFDD1 0xFFFFFEE8
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(16.dp)

                    ) {
                        items(currentRecipes) { recipe ->
                            RecipeListItem(imageHandler, recipe = recipe) {
                                selectedRecipe = recipe
                            }
                            Spacer(modifier = Modifier.height(8.dp))
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
            RecipeDetails(recipe = selectedRecipe!!, onClose = { selectedRecipe = null }, imageHandler)
        }
    }
}

@Composable
fun RecipeListItem(imageHandler: ImageHandler, recipe: RecipeItem, onClick: () -> Unit) { //0xFFFFF3B0
    Card(elevation = 10.dp, modifier = Modifier.padding(5.dp), backgroundColor = Color.White) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            recipe.image?.let { imageData ->
                val imageBitmap = imageHandler.toImageBitmap(imageData)
                Image(
                    bitmap = imageBitmap,
                    contentDescription = null,
                    modifier = Modifier
                        .width(72.dp)
                        .fillMaxWidth()
                        .padding(6.dp, 0.dp)
                )
            }
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
        }
    }
}

@Composable
fun RecipeDetails(recipe: RecipeItem, onClose: () -> Unit, imageHandler: ImageHandler) {
    var imageBitmap = ImageBitmap(1, 1)
    recipe.image?.let { imageData ->
        imageBitmap = imageHandler.toImageBitmap(imageData)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDD1)),
    ) {
        detailsLayout(recipe, imageBitmap, onClose)
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