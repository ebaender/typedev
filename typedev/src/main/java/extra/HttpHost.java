package extra;

public enum HttpHost {

    PI("a3dfe915bae2.ngrok.io");

    private final String url;

    HttpHost(final String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "https://" + url + "/";
    }

}