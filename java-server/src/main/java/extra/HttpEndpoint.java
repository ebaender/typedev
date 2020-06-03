package extra;

public enum HttpEndpoint {

    ROOM_LIVE("raum", null),
    ROOM_WEEK("raum_week", null);


    private final String subdomain;
    private final String paramExample;

    HttpEndpoint(final String subdomain, final String paramExmaple) {
        this.subdomain = subdomain;
        this.paramExample = paramExmaple;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public String getParamExample() {
        return paramExample;
    }

    @Override
    public String toString() {
        return getSubdomain();
    }
}