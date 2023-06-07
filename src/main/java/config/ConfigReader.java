package config;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileInputStream;
import java.io.IOException;

public class ConfigReader {
    public static <T> T readConfig(String filename, Class<T> clazz) throws IOException {
        try (FileInputStream inputStream = new FileInputStream("src/main/resources/" + filename)){
            Constructor constructor = new Constructor(clazz, new LoaderOptions());
            Yaml yaml = new Yaml(constructor);
            return yaml.load(inputStream);
        }
    }
}
