package `data`

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.ByteArray
import kotlin.Long
import kotlin.String

public class DatabaseQueries(
  driver: SqlDriver,
) : TransacterImpl(driver) {
  public fun <T : Any> selectAllRecipesInfo(mapper: (
    id: Long,
    name: String,
    instructions: String,
    ingredients: String,
    category: String,
    saved: Long,
    preptime: String,
    image: ByteArray?,
  ) -> T): Query<T> = Query(-1_035_542_329, arrayOf("RecipeItem"), driver, "Database.sq",
      "selectAllRecipesInfo", """
  |SELECT RecipeItem.*
  |FROM RecipeItem
  """.trimMargin()) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getLong(5)!!,
      cursor.getString(6)!!,
      cursor.getBytes(7)
    )
  }

  public fun selectAllRecipesInfo(): Query<RecipeItem> = selectAllRecipesInfo { id, name,
      instructions, ingredients, category, saved, preptime, image ->
    RecipeItem(
      id,
      name,
      instructions,
      ingredients,
      category,
      saved,
      preptime,
      image
    )
  }

  public fun <T : Any> getLastRecipe(mapper: (
    id: Long,
    name: String,
    instructions: String,
    ingredients: String,
    category: String,
    saved: Long,
    preptime: String,
    image: ByteArray?,
  ) -> T): Query<T> = Query(-312_270_911, arrayOf("RecipeItem"), driver, "Database.sq",
      "getLastRecipe", "SELECT RecipeItem.* FROM RecipeItem ORDER BY id DESC LIMIT 1") { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getLong(5)!!,
      cursor.getString(6)!!,
      cursor.getBytes(7)
    )
  }

  public fun getLastRecipe(): Query<RecipeItem> = getLastRecipe { id, name, instructions,
      ingredients, category, saved, preptime, image ->
    RecipeItem(
      id,
      name,
      instructions,
      ingredients,
      category,
      saved,
      preptime,
      image
    )
  }

  public fun <T : Any> getSavedRecipes(mapper: (
    id: Long,
    name: String,
    instructions: String,
    ingredients: String,
    category: String,
    saved: Long,
    preptime: String,
    image: ByteArray?,
  ) -> T): Query<T> = Query(-408_913_701, arrayOf("RecipeItem"), driver, "Database.sq",
      "getSavedRecipes", """
  |SELECT RecipeItem.*
  |FROM RecipeItem
  |WHERE saved = 1
  """.trimMargin()) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getLong(5)!!,
      cursor.getString(6)!!,
      cursor.getBytes(7)
    )
  }

  public fun getSavedRecipes(): Query<RecipeItem> = getSavedRecipes { id, name, instructions,
      ingredients, category, saved, preptime, image ->
    RecipeItem(
      id,
      name,
      instructions,
      ingredients,
      category,
      saved,
      preptime,
      image
    )
  }

  public fun <T : Any> getRecipe(id: Long, mapper: (
    id: Long,
    name: String,
    instructions: String,
    ingredients: String,
    category: String,
    saved: Long,
    preptime: String,
    image: ByteArray?,
  ) -> T): Query<T> = GetRecipeQuery(id) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getString(4)!!,
      cursor.getLong(5)!!,
      cursor.getString(6)!!,
      cursor.getBytes(7)
    )
  }

  public fun getRecipe(id: Long): Query<RecipeItem> = getRecipe(id) { id_, name, instructions,
      ingredients, category, saved, preptime, image ->
    RecipeItem(
      id_,
      name,
      instructions,
      ingredients,
      category,
      saved,
      preptime,
      image
    )
  }

  public fun insertRecipe(
    name: String,
    instructions: String,
    ingredients: String,
    category: String,
    saved: Long,
    preptime: String,
    image: ByteArray?,
  ) {
    driver.execute(1_342_233_792, """
        |INSERT INTO RecipeItem (name, instructions, ingredients, category, saved, preptime, image)
        |VALUES (?, ?, ?, ?,  ?,?,?)
        """.trimMargin(), 7) {
          bindString(0, name)
          bindString(1, instructions)
          bindString(2, ingredients)
          bindString(3, category)
          bindLong(4, saved)
          bindString(5, preptime)
          bindBytes(6, image)
        }
    notifyQueries(1_342_233_792) { emit ->
      emit("RecipeItem")
    }
  }

  public fun removeAllRecipes() {
    driver.execute(1_342_432_769, """DELETE FROM RecipeItem""", 0)
    notifyQueries(1_342_432_769) { emit ->
      emit("RecipeItem")
    }
  }

  public fun delete(id: Long) {
    driver.execute(538_547_076, """DELETE FROM RecipeItem WHERE id = ?""", 1) {
          bindLong(0, id)
        }
    notifyQueries(538_547_076) { emit ->
      emit("RecipeItem")
    }
  }

  public fun update(
    name: String,
    instructions: String,
    ingredients: String,
    category: String,
    saved: Long,
    preptime: String,
    image: ByteArray?,
    id: Long,
  ) {
    driver.execute(1_035_159_202, """
        |UPDATE RecipeItem
        |SET name = ?, instructions = ?, ingredients = ?, category = ?, saved = ?, preptime = ?, image = ?
        |WHERE id = ?
        """.trimMargin(), 8) {
          bindString(0, name)
          bindString(1, instructions)
          bindString(2, ingredients)
          bindString(3, category)
          bindLong(4, saved)
          bindString(5, preptime)
          bindBytes(6, image)
          bindLong(7, id)
        }
    notifyQueries(1_035_159_202) { emit ->
      emit("RecipeItem")
    }
  }

  private inner class GetRecipeQuery<out T : Any>(
    public val id: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("RecipeItem", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("RecipeItem", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_021_972_469, """
    |SELECT RecipeItem.*
    |FROM RecipeItem
    |WHERE id = ?
    """.trimMargin(), mapper, 1) {
      bindLong(0, id)
    }

    override fun toString(): String = "Database.sq:getRecipe"
  }
}
