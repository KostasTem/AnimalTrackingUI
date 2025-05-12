package com.example.animaltrackingui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.text.format.DateUtils
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.animaltrackingui.db.Device
import com.example.animaltrackingui.db.getListOfCountries
import com.example.animaltrackingui.retrofit.RetrofitObject
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.util.UUID


object Utils {

    private lateinit var geocoder: Geocoder

    fun initiateGeocoder(context: Context) {
        geocoder = Geocoder(context)
    }

    fun calculateTimeAgo(timestamp: Long): CharSequence {
        return DateUtils.getRelativeTimeSpanString(
            timestamp,
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS
        )
    }

    fun createDevice(deviceID: String, initialized: Boolean): Device {
        return Device(
            0,
            deviceID,
            Build.MANUFACTURER + " " + Build.PRODUCT,
            Build.VERSION.RELEASE,
            listOf(""),
            getListOfCountries().first().countryCode,
            false,
            initialized
        )
    }

    fun getLocation(lat: Double, long: Double): List<String>? {
        val address = geocoder.getFromLocation(lat, long, 1)
        val addressList: MutableList<String> = mutableListOf()
        if (address != null) {
            if (address[0].locality == null) {
                return null
            }
            addressList.add(address[0].countryCode)
            addressList.add(address[0].countryName)
            addressList.add(address[0].locality)
            addressList.add(address[0].postalCode)
        }
        return addressList
    }

    suspend fun uploadImageToFirebase(
        uri: Uri,
        folder: String,
        uid: String,
        context: Context
    ): Pair<String, StorageReference?> {
        try {
            val uuid = UUID.randomUUID().toString()
            val ref = Firebase.storage.reference.child("$folder/$uid/$uuid.jpg")
            val byteArray: ByteArray? =
                context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            byteArray?.let {
                val uploadTask = ref.putBytes(byteArray)
                return Pair(uploadTask.await().storage.downloadUrl.await().toString(), ref)
            }
        } catch (exception: FirebaseException) {
            Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_SHORT).show()
        }
        return Pair("", null)
    }

    fun createImageFile(context: Context): Uri {
        val file =
            File(context.externalCacheDir?.absolutePath + File.separator + "TemporaryFile_" + System.currentTimeMillis() + ".png")
        return FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            file
        )
    }

    fun hasNotificationPermission(context: Context): Boolean {
        return (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) ||
                context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
    }

    suspend fun uploadImageForIdentification(
        context: Context,
        uri: Uri,
        callback: (String?) -> Unit
    ) {
        val byteArray: ByteArray? =
            context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
        context.contentResolver.query(uri, null, null, null, null)?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            val fileName = it.getString(nameIndex)
            val imageFile = File.createTempFile(
                UUID.randomUUID().toString(),
                "." + fileName.split(".").last(),
                null
            )
            val fos = FileOutputStream(imageFile)
            fos.write(byteArray)
            if (imageFile.exists()) {
                val requestFile = RequestBody.create(MultipartBody.FORM, imageFile)
                val body =
                    MultipartBody.Part.createFormData("image_file", imageFile.name, requestFile)
                val identificationResponse =
                    RetrofitObject.animalRecognitionAPI.identifyAnimal(body, "")
                if (identificationResponse.isSuccessful && identificationResponse.body() != null) {
                    callback(identificationResponse.body()!!)
                } else {
                    callback(null)
                }
                imageFile.delete()
            }
        }
    }
}