package subject;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;

public class LoudSubject<T> {
    
    private HashMap<String, T> content;
    private PropertyChangeSupport support;

    public LoudSubject() {
        content = new HashMap<>();
        support = new PropertyChangeSupport(this);
    }

    public void addObserver(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
        System.out.println(this + ": added observer " + listener);
    }

    public void removeObserver(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
        System.out.println(this + ": removed observer " + listener);
    }

    public void setValue(String key, T value) {
        if (key == null || value == null) {
            System.out.println(this + ": received null");
        } else {

            // no need to check if old and new values are the same, support already handles that
            support.firePropertyChange(key, content.get(key), value);
            
            content.put(key, value);
        }
    }

    public T getContent(String key) {
        return content.get(key);
    }

    protected HashMap<String, T> getContent() {
        return content;
    }

    protected void setContent(HashMap<String, T> content) {
        this.content = content;
    }

    protected PropertyChangeSupport getSupport() {
        return support;
    }

    protected void setSupport(PropertyChangeSupport support) {
        this.support = support;
    }

}