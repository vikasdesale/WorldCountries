
package com.countries.mycountries.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountryList {

    @Nullable
    @SerializedName("worldpopulation")
    private List<Worldpopulation> worldpopulation = null;

}
