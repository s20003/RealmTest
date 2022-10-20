package jp.ac.it_college.std.s20003.realmtest

import io.realm.RealmObject

open class Memo (
        open var name: String = ""
        ) : RealmObject()