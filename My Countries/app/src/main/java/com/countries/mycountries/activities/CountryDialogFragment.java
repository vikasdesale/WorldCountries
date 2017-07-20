package com.countries.mycountries.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.countries.mycountries.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.countries.mycountries.R.drawable.error;
import static com.countries.mycountries.R.drawable.loading;

/**
 * Created by Dell on 7/19/2017.
 */
public class CountryDialogFragment extends DialogFragment {


    Unbinder unbinder;
    @Nullable
    private String imgURL;
    private Bundle mArgs;
    @Nullable
    @BindView(R.id.fullimage)
    ImageView fullimage;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
        View layout = layoutInflaterAndroid.inflate(R.layout.fullimg, null);
        unbinder = ButterKnife.bind(this, layout);

        mArgs = getArguments();
        imgURL = mArgs.getString("imgURL");
        Dialog imageDialog=new Dialog(getActivity());


        //Got Advantages why to use Glide over picasso that's why replaced picasso.
        Glide.with(getActivity()).load(imgURL)
                .thumbnail(0.1f)
                .error(error)
                .crossFade() //animation
                .placeholder(loading)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .skipMemoryCache(true)
                .into(fullimage != null ? fullimage : null);
        imageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        imageDialog.setContentView(layout);
        imageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        imageDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return imageDialog;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
