package bot.data;

import bot.data.Departure;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;

public class Curl {

    protected Optional<ArrayList<Departure>> getDepartureInformation(int platformID) {
        String json;
        try {
            json = getRawInfoAboutDepartures(platformID);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Type targetClassType = new TypeToken<ArrayList<Departure>>(){}.getType();

            ArrayList<Departure> targetArray = gsonBuilder.create().fromJson(json, targetClassType);
            return Optional.of(targetArray);
        }
        catch (Exception e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    protected String getRawInfoAboutDepartures(int platformId) throws Exception{
        int maxTries = 1;
        String url = "https://tw.waw.pl/wp-admin/admin-ajax.php";
        String urlParameters = "action=sip_get_llegadas_parada&id="+String.valueOf(platformId);
        URL obj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");

        for(int i =0; i<maxTries;i++) {
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = conn.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String result = response.toString();
            if (result != "Strona nie została wyświetlona z powodu istnienia konfliktu.") {
                return result;
            }
        }
        return "Error.";
    }
}
