package Model.Entities;

public class WarningOrError {
    private String type;
    private String locate;
    private String identificator;
    private String description;
    private String detail;

    public WarningOrError() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocate() {
        return locate;
    }

    public void setLocate(String locate) {
        this.locate = locate;
    }

    public String getIdentificator() {
        return identificator;
    }

    public void setIdentificator(String identificator) {
        this.identificator = identificator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
    
    
}
