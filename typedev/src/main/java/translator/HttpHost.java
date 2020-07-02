package translator;

public enum HttpHost {

    PI("https://a3dfe915bae2.ngrok.io/", "http://192.168.0.22:1880/");

    private final String LOCAL_URL;
    private final String EXTERNAL_URL;

    HttpHost(final String LOCAL_URL, final String EXTERNAL_URL) {
        this.LOCAL_URL = LOCAL_URL;
        this.EXTERNAL_URL = EXTERNAL_URL;
    }

    @Override
    public String toString() {
        return EXTERNAL_URL;
    }

    public String toLocalString() {
        return LOCAL_URL;
    }

}