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


class MainActivity : ComponentActivity() {
    private lateinit var repository: RecipeRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val driver = DriverFactory(applicationContext).createDriver()
        val database = Database(driver)
        repository = RecipeRepository(database)

        var allRecipes = repository.getAllRecipes()

        setContent {
            App(allRecipes, repository)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    //App(allRecipes)
}

