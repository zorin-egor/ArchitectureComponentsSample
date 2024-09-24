package com.sample.architecturecomponents.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.sample.architecturecomponents.core.datastore.DataStoreProto
import com.sample.architecturecomponents.core.datastore.DataStoreProtoSerializer
import com.sample.architecturecomponents.core.datastore.SettingsDataStore
import com.sample.architecturecomponents.core.datastore.SettingsDataStoreProto
import com.sample.architecturecomponents.core.di.DefaultScope
import com.sample.architecturecomponents.core.di.Dispatcher
import com.sample.architecturecomponents.core.di.Dispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    internal fun providesUserPreferencesDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(Dispatchers.IO) ioDispatcher: CoroutineDispatcher,
        @DefaultScope scope: CoroutineScope,
        userPreferencesSerializer: DataStoreProtoSerializer,
    ): DataStore<SettingsDataStore> =
        DataStoreFactory.create(
            serializer = userPreferencesSerializer,
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
            migrations = emptyList(),
        ) {
            context.dataStoreFile("settings_data_store.pb")
        }

    @Provides
    @Singleton
    internal fun providesSettingsDataStore(
        dataStore: DataStore<SettingsDataStore>
    ): SettingsDataStoreProto = DataStoreProto(dataStore)

}
