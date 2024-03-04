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



@OptIn(ExperimentalResourceApi::class)
@Composable
fun App(recipes: List<RecipeItem>, repository: RecipeRepository) {
    var selectedRecipe by remember { mutableStateOf<RecipeItem?>(null) }
    var addingRecipe by remember { mutableStateOf(false) }
    var currentRecipes by remember { mutableStateOf(recipes) }

    MaterialTheme {
        if (addingRecipe) {
            AddRecipe(
                onRecipeAdded = { newRecipe ->
                    addingRecipe = false
                    repository.insertRecipe(newRecipe)
                    currentRecipes  += newRecipe
                    selectedRecipe = null
                }
            )
        } else if (selectedRecipe == null) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(currentRecipes) { recipe ->
                            RecipeListItem(recipe = recipe) {
                                selectedRecipe = recipe
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    Button(
                        onClick = { addingRecipe = true },
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                    ) {
                        Text("Add Recipe")
                    }
                }
        } else {
            RecipeDetails(recipe = selectedRecipe!!, onClose = { selectedRecipe = null })
        }
    }
}

@Composable
fun RecipeListItem(recipe: RecipeItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
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
                style = MaterialTheme.typography.h5
            )
            Text(
                text = "Category: ${recipe.category}",
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
fun RecipeDetails(recipe: RecipeItem, onClose: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = recipe.name,
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Instructions",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = recipe.instructions,
            style = MaterialTheme.typography.body1
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onClose) {
            Text("Close")
        }
    }
}