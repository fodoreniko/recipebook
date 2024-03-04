import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import data.Database
import data.DriverFactory
import data.RecipeItem
import data.RecipeRepository


fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "RecipeBook") {
        val driver = DriverFactory().createDriver()
        val database = Database(driver)
        val repository = RecipeRepository(database)

        var allRecipes = repository.getAllRecipes()
        App(allRecipes, repository)

    }
}

@Preview
@Composable
fun AppDesktopPreview() {
    //App()
}
