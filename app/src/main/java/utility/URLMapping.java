package utility;

/**
 * Created by SAI on 4/4/2016.
 */
public class URLMapping {
    private String []paramNames;
    private String url;

    public URLMapping(String[] paramNames, String url) {
        this.paramNames = paramNames;
        this.url = url;
    }

    public String[] getParamNames() {
        return paramNames;
    }

    public void setParamNames(String[] paramNames) {
        this.paramNames = paramNames;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
