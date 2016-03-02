package xyz.marianomolina.misube.model;

/**
 * Created by hernancoppola on 29/2/16.
 */
public class Distancia {
    private Double valorNumerico;
    private String unidad;
    private String valorString;

    public Distancia(Double valorNum, String valorStr, String unit) {
        this.setValorNumerico(valorNum);
        this.setValorString(valorStr);
        this.setUnidad(unit);
    }

    public Double getValorNumerico() {
        return valorNumerico;
    }

    public void setValorNumerico(Double valorNumerico) {
        this.valorNumerico = valorNumerico;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getValorString() {
        return valorString;
    }

    public void setValorString(String valorString) {
        this.valorString = valorString;
    }
}

