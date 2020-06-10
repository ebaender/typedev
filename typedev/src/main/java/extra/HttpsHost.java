package extra;

public enum HttpsHost {

    PI("2550a6e5.ngrok.io");

    private final String url;

    HttpsHost(final String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "https://" + url + "/";
    }

}