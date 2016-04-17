package xyz.marianomolina.misube.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Mariano Molina on 16/4/16.
 * Twitter: @xsincrueldadx
 */
public class Tarjeta extends RealmObject {
    private int id;
    private double saldo;
    private RealmList<Movimientos> movimientos;

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

    public RealmList<Movimientos> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(RealmList<Movimientos> movimientos) {
        this.movimientos = movimientos;
    }
}
