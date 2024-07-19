package dev.borisochieng.malltiverse.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dev.borisochieng.malltiverse.data.local.database.MalltiverseDatabase.Companion.LATEST_VERSION
import dev.borisochieng.malltiverse.data.local.dao.OrderHistoryDao
import dev.borisochieng.malltiverse.data.local.dao.WishListDao
import dev.borisochieng.malltiverse.data.local.entities.Order
import dev.borisochieng.malltiverse.data.local.entities.WishListItem
import dev.borisochieng.malltiverse.util.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Database(
    entities = [Order::class, WishListItem::class],
    version = LATEST_VERSION,
    exportSchema = false
)
abstract class MalltiverseDatabase(private val dispatcher: CoroutineDispatcherProvider) :
    RoomDatabase() {
    abstract fun orderHistoryDao(): OrderHistoryDao
    abstract fun wishListDao(): WishListDao

    companion object {
        const val LATEST_VERSION = 1

        //create singleton instance
        @Volatile
        private var DB_INSTANCE: MalltiverseDatabase? = null


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
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            prepopulateDBOrder.forEach {
                                getDatabase(context).orderHistoryDao().insertOrder(it)
                            }
                        }

                        CoroutineScope(Dispatchers.IO).launch {
                            prepolutaeDBWishList.forEach {
                                getDatabase(context).wishListDao().addToWishList(it)
                            }
                        }

                    }

                }).build()

        private val prepopulateDBOrder: List<Order> = listOf(
            Order(
                id = "cwncwocw",
                name = "test",
                price = 1000.0,
                description = "test",
                imageUrl = "test"
            ),
            Order(
                id = "cwncwocw",
                name = "test",
                price = 1000.0,
                description = "test",
                imageUrl = "test"
            ),
            Order(
                id = "cwncwocw",
                name = "test",
                price = 1000.0,
                description = "test",
                imageUrl = "test"
            ),
            Order(
                id = "cwncwocw",
                name = "test",
                price = 1000.0,
                description = "test",
                imageUrl = "test"
            )

        )


        private val prepolutaeDBWishList: List<WishListItem> = listOf(
            WishListItem(
                id = "cwncwocw",
                name = "test",
                price = 1000.0,
                description = "test",
                imageURL = "test",
                isAddedToCart = true,
                isAddedToWishList = true,
                quantity = 2,
                availableQuantity = 3
            ),
            WishListItem(
                id = "cwncwocw",
                name = "test",
                price = 1000.0,
                description = "test",
                imageURL = "test",
                isAddedToCart = true,
                isAddedToWishList = true,
                quantity = 2,
                availableQuantity = 3
            ),
            WishListItem(
                id = "cwncwocw",
                name = "test",
                price = 1000.0,
                description = "test",
                imageURL = "test",
                isAddedToCart = true,
                isAddedToWishList = true,
                quantity = 2,
                availableQuantity = 3
            ),
            WishListItem(
                id = "cwncwocw",
                name = "test",
                price = 1000.0,
                description = "test",
                imageURL = "test",
                isAddedToCart = true,
                isAddedToWishList = true,
                quantity = 2,
                availableQuantity = 3
            ),
            WishListItem(
                id = "cwncwocw",
                name = "test",
                price = 1000.0,
                description = "test",
                imageURL = "test",
                isAddedToCart = true,
                isAddedToWishList = true,
                quantity = 2,
                availableQuantity = 3
            )
        )
    }
}