package xyz.marianomolina.misube.model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Mariano Molina on 16/4/16.
 * Twitter: @xsincrueldadx
 */
public class Movimientos extends RealmObject {
    private Date fechaMovimiento;
    private double valorMovimiento;

    public Date getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(Date fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public double getValorMovimiento() {
        return valorMovimiento;
    }

    public void setValorMovimiento(double valorMovimiento) {
        this.valorMovimiento = valorMovimiento;
    }
}
