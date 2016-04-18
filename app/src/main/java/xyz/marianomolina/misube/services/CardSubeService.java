package xyz.marianomolina.misube.services;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import xyz.marianomolina.misube.model.Tarjeta;

/**
 * Created by Mariano Molina on 17/4/16.
 * Twitter: @xsincrueldadx
 */
public class CardSubeService {
    private Tarjeta mTarjeta;
    private Context mContext;
    private RealmConfiguration realmConfiguration;
    private Realm realm;

    public CardSubeService(Context context) {
        // setContext
        this.mContext = context;
        // setCard
        this.mTarjeta = new Tarjeta();
        mTarjeta.setId(0);
        // setRealmConfig
        realmConfiguration = new RealmConfiguration.Builder(mContext).build();
        realm = Realm.getInstance(realmConfiguration);
    }

    public Tarjeta getmTarjeta() {
        return mTarjeta;
    }

    //public RealmList<Movimientos> listadoDeMovimientos() {}



}
