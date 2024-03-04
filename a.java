import javax.net.ssl.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.cert.X509Certificate;
import org.json.*;

public class GitHubFileDownloader {

    public static void main(String[] args) {
        String token = "tu_token_personal";
        String owner = "tu_usuario";
        String repo = "certificates";
        String baseUrl = "https://github.enterprise.com/api/v3/repos/";

        // Configurar SSL para confiar en todos los certificados
        try {
            trustAllCertificates();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Obtener lista de archivos en el repositorio
        JSONArray files = getFilesList(baseUrl, token, owner, repo);

        // Descargar archivos del repositorio
        for (int i = 0; i < files.length(); i++) {
            try {
                JSONObject fileObj = files.getJSONObject(i);
                String fileName = fileObj.getString("name");
                String downloadUrl = fileObj.getString("download_url");
                downloadFileFromUrl(downloadUrl, fileName);
                System.out.println("Archivo descargado: " + fileName);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static JSONArray getFilesList(String baseUrl, String token, String owner, String repo) {
        String apiUrl = baseUrl + owner + "/" + repo + "/contents?access_token=" + token;
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String response = new String(connection.getInputStream());
                return new JSONArray(response);
            } else {
                System.out.println("Error al obtener la lista de archivos del repositorio");
                System.out.println("Código de respuesta: " + responseCode);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    private static void downloadFileFromUrl(String downloadUrl, String fileName) throws IOException {
        URL url = new URL(downloadUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            Files.copy(connection.getInputStream(), Path.of(fileName));
        } else {
            System.out.println("Error al descargar el archivo " + fileName);
            System.out.println("Código de respuesta: " + responseCode);
        }
    }

    private static void trustAllCertificates() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Bypass hostname verification
        HostnameVerifier allHostsValid = (hostname, session) -> true;
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }
}
