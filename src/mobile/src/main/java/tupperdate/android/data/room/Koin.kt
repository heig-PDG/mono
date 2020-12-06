package tupperdate.android.data.room

import androidx.room.Room
import org.koin.dsl.module

val KoinRoomModule = module {
    single {
        Room.databaseBuilder(get(), TupperdateDatabase::class.java, "db.sqlite")
            .fallbackToDestructiveMigration()
            .build()
    }
}
