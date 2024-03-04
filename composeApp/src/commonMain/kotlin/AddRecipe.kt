import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.RecipeItem

@Composable
fun AddRecipe(onRecipeAdded: (RecipeItem) -> Unit) {
    var name by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var preptime by remember { mutableStateOf("") }

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

            }
        ) {
            Text("Upload picture")
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
                    image = null
                )
                onRecipeAdded(newRecipe)
            }
        ) {
            Text("Add recipe")
        }
    }
}