package extra;

public enum HttpHost {

    PI("https://a3dfe915bae2.ngrok.io/", "http://192.168.0.22:1880/");

    private static boolean localOverride = false;
    private final String LOCAL_URL;
    private final String EXTERNAL_URL;

    HttpHost(final String LOCAL_URL, final String EXTERNAL_URL) {
        this.LOCAL_URL = LOCAL_URL;
        this.EXTERNAL_URL = EXTERNAL_URL;
    }

    public static void setLocalOverride(boolean enableOverride) {
        localOverride = enableOverride;
    }

    @Override
    public String toString() {
        return localOverride ? LOCAL_URL : EXTERNAL_URL;
    }

}