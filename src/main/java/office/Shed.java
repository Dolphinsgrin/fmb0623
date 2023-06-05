package office;

import config.ConfigReader;

import java.util.List;

import static config.Common.definedExitCode;

public class Shed {
    private static final String FILE_NAME = "inventory.yaml";
    private static final String TOOL_CODE_EXIT_CONFLICT_MESSAGE = "cannot use %s as a tool code, conflicts with exit code";
    private List<Tool> shed;

    public List<Tool> getTools() {
        return shed;
    }

    public static Shed loadShed() {
        Shed shed = ConfigReader.readConfig(Shed.FILE_NAME, Shed.class);
        // quick validation that 'exit' is never added as a tool code in the future
        shed.getTools().forEach(t -> {
            if (t.getCode().equals(definedExitCode)) {
                throw new RuntimeException(String.format(TOOL_CODE_EXIT_CONFLICT_MESSAGE, definedExitCode));
            }
        });
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