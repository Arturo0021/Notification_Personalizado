package triplei.mx.notification;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import triplei.mx.notification.Entities.Entities_Usuario;

public class Adapter_Usuarios extends BaseAdapter {

    public  Context context;
    public int layout;
    public List<Entities_Usuario> usuarios;

    public Adapter_Usuarios(Context context, int layout, List<Entities_Usuario> usuarios) {
        this.context = context;
        this.layout = layout;
        this.usuarios = usuarios;
    }

    @Override
    public int getViewTypeCount() {
        return usuarios.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return this.usuarios.size();
    }

    @Override
    public Object getItem(int position) {
        return this.usuarios.get(position);
    }

    @Override
    public long getItemId(int Id) {
        return Id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        view = layoutInflater.inflate(R.layout.adapter_usuarios, null);
        Entities_Usuario entidades = usuarios.get(position);


        TextView adap_avatar = view.findViewById(R.id.adap_avatar);
        TextView adap_title = view.findViewById(R.id.adap_title);
        TextView adapt_desc = view.findViewById(R.id.adapt_desc);
        TextView adap_anex = view.findViewById(R.id.adap_anex);

            adap_avatar.setText(entidades.getNombre().substring(0,1));
            adap_title.setText(entidades.getNombre());
            adapt_desc.setText(entidades.getNombreRol());
            adap_anex.setText(entidades.getSin_Validar() + " Días Sin Validar");

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_left);
        view.startAnimation(animation);

        return view;
    }

}
