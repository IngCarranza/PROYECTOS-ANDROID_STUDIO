package com.ugb.controlesbasicos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class adaptadorImagenes extends BaseAdapter {
    Context context;
    ArrayList<amigos> datosVehiculosArrayList;
    amigos misVehiculos;
    LayoutInflater layoutInflater;
    public adaptadorImagenes(Context context, ArrayList<amigos> datosVehiculosArrayList) {
        this.context = context;
        this.datosVehiculosArrayList = datosVehiculosArrayList;
    }
    @Override
    public int getCount() {
        return datosVehiculosArrayList.size();
    }
    @Override
    public Object getItem(int i) {
        return datosVehiculosArrayList.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.listview_imagenes, viewGroup, false);
        try{
            misVehiculos = datosVehiculosArrayList.get(i);

            TextView tempVal = itemView.findViewById(R.id.lblMarca);
            tempVal.setText(misVehiculos.getMarca());

            tempVal = itemView.findViewById(R.id.lblMotor);
            tempVal.setText(misVehiculos.getMotor());

            tempVal = itemView.findViewById(R.id.lblChasis);
            tempVal.setText(misVehiculos.getChasis());

            tempVal = itemView.findViewById(R.id.lblVin);
            tempVal.setText(misVehiculos.getVin());


            ImageView imgView = itemView.findViewById(R.id.imgFoto);
            Bitmap imagenBitmap = BitmapFactory.decodeFile(misVehiculos.getFoto());
            imgView.setImageBitmap(imagenBitmap);
        }catch (Exception e){
            Toast.makeText(context, "Error en Adaptador Imagenes: "+ e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return itemView;
    }

}