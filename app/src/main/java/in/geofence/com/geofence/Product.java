package in.geofence.com.geofence;


import com.google.gson.annotations.SerializedName;

/**
 *
 */
public class Product
{
    @SerializedName("sid")
    public int sid;

    @SerializedName("status")
    public  String status;
    @SerializedName("full_name")
    public  String username;
    @SerializedName("year")
    public  String year;

    @SerializedName("qty")
    public int qty;

    @SerializedName("price")
    public int price;
    @SerializedName("image_url")
    public String image_url;

    @SerializedName("subid")
    public int subid;


    @SerializedName("branch")
    public  String branch;

    @SerializedName("file")
    public String file;
}
