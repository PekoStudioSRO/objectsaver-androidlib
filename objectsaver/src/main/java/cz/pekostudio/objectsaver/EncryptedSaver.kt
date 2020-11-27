package cz.pekostudio.objectsaver

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys


/**
 * Created by Lukas Urbanek on 09/07/2020.
 */
//todo upravit to tak aby to bylo 1:1 k ObjectSaveru
class EncryptedSaver(context: Context, fileName: String = "EncryptedData") {

    private val preferences = EncryptedSharedPreferences.create(
        fileName,
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    operator fun set(name: String, data: String) = preferences.edit { putString(name, data) }

    operator fun get(name: String) = preferences.getString(name, null)

    fun remove(name: String) = preferences.edit { remove(name) }

    fun clear() = preferences.edit { clear() }

    fun exists(name: String) = preferences.contains(name)

}