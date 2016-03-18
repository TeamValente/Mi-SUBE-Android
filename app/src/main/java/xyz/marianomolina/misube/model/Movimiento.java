package xyz.marianomolina.misube.model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Mariano Molina on 18/3/16.
 * Twitter: @xsincrueldadx
 */
public class Movimiento extends RealmObject {

    private Date fechaDeMovimiento = new Date();
    private Double valorMovimiento = 0d;

    public Date getFechaDeMovimiento() {
        return fechaDeMovimiento;
    }

    public void setFechaDeMovimiento(Date fechaDeMovimiento) {
        this.fechaDeMovimiento = fechaDeMovimiento;
    }

    public Double getValorMovimiento() {
        return valorMovimiento;
    }

    public void setValorMovimiento(Double valorMovimiento) {
        this.valorMovimiento = valorMovimiento;
    }
}
