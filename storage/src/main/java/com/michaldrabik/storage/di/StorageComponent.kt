package com.michaldrabik.storage.di

import com.michaldrabik.storage.database.AppDatabase
import com.michaldrabik.storage.repository.UserRepository
import dagger.Component

@Component(modules = [StorageModule::class])
interface StorageComponent {

  fun userRepository(): UserRepository

  fun database(): AppDatabase
}