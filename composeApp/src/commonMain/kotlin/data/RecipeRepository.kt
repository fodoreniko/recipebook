package data


class RecipeRepository(private val database: Database) {
    fun insertRecipe(recipe: RecipeItem) {
        database.databaseQueries.insertRecipe(
            name = recipe.name,
            instructions = recipe.instructions,
            ingredients = recipe.ingredients,
            category = recipe.category,
            saved = recipe.saved,
            preptime = recipe.preptime,
            image = recipe.image
        )
    }

    fun getAllRecipes(): List<RecipeItem> {
        return database.databaseQueries.selectAllRecipesInfo().executeAsList()
    }

    fun removeAllRecipes() {
        database.databaseQueries.removeAllRecipes()
    }
}

