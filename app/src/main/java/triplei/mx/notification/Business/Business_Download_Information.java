package triplei.mx.notification.Business;

import android.content.Context;

import triplei.mx.notification.Data.Data_Download_Information;

public class Business_Download_Information {

    public static void Download_Usuario(Context context, String Token) throws Exception {

        if(Token == null && Token.equals("")){
            throw new Exception("Token No Referenciado");
        }

        Data_Download_Information.Download_Usuario(context, Token);

    }

}
