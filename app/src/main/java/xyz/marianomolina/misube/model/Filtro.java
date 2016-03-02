package xyz.marianomolina.misube.model;

/**
 * Created by hernancoppola on 1/3/16.
 */
public class Filtro {

    boolean ocultarCobraCarga = false;
    boolean ocultarCerrados = false;
    boolean ocultarNoVendeSUBE = false;
    boolean ocutarHorarioSinIndicar = false;


    public Filtro(){}

    public Filtro(boolean ocultarCobraCarga,boolean ocultarCerrados,boolean ocultarNoVendeSUBE,boolean ocutarHorarioSinIndicar)
    {
        //Siempre se reciben
        this.ocultarCobraCarga = ocultarCobraCarga;
        this.ocultarCerrados = ocultarCerrados;
        this.ocultarNoVendeSUBE   = ocultarNoVendeSUBE;
        this.ocutarHorarioSinIndicar = ocutarHorarioSinIndicar;
    }


}
