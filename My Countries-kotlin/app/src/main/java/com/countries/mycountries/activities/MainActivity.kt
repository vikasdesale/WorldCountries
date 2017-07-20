package com.countries.mycountries.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.countries.mycountries.R
import com.countries.mycountries.adapters.CountryAdapter
import com.countries.mycountries.model.CountryList
import com.countries.mycountries.model.Worldpopulation
import com.countries.mycountries.network.ApiClient
import com.countries.mycountries.network.ApiInterface
import com.countries.mycountries.network.NetworkUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.parceler.Parcels
import java.util.*

open class MainActivity : AppCompatActivity() {

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar
    @BindView(R.id.recycler_view)
    lateinit var mRecyclerView: RecyclerView
    var mAdapter: CountryAdapter? = null
    @BindView(R.id.country_empty)
    lateinit var countryEmpty: TextView
    private var mWorldPopulations: ArrayList<Worldpopulation>? = null
    private var mCompositeDisposable: CompositeDisposable? = null
    private val Country_Parse = "v"
    private var clickDispose: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        val mLayoutManager = LinearLayoutManager(applicationContext)
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        mCompositeDisposable = CompositeDisposable()
        if (!NetworkUtil.isNetworkConnected(this)) {
            mRecyclerView.visibility = View.GONE
            countryEmpty.setText(R.string.no_network_found)
            countryEmpty.visibility = View.VISIBLE
        } else {
            mRecyclerView.visibility = View.VISIBLE
            countryEmpty.visibility = View.GONE
            if (savedInstanceState == null) {
                loadJSON()
            } else {
               mWorldPopulations = Parcels.unwrap<ArrayList<Worldpopulation>>(savedInstanceState.getParcelable(Country_Parse))
                setupAdapter(mWorldPopulations as ArrayList<Worldpopulation>)
            }
        }
    }

    private fun loadJSON() {
        val apiService = ApiClient.client!!.create<ApiInterface>(ApiInterface::class.java)
        mCompositeDisposable!!.add(apiService.countries
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.handleResponse(it) }, { this.handleError(it) })
        )
    }

    private fun handleResponse(countryLists: CountryList) {
        mWorldPopulations = countryLists.worldpopulation as ArrayList<Worldpopulation>?
        if (mWorldPopulations != null) {
            setupAdapter(mWorldPopulations!!)
        } else {
            mRecyclerView.visibility = View.GONE
            countryEmpty.setText(R.string.data_not_found)
            countryEmpty.visibility = View.VISIBLE
        }
    }

    private fun handleClick(position: Int?) {

        val args = Bundle()
        args.putString("imgURL", mWorldPopulations!![position!!].flag)
        val dialogFragment = CountryDialogFragment()
        dialogFragment.arguments = args
        dialogFragment.show(supportFragmentManager, "TAG")


    }

    private fun handleError(error: Throwable) {
        mRecyclerView.visibility = View.GONE
        countryEmpty.setText(R.string.server_error)
        countryEmpty.visibility = View.VISIBLE
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        if (mWorldPopulations != null) {
            outState.putParcelable(Country_Parse, Parcels.wrap<ArrayList<Worldpopulation>>(mWorldPopulations))
        }
        super.onSaveInstanceState(outState)
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (mCompositeDisposable != null && clickDispose != null) {
            mCompositeDisposable!!.dispose()
            clickDispose!!.dispose()
        }
    }


    fun setupAdapter(world: ArrayList<Worldpopulation>) {
        mAdapter = CountryAdapter(this, world)
        clickDispose = mAdapter!!.itemClickSignal
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ this.handleClick(it) }, { this.handleError(it) })
        mRecyclerView.adapter = mAdapter
    }
}
