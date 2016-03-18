package xyz.marianomolina.misube.model;

import java.util.List;

import io.realm.RealmObject;

/**
 * Created by Mariano Molina on 18/3/16.
 * Twitter: @xsincrueldadx
 */
public class Terjeta extends RealmObject {

    private String alias = "Tarjeta";
    private int id;
    private double saldo;
    private List<Movimiento> movimientos;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public List<Movimiento> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<Movimiento> movimientos) {
        this.movimientos = movimientos;
    }
}
