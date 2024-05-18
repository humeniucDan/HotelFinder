package HotelFinder.hotelFinder.DBConnection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

public class MSDataBaseCredentials {
    public static final String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static String connectionURL;
    private static final MSDataBaseCredentials dbCred = new MSDataBaseCredentials();

    private MSDataBaseCredentials() {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = null;
        try {
            is = MSDataBaseCredentials.class.getClassLoader().getResourceAsStream("config.database/credentials.conf");

            JsonNode jsonNode = mapper.readTree(is);


            connectionURL =
                    jsonNode.get("url").asText() + ";" +
                            "Database=" + jsonNode.get("database").asText() + ";" +
                            "User=" + jsonNode.get("user").asText() + ";" +
                            "Password=" + jsonNode.get("pass").asText() + ";" +
                            "Trusted_Connection=" + jsonNode.get("trustedConnection").asText() + ";" +
                            "encrypt=" + jsonNode.get("encrypt").asText() + ";" +
                            "trustServerCertificate=" + jsonNode.get("trustServerCertificate").asText() + ";" +
                            "authenticationScheme=" + jsonNode.get("authenticationScheme").asText() + ";"
            ;

            System.out.println(connectionURL);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static MSDataBaseCredentials getConfiguration(){
        return dbCred;
    }

    String getConnUrl(){
        return connectionURL;
    }

    String getDriver(){
        return driver;
    }
}
