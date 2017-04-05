package com.spell.fotopoc.activities

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ProgressBar
import com.spell.fotopoc.*
import com.spell.fotopoc.adapters.HomePagerAdapter
import com.spell.fotopoc.model.POIResponse
import com.spell.fotopoc.tasks.ParseJsonTask
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeActivity : AppCompatActivity() {

    private val toolbar by bindView<Toolbar>(R.id.toolbar)
    private val progress by bindView<ProgressBar>(R.id.progress)
    private val tablayout by bindView<TabLayout>(R.id.tab_layout)
    private val viewpager by bindView<ViewPager>(R.id.pager)

    private val disp = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.mipmap.ic_launcher)

        if (isFirstRun()) {
            saveFirstRun(false)
            Observable.fromCallable(ParseJsonTask(this))
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe { progress.visibility = View.VISIBLE }
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete { progress.visibility = View.GONE }
                    .subscribe{ onImportNext(it) }.addTo(disp)
        } else {
            initViewPager()
        }
    }

    override fun onStop() {
        super.onStop()
        disp.dispose()
    }

    private fun onImportNext(resp: POIResponse) {
        for (poi in resp.locations) {
            poi.save()
        }
        initViewPager()
    }

    private fun initViewPager() {
        viewpager.adapter = HomePagerAdapter(supportFragmentManager)
        tablayout.setupWithViewPager(viewpager)
    }

}