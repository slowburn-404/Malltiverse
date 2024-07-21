package dev.borisochieng.malltiverse.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.borisochieng.malltiverse.data.local.database.MalltiverseDatabase.Companion.LATEST_VERSION
import dev.borisochieng.malltiverse.data.local.dao.OrderHistoryDao
import dev.borisochieng.malltiverse.data.local.dao.WishListDao
import dev.borisochieng.malltiverse.data.local.entities.Order
import dev.borisochieng.malltiverse.data.local.entities.WishListItem
import androidx.room.AutoMigration
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Order::class, WishListItem::class],
    version = LATEST_VERSION,
    exportSchema = true
//    autoMigrations = [
//        AutoMigration(from = 1, to = 2)
//    ]
)
@TypeConverters(DateConverter::class)
abstract class MalltiverseDatabase :
    RoomDatabase() {
    abstract fun orderHistoryDao(): OrderHistoryDao
    abstract fun wishListDao(): WishListDao

    companion object {
        const val LATEST_VERSION = 3

        //create singleton instance
        @Volatile
        private var DB_INSTANCE: MalltiverseDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DROP TABLE order_history")
                db.execSQL("DROP TABLE wishlist")
                db.execSQL("CREATE TABLE wishlist (id TEXT PRIMARY KEY NOT NULL, name TEXT NOT NULL, description TEXT NOT NULL, image_url TEXT NOT NULL)")
                db.execSQL("CREATE TABLE order_history (name TEXT NOT NULL, description TEXT NOT NULL, id TEXT PRIMARY KEY NOT NULL,  imageUrl TEXT NOT NULL)")
            }

        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Create new table with the correct schema
        db.execSQL("""
            CREATE TABLE order_history_new (
                id TEXT NOT NULL PRIMARY KEY,
                name TEXT NOT NULL,
                description TEXT NOT NULL,
                imageUrl TEXT NOT NULL,
                price TEXT NOT NULL DEFAULT '0.0',
                timestamp INTEGER NOT NULL DEFAULT 0,
                status INTEGER NOT NULL DEFAULT 0
            )
        """.trimIndent())

        // Copy data from the old table to the new table
        db.execSQL("""
            INSERT INTO order_history_new (id, name, description, imageUrl)
            SELECT id, name, description, imageUrl
            FROM order_history
        """.trimIndent())

        // Drop the old table
        db.execSQL("DROP TABLE order_history")

        // Rename the new table to the old table's name
        db.execSQL("ALTER TABLE order_history_new RENAME TO order_history")
    }
}



        fun getDatabase(context: Context): MalltiverseDatabase =
            DB_INSTANCE ?: synchronized(this) {
                DB_INSTANCE ?: buildDataBase(context).also { DB_INSTANCE = it }
            }

        private fun buildDataBase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MalltiverseDatabase::class.java,
                "malltiverse.db"
            )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .build()
//                .addCallback(object : Callback() {
//                    override fun onCreate(db: SupportSQLiteDatabase) {
//                        super.onCreate(db)
//                        CoroutineScope(Dispatchers.IO).launch {
//                            prepopulateDBOrder.forEach {
//                                getDatabase(context).orderHistoryDao().insertOrder(it)
//                            }
//                        }
//
//                        CoroutineScope(Dispatchers.IO).launch {
//                            prepolutaeDBWishList.forEach {
//                                getDatabase(context).wishListDao().addToWishList(it)
//                            }
//                        }
//
//                    }
//
//                })

//        private val prepopulateDBOrder: List<Order> = listOf(
//            Order(
//                id = "cwncwocw",
//                name = "test",
//                description = "test",
//                imageUrl = "test"
//            ),
//            Order(
//                id = "cwncwocw",
//                name = "test",
//                description = "test",
//                imageUrl = "test"
//            ),
//            Order(
//                id = "cwncwocw",
//                name = "test",
//                description = "test",
//                imageUrl = "test"
//            ),
//            Order(
//                id = "cwncwocw",
//                name = "test",
//                description = "test",
//                imageUrl = "test"
//            )
//
//        )
//
//
//        private val prepolutaeDBWishList: List<WishListItem> = listOf(
//            WishListItem(
//                id = "cwncwocw",
//                name = "test",
//                price = 1000.0,
//                description = "test",
//                imageURL = "test",
//                isAddedToCart = true,
//                isAddedToWishList = true,
//                quantity = 2,
//                availableQuantity = 3
//            ),
//            WishListItem(
//                id = "cjbjneobmeo",
//                name = "test",
//                price = 1000.0,
//                description = "test",
//                imageURL = "test",
//                isAddedToCart = true,
//                isAddedToWishList = true,
//                quantity = 2,
//                availableQuantity = 3
//            ),
//            WishListItem(
//                id = "cwncwocjnvn2w",
//                name = "test",
//                price = 1000.0,
//                description = "test",
//                imageURL = "test",
//                isAddedToCart = true,
//                isAddedToWishList = true,
//                quantity = 2,
//                availableQuantity = 3
//            ),
//            WishListItem(
//                id = "cwncwocvkrk4w",
//                name = "test",
//                price = 1000.0,
//                description = "test",
//                imageURL = "test",
//                isAddedToCart = true,
//                isAddedToWishList = true,
//                quantity = 2,
//                availableQuantity = 3
//            ),
//            WishListItem(
//                id = "cwncwoknvk5cw",
//                name = "test",
//                price = 1000.0,
//                description = "test",
//                imageURL = "test",
//                isAddedToCart = true,
//                isAddedToWishList = true,
//                quantity = 2,
//                availableQuantity = 3
//            )
        //       )
    }
}