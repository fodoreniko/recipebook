package `data`

import kotlin.ByteArray
import kotlin.Long
import kotlin.String

public data class RecipeItem(
  public val id: Long,
  public val name: String,
  public val instructions: String,
  public val ingredients: String,
  public val category: String,
  public val saved: Long,
  public val preptime: String,
  public val image: ByteArray?,
)
