package io.swagger.client.api;

public class BasePath {

    public BasePath(){}

    private static BasePath ourInstance = new BasePath();
    public static BasePath getInstance() {
        return ourInstance;
    }
    private String basePath = "http://175.207.13.212:8080";

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }
}
