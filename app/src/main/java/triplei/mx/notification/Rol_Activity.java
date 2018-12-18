package triplei.mx.notification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.rom4ek.arcnavigationview.ArcNavigationView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import triplei.mx.notification.Business.Business_Download_Information;
import triplei.mx.notification.Business.Business_Usuario;
import triplei.mx.notification.Entities.Collection_Rol;
import triplei.mx.notification.Entities.Entities_Rol;
import triplei.mx.notification.Utilerias.Utilerias_Activity;

public class Rol_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    Context context;
    LinearLayout nav_principal;
    String IdUsuario = "";
    triplei.mx.notification.Entities.Entities_Usuario Res_usuario;
    triplei.mx.notification.Entities.Collection_Rol Collection_Rol;
    SharedPreferences preferences;
    SwipeRefreshLayout container;
    ExpandableListView list_principal;
    HashMap<String, List<Entities_Rol>> Child = new HashMap<String, List<Entities_Rol>>();
    ArrayList<String> titles;
    ArrayList<Entities_Rol> ListRol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rol_);
        context = this;
        preferences = getSharedPreferences("Preference", Context.MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Periodos Por Rol");
        toolbar.setBackground(getResources().getDrawable(R.drawable.fondo, null));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        IdUsuario = getIntent().getStringExtra("IdUsuario");

        try {

            if(TextUtils.isEmpty(IdUsuario) || IdUsuario == null){
                IdUsuario = Utilerias_Activity.getIdUsuarioPreference(preferences);
            } else {
                savePreference(IdUsuario);
            }

            Res_usuario = Business_Usuario.Consulta_Usuario(context, IdUsuario);
            Collection_Rol = Business_Usuario.Consulta_Roles(context);

        } catch (Exception e){
            e.getMessage();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ArcNavigationView navigationView = (ArcNavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        LinearLayout mParent = (LinearLayout)navigationView.getHeaderView(0);
        LinearLayout nav_principal = mParent.findViewById(R.id.nav_principal);
        nav_principal.setBackground(getResources().getDrawable(R.drawable.fondo, null));

        String nombre_usuario = Res_usuario.getNombre();

        TextView text_avatar_nav = (TextView)mParent.findViewById(R.id.text_avatar_nav);
        text_avatar_nav.setText(nombre_usuario.substring(0,1));

        TextView text_usuario = mParent.findViewById(R.id.text_usuario);
        text_usuario.setText(nombre_usuario);

        TextView text_email = mParent.findViewById(R.id.text_email);
        text_email.setText(Res_usuario.getEmail());


        list_principal = (ExpandableListView)findViewById(R.id.list_principal);

        // Encuanto el listview este hasta la parte superior activara el SwipeRefresh
        container = (SwipeRefreshLayout)findViewById(R.id.container);
        list_principal.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition = (list_principal == null || list_principal.getChildCount() == 0) ?  0 : list_principal.getChildAt(0).getTop();
                if(firstVisibleItem == 0 && topRowVerticalPosition >= 0){
                    container.setEnabled(true);
                    container.setOnRefreshListener(Rol_Activity.this);
                    //onRefresh();
                } else {
                    container.setEnabled(false);
                }
            }
        });

        ListRol = new ArrayList<Entities_Rol>();
        titles = new ArrayList<String>();

        String key_principal;
        String key_anterior = "";


        // Esta Iteracion Separa los periodos x roles cuando encuentra un nuevo periodo, Genera una nueva lista
        for(int i = 0; i < Collection_Rol.size(); i++){

            key_principal = Collection_Rol.get(i).getFecha_Inicio();  // identificador

                if(key_principal.equalsIgnoreCase(key_anterior) || i == 0){ // Si la fecha principal es = a la fecha anterior o i es = a 0 entonces agregara el item a la lista
                    ListRol.add(Collection_Rol.get(i));
                }

                if(!key_principal.equalsIgnoreCase(key_anterior)){ // Si la fecha principal cambio
                    titles.add(Collection_Rol.get(i).getFecha_Inicio() + " - " + Collection_Rol.get(i).getFecha_Fin()); // Guardara el primer periodo en una lista
                    if(i > 0){
                        ListRol = new ArrayList<Entities_Rol>(); // en caso de que sea la primera vuelta aqui no entrara
                        ListRol.add(Collection_Rol.get(i));
                    }
                    Child.put(Collection_Rol.get(i).getFecha_Inicio() + " - " + Collection_Rol.get(i).getFecha_Fin(), ListRol); // en el Hasmap guardara su key y el valor de la lista
                }

            key_anterior = key_principal; //  Fecha principal es igual a la fecha anterior

        }

        // Adaptador
        Adapter_Rol adapter = new Adapter_Rol(context, R.layout.adapter_rol, titles, Child);
        list_principal.setAdapter(adapter);

        list_principal.setOnChildClickListener(new ExpandableListView.OnChildClickListener() { // Clic del Expandable
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                String tl = titles.get(groupPosition); // Key del HashMap
                Entities_Rol rol = Child.get(tl).get(childPosition); // childPosition, la posicion del hijo con respecto al grupo

                Intent intent = new Intent(Rol_Activity.this, Usuarios_Activity.class);
                intent.putExtra("Collection_Rol", rol);
                startActivity(intent);
                return false;
            }
        });


        container.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright,null),
                getResources().getColor(android.R.color.holo_green_light,null),
                getResources().getColor(android.R.color.holo_orange_light,null),
                getResources().getColor(android.R.color.holo_red_light,null)
        );


    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Business_Download_Information.Download_Usuario(context, FirebaseInstanceId.getInstance().getToken());
                            }catch (Exception e){
                                e.getMessage();
                            }
                        }
                    }).start();

                } catch (Exception e){
                    e.getMessage();
                }
                    container.setRefreshing(false);
            }
        }, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    // Abre y Cierra el DrawableNavigation
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_salir) {
            Logout_Session();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void Logout_Session(){
        removePreference();
        Intent intent = new Intent(Rol_Activity.this, Main_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void removePreference() { // Permite Salir de la session y borrar las credenciales del SharedPreference
        preferences.edit().clear().apply();
    }

    private void savePreference(String Id_Usuario) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Id_Usuario", IdUsuario);
        editor.apply();
    }


}
