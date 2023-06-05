package office;

import config.ConfigReader;

import java.util.List;

import static config.Common.definedExitCode;

/***
 * Used by YAML reader to create instance. Suppressing 'unused' warnings for setters.
 */
public class Shed {
    private static final String FILE_NAME = "inventory.yaml";
    private static final String TOOL_CODE_EXIT_CONFLICT_MESSAGE = "cannot use %s as a tool code, conflicts with exit code";
    private List<Tool> tools;
    private static Shed shed;

    public List<Tool> getTools() {
        return tools;
    }

    private static Shed loadShed() {
        if (shed == null) {
            shed = ConfigReader.readConfig(Shed.FILE_NAME, Shed.class);
            // quick validation that 'exit' is never added as a tool code in the future
            shed.getTools().forEach(t -> {
                if (t.getCode().equals(definedExitCode)) {
                    throw new RuntimeException(String.format(TOOL_CODE_EXIT_CONFLICT_MESSAGE, definedExitCode));
                }
            });
        }
        return shed;
    }

    public static Tool getTool(String code) throws IllegalArgumentException {
        for (Tool tool : loadShed().tools) {
            if (tool.getCode().equals(code)) {
                return tool;
            }
        }
        throw new IllegalArgumentException(String.format("unknown tool code [%s]", code));
    }

    @SuppressWarnings("unused")
    public void setTools(List<Tool> tools) {
        this.tools = tools;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Tool tool : tools) {
            sb.append(tool);
        }
        return sb.toString();
    }
}