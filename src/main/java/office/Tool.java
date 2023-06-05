package office;

public class Tool {
    private String code;
    private String type;
    private String brand;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return "Tool{" + "code='" + code + '\'' + ", type='" + type + '\'' + ", brand='" + brand + '\'' + '}';
    }
}
