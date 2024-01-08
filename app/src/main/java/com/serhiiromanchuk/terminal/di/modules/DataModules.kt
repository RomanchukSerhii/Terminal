package com.serhiiromanchuk.terminal.di.modules

import com.serhiiromanchuk.terminal.data.network.ApiFactory
import com.serhiiromanchuk.terminal.data.network.ApiService
import com.serhiiromanchuk.terminal.data.repository.TerminalRepositoryImpl
import com.serhiiromanchuk.terminal.di.annotations.ApplicationScope
import com.serhiiromanchuk.terminal.domain.repository.TerminalRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModules {

    @Binds
    @ApplicationScope
    fun bindRepository(impl: TerminalRepositoryImpl): TerminalRepository

    companion object {

        @Provides
        @ApplicationScope
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }
    }
}