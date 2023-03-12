package antifraud;

public class GetMappingResult {

    int id;
    String name;
    String username;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public GetMappingResult(int id, String name, String username) {
        this.id = id;
        this.name = name;
        this.username = username;
    }
}
