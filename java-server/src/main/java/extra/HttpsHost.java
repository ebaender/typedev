package extra;

public enum HttpsHost {

    PI("2550a6e5.ngrok.io"),
    LOCAL("localhost:8080/java-server");

    private final String url;

    HttpsHost(final String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "https://" + url + "/";
    }

}