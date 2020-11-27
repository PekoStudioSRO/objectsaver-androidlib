package cz.pekostudio.objectsaver

import com.google.gson.Gson

/**
 * Created by Lukas Urbanek on 25/05/2020.
 */

/**
 * Vytváří Json z jakéhokoliv objektu
 *
 * Výchozí Gson()
 *
 * @receiver Objekt
 * @param T typ objektu
 * @return vrací json ve stringu
 */
fun <T> T.toJson(): String {
    return Gson().toJson(this)
}

/**
 * Rozparsuje Json do objektu
 *
 * Výchozí Gson()
 *
 * @receiver String, musí být json
 * @param T typ objektu
 * @return vrací objekt
 */
inline fun <reified T> String.fromJson(): T {
    return Gson().fromJson(this, T::class.java)
}