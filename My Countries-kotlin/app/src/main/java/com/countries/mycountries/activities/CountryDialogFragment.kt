package com.countries.mycountries.activities

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.countries.mycountries.R
import com.countries.mycountries.R.drawable.error
import com.countries.mycountries.R.drawable.loading

/**
 * Created by Dell on 7/19/2017.
 */
class CountryDialogFragment : DialogFragment() {


    internal var unbinder: Unbinder? = null;
    private var imgURL: String? = null
    private var mArgs: Bundle? = null
    @BindView(R.id.fullimage)
    lateinit var fullimage: ImageView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layoutInflaterAndroid = LayoutInflater.from(activity)
        val layout = layoutInflaterAndroid.inflate(R.layout.fullimg, null)
        unbinder = ButterKnife.bind(this, layout)

        mArgs = arguments
        imgURL = mArgs!!.getString("imgURL")
        val imageDialog = Dialog(activity)


        //Got Advantages why to use Glide over picasso that's why replaced picasso.
        Glide.with(activity).load(imgURL)
                .thumbnail(0.1f)
                .error(error)
                .crossFade() //animation
                .placeholder(loading)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .skipMemoryCache(true)
                .into(fullimage)
        imageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        imageDialog.setContentView(layout)
        imageDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        imageDialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        return imageDialog

    }


    override fun onDestroyView() {
        super.onDestroyView()
        unbinder!!.unbind()
    }

}
