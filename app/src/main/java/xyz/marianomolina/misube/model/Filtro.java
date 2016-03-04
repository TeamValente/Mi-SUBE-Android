package xyz.marianomolina.misube.model;

/**
 * Created by hernancoppola on 1/3/16.
 */
public class Filtro {

    boolean ocultarCobraCarga = false;
    boolean ocultarCerrados = false;
    boolean ocultarNoVendeSUBE = false;

    public boolean isOcultarCerrados() {
        return ocultarCerrados;
    }

    public void setOcultarCerrados(boolean ocultarCerrados) {
        this.ocultarCerrados = ocultarCerrados;
    }

    public boolean isOcultarCobraCarga() {
        return ocultarCobraCarga;
    }

    public void setOcultarCobraCarga(boolean ocultarCobraCarga) {
        this.ocultarCobraCarga = ocultarCobraCarga;
    }

    public boolean isOcultarNoVendeSUBE() {
        return ocultarNoVendeSUBE;
    }

    public void setOcultarNoVendeSUBE(boolean ocultarNoVendeSUBE) {
        this.ocultarNoVendeSUBE = ocultarNoVendeSUBE;
    }

    public boolean isOcutarHorarioSinIndicar() {
        return ocutarHorarioSinIndicar;
    }

    public void setOcutarHorarioSinIndicar(boolean ocutarHorarioSinIndicar) {
        this.ocutarHorarioSinIndicar = ocutarHorarioSinIndicar;
    }

    boolean ocutarHorarioSinIndicar = false;

    public Filtro() {

    }

    public Filtro(boolean ocultarCobraCarga,boolean ocultarCerrados,boolean ocultarNoVendeSUBE,boolean ocutarHorarioSinIndicar) {
        //Siempre se reciben
        this.ocultarCobraCarga = ocultarCobraCarga;
        this.ocultarCerrados = ocultarCerrados;
        this.ocultarNoVendeSUBE   = ocultarNoVendeSUBE;
        this.ocutarHorarioSinIndicar = ocutarHorarioSinIndicar;
    }

}
