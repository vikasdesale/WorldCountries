
package com.countries.mycountries.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountryList {

    @SerializedName("worldpopulation")
    public List<Worldpopulation> worldpopulation = null;

}
