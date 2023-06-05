package config;

import office.Shed;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

public class ConfigReader {
    public static <T> T readConfig(String filename, Class<T> clazz) {
        Constructor constructor = new Constructor(clazz, new LoaderOptions());
        Yaml yaml = new Yaml(constructor);
        InputStream inputStream = Shed.class.getClassLoader().getResourceAsStream(filename);
        return yaml.load(inputStream);
    }
}
