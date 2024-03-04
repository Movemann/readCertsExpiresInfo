import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class GitHubFileDownloader {

    public static void main(String[] args) {
        String token = "tu_token_personal";
        String owner = "tu_usuario";
        String repo = "certificates";

        HttpClient client = HttpClient.newHttpClient();

        List<String> files = getFilesInRepository(client, token, owner, repo);

        for (String file : files) {
            downloadFile(client, token, owner, repo, file);
        }
    }

    private static List<String> getFilesInRepository(HttpClient client, String token, String owner, String repo) {
        List<String> files = new ArrayList<>();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://github.enterprise.com/api/v3/repos/" + owner + "/" + repo + "/contents"))
                .header("Authorization", "token " + token)
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONArray jsonArray = new JSONArray(response.body());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                files.add(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return files;
    }

    private static void downloadFile(HttpClient client, String token, String owner, String repo, String fileName) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://github.enterprise.com/api/v3/repos/" + owner + "/" + repo + "/contents/" + fileName))
                .header("Authorization", "token " + token)
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonObject = new JSONObject(response.body());
            String downloadUrl = jsonObject.getString("download_url");

            HttpRequest downloadRequest = HttpRequest.newBuilder()
                    .uri(URI.create(downloadUrl))
                    .header("Authorization", "token " + token)
                    .build();

            HttpResponse<byte[]> downloadResponse = client.send(downloadRequest, HttpResponse.BodyHandlers.ofByteArray());
            byte[] fileContents = downloadResponse.body();
            // Aquí puedes guardar el contenido del archivo como desees
            System.out.println("Archivo descargado: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
