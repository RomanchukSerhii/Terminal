package com.serhiiromanchuk.terminal.di.components

import android.content.Context
import com.serhiiromanchuk.terminal.di.annotations.ApplicationScope
import com.serhiiromanchuk.terminal.di.modules.DataModules
import com.serhiiromanchuk.terminal.di.modules.ViewModelModule
import com.serhiiromanchuk.terminal.presentation.ViewModelFactory
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DataModules::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {

    fun getViewModelFactory(): ViewModelFactory

    fun getDiagramScreenComponent(): DiagramScreenComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }
}