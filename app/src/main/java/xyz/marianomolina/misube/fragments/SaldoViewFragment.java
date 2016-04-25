package xyz.marianomolina.misube.fragments;

import android.app.DialogFragment;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.marianomolina.misube.MovimientosActivity;
import xyz.marianomolina.misube.R;


/**
 * Created by Mariano Molina on 17/2/16.
 * Twitter: @xsincrueldadx
 */
public class SaldoViewFragment extends Fragment {
    private static final String LOG_TAG = SaldoViewFragment.class.getSimpleName();
    // Bind Views
    @Nullable @Bind(R.id.view_saldo) TextView view_saldo;
    @Nullable @Bind(R.id.label_moves) TextView view_movimientos;
    @Bind(R.id.carview) CardView mCardview;
    @Bind(R.id.btn_open_move_list) ImageButton btn_open_movimientos;
    @Bind(R.id.btnAddTrip) Button btn_add_trip;
    @Bind(R.id.btnAddCredit) Button btn_add_credit;

    public SaldoViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saldo_view, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ViewAnimator.animate(mCardview)
                .translationY(-1000,0)
                .alpha(0, 1)
                .descelerate()
                .duration(500)
                .start();

        btn_open_movimientos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMovimientos = new Intent(getContext(), MovimientosActivity.class);
                getActivity().startActivity(intentMovimientos);
            }
        });


        btn_add_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void showDialog() {
        DialogFragment dialogFragment = new DialogFragment();
        dialogFragment.show(getActivity().getFragmentManager(), "Registrar un viaje");
    }

}
