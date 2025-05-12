package com.example.animaltrackingui.viewModels

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.animaltrackingui.AnimalTrackingApplication

object AnimalTrackingViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            val application =
                (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AnimalTrackingApplication)
            HomeScreenViewModel(application.container.appRepository, application.container.mGoogleSignInClient, application.container.toastService)
        }
        initializer {
            val application =
                (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AnimalTrackingApplication)
            SignInViewModel(application.container.appRepository, application.container.mGoogleSignInClient, application.container.toastService)
        }
        initializer {
            val application =
                (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AnimalTrackingApplication)
            RegisterViewModel(application.container.appRepository, application.container.mGoogleSignInClient, application.container.toastService)
        }
        initializer {
            val application =
                (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AnimalTrackingApplication)
            EncounteredPostViewModel(application.container.appRepository)
        }
        initializer {
            val application =
                (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AnimalTrackingApplication)
            MissingPostViewModel(application.container.appRepository)
        }
        initializer {
            val application =
                (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AnimalTrackingApplication)
            SettingsViewModel(application.container.appRepository)
        }
        initializer {
            val application =
                (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AnimalTrackingApplication)
            CreatePostViewModel(application.container.appRepository, application.container.toastService)
        }
        initializer {
            val application =
                (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AnimalTrackingApplication)
            InitViewModel(application.container.appRepository, application.container.toastService)
        }
        initializer {
            val application =
                (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AnimalTrackingApplication)
            GovViewModel(application.container.appRepository)
        }
        initializer {
            val application =
                (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AnimalTrackingApplication)
            CompleteFirstGoogleSignInViewModel(application.container.appRepository)
        }
    }
}