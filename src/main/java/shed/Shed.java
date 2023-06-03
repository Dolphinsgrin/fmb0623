package shed;

import java.util.List;

public class Shed {
    public static final String FILE_NAME = "inventory.yaml";
    private List<Tool> shed;

    public List<Tool> getTools() {
        return shed;
    }

    public Tool getTool(String code) throws IllegalArgumentException {
        for (Tool tool : shed) {
            if (tool.getCode().equals(code)) {
                return tool;
            }
        }
        throw new IllegalArgumentException(String.format("unknown tool code [%s]", code));
    }

    public void setShed(List<Tool> shed) {
        this.shed = shed;
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