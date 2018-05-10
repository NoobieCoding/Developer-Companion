package corp.tuter.developerscompanionapp.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Project implements Serializable{

    private String name, description;
    public List<Node> frameworks, libraries, tools;

    public  Project() {
    }

    public Project(String name, String description) {
        frameworks = new ArrayList<>();
        libraries = new ArrayList<>();
        tools = new ArrayList<>();
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
