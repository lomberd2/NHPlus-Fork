package de.hitec.nhplus.model;

public class User {
    private long id;
    private String name;
    private String description;
    private String hashedPw;
    private String hashedMasterPwKey;

    public User(String name, String description, String hashedPw, String hashedMasterPwKey) {
        this.name = name;
        this.description = description;
        this.hashedPw = hashedPw;
        this.hashedMasterPwKey = hashedMasterPwKey;
    }

    // Getter und Setter Methoden
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHashedPw() {
        return hashedPw;
    }

    public void setHashedPw(String hashedPw) {
        this.hashedPw = hashedPw;
    }

    public String getHashedMasterPwKey() {
        return hashedMasterPwKey;
    }

    public void setHashedMasterPwKey(String hashedMasterPwKey) {
        this.hashedMasterPwKey = hashedMasterPwKey;
    }
}
