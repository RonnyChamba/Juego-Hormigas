package com.example.juegohormigas;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adaptador extends  RecyclerView.Adapter<Adaptador.MyHolder>{

    private Context context;
    private List<Jugador> jugadorList;

    public Adaptador(Context context, List<Jugador> jugadorList){
        this.context = context;
        this.jugadorList = jugadorList;
    }

    /**
     * Inflamos el diseño
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(context).inflate(R.layout.jugadores, parent, false);
        return  new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        // Obtener datos del modelo, recibo desde Puntajes
        Jugador jugadorTemp = jugadorList.get(position);

        holder.txtNombreJugador.setText(jugadorTemp.getNombres());
        holder.txtCorreoJugador.setText(jugadorTemp.getEmail());
        holder.txtPuntajeJugador.setText( String.valueOf(jugadorTemp.getPuntaje()));

        int codigoImg = 0;
        switch (jugadorTemp.getPais()) {

            case "Ecuador" : codigoImg = R.drawable.flag_ecuador;
                break;
            case "Colombia" : codigoImg = R.drawable.flag_colombia;
                break;

            case "Peru" : codigoImg = R.drawable.flag_peru;
                break;


            case "Venezuela" : codigoImg = R.drawable.flag_venezuela;
                break;

            case "Argentina" : codigoImg = R.drawable.flag_argentina;
                break;

            default:
                codigoImg = R.drawable.default_perfil;
                break;
        }

        Picasso.get().load(codigoImg).into(holder.imgPaisJugador);

        if (!jugadorTemp.getImagen().equals("")){
            Picasso.get().load(jugadorTemp.getImagen()).into(holder.imagenJugador);
        }else Picasso.get().load(R.drawable.default_perfil).into(holder.imagenJugador);

        // Click sobre la filla del jugador
        holder.itemView.setOnClickListener( (even) ->{

           Intent intent = new Intent(context, DetalleJugador.class);
            intent.putExtra("jugadorClick", jugadorTemp);
            context.startActivity(intent);

        });

    }
    @Override
    public int getItemCount() {
        return jugadorList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        CircleImageView imagenJugador;
        TextView txtNombreJugador;
        TextView txtCorreoJugador;
        TextView txtPuntajeJugador;
        CircleImageView imgPaisJugador;
        View itemView;

        public  MyHolder(@NonNull View itemView){
            super(itemView);

            this.itemView = itemView;
            findByObjects();
            setFont();
        }
        private  void  findByObjects(){

            imagenJugador = itemView.findViewById(R.id.imgJugador);
            txtNombreJugador = itemView.findViewById(R.id.txtNombreJugador);
            txtCorreoJugador = itemView.findViewById(R.id.txtCorreoJugador);
            txtPuntajeJugador = itemView.findViewById(R.id.txtPuntajeJugador);
            imgPaisJugador = itemView.findViewById(R.id.imgPaisJugador);
        }
        private void setFont(){
            Typeface typeface = Typeface.createFromAsset(itemView.getContext().getAssets(), "fuentes/zombie.TTF");

            txtNombreJugador.setTypeface(typeface);
            txtCorreoJugador.setTypeface(typeface);
            txtPuntajeJugador.setTypeface(typeface);
        }

    }
}
