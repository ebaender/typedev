package session;

public class EmptyLanguageException extends Exception {

    private static final long serialVersionUID = 1L;
    private String language;

    public EmptyLanguageException(String language) {
        super();
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }
    
}