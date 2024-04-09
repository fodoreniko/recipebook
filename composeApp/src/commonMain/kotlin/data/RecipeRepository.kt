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

    fun delete(recipe: RecipeItem) {
        database.databaseQueries.delete(recipe.id)
    }
    fun getLastRecipe(): RecipeItem {
        return database.databaseQueries.getLastRecipe().executeAsOne()
    }

    fun update(recipe: RecipeItem) {
        database.databaseQueries.update(recipe.name, recipe.instructions, recipe.ingredients, recipe.category, recipe.saved, recipe.preptime, recipe.image, recipe.id)
    }

    fun getSavedRecipes(): List<RecipeItem> {
        return database.databaseQueries.getSavedRecipes().executeAsList()
    }

    fun getRecipe(recipe: RecipeItem): RecipeItem {
        return database.databaseQueries.getRecipe(recipe.id).executeAsOne()
    }
}

