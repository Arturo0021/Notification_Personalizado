package triplei.mx.notification.Data;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Wcf_Service {

    private static final String Location = "http://www.webteam.mx/WCFTeamGruma/";
    private static final String SERVICE_URI = Location + "svcTeamGruma.svc";

    public JSONObject HttpPost(String methodName, StringEntity entity) throws IOException, JSONException {
        JSONObject jsonresult = null;
        HttpPost request = new HttpPost(SERVICE_URI + methodName);

        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        request.setHeader("charset", "UTF-8");

        request.setEntity(entity);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(request);
        HttpEntity responseEntity = response.getEntity();

        BufferedReader reader1 = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));

        StringBuilder builder = new StringBuilder();
        for (String line = null; (line = reader1.readLine()) != null;) {
            builder.append(line).append("\n");
        }
        Log.i(methodName, builder.toString());
        jsonresult = new JSONObject(builder.toString());

        return jsonresult;
    }

}
