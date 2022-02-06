package com.newscurator.app;

public class EnvironmentVariableKeeper {

    private static EnvironmentVariableKeeper instance;

    private EnvironmentVariableKeeper() {
    }

    public static EnvironmentVariableKeeper getInstance(){
        if (instance == null){
            instance = new EnvironmentVariableKeeper();
        }
        return instance;
    }

    public String getVariable(String variableName){
        return System.getenv(variableName);
    }
}
