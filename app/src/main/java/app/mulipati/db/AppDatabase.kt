package app.mulipati.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.mulipati.db.daos.MessagesDao
import app.mulipati.db.daos.NotificationsDao
import app.mulipati.db.daos.TripsDao
import app.mulipati.db.daos.UsersDao
import app.mulipati.db.entities.Messages
import app.mulipati.helpers.Converters
import app.mulipati.network.responses.Trip
import app.mulipati.network.responses.notifications.Notification
import app.mulipati.network.responses.trips.UserBooking
import app.mulipati.network.responses.trips.UserTrip
import app.mulipati.network.responses.users.AppUser
import app.mulipati.util.Constants

@Database(entities = [Trip::class, UserTrip::class, Messages::class, Notification::class, UserBooking::class, AppUser::class], version = 2, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tripsDao(): TripsDao
    abstract fun messagesDao() : MessagesDao
    abstract fun notificationsDao() : NotificationsDao
    abstract fun usersDao() : UsersDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, Constants.DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }

}