package data;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
    private Properties props = new Properties();

    public ConfigLoader() {
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            props.load(fis);
        } catch (IOException e) {
            System.err.println("No se encontró config.properties en la raíz.");
        }
    }

    public String get(String key) { return props.getProperty(key); }
    public int getInt(String key) { return Integer.parseInt(props.getProperty(key, "0")); }
}