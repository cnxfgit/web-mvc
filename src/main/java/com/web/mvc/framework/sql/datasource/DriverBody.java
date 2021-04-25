package com.web.mvc.framework.sql.datasource;

public class DriverBody {

    private String driverClass;
    private String username;
    private String password;
    private String url;

    private int maxConnection = 20;
    private int minConnection = 5;
    private int networkTimeout = 1000;

    public DriverBody(){
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getMaxConnection() {
        return maxConnection;
    }

    public void setMaxConnection(int maxConnection) {
        if (maxConnection > 30) this.maxConnection = 30;
        else if (maxConnection < 5) this.maxConnection = 5;
        else this.maxConnection = maxConnection;
    }

    public int getMinConnection() {
        return minConnection;
    }

    public void setMinConnection(int minConnection) {
        if (minConnection > 30) this.minConnection = 30;
        else if (minConnection < 5) this.minConnection = 5;
        else this.minConnection = minConnection;
    }

    public int getNetworkTimeout() {

        return networkTimeout;
    }

    public void setNetworkTimeout(int networkTimeout) {
        if (networkTimeout > 10 * 1000) this.networkTimeout = 10 * 1000;
        else if (networkTimeout < 1000) this.networkTimeout = 1000;
        else this.networkTimeout = networkTimeout;
    }
}
