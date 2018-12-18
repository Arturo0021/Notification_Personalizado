package triplei.mx.notification;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import triplei.mx.notification.Entities.Entities_Rol;

public class Adapter_Rol extends BaseExpandableListAdapter {
    // Esta complejo de entender :'v
    private Context context;
    private int layout;
    private List<String> Roles; // Esta lista Son los titulos Que mostrara la lista agrupada
    // El Key del Hashmap puede ser un String, un Integer, Boolean ETC. yo lo amarre con la fecha
    private HashMap<String, List<Entities_Rol>> child; // Está lista seran los datos que muestre al dar clic en la lista agrupada

    public Adapter_Rol(Context context, int layout, List<String> Roles, HashMap<String, List<Entities_Rol>> child) { // Constructor
        this.context = context;
        this.layout = layout;
        this.Roles = Roles;
        this.child = child;
    }

    @Override
    public int getGroupCount() {
        return this.Roles.size(); // Tamaño de la lista agrupada (titulos)
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // child es el hasmap el primer get debe traer el key, se guardo en los titulos
        // Ejemplo this.chil.get('01-01-2018').size();
        return this.child.get(Roles.get(groupPosition)).size(); // Datos del Hasmap, hijos de la lista agrupada
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.Roles.get(groupPosition); // La posicion de los titulos
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // Ejemplo this.child.get('01-01-2018').get(0);
        return this.child.get(Roles.get(groupPosition)).get(childPosition); // Posicion de los hijos
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // Inflar el layout de los titulos
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.inflater_groupparent, null);
        }

        TextView parent_group = (TextView)convertView.findViewById(R.id.parent_group);
        parent_group.setTypeface(null, Typeface.BOLD);
        parent_group.setText(Roles.get(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // Inflar el layout de los hijos
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_rol, null);
        }

        TextView text_avatar = (TextView)convertView.findViewById(R.id.text_avatar);
        TextView text_title = (TextView)convertView.findViewById(R.id.text_title);
        TextView text_desc = (TextView)convertView.findViewById(R.id.text_desc);
        TextView text_anex = (TextView)convertView.findViewById(R.id.text_anex);

        String total = child.get(Roles.get(groupPosition)).get(childPosition).getTotal() + "  " + convertView.getResources().getString(R.string.usersinvalidar);

        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString redSpannable= new SpannableString(total);
        redSpannable.setSpan(new ForegroundColorSpan(Color.BLUE), 0, child.get(Roles.get(groupPosition)).get(childPosition).getTotal().toString().length(), 0);
        builder.append(redSpannable);


        text_avatar.setText(child.get(Roles.get(groupPosition)).get(childPosition).getNombreRol().substring(0,1));
        text_title.setText(child.get(Roles.get(groupPosition)).get(childPosition).getNombreRol());
        text_desc.setText(builder);
        text_anex.setText(Roles.get(groupPosition));

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_left);
        convertView.startAnimation(animation);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
