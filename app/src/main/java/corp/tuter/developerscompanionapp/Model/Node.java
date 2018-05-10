package corp.tuter.developerscompanionapp.Model;

import java.io.Serializable;

public class Node implements Serializable{
    private String name, imageName, description, path;

    public Node() {
    }

    public Node(String name, String imageName, String description, String path) {
        this.name = name;
        this.imageName = imageName;
        this.description = description;
        this.path = path;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageName() {
        return imageName;
    }

    public String getPath() {
        return path;
    }
}
