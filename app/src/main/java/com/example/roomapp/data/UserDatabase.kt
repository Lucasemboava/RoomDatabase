package com.example.roomapp.data

import android.content.Context
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import com.example.roomapp.model.User

@Database(entities = [User::class], version = 3,


    autoMigrations = [AutoMigration (from = 1, to = 2)
                      ,AutoMigration (from = 2, to = 3, spec = UserDatabase.RenameFromUfToEstado::class)


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

}