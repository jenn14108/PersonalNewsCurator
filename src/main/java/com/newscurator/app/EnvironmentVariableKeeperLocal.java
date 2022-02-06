package com.newscurator.app;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvironmentVariableKeeperLocal {

    private static EnvironmentVariableKeeperLocal instance;

    private Dotenv dotenv;

    private EnvironmentVariableKeeperLocal() {
        dotenv = Dotenv.load();
    }

    public static EnvironmentVariableKeeperLocal getInstance(){
        if (instance == null){
            instance = new EnvironmentVariableKeeperLocal();
        }
        return instance;
    }

    public String getVariable(String variableName){
        return dotenv.get(variableName);
    }
}
