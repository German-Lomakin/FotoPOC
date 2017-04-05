package com.spell.fotopoc

import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by Spellbound on 03.04.2017.
 */
fun Disposable.addTo(disp: CompositeDisposable) {
    disp.add(this)
}

fun AppCompatActivity.isFirstRun(): Boolean {
    return this.getSharedPreferences(getString(R.string.app_name), 0)
            .getBoolean("isFirstRun", true)
}

fun AppCompatActivity.saveFirstRun(isFirstRun: Boolean) {
    this.getSharedPreferences(getString(R.string.app_name), 0)
            .edit()
            .putBoolean("isFirstRun", isFirstRun)
            .commit()
}