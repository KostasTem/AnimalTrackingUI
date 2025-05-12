package com.example.animaltrackingui

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.ContextCompat.getSystemService
import com.example.animaltrackingui.db.AppDatabase
import com.example.animaltrackingui.db.AppRepository
import com.example.animaltrackingui.db.DefaultAppRepository
import com.example.animaltrackingui.db.Setting
import com.example.animaltrackingui.db.SettingCategory
import com.example.animaltrackingui.db.getListOfCountries
import com.example.animaltrackingui.notifications.NotificationManager
import com.example.animaltrackingui.retrofit.RetrofitObject
import com.example.animaltrackingui.services.DefaultSettingUpdateService
import com.example.animaltrackingui.services.SettingUpdateService
import com.example.animaltrackingui.services.ToastService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


interface AppContainer {
    val appScope: CoroutineScope
    val notificationManager: NotificationManager
    val appRepository: AppRepository
    val settingUpdateService: SettingUpdateService
    val toastService: ToastService
    val mGoogleSignInClient: GoogleSignInClient
    fun init()
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    override val appScope: CoroutineScope by lazy { CoroutineScope(SupervisorJob()) }

    override fun init() {
        runBlocking {
            withContext(Dispatchers.IO) {
                RetrofitObject.init(context)
                appRepository.initDevice()
                notificationManager.createChannels()
                settingUpdateService.init(notificationManager, appRepository)
                Utils.initiateGeocoder(context)
                LocationProvider.init(context)
                appDatabase.settingDao().insert(
                    Setting(
                        "Receive Notifications",
                        SettingCategory.Notification,
                        "true",
                        "switch",
                        listOf("true", "false")
                    )
                )
                appDatabase.settingDao().insert(
                    Setting(
                        "Receive Missing Notifications",
                        SettingCategory.Notification,
                        "None",
                        "radio",
                        listOf("Nearby Posts", "None")
                    )
                )
                appDatabase.settingDao().insert(
                    Setting(
                        "Receive Encountered Notifications",
                        SettingCategory.Notification,
                        "None",
                        "radio",
                        listOf("Nearby Posts", "None")
                    )
                )
                appDatabase.settingDao().insert(
                    Setting(
                        "Selected Country",
                        SettingCategory.Localization,
                        getListOfCountries().first().countryCode,
                        "dropdown",
                        listOf()
                    )
                )
                val connectivityManager = getSystemService(
                    context,
                    ConnectivityManager::class.java
                ) as ConnectivityManager
                connectivityManager.requestNetwork(networkRequest, networkCallback)
                appRepository.updateUserLocale()
            }
        }
    }

    override val mGoogleSignInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(
            context, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("Google Client Id")
                .requestId()
                .requestProfile()
                .build()
        )
    }

    private val appDatabase: AppDatabase by lazy {
        AppDatabase.getDatabase(context)
    }
    override val notificationManager: NotificationManager by lazy {
        NotificationManager(context)
    }
    override val settingUpdateService: SettingUpdateService by lazy {
        DefaultSettingUpdateService()
    }
    override val toastService: ToastService by lazy {
        ToastService(context)
    }
    override val appRepository: AppRepository by lazy {
        DefaultAppRepository(appDatabase, settingUpdateService)
    }

    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        // network is available for use
        override fun onAvailable(network: Network) {
            RetrofitObject.blockRequest.update {
                false
            }
            super.onAvailable(network)
        }

        // Network capabilities have changed for the network
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            val unmetered =
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
        }

        // lost network connection
        override fun onLost(network: Network) {
            RetrofitObject.blockRequest.update {
                true
            }
            super.onLost(network)
        }
    }

}