package xyz.marianomolina.misube.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.florent37.viewanimator.ViewAnimator;

import xyz.marianomolina.misube.MovimientosActivity;
import xyz.marianomolina.misube.R;


/**
 * Created by Mariano Molina on 17/2/16.
 * Twitter: @xsincrueldadx
 */
public class SaldoViewFragment extends Fragment {
    // TAG
    private static final String LOG_TAG = SaldoViewFragment.class.getSimpleName();

    // viewOulets
    private TextView view_saldo;
    private TextView view_movimientos;


    public SaldoViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saldo_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        view_saldo = (TextView) getActivity().findViewById(R.id.view_saldo);
        view_movimientos = (TextView) getActivity().findViewById(R.id.label_moves);

        CardView mCardview = (CardView) getActivity().findViewById(R.id.carview);
        ViewAnimator.animate(mCardview)
                .translationY(-1000,0)
                .alpha(0, 1)
                .descelerate()
                .duration(1000)
                .start();


        ImageButton btn_open_movimientos = (ImageButton) getActivity().findViewById(R.id.btn_open_move_list);
        btn_open_movimientos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMovimientos = new Intent(getContext(), MovimientosActivity.class);
                getActivity().startActivity(intentMovimientos);
            }
        });
    }
}
