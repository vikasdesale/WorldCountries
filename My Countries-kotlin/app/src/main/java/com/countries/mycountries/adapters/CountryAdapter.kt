package com.countries.mycountries.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.countries.mycountries.R
import com.countries.mycountries.model.Worldpopulation
import com.jakewharton.rxbinding2.view.RxView

import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

import com.countries.mycountries.R.drawable.error
import com.countries.mycountries.R.drawable.loading


class CountryAdapter(private val mContext: Context, private val mWorldPopulations: ArrayList<Worldpopulation>?) : RecyclerView.Adapter<CountryAdapter.ViewHolder>() {

    private val onClickSubject = PublishSubject.create<Int>()

    val itemClickSignal: Observable<Int>
        get() = onClickSubject
    private var view: View? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_card, parent, false)
        return ViewHolder(view!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val worldpopulation = mWorldPopulations!![position]
        val image = worldpopulation.flag

        //Got Advantages why to use Glide over picasso that's why replaced picasso.
        Glide.with(mContext).load(image)
                .thumbnail(0.1f)
                .error(error)
                .placeholder(loading)
                .crossFade() //animation
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .skipMemoryCache(true)
                .into(holder.countryImage)
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemCount(): Int {
        return mWorldPopulations?.size ?: 0
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.countryImage)
        lateinit var countryImage: ImageView
        init {
            ButterKnife.bind(this, view!!)
            RxView.clicks(itemView)
                    .map { _ -> adapterPosition }
                    .subscribe(onClickSubject)
        }


    }



}
