package com.serhiiromanchuk.terminal.di.modules

import androidx.lifecycle.ViewModel
import com.serhiiromanchuk.terminal.di.annotations.ViewModelKey
import com.serhiiromanchuk.terminal.presentation.diagram.DiagramViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface DiagramViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DiagramViewModel::class)
    fun bindDiagramViewModel(viewModel: DiagramViewModel): ViewModel
}