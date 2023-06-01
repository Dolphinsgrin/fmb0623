package shed;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.List;

public class Shed {
    private List<Tool> shed;

    public List<Tool> getShed() {
        return shed;
    }

    public void setShed(List<Tool> shed) {
        this.shed = shed;
    }

    public static Shed InitShed() {
        String inventoryFileName = "inventory.yaml";
        Constructor constructor = new Constructor(Shed.class, new LoaderOptions());
        Yaml yaml = new Yaml(constructor);
        InputStream inputStream = Shed.class.getClassLoader().getResourceAsStream(inventoryFileName);
        return yaml.load(inputStream);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Tool tool : shed) {
            sb.append(tool);
        }
        return sb.toString();
    }
}