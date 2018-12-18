package triplei.mx.notification.Data;

import android.content.Context;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;

import triplei.mx.notification.Entities.Entities_Periodo;
import triplei.mx.notification.Entities.Entities_Usuario;
import triplei.mx.notification.Entities.Entities_Usuario_Periodo;
import triplei.mx.notification.Utilerias.Utilerias_Activity;

public class Data_Download_Information {

    private static String METHOD_NAME = "";
    private static StringEntity strEntity = null;
    private static JSONObject jsonResult = null;
    private static JSONArray jsonArray = null;
    private static JSONStringer jsonString = null;
    private static String strResult = null;

    public static void Download_Usuario(Context context, String Token) throws JSONException, IOException {

        METHOD_NAME = "/GetUsuarios";
        Wcf_Service service = new Wcf_Service();

        jsonString = new JSONStringer()
                    .object()
                        .key("token").value(Token)
                    .endObject();

        strEntity = new StringEntity(jsonString.toString());
        jsonResult = service.HttpPost(METHOD_NAME, strEntity);
        jsonArray = jsonResult.getJSONArray("GetUsuariosResult");

        for (int i = 0; i < jsonArray.length(); ++i) {

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Entities_Usuario usuario = new Entities_Usuario();

                usuario.setUsuarioId(jsonObject.getString("UsuarioId").toLowerCase());
                usuario.setUsuario(jsonObject.getString("UserName"));
                usuario.setNombre(jsonObject.getString("Nombre"));
                usuario.setApellidoPaterno(jsonObject.getString("ApellidoPaterno"));
                usuario.setApellidoMaterno(jsonObject.getString("ApellidoMaterno"));
                usuario.setTelefono(jsonObject.getString("Telefono"));
                usuario.setEmail(jsonObject.getString("Email"));
                usuario.setActivo(jsonObject.getString("Activo") == "true" ? "1" : "0");

            JSONObject json = jsonObject.getJSONObject("UsuarioRol");
            JSONObject jsonRol = json.getJSONObject("Rol");

                usuario.setNombreRol(jsonRol.getString("Nombre"));
                usuario.setRolId(jsonRol.getString("RolId"));


            Data_Usuario.Insert_Usuarios(context, usuario);

        }

        Data_Download_Information.Download_Periodos(context, Token);

    }

    public static void Download_Periodos(Context context, String Token) throws JSONException, IOException {

        METHOD_NAME = "/GetPeridos";
        Wcf_Service service = new Wcf_Service();

        jsonString = new JSONStringer()
                .object()
                    .key("token").value(Token)
                .endObject();

        strEntity = new StringEntity(jsonString.toString());
        jsonResult = service.HttpPost(METHOD_NAME, strEntity);
        jsonArray = jsonResult.getJSONArray("GetPeridosResult");

        for (int i = 0; i < jsonArray.length(); ++i) {

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Entities_Periodo periodo = new Entities_Periodo();

            periodo.setPeriodoId(jsonObject.getString("PeriodoId").toLowerCase());
            periodo.setCodigo(jsonObject.getString("Codigo"));
            periodo.setDias(jsonObject.getString("Dias"));
            periodo.setDiasTrabajados(jsonObject.getString("DiasTrabajados"));
            periodo.setMes(jsonObject.getString("Mes"));
            periodo.setEjercicioFiscal(jsonObject.getString("EjercicioFiscal"));
            periodo.setPeriodicidad(jsonObject.getString("Periodicidad"));
            periodo.setFechaInicio(jsonObject.getString("FechaInicioStr"));
            periodo.setFechaFin(jsonObject.getString("FechaFinStr"));
            periodo.setActivo(jsonObject.getString("Activo") == "true" ? "1" : "0");

            Data_Periodo.Insert_Periodo(context, periodo);
        }

        Download_Periodos_x_Usuario(context, Token);
    }

    public static void Download_Periodos_x_Usuario(Context context, String Token) throws JSONException, IOException{

        METHOD_NAME = "/GetReporte";
        Wcf_Service service = new Wcf_Service();

        jsonString = new JSONStringer()
                    .object()
                        .key("token").value(Token)
                        .key("fechaInicio").value("2018-08-01")
                        .key("fechaFin").value(Utilerias_Activity.Get_Date())
                    .endObject();

        strEntity = new StringEntity(jsonString.toString());
        jsonResult = service.HttpPost(METHOD_NAME, strEntity);
        jsonArray = jsonResult.getJSONArray("GetReporteResult");

        for (int i = 0; i < jsonArray.length(); ++i) {

            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Entities_Usuario_Periodo us_pe = new Entities_Usuario_Periodo();
            us_pe.setIdUsuario(jsonObject.getString("UsuarioId"));
            us_pe.setIdPeriodo(jsonObject.getString("PeriodoId"));
            us_pe.setSin_Validar(jsonObject.getInt("SinValidar"));

            Data_Periodo.Insert_Usuario_Periodo(context, us_pe);
        }

    }

}
