package xyz.marianomolina.misube.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.marianomolina.misube.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovimientosActivityFragment extends Fragment {

    public MovimientosActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movimientos, container, false);
    }
}
