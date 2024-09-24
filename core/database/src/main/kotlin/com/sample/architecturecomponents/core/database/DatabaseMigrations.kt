package com.sample.architecturecomponents.core.database

import androidx.room.RenameTable
import androidx.room.migration.AutoMigrationSpec

internal object DatabaseMigrations {
    @RenameTable.Entries(
        RenameTable(
            fromTableName = "Repositories_details",
            toTableName = "RepositoriesDetails"
        )
    )
    class Schema2to3 : AutoMigrationSpec

    @RenameTable.Entries(
        RenameTable(
            fromTableName = "RepositoriesDetails",
            toTableName = "RepositoryDetails"
        )
    )
    class Schema3to4 : AutoMigrationSpec
}