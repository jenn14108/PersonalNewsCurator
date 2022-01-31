package com.newscurator.app;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvironmentVariableKeeper {

    private static EnvironmentVariableKeeper instance;

    private Dotenv dotenv;

    private EnvironmentVariableKeeper() {
        dotenv = Dotenv.load();
    }

    public static EnvironmentVariableKeeper getInstance(){
        if (instance == null){
            instance = new EnvironmentVariableKeeper();
        }
        return instance;
    }

    public String getVariable(String variableName){
        return dotenv.get(variableName);
    }
}
