package cz.pekostudio.objectsaver

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Jednoduše ukládá datové třídy do sharedPrefrences
 *
 * @receiver String, musí být json
 * @param context Context
 * @property gson Gson instance (nepovinný parametr)
 */
class ObjectSaver(
    context: Context,
    val gson: Gson = Gson()
) {

    val preferences: SharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE)

    /**
     * Uloží data/objekt do sharedPrefrences
     *
     * @param O Typ třídy
     * @param obj Jakákoliv třída
     * @param name Id pod kterým to bude uložené v sharedPrefrences (nepovinný parametr)
     */
    inline fun <reified O> put(obj: O, name: String = O::class.java.name) {
        obj?.let {
            preferences.edit { putString(name, gson.toJson(it)) }
        }
    }
    private fun <O> put(obj: O, klazz: Class<O>) {
        obj?.let {
            preferences.edit { putString(klazz.name, gson.toJson(it)) }
        }
    }

    /**
     * Získá data/třídu ze shared preferencí, pokud není třída uložená vrátí null
     *
     * @param O Typ třídy
     * @param name Id pod kterým to je uložené v sharedPrefrences (nepovinný parametr)
     * @return Vrací třídu s daty nebo null pokud nic uložené není
     */
    inline fun <reified O> get(name: String = O::class.java.name): O? {
        (preferences.getString(name, null) ?: return null).let {
            return gson.fromJson(it, O::class.java)
        }
    }

    /**
     * Získá data/třídu ze shared preferencí, pokud není třída uložená vytvoří nový
     *
     * @param O Typ třídy
     * @param name Id pod kterým to je uložené v sharedPrefrences (nepovinný parametr)
     * @return Vrací třídu s daty
     */
    inline fun <reified O> getOrCreate(name: String = O::class.java.name): O {
        (preferences.getString(name, null) ?: "{}").let {
            return gson.fromJson(it, O::class.java)
        }
    }

    private fun <O> getOrCreate(klazz: Class<O>): O {
        (preferences.getString(klazz.name, null) ?: "{}").let {
            return gson.fromJson(it, klazz)
        }
    }

    /**
     * Vrací boolean jestli je třída uložená
     *
     * @param O Typ třídy
     * @param name Id pod kterým to je uložené v sharedPrefrences (nepovinný parametr)
     * @return Pokud existuje vrátí true
     */
    inline fun <reified O> exists(name: String = O::class.java.name) = preferences.contains(name)

    /**
     * Maže třídu ze sharedPrefrences
     *
     * @param O Typ třídy
     * @param name Id pod kterým to je uložené v sharedPrefrences (nepovinný parametr, defaultně jméno třídy)
     */
    inline fun <reified O> remove(name: String = O::class.java.name) = preferences.edit { remove(name) }

    /**
     * Vymaže všechny záznamy které ObjectSaver uložil
     */
    fun clear() = preferences.edit { clear() }

    class JsonSave<O>(context: Context, private val klazz: Class<O>) : ReadWriteProperty<Any?, O> {

        private val objectSaver = ObjectSaver(context)

        private val data by lazy { objectSaver.getOrCreate(klazz) }

        override fun getValue(thisRef: Any?, property: KProperty<*>): O = data

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: O) {
            objectSaver.put(data, klazz)
        }

    }

}

inline fun <reified O> O?.save(context: Context) {
    this?.let {
        context.getSharedPreferences("data", Context.MODE_PRIVATE).edit().putString(O::class.java.name, Gson().toJson(it)).apply()
    }
}

inline fun <reified O> jsonSave(context: Context): ObjectSaver.JsonSave<O> {
    return ObjectSaver.JsonSave(context, O::class.java)
}

@JvmName("jsonSaveExtension")
inline fun <reified O> Context.jsonSave(): ObjectSaver.JsonSave<O> {
    return ObjectSaver.JsonSave(this, O::class.java)
}