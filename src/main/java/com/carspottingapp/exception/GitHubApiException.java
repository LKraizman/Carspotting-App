package com.carspottingapp.exception;

public class GitHubApiException extends Exception{
    public GitHubApiException(String errorMessage, Throwable err){
        super(errorMessage, err);
    }
}
