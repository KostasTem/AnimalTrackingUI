package com.example.animaltrackingui.services

import com.example.animaltrackingui.db.AppRepository
import com.example.animaltrackingui.db.Setting
import com.example.animaltrackingui.db.SettingCategory
import com.example.animaltrackingui.notifications.NotificationManager


interface SettingUpdateService {
    suspend fun update(setting: Setting)
    fun init(notificationManager: NotificationManager, appRepository: AppRepository)
}

class DefaultSettingUpdateService: SettingUpdateService {
    private lateinit var notificationManager: NotificationManager
    private lateinit var appRepository: AppRepository
    override fun init(notificationManager: NotificationManager, appRepository: AppRepository) {
        this.notificationManager = notificationManager
        this.appRepository = appRepository
    }


    override suspend fun update(setting: Setting){
        when(setting.category){
            SettingCategory.Notification -> {
                when(setting.name) {
                    "Receive Notifications" -> {
                        when(setting.selected) {
                            "false" -> {
                                notificationManager.unsubscribeForNearbyEncounteredPosts()
                                notificationManager.unsubscribeForNearbyMissingPosts()
                            }
                            "true" -> {
                                if(appRepository.getSetting("Receive Missing Notifications")!!.selected == "Nearby Posts"){
                                    notificationManager.subscribeForNearbyMissingPosts()
                                }
                                if(appRepository.getSetting("Receive Encountered Notifications")!!.selected == "Nearby Posts"){
                                    notificationManager.subscribeForNearbyEncounteredPosts()
                                }
                            }
                        }
                    }
                    "Receive Missing Notifications" -> {
                        when(setting.selected) {
                            "Nearby Posts" -> {
                                notificationManager.subscribeForNearbyMissingPosts()
                            }
                            "None" -> {
                                notificationManager.unsubscribeForNearbyMissingPosts()
                            }
                        }
                    }
                    "Receive Encountered Notifications" -> {
                        when(setting.selected) {
                            "Nearby Posts" -> {
                                notificationManager.subscribeForNearbyEncounteredPosts()
                            }
                            "None" -> {
                                notificationManager.unsubscribeForNearbyEncounteredPosts()
                            }
                        }
                    }
                }
            }
            SettingCategory.Localization -> {
                when(setting.name){
                    "Selected Country" -> {
                        appRepository.updateDeviceLocale(setting.selected)
                    }
                }
            }
            else -> {
                return
            }
        }

    }

}