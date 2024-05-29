package de.hitec.nhplus.model;

public class CryptoModel {
    private final int id = 1;
    private boolean isDBEncrypted;
    private String testEncrypted;


    public CryptoModel(boolean isDBEncrypted, String testEncrypted) {
        this.isDBEncrypted = isDBEncrypted;
        this.testEncrypted = testEncrypted;
    }

    public int getId() {
        return id;
    }

    public boolean getIsDBEncrypted() {
        return isDBEncrypted;
    }

    public String getTestEncrypted() {
        return testEncrypted;
    }

    public void setIsDBEncrypted(boolean isDBEncrypted) {
        this.isDBEncrypted = isDBEncrypted;
    }

    public void setTestEncrypted(String testEncrypted) {
        this.testEncrypted = testEncrypted;
    }

    @Override
    public String toString() {
        return "CryptoModel{" + "id=" + id + ", isDBEncrypted=" + isDBEncrypted + ", testEncrypted=" + testEncrypted + '}';
    }
}
