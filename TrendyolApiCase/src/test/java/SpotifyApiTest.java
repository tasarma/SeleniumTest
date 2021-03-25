import com.google.common.io.Resources;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;


import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;

public class SpotifyApiTest {
    String userId = "";
    String playlistId = "";
    String tracks = "";
    String token = "BQBsfxsGcsCH6g9rV_IGsT6zO7GoZiWsB_LgnGJtG9_4n1rFR5iGPO6cbR1yZzxBhQyaR9nXwdKD3fVmNDtwgz6VTDb5VdK-b75Pwa6_UBSR331J1XzckaUJ6pzWsMTjFcyWwfrhGZq6poYeYFR75ye03VZfX5KlA1x5TMDK_-kMYXPdRs6wjz8zVmyPQTTJtd-b_ra4qim-f4uj9avg5moIUHSNVSq1wRQNatwFI7yUJgM3MGmIKAEDQuIdbiwyB35Ohre2VyoTwU3r8dTVzFAj4OFlXyp970H2idOv";


    @BeforeMethod
    public void beforeTest() throws IOException {
        RestAssured.baseURI = "https://api.spotify.com/v1";
    }
    @Test
    public void spotifyTest() throws IOException {
        String trackName = "Beat it";
        String newName = "Update Name";
        getUserId();
        createNewPlaylist();
        String TrackUriName = trackUri(trackName);
        addItemsToPlaylist(TrackUriName);
    }

    public void getUserId() {
        Response response =
                given()
                        .contentType("application/json; charset=UTF-8")
                        .header("Authorization", "Bearer " + token)
                        .when()
                        .get("/me")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();
        userId = response.getBody().jsonPath().getString("id");

    }
    public void createNewPlaylist() throws IOException {
        URL file = Resources.getResource("playlist.json");
        String myJson = Resources.toString(file, Charset.defaultCharset());
        JSONObject json = new JSONObject(myJson);
        Response playlistResponse =
                given()
                        .contentType("application/json; charset=UTF-8")
                        .header("Authorization", "Bearer " + token)
                        .body(json.toString())
                        .when()
                        .post("/users/{userId}/playlists",userId)
                        .then()
                        .statusCode(201)
                        .extract()
                        .response();
        playlistId = playlistResponse.getBody().jsonPath().getString("id");
    }


    public String trackUri(String trackName){
        Response trackUriResponse =
                given()
                        .contentType("application/json; charset=UTF-8")
                        .header("Authorization", "Bearer " + token)
                        .queryParam("q",trackName )
                        .queryParam("type", "track")
                        .queryParam("market", "US")
                        .queryParam("limit","1")
                        .when()
                        .get("search")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();
        ArrayList arrayList = trackUriResponse.path("tracks.items.uri");
        return arrayList.get(0).toString();
    }

    public void addItemsToPlaylist(String trackUri){
        given()
                .contentType("application/json; charset=UTF-8")
                .header("Authorization", "Bearer " + token)
                .queryParam("playlist_id",playlistId)
                .queryParam("uris",trackUri)
                .when()
                .post("playlists/{playlist_id}/tracks",playlistId)
                .then()
                .statusCode(201);
    }

}
