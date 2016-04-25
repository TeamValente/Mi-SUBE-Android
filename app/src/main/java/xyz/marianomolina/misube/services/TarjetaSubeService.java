package xyz.marianomolina.misube.services;

import android.content.Context;
import android.util.Log;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import xyz.marianomolina.misube.model.Movimientos;
import xyz.marianomolina.misube.model.Tarjeta;

/**
 * Created by Mariano Molina on 17/4/16.
 * Twitter: @xsincrueldadx
 */
public class TarjetaSubeService {
    private static final String LOG_TAG = TarjetaSubeService.class.getSimpleName();

    private Tarjeta mTarjeta;
    private Realm realm;

    public TarjetaSubeService(Context context) {
        // setRealmConfig
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(context).build();
        realm = Realm.getInstance(realmConfiguration);

        realm.beginTransaction();
        // setCard
        mTarjeta = realm.createObject(Tarjeta.class);
        mTarjeta.setId(0);
        realm.commitTransaction();
    }

    public Tarjeta getmTarjeta() {
        return mTarjeta;
    }

    /** Swift version
    func removeTarjeta() {
        //ManagerRealm
        let realm = try! Realm()
        //Traigo las tarjeta debe venir solo una
        let tarjetas = realm.objects(Tarjeta).filter("id = 1")
        if tarjetas.count != 0 {
            try! realm.write {
                realm.deleteAll()
                //realm.delete(tarjetas[0])
            }
        }
    }
    */

    public void removeTarjeta() {
        RealmResults<Tarjeta> tarjetas = realm.where(Tarjeta.class).findAll();
        realm.beginTransaction();
        tarjetas.remove(0);
        realm.commitTransaction();
    }

    /** Swift version
    func listadoDeMovimientos(orderBy: String) -> [Movimiento] {
        //ManagerRealm
        let realm = try! Realm()
        var order = true

        if orderBy == "desc" {
            order = false
        }

        let  movimientos = realm.objects(Movimiento).sorted("fechaMovimiento", ascending: order)
        return movimientos.toArray()
    }
    */

    public RealmResults<Movimientos> listadoDeMovimientos() {
        RealmResults<Movimientos> listado = realm.where(Movimientos.class).findAll();
        listado.sort("fechaMovimiento", Sort.DESCENDING);
        return listado;
    }

    /** Swift version
    func actualizarSaldo(nuevoMovimiento: Movimiento) {
        //ManagerRealm
        let realm = try! Realm()
        let nuevoSaldo:Double = miTarjeta.saldo + nuevoMovimiento.valorMovimiento
        try! realm.write {
            self.miTarjeta.saldo = nuevoSaldo
            self.miTarjeta.movimientos.append(nuevoMovimiento)
            realm.add(self.miTarjeta , update: true)
        }
    }
    */

    public void actualizarSaldo(Movimientos nuevoMovimiento) {
        Double nuevoSaldo = mTarjeta.getSaldo() + nuevoMovimiento.getValorMovimiento();

        realm.beginTransaction();

        Movimientos mMovimiento = realm.createObject(Movimientos.class);
        mMovimiento.setValorMovimiento(nuevoMovimiento.getValorMovimiento());

        mTarjeta.setSaldo(nuevoSaldo);
        realm.commitTransaction();
    }

    /** Swift version
    func getUltimoMovimiento() -> String {
        let realm = try! Realm()
        var retorno:String = ""
        if miTarjeta.movimientos.count > 0
        {
            if let ultimoMov = realm.objects(Movimiento).last
            {
                let fechaUltimoMov: NSDate = ultimoMov.fechaMovimiento
                print(retorno)

                retorno = NSDate().offsetFrom(fechaUltimoMov)
                print(NSDate().offsetFrom(fechaUltimoMov))

            }

        }
        return retorno
    }
    */

    public String getUltimoMovimiento() {
        String result = "";

        if (mTarjeta.getMovimientos().size() > 0) {
            RealmResults<Movimientos> ultimoMov = realm.where(Movimientos.class).findAllSorted("timestamp", Sort.DESCENDING);

            Date fechaUltimoMovimiento = ultimoMov.get(0).getFechaMovimiento();

            Log.d(LOG_TAG, "getUltimoMovimiento: " + fechaUltimoMovimiento.toString());

            result = fechaUltimoMovimiento.toString();
        }

        return result;
    }

}
