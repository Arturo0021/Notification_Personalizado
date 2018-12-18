package triplei.mx.notification.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;

import triplei.mx.notification.Entities.Collection_Rol;
import triplei.mx.notification.Entities.Collection_Usuario;
import triplei.mx.notification.Entities.Entities_Rol;
import triplei.mx.notification.Entities.Entities_Usuario;

public class Data_Usuario {

    private static SQLiteDatabase db = null;
    private static String METHOD_GETUSUARIO = "";
    private static StringEntity stringEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONObject jsonObject = null;
    private static JSONStringer jsonStringer = null;
    private static String strResult = null;

    public static String Valida_Credenciales(String usuario, String pass, String token) throws JSONException, IOException, Exception {

        String respuesta = "";
        METHOD_GETUSUARIO = "/LoginUsuarioToken";

        Wcf_Service service = new Wcf_Service();
        jsonStringer = new JSONStringer()
                .object()
                        .key("usuario").value(usuario)
                        .key("pass").value(pass)
                        .key("token").value(token)
                .endObject();

        stringEntity = new StringEntity(jsonStringer.toString());
        jsonResult = service.HttpPost(METHOD_GETUSUARIO, stringEntity);

          respuesta = jsonResult.getString("LoginUsuarioTokenResult");


        return respuesta;
    }

    public static void Insert_Usuarios(Context context, Entities_Usuario entities_usuario) {

        db = (new DbHelper(context)).getWritableDatabase();

        String query = "SELECT COUNT(1) FROM " + DbHelper.TABLE_USUARIOS
                            + " WHERE " + DbHelper.COLUMN_ID + " = '" + entities_usuario.getUsuarioId() + "';";

        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {

            int existe = cursor.getInt(0);

            if(existe == 0){ // Inserta

                String insert = "INSERT INTO " + DbHelper.TABLE_USUARIOS
                                    + "("
                                        + DbHelper.COLUMN_ID + ", "
                                        + DbHelper.COLUMN_USUARIO + ", "
                                        + DbHelper.COLUMN_NOMBRE + ", "
                                        + DbHelper.COLUMN_APAT + ", "
                                        + DbHelper.COLUMN_AMAT + ", "
                                        + DbHelper.COLUMN_TELEFONO + ", "
                                        + DbHelper.COLUMN_EMAIL + ", "
                                        + DbHelper.COLUMN_ROLID + ", "
                                        + DbHelper.COLUMN_ROL + ", "
                                        + DbHelper.COLUMN_ACTIVO
                                    + ") VALUES ("
                                        + "'" +entities_usuario.getUsuarioId() + "', "
                                        + "'" + entities_usuario.getUsuario() + "', "
                                        + "'" + entities_usuario.getNombre() + "', "
                                        + "'" + entities_usuario.getApellidoPaterno() + "', "
                                        + "'" + entities_usuario.getApellidoMaterno() + "', "
                                        + "'" + entities_usuario.getTelefono() + "', "
                                        + "'" + entities_usuario.getEmail() + "', "
                                        + "'" + entities_usuario.getRolId() + "', "
                                        + "'" + entities_usuario.getNombreRol() + "', "
                                        + Integer.parseInt(entities_usuario.getActivo())
                                    + ");";
                db.execSQL(insert);

            } else { // Actualiza

                String update = "UPDATE " + DbHelper.TABLE_USUARIOS
                                    + " SET " + DbHelper.COLUMN_USUARIO + " = '" + entities_usuario.getUsuario() + "', "
                                                + DbHelper.COLUMN_NOMBRE + " = '" + entities_usuario.getNombre() + "', "
                                                + DbHelper.COLUMN_APAT + " = '" + entities_usuario.getApellidoPaterno() + "', "
                                                + DbHelper.COLUMN_AMAT + " = '" + entities_usuario.getApellidoMaterno() + "', "
                                                + DbHelper.COLUMN_TELEFONO + " = '" + entities_usuario.getTelefono() + "', "
                                                + DbHelper.COLUMN_EMAIL + " = '" + entities_usuario.getEmail() + "', "
                                                + DbHelper.COLUMN_ROLID + " = '" + entities_usuario.getRolId() + "', "
                                                + DbHelper.COLUMN_ROL + " = '" + entities_usuario.getNombreRol() + "', "
                                                + DbHelper.COLUMN_ACTIVO + " = " + Integer.parseInt(entities_usuario.getActivo())
                                + " WHERE " + DbHelper.COLUMN_ID + " = '"+ entities_usuario.getUsuarioId() + "';";

                db.execSQL(update);
            }

        }
        db.close();
    }

    public static Entities_Usuario Consulta_Usuario(Context context, String Id_Usuario){

        Entities_Usuario usuario = new Entities_Usuario();
        db = (new DbHelper(context)).getWritableDatabase();

        String query = "SELECT " + DbHelper.COLUMN_ID + ", "
                                 + DbHelper.COLUMN_NOMBRE + " || ' ' || " + DbHelper.COLUMN_APAT + ", "
                                 + DbHelper.COLUMN_TELEFONO + " , "
                                 + DbHelper.COLUMN_EMAIL + ", "
                                 + DbHelper.COLUMN_ROL
                    + " FROM " + DbHelper.TABLE_USUARIOS
                    + " WHERE " + DbHelper.COLUMN_ID + " = '" + Id_Usuario + "';";

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
                usuario.setUsuarioId(cursor.getString(0));
                usuario.setNombre(cursor.getString(1));
                usuario.setTelefono(cursor.getString(2));
                usuario.setEmail(cursor.getString(3));
                usuario.setNombreRol(cursor.getString(4));
        }

        return usuario;
    }

    public static Collection_Rol Consulta_Roles(Context context) {

        Collection_Rol collection = new Collection_Rol();
        db = (new DbHelper(context)).getWritableDatabase();

        String query = "SELECT "
                            + " TBU." + DbHelper.COLUMN_ROLID + ", "
                            + " TBP." + DbHelper.COLUMN_ID + ", "
                            + " TBU." + DbHelper.COLUMN_ROL + ", "
                            + " TBP." + DbHelper.COLUMN_FECHA_INICIO + ", "
                            + " TBP." + DbHelper.COLUMN_FECHA_FIN + ", "
                            + " COUNT(1) "
                        + " FROM " + DbHelper.TABLE_USUARIOS + " TBU "
                            + " INNER JOIN " + DbHelper.TABLE_USUARIOS_PERIODOS + " TBUP ON TBUP." + DbHelper.COLUMN_ID_USUARIO + " = TBU." + DbHelper.COLUMN_ID
                            + " INNER JOIN " + DbHelper.TABLE_PERIODO + " TBP ON TBP." + DbHelper.COLUMN_ID + " = TBUP." + DbHelper.COLUMN_ID_PERIODO
                        + " WHERE TBU." + DbHelper.COLUMN_ACTIVO + " = 1 AND TBUP." + DbHelper.COLUMN_SIN_VALIDAR + " >= 1  AND TBU." + DbHelper.COLUMN_ROLID + " != 7"
                            + " GROUP BY TBP." + DbHelper.COLUMN_ID + ", TBU." + DbHelper.COLUMN_ROLID
                        + " ORDER BY TBP." + DbHelper.COLUMN_FECHA_INICIO + " ASC, TBU." + DbHelper.COLUMN_ROLID + " DESC;";

        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){

            do {

                Entities_Rol rol = new Entities_Rol();

                    rol.setRolId(cursor.getString(0));
                    rol.setIdPeriodo(cursor.getString(1));
                    rol.setNombreRol(cursor.getString(2));
                    rol.setFecha_Inicio(cursor.getString(3));
                    rol.setFecha_Fin(cursor.getString(4));
                    rol.setTotal(cursor.getInt(5));

                collection.add(rol);

            } while (cursor.moveToNext());

        }

        return collection;
    }

    public static Collection_Usuario Get_Usuarios(Context context, String RolId, String IdPeriodo) {

        db = (new DbHelper(context)).getWritableDatabase();
        Collection_Usuario collection = new Collection_Usuario();

        String query = "SELECT "
                            + " TBU." + DbHelper.COLUMN_ID + ", "
                            + " TBU." + DbHelper.COLUMN_USUARIO + ", "
                            + " TBU." + DbHelper.COLUMN_NOMBRE + " || ' ' || " + DbHelper.COLUMN_APAT + ", "
                            + " TBU." + DbHelper.COLUMN_EMAIL + ", "
                            + " TBU." + DbHelper.COLUMN_TELEFONO + ", "
                            + " TBU." + DbHelper.COLUMN_ROL + ", "
                            + " TBUP." + DbHelper.COLUMN_SIN_VALIDAR
                        + " FROM " + DbHelper.TABLE_USUARIOS + " TBU "
                         + " INNER JOIN " + DbHelper.TABLE_USUARIOS_PERIODOS + " TBUP ON TBUP." + DbHelper.COLUMN_ID_USUARIO + " = TBU." + DbHelper.COLUMN_ID
                         + " INNER JOIN " + DbHelper.TABLE_PERIODO + " TBP ON TBP." + DbHelper.COLUMN_ID + " = TBUP." + DbHelper.COLUMN_ID_PERIODO
                        + " WHERE TBU." + DbHelper.COLUMN_ACTIVO + " = 1 AND TBUP." + DbHelper.COLUMN_SIN_VALIDAR + " >= 1 "
                                + " AND TBP." + DbHelper.COLUMN_ID + " = '" + IdPeriodo + "'"
                                + " AND TBU." + DbHelper.COLUMN_ROLID + " = " + RolId
                        + " ORDER BY TBUP." + DbHelper.COLUMN_SIN_VALIDAR + " DESC;";

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do {

                Entities_Usuario entities_usuario = new Entities_Usuario();

                entities_usuario.setUsuarioId(cursor.getString(0));
                entities_usuario.setUsuario(cursor.getString(1));
                entities_usuario.setNombre(cursor.getString(2));
                entities_usuario.setEmail(cursor.getString(3));
                entities_usuario.setTelefono(cursor.getString(4));
                entities_usuario.setNombreRol(cursor.getString(5));
                entities_usuario.setSin_Validar(cursor.getInt(6));

                collection.add(entities_usuario);

            } while (cursor.moveToNext());
        }

        return collection;
    }


    public static Entities_Rol Consulta_Roles_Usuario(Context context, Entities_Rol roles) {

        Entities_Rol rol = new Entities_Rol();
        db = (new DbHelper(context)).getWritableDatabase();

        String query = "SELECT "
                            + " TBU." + DbHelper.COLUMN_ROLID + ", "
                            + " TBP." + DbHelper.COLUMN_ID + ", "
                            + " TBU." + DbHelper.COLUMN_ROL + ", "
                            + " TBP." + DbHelper.COLUMN_FECHA_INICIO + ", "
                            + " TBP." + DbHelper.COLUMN_FECHA_FIN + ", "
                            + " TBU." + DbHelper.COLUMN_NOMBRE + " || ' ' || " + DbHelper.COLUMN_APAT + ", "
                            + " TBU." + DbHelper.COLUMN_TELEFONO
                            + " FROM " + DbHelper.TABLE_USUARIOS + " TBU "
                            + " INNER JOIN " + DbHelper.TABLE_USUARIOS_PERIODOS + " TBUP ON TBUP." + DbHelper.COLUMN_ID_USUARIO + " = TBU." + DbHelper.COLUMN_ID
                            + " INNER JOIN " + DbHelper.TABLE_PERIODO + " TBP ON TBP." + DbHelper.COLUMN_ID + " = TBUP." + DbHelper.COLUMN_ID_PERIODO
                            + " WHERE TBU." + DbHelper.COLUMN_ACTIVO + " = 1 AND TBUP." + DbHelper.COLUMN_SIN_VALIDAR + " >= 1  AND TBU." + DbHelper.COLUMN_ROLID + " != 7"
                                + " AND TBU." + DbHelper.COLUMN_ROLID + " = " + roles.getRolId()
                                + " AND TBU." + DbHelper.COLUMN_ID + " = '" + roles.getIdUsuario() + "'"
                                + " AND TBP." + DbHelper.COLUMN_ID + " = '" + roles.getIdPeriodo() + "'"
                            + " ORDER BY TBP." + DbHelper.COLUMN_FECHA_INICIO + " ASC, TBU." + DbHelper.COLUMN_ROLID + " DESC;";

        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){

                rol.setRolId(cursor.getString(0));
                rol.setIdPeriodo(cursor.getString(1));
                rol.setNombreRol(cursor.getString(2));
                rol.setFecha_Inicio(cursor.getString(3));
                rol.setFecha_Fin(cursor.getString(4));
                rol.setNombreUsuario(cursor.getString(5));
                rol.setTelefono(cursor.getString(6));

        }

        return rol;
    }

}
