package office;

import config.ConfigReader;

import java.io.IOException;
import java.util.List;

import static config.Common.definedExitCode;

/***
 * Used by YAML reader to create instance. Suppressing 'unused' warnings for setters.
 */
public class Tools {
    private static final String FILE_NAME = "inventory.yaml";
    private static final String TOOL_CODE_EXIT_CONFLICT_MESSAGE = "cannot use %s as a tool code, conflicts with exit code";
    private List<Tool> tools;
    private static Tools toolsInstance;

    public List<Tool> getTools() {
        return tools;
    }

    private static Tools loadTools() throws IOException {
        if (toolsInstance == null) {
            toolsInstance = ConfigReader.readConfig(Tools.FILE_NAME, Tools.class);
            // quick validation that 'exit' is never added as a tool code in the future
            toolsInstance.getTools().forEach(t -> {
                if (t.getCode().equals(definedExitCode)) {
                    throw new RuntimeException(String.format(TOOL_CODE_EXIT_CONFLICT_MESSAGE, definedExitCode));
                }
            });
        }
        return toolsInstance;
    }

    public static Tool getTool(String code) throws IllegalArgumentException, IOException {
        for (Tool tool : loadTools().tools) {
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

    public static class Tool {
        private String code;
        private String type;
        private String brand;

        public String getCode() {
            return code;
        }

        @SuppressWarnings("unused")
        public void setCode(String code) {
            this.code = code;
        }

        public String getType() {
            return type;
        }

        @SuppressWarnings("unused")
        public void setType(String type) {
            this.type = type;
        }

        public String getBrand() {
            return brand;
        }

        @SuppressWarnings("unused")
        public void setBrand(String brand) {
            this.brand = brand;
        }

        @Override
        public String toString() {
            return "Tool{" + "code='" + code + '\'' + ", type='" + type + '\'' + ", brand='" + brand + '\'' + '}';
        }
    }
}