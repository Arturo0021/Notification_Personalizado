package triplei.mx.notification.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import triplei.mx.notification.Entities.Entities_Periodo;
import triplei.mx.notification.Entities.Entities_Usuario_Periodo;

public class Data_Periodo {

    private static SQLiteDatabase db = null;

    public static void Insert_Periodo(Context context, Entities_Periodo entities_periodo){

        db = (new DbHelper(context)).getWritableDatabase();

        String query = "SELECT COUNT(1) FROM " + DbHelper.TABLE_PERIODO + " WHERE " + DbHelper.COLUMN_ID + " = '" + entities_periodo.getPeriodoId() + "';";

        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {

            int existe = cursor.getInt(0);

            if(existe == 0) {

                String insert = "INSERT INTO " + DbHelper.TABLE_PERIODO
                                    + "("
                                        + DbHelper.COLUMN_ID + ", "
                                        + DbHelper.COLUMN_CODIGO + ", "
                                        + DbHelper.COLUMN_DIAS + ", "
                                        + DbHelper.COLUMN_DIAS_TRABAJADOS + ", "
                                        + DbHelper.COLUMN_MES + ", "
                                        + DbHelper.COLUMN_EJERCICIO_FISCAL + ", "
                                        + DbHelper.COLUMN_PERIODICIDAD + ", "
                                        + DbHelper.COLUMN_FECHA_INICIO + ", "
                                        + DbHelper.COLUMN_FECHA_FIN + ", "
                                        + DbHelper.COLUMN_ACTIVO
                                    + ") VALUES ("
                                        + "'" + entities_periodo.getPeriodoId() + "', "
                                        + "'" + entities_periodo.getCodigo() + "', "
                                        + "'" + entities_periodo.getDias() + "', "
                                        + "'" + entities_periodo.getDiasTrabajados() + "', "
                                        + "'" + entities_periodo.getMes() + "', "
                                        + "'" + entities_periodo.getEjercicioFiscal() + "', "
                                        + "'" + entities_periodo.getPeriodicidad() + "', "
                                        + "'" + entities_periodo.getFechaInicio() + "', "
                                        + "'" + entities_periodo.getFechaFin() + "', "
                                        + Integer.parseInt(entities_periodo.getActivo())
                                    + ");";

                db.execSQL(insert);

            } else {

                String update = "UPDATE " + DbHelper.TABLE_PERIODO
                                    + " SET " + DbHelper.COLUMN_CODIGO + " = '" + entities_periodo.getCodigo() + "', "
                                            + DbHelper.COLUMN_DIAS + " = '" + entities_periodo.getDias() + "', "
                                            + DbHelper.COLUMN_DIAS_TRABAJADOS + " = '" + entities_periodo.getDiasTrabajados() + "', "
                                            + DbHelper.COLUMN_MES + " = '" + entities_periodo.getMes() + "', "
                                            + DbHelper.COLUMN_EJERCICIO_FISCAL + " = '" + entities_periodo.getEjercicioFiscal() + "', "
                                            + DbHelper.COLUMN_PERIODICIDAD + " = '" + entities_periodo.getPeriodicidad() + "', "
                                            + DbHelper.COLUMN_FECHA_INICIO + " = '" + entities_periodo.getFechaInicio() + "', "
                                            + DbHelper.COLUMN_FECHA_FIN + " = '" + entities_periodo.getFechaFin() + "', "
                                            + DbHelper.COLUMN_ACTIVO + " = " + Integer.parseInt(entities_periodo.getActivo())
                                    + " WHERE " + DbHelper.COLUMN_ID + " = '" + entities_periodo.getPeriodoId() + "';";

                db.execSQL(update);

            }

        }
        db.close();
    }

    public static void Insert_Usuario_Periodo(Context context, Entities_Usuario_Periodo us_pe) {

        db = (new DbHelper(context)).getWritableDatabase();

        String query = "SELECT COUNT(1) FROM " + DbHelper.TABLE_USUARIOS_PERIODOS
                            + " WHERE " + DbHelper.COLUMN_ID_USUARIO + " = '" + us_pe.getIdUsuario() + "'"
                                    + " AND " + DbHelper.COLUMN_ID_PERIODO + " = '" + us_pe.getIdPeriodo() + "';";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){

            int existe = cursor.getInt(0);
            if(existe == 0){
                String insert = "INSERT INTO " + DbHelper.TABLE_USUARIOS_PERIODOS
                                    + "("
                                        + DbHelper.COLUMN_ID_USUARIO + ", "
                                        + DbHelper.COLUMN_ID_PERIODO + ", "
                                        + DbHelper.COLUMN_SIN_VALIDAR
                                    + ") VALUES ("
                                        + "'" + us_pe.getIdUsuario() + "', "
                                        + "'" + us_pe.getIdPeriodo() + "', "
                                        + us_pe.getSin_Validar()
                                    + ");";
                db.execSQL(insert);
            } else {
                String update = "UPDATE " + DbHelper.TABLE_USUARIOS_PERIODOS
                                    + " SET " + DbHelper.COLUMN_SIN_VALIDAR + " = " + us_pe.getSin_Validar()
                                + " WHERE " + DbHelper.COLUMN_ID_USUARIO + " = '" + us_pe.getIdUsuario() + "' "
                                    + " AND " + DbHelper.COLUMN_ID_PERIODO + " = '" + us_pe.getIdPeriodo() + "';";
                db.execSQL(update);
            }
        }
        db.close();
    }

}
