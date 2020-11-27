package cz.pekostudio

import android.app.Activity
import cz.pekostudio.objectsaver.ObjectSaver
import cz.pekostudio.objectsaver.jsonSave
import cz.pekostudio.objectsaver.save

/**
 * Created by Lukas Urbanek on 03/06/2020.
 */
fun Activity.test() {

    var accountAuto: Pair<String, Int> by jsonSave()

    val objectSaver = ObjectSaver(this)

    var account = Pair("test", "2")

    objectSaver.put(account)

    val account2 = objectSaver.get<Pair<String, String>>("")

    account = objectSaver.get() ?: Pair("test", "null")

    println(account)
    println(account2)

}
