package com.futureapp.studyground.viajes;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.futureapp.studyground.R;


public class ViajesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public View mView;

    public ViajesHolder(@NonNull View itemView) {
        super(itemView);

        mView=itemView;

        itemView.setOnClickListener(this);

    }

    public void setDestino(String destino) {
        TextView field = (TextView) mView.findViewById(R.id.lblDestino);
        field.setText(destino);
    }


    public void setOrigen(String origen) {
        TextView field = (TextView) mView.findViewById(R.id.lblOrigen);
        field.setText("Desde: "+origen);
    }

    public void setHora(String hora) {
        TextView field = (TextView) mView.findViewById(R.id.lblHora);
        field.setText("Hora: "+hora);
    }


    public void setPrecio(String precio) {
        TextView field = (TextView) mView.findViewById(R.id.lblPrecio);
        field.setText("Valor: "+precio);
    }

    public void setTelefono(String telefono) {
        TextView field = (TextView) mView.findViewById(R.id.lblTelefono);
        field.setText("Teléfono: "+telefono);
    }

    public void setNombre(String nombre) {
        TextView field = (TextView) mView.findViewById(R.id.lblNombre);
        field.setText("Nombre: "+nombre);
    }


    @Override
    public void onClick(View view) {
        System.out.println("RUTAS onClick: " + getAdapterPosition());
        /*Uri uri = Uri.parse("https://wa.me/57");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);*/
    }
}
