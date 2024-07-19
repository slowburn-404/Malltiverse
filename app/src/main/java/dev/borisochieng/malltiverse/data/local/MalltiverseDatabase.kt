package dev.borisochieng.malltiverse.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.borisochieng.malltiverse.data.local.MalltiverseDatabase.Companion.LATEST_VERSION
import dev.borisochieng.malltiverse.data.local.dao.OrderHistoryDao
import dev.borisochieng.malltiverse.data.local.dao.WishListDao
import dev.borisochieng.malltiverse.data.local.entities.Order
import dev.borisochieng.malltiverse.data.local.entities.WishListItem

@Database(
    entities = [Order::class, WishListItem::class],
    version = LATEST_VERSION,
    exportSchema = false
)
abstract class MalltiverseDatabase: RoomDatabase() {
    abstract fun orderHistoryDao(): OrderHistoryDao
    abstract fun wishListDao(): WishListDao

    companion object {
        const val LATEST_VERSION = 1
        //create singleton instance
        @Volatile
        private var DB_INSTANCE: MalltiverseDatabase? = null


        fun getDatabase(context: Context): MalltiverseDatabase {
            return DB_INSTANCE ?: synchronized(this) {
               val localDBInstance = Room.databaseBuilder(
                    context = context.applicationContext,
                    klass = MalltiverseDatabase::class.java,
                    name = "malltiverse_db"
                ).build()

                DB_INSTANCE = localDBInstance

                //return local instance
                localDBInstance


            }
        }
    }
}