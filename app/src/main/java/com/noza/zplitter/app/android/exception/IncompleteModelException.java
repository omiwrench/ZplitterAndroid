package com.noza.zplitter.app.android.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omiwrench on 2016-02-17.
 */
public class IncompleteModelException extends RuntimeException{
    private final String className;
    private List<String> missingArgs = new ArrayList<>();
    public IncompleteModelException(String className){
        this.className = className;
    }
    public IncompleteModelException(String className, String missingArg){
        super();
        this.className = className;
        missingArgs.add(missingArg);
    }
    public IncompleteModelException(String className, List<String> missingArgs){
        super();
        this.className = className;
        missingArgs.addAll(missingArgs);
    }

    @Override
    public String getMessage(){
        StringBuilder message = new StringBuilder();
        message.append("Error when instansiating immutable class " + className + ", missing arguments: ");
        for(String missing : missingArgs){
            message.append(missing + ", ");
        }
        return message.toString();
    }
    public void addArgument(String argument){
        missingArgs.add(argument);
    }
    public int getNumArguments(){
        return missingArgs.size();
    }
}
