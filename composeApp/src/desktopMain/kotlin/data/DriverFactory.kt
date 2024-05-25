package data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:test.db")
        if (!checkIfDatabaseExists()) {
            Database.Schema.create(driver)
        }
        return driver
    }

    private fun checkIfDatabaseExists(): Boolean {
        val dbFile = File("test.db")
        return dbFile.exists()
    }
}