package com.alejandroarcas.reminder_manager.reminder.di

import android.app.Application
import androidx.room.Room
import com.alejandroarcas.reminder_manager.core.data.database.ReminderDB
import com.alejandroarcas.reminder_manager.core.data.repository.ReminderRepositoryImpl
import com.alejandroarcas.reminder_manager.reminder.domain.repository.ReminderRepository
import com.alejandroarcas.reminder_manager.reminder.use_cases.DeleteReminder
import com.alejandroarcas.reminder_manager.reminder.use_cases.GetAllReminders
import com.alejandroarcas.reminder_manager.reminder.use_cases.GetReminder
import com.alejandroarcas.reminder_manager.reminder.use_cases.UpsertReminder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReminderModule {

    // Reminder Database

    @Provides
    @Singleton
    fun provideReminderDatabase(application: Application): ReminderDB {
        return Room.databaseBuilder(
            application,
            ReminderDB::class.java,
            "reminder_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    // Reminder Repository

    @Provides
    @Singleton
    fun provideReminderRepository(db: ReminderDB): ReminderRepository {
        return ReminderRepositoryImpl(db)
    }

    // Use cases

    @Provides
    @Singleton
    fun provideGetAllRemindersUseCase(
        reminderRepository: ReminderRepository
    ): GetAllReminders {
        return GetAllReminders(reminderRepository)
    }

    @Provides
    @Singleton
    fun provideGetReminderUseCase(
        reminderRepository: ReminderRepository
    ): GetReminder {
        return GetReminder(reminderRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteReminderUseCase(
        reminderRepository: ReminderRepository
    ): DeleteReminder {
        return DeleteReminder(reminderRepository)
    }

    @Provides
    @Singleton
    fun provideUpsertReminderUseCase(
        reminderRepository: ReminderRepository
    ): UpsertReminder {
        return UpsertReminder(reminderRepository)
    }

}