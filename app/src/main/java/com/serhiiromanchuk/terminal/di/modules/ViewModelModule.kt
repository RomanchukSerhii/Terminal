package com.serhiiromanchuk.terminal.di.modules

import androidx.lifecycle.ViewModel
import com.serhiiromanchuk.terminal.di.annotations.ViewModelKey
import com.serhiiromanchuk.terminal.presentation.diagram.TerminalViewModel
import com.serhiiromanchuk.terminal.presentation.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(TerminalViewModel::class)
    fun bindTerminalViewModel(viewModel: TerminalViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel
}