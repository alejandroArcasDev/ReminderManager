package com.alejandroarcas.reminder_manager.reminder.di

import android.app.Application
import androidx.room.Room
import com.alejandroarcas.reminder_manager.core.data.database.ReminderDB
import com.alejandroarcas.reminder_manager.core.data.repository.ReminderRepositoryImpl
import com.alejandroarcas.reminder_manager.reminder.domain.repository.ReminderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReminderModule {

    @Provides
    @Singleton
    fun provideReminderDatabase(application: Application): ReminderDB {
        return Room.databaseBuilder(
            application,
            ReminderDB::class.java,
            "reminder_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideReminderRepository(db: ReminderDB): ReminderRepository {
        return ReminderRepositoryImpl(db)
    }
}