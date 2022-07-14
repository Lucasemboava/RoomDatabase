package com.example.roomapp.data

import android.content.Context
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.roomapp.model.User

@Database(entities = [User::class], version = 4,


    autoMigrations = [AutoMigration (from = 1, to = 2)
                      ,AutoMigration (from = 2, to = 3, spec = UserDatabase.RenameFromUfToEstado::class)
                      ,AutoMigration (from = 3, to = 4, spec = UserDatabase.RenameTableFromUserTableToUser::class)

                     ],

    exportSchema = true)

abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

    @RenameColumn(
        tableName = "user_table",
        fromColumnName = "uf",
        toColumnName  = "estado"
    )
    class RenameFromUfToEstado: AutoMigrationSpec{}

//    if it was a manual migration
//    val MIGRATION_2_3: Migration = Migration(2,3){
//        fun migrate(db: SupportSQLiteDatabase){
//            db.execSQL("CREATE TABLE IF NOT EXISTS _new_user_table ('id' INTEGER NOT NULL, firstName TEXT, lastName TEXT, PRIMARY KEY('id'))")
//            db.execSQL("INSERT INTO '_new_user_table' (id, estado) SELECT id, uf FROM 'user_table'")
//            db.execSQL("DROP TABLE 'user_table'")
//            db.execSQL("ALTER TABLE '_new_user_table' RENAME TO 'user_table'")
//        }
//    }

    @RenameTable(
        fromTableName = "user_table",
        toTableName = "user",
    )
    class RenameTableFromUserTableToUser: AutoMigrationSpec{}


}