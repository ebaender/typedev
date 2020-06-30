public static JsonObject get(URL host) {
    try {
        InputStream input = host.openStream();
        Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
        return new Gson().fromJson(reader, JsonObject.class);
    } catch (IOException e) {
        latestException = e;
        return null;
    }
}