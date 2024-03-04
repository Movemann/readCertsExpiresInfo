import javax.net.ssl.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.cert.X509Certificate;

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

        // Descargar archivos del repositorio
        String[] files = {"archivo1.pem", "archivo2.crt"};

        for (String file : files) {
            try {
                downloadFile(baseUrl, token, owner, repo, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void downloadFile(String baseUrl, String token, String owner, String repo, String fileName) throws IOException {
        String apiUrl = baseUrl + owner + "/" + repo + "/contents/" + fileName + "?access_token=" + token;
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String downloadUrl = connection.getHeaderField("Location");
            downloadFileFromUrl(downloadUrl, fileName);
            System.out.println("Archivo descargado: " + fileName);
        } else {
            System.out.println("Error al descargar el archivo: " + fileName);
            System.out.println("Código de respuesta: " + responseCode);
        }
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
