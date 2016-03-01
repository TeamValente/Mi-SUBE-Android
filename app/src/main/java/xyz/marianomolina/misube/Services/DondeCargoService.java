package xyz.marianomolina.misube.services;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by hernancoppola on 29/2/16.
 */
public class DondeCargoService {


    public String generarURLValida(String url) throws UnsupportedEncodingException {
        //Devuelve una url valida
        return URLEncoder.encode(url,"UTF-8");
    }




}
