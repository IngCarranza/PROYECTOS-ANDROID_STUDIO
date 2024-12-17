package com.ugb.controlesbasicos;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

//import com.denzcoskun.imageslider.ImageSlider;
//import com.denzcoskun.imageslider.constants.ScaleTypes;
//import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Referencias a los CardView
        CardView clothingCard = view.findViewById(R.id.clothingCard);
        CardView elecCard = view.findViewById(R.id.elecCard);
        CardView homeCard = view.findViewById(R.id.homeCard);
        CardView beautyCard = view.findViewById(R.id.beautyCard);
        CardView pharmCard = view.findViewById(R.id.pharmCard);
        CardView grocCard = view.findViewById(R.id.grocCard);

        // Configurar los listeners de clic
        clothingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToActivity(SaludBucalActivity.class);
            }
        });

        elecCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToActivity(SaludVisualActivity.class);
            }
        });

        homeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToActivity(VitaminasActivity.class);
            }
        });

        beautyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToActivity(GripeTosActivity.class);
            }
        });

        pharmCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToActivity(ProductosActivity.class);
            }
        });

        grocCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToActivity(PrimerosAuxiliosActivity.class);
            }
        });

        return view;
    }

    private void navigateToActivity(Class<?> activityClass) {
        Intent intent = new Intent(getActivity(), activityClass);
        startActivity(intent);
    }
}
