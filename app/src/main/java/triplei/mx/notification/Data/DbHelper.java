package triplei.mx.notification.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public final Context context;
    public static final String DATABASE_NAME = "notification_bd";
    public static final int DATABASE_VERSION = 1;

    // Tablas
    public static final String TABLE_CONFIG = "Tbl_Config";
    public static final String TABLE_USUARIOS = "Tbl_Usuarios";
    public static final String TABLE_PERIODO = "Tbl_Periodos";
    public static final String TABLE_USUARIOS_PERIODOS = "Tbl_Usuarios_Periodos";

    // Catalogos
    public static final String CATALOGO_CONFIG = "Cat_Config";

    //Columnas
    public static final String COLUMN_ID = "_Id";
    public static final String COLUMN_ID_PERIODO = "_IdPeriodo";
    public static final String COLUMN_ID_USUARIO = "_IdUsuario";
    public static final String COLUMN_NOMBRE = "Nombre";
    public static final String COLUMN_GRUPO = "Grupo";
    public static final String COLUMN_ACTIVO = "Activo";
    public static final String COLUMN_USUARIO = "Usuario";
    public static final String COLUMN_APAT = "Apellido_P";
    public static final String COLUMN_AMAT = "Apellido_M";
    public static final String COLUMN_TELEFONO = "Telefono";
    public static final String COLUMN_EMAIL = "Email";
    public static final String COLUMN_ROLID = "Rol_Id";
    public static final String COLUMN_ROL = "Rol";
    public static final String COLUMN_CODIGO = "Codigo";
    public static final String COLUMN_DIAS = "Dias";
    public static final String COLUMN_DIAS_TRABAJADOS = "Dias_Trabajados";
    public static final String COLUMN_MES = "Mes";
    public static final String COLUMN_EJERCICIO_FISCAL = "Ejercicio_Fiscal";
    public static final String COLUMN_PERIODICIDAD = "Periodicidad";
    public static final String COLUMN_FECHA_INICIO = "Fecha_Inicio";
    public static final String COLUMN_FECHA_FIN = "Fecha_Fin";
    public static final String COLUMN_SIN_VALIDAR = "Sin_Validar";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tablas
        Tabla_config(db);
        Tabla_Usuarios(db);
        Tabla_Periodos(db);
        Tabla_Usuarios_Periodos(db);

        //Catalogos
        Catalogo_config(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Tablas
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_USUARIOS + "; ");
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_PERIODO + "; ");
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_CONFIG + "; ");
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_USUARIOS_PERIODOS + "; ");

        // Catalogos
        db.execSQL(" DROP TABLE IF EXISTS " + CATALOGO_CONFIG + "; ");
    }

    public void Tabla_Usuarios(SQLiteDatabase db){
        String Table_config =
                "CREATE TABLE IF NOT EXISTS " + TABLE_USUARIOS
                        + "("
                            + COLUMN_ID + " VARCHAR, "
                            + COLUMN_USUARIO + " VARCHAR, "
                            + COLUMN_NOMBRE + " VARCHAR, "
                            + COLUMN_APAT + " VARCHAR, "
                            + COLUMN_AMAT + " VARCHAR, "
                            + COLUMN_TELEFONO + " VARCHAR, "
                            + COLUMN_EMAIL + " VARCHAR, "
                            + COLUMN_ROLID + " VARCHAR, "
                            + COLUMN_ROL + " VARCHAR, "
                            + COLUMN_ACTIVO + " INTEGER "
                        + ");";
        db.execSQL(Table_config);
    }

    public void Tabla_Periodos(SQLiteDatabase db) {

        String Table_Periodos =
                "CREATE TABLE IF NOT EXISTS " + TABLE_PERIODO
                        + "("
                           + COLUMN_ID + " VARCHAR, "
                           + COLUMN_CODIGO + " VARCHAR, "
                           + COLUMN_DIAS + " VARCHAR, "
                           + COLUMN_DIAS_TRABAJADOS + " VARCHAR, "
                           + COLUMN_MES + " VARCHAR, "
                           + COLUMN_EJERCICIO_FISCAL + " VARCHAR, "
                           + COLUMN_PERIODICIDAD + " VARCHAR, "
                           + COLUMN_FECHA_INICIO + " VARCHAR, "
                           + COLUMN_FECHA_FIN + " VARCHAR, "
                           + COLUMN_ACTIVO + " INTEGER "
                        + ");";
        db.execSQL(Table_Periodos);
    }

    public void Tabla_Usuarios_Periodos(SQLiteDatabase db) {
        String Table_Usuarios_Periodos =
                "CREATE TABLE IF NOT EXISTS " + TABLE_USUARIOS_PERIODOS
                        + "("
                            + COLUMN_ID_USUARIO + " VARCHAR, "
                            + COLUMN_ID_PERIODO + " VARCHAR, "
                            + COLUMN_SIN_VALIDAR + " INTEGER"
                        + ");";
        db.execSQL(Table_Usuarios_Periodos);
    }

    public void Tabla_config(SQLiteDatabase db){
        String Table_config =
                "CREATE TABLE IF NOT EXISTS " + TABLE_CONFIG
                    + "("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUMN_NOMBRE + " VARCHAR, "
                        + COLUMN_GRUPO + " INTEGER, "
                        + COLUMN_ACTIVO + " INTEGER "
                    + ");";
         db.execSQL(Table_config);
    }

    public void Catalogo_config(SQLiteDatabase db) {
        String Cat_config =
                "CREATE TABLE IF NOT EXISTS " + CATALOGO_CONFIG
                    + "("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUMN_NOMBRE + " VARCHAR, "
                        + COLUMN_ACTIVO + " INTEGER "
                    + ");";
        db.execSQL(Cat_config);
    }

}
