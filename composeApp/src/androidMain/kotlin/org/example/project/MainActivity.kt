package org.example.project

import App

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

import data.Database
import data.DriverFactory
import data.RecipeRepository
import setA


class MainActivity : ComponentActivity() {
    private lateinit var repository: RecipeRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val driver = DriverFactory(applicationContext).createDriver()
        val database = Database(driver)
        repository = RecipeRepository(database)
        //repository.removeAllRecipes()
        var allRecipes = repository.getAllRecipes()
        //var imageHandler = ImageHandler()
        setA(applicationContext)

        setContent {
            App(allRecipes, repository)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {

}

