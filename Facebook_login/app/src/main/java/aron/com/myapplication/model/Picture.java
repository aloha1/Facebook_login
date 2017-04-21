package aron.com.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Yunwen on 4/20/2017.
 */

public class Picture implements Serializable {

    @SerializedName("is_silhouette") public boolean isSilhouette;

    @SerializedName("url") public String url;
}
