package xyz.marianomolina.misube.model;

/**
 * Created by hernancoppola on 29/2/16.
 */
public class PuntoCarga {

    private int idPunto;
    private String address;
    private Double latitude;
    private Double longitude;
    private String type;
    private String icon;
    private int cost;
    private int hourOpen;
    private int hourClose;
    private int flagSeller;
    private int flagInvalid;
    private int idType;

    public int getIdPunto() {
        return idPunto;
    }

    public String getAddress() {
        return address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getType() {
        return type;
    }

    public String getIcon() {
        return icon;
    }

    public int getCost() {
        return cost;
    }

    public int getHourOpen() {
        return hourOpen;
    }

    public int getHourClose() {
        return hourClose;
    }

    public int getFlagSeller() {
        return flagSeller;
    }

    public int getFlagInvalid() {
        return flagInvalid;
    }

    public int getIdType() {
        return idType;
    }

    
}
