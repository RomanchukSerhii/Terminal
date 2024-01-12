package com.serhiiromanchuk.terminal.di.modules

import androidx.lifecycle.ViewModel
import com.serhiiromanchuk.terminal.di.annotations.ViewModelKey
import com.serhiiromanchuk.terminal.presentation.stocks.StocksViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(StocksViewModel::class)
    fun bindStocksViewModel(viewModel: StocksViewModel): ViewModel
}