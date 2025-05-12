package com.example.animaltrackingui.db

import android.util.Log
import com.example.animaltrackingui.Utils
import com.example.animaltrackingui.retrofit.RetrofitObject
import com.example.animaltrackingui.services.SettingUpdateService
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface AppRepository {
    suspend fun initDevice()
    suspend fun isAuthenticated(): String?
    suspend fun addDevice(device: Device)
    suspend fun getUser(): AppUser?
    suspend fun getDevice(): Device?
    suspend fun updateDeviceLocale(countryCode: String)
    suspend fun addUser(appUser: AppUser)
    suspend fun removeUser()
    suspend fun updateUser(appUser: AppUser)
    suspend fun getSettings(): List<Setting>?
    suspend fun updateSetting(setting: Setting)
    suspend fun getSetting(name: String): Setting?
    suspend fun updateDeviceInitialized(initialized: Boolean)
    suspend fun setDeviceUploaded(uploaded: Boolean)
    suspend fun updateUserLocale()
}

class DefaultAppRepository(
    private val appDatabase: AppDatabase,
    private val settingUpdateService: SettingUpdateService
) : AppRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    override suspend fun initDevice() {
        val device = appDatabase.deviceDao().getDevice()
        if (device == null) {
            FirebaseMessaging.getInstance().token.addOnSuccessListener {
                scope.launch{
                    val device1 = Utils.createDevice(it, false)
                    appDatabase.deviceDao().insert(device1)
                }
            }.addOnFailureListener {
                it.localizedMessage?.let { it1 -> Log.e("Failed Getting Token", it1) }
            }
        }
    }

    override suspend fun isAuthenticated(): String? {
        val user = appDatabase.userDao().getUser()
        return user?.email
    }

    override suspend fun addDevice(device: Device) {
        appDatabase.deviceDao().getDevice()?.let {
            appDatabase.deviceDao().delete(it)
            appDatabase.deviceDao().insert(device)
        }
    }

    override suspend fun getUser(): AppUser? {
        return appDatabase.userDao().getUser()
    }

    override suspend fun getDevice(): Device? {
        return appDatabase.deviceDao().getDevice()
    }

    override suspend fun updateDeviceLocale(countryCode: String) {
        val device = appDatabase.deviceDao().getDevice()
        if (device != null) {
            device.countryCode = countryCode.uppercase()
            appDatabase.deviceDao().update(device)
            val user = appDatabase.userDao().getUser()
            if(user != null){
                updateUserLocale()
            }
        }
    }

    override suspend fun addUser(appUser: AppUser) {
        appDatabase.userDao().insert(appUser)
    }

    override suspend fun removeUser() {
        val appUser = appDatabase.userDao().getUser()
        if(appUser!=null) {
            appDatabase.userDao().delete(appUser)
        }
    }

    override suspend fun updateUser(appUser: AppUser) {
        appDatabase.userDao().update(appUser)
    }

    override suspend fun getSettings(): List<Setting>? {
        return appDatabase.settingDao().getSettings()
    }

    override suspend fun updateSetting(setting: Setting) {
        settingUpdateService.update(setting)
        appDatabase.settingDao().update(setting)
    }

    override suspend fun getSetting(name: String): Setting? {
        return appDatabase.settingDao().getSetting(name)
    }

    override suspend fun updateDeviceInitialized(initialized: Boolean) {
        val device = appDatabase.deviceDao().getDevice()
        if (device != null) {
            device.initialized = initialized
            appDatabase.deviceDao().update(device)
        }
    }

    override suspend fun setDeviceUploaded(uploaded: Boolean) {
        appDatabase.deviceDao().getDevice()?.let {
            it.uploaded = uploaded
            appDatabase.deviceDao().update(it)
        }
    }

    override suspend fun updateUserLocale(){
        val user = appDatabase.userDao().getUser() ?: return
        val device = appDatabase.deviceDao().getDevice() ?: return
        if(user.roles.contains("GOV")) return
        if(user.countryCode != device.countryCode){
            RetrofitObject.userAPI.updateLocale(device.countryCode, user.token)
        }
    }


}