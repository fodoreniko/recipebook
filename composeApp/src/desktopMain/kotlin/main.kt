import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import data.Database
import data.DriverFactory
import data.RecipeRepository

fun main() = application {

    Window(onCloseRequest = ::exitApplication, title = "RecipeBook") {
        val driver = DriverFactory().createDriver()
        val database = Database(driver)
        val repository = RecipeRepository(database)
        //repository.removeAllRecipes()
        var allRecipes = repository.getAllRecipes()
        //var imageHandler = ImageHandler()
        App(allRecipes, repository)

    }
}

@Preview
@Composable
fun AppDesktopPreview() {

}
