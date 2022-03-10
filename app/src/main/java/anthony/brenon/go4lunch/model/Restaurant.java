package anthony.brenon.go4lunch.model;

import com.google.gson.annotations.SerializedName;

import anthony.brenon.go4lunch.model.googleplace_models.GeometryPlace;

/**
 * Created by Lycast on 01/03/2022.
 */
public class Restaurant {

    @SerializedName("place_id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("vicinity")
    private String address;
    @SerializedName("geometry")
    private GeometryPlace geometryPlace;

    //GETTERS
    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public GeometryPlace getGeometryPlace() { return geometryPlace; }
}
