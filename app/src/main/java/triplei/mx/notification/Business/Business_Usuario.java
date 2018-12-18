package triplei.mx.notification.Business;

import android.content.Context;

import triplei.mx.notification.Data.Data_Usuario;
import triplei.mx.notification.Entities.Collection_Rol;
import triplei.mx.notification.Entities.Collection_Usuario;
import triplei.mx.notification.Entities.Entities_Rol;
import triplei.mx.notification.Entities.Entities_Usuario;

public class Business_Usuario {

    public static String Valida_Credenciales(String usuario, String pass, String token) throws Exception{

        String respuesta =  triplei.mx.notification.Data.Data_Usuario.Valida_Credenciales(usuario, pass, token);

        return respuesta;
    }

    public static Entities_Usuario Consulta_Usuario(Context context, String Id_Usuario) throws  Exception {

        if(Id_Usuario.equals("")){
            throw new Exception("Id_Usuario No Referenciado Consulta_Usuario");
        }

        triplei.mx.notification.Entities.Entities_Usuario usuario = Data_Usuario.Consulta_Usuario(context, Id_Usuario);

        return usuario;
    }

    public static Collection_Rol Consulta_Roles(Context context) throws  Exception {

        triplei.mx.notification.Entities.Collection_Rol collection = Data_Usuario.Consulta_Roles(context);

        return collection;
    }

    public static Collection_Usuario Get_Usuarios(Context context, String RolId, String IdPeriodo) throws Exception{

        if (IdPeriodo.equals("")){
            throw new Exception("No Existe IdPeriodo Get_Usuarios");
        }

        Collection_Usuario collection = Data_Usuario.Get_Usuarios(context, RolId, IdPeriodo);

        return collection;
    }

    public static Entities_Rol Consulta_Roles_Usuario(Context context, Entities_Rol roles) {

       Entities_Rol entities_rol =  Data_Usuario.Consulta_Roles_Usuario(context, roles);

       return entities_rol;
    }

}
