package utility;

/**
 * Created by SAI on 4/8/2016.
 */
public class ServerResponse {
    String responseString, responseCode;

    public ServerResponse(String responseString, String responseCode) {
        this.responseString = responseString;
        this.responseCode = responseCode;
    }

    public String getResponseString() {
        return responseString;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
}
