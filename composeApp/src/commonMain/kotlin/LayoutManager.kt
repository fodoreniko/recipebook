import androidx.compose.runtime.Composable
import data.RecipeItem
import androidx.compose.ui.graphics.ImageBitmap

@Composable
expect fun detailsLayout(recipe: RecipeItem, imageBitmap: ImageBitmap, onClose: () -> Unit)