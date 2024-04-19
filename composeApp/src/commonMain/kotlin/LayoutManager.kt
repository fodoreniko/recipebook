import androidx.compose.runtime.Composable
import data.RecipeItem
import androidx.compose.ui.graphics.ImageBitmap
import data.RecipeRepository

@Composable
expect fun detailsLayout(repository: RecipeRepository, recipe: RecipeItem, onClose: (RecipeItem?) -> Unit, onDelete: () -> Unit)