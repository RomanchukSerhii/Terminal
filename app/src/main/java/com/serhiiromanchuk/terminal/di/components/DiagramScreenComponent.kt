package com.serhiiromanchuk.terminal.di.components

import com.serhiiromanchuk.terminal.di.modules.DiagramViewModelModule
import com.serhiiromanchuk.terminal.presentation.ViewModelFactory
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(
    modules = [
        DiagramViewModelModule::class
    ]
)
interface DiagramScreenComponent {

    fun getViewModelFactory(): ViewModelFactory

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance ticker: String
        ): DiagramScreenComponent
    }
}