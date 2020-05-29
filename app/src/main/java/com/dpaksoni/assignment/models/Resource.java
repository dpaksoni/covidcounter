package com.dpaksoni.assignment.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Resource<T> {
    @NonNull
    private DataStatus status;

    @Nullable
    private T data;

    @Nullable
    private String error;

    public Resource() {
        this.status = DataStatus.CREATED;
        this.data = null;
        this.error = null;
    }

    public Resource<T> loading() {
        this.status = DataStatus.LOADING;
        this.data = null;
        this.error = null;
        return this;
    }

    public Resource<T> loading(T data) {
        this.status = DataStatus.LOADING;
        this.data = data;
        this.error = null;
        return this;
    }

    public Resource<T> success(@NonNull T data) {
        this.status = DataStatus.SUCCESS;
        this.data = data;
        this.error=null;
        return this;
    }

    public Resource<T> error(@NonNull String error) {
        this.status = DataStatus.ERROR;
        this.error = error;
        this.data = null;
        return this;
    }

    /*public static <T> Resource<T> error(String error, T data) {
        Resource<T> tmp = new Resource<>();
        tmp.error = error;
        tmp.data = data;
        return tmp;
    }
*/
    public Resource<T> error(@NonNull String error, T data) {
        this.status = DataStatus.ERROR;
        this.error = error;
        this.data = data;
        return this;
    }

    public Resource<T> complete() {
        this.status = DataStatus.COMPLETE;
        return this;
    }

    @NonNull
    public DataStatus getStatus() {
        return status;
    }

    @Nullable
    public T getData() {
        return data;
    }

    @Nullable
    public String getError() {
        return error;
    }

    public enum DataStatus {
        CREATED,
        SUCCESS,
        ERROR,
        LOADING,
        COMPLETE
    }

    @NonNull
    @Override
    public String toString() {
        return "Resournce: status: "+status+"\n data: "+data+"\n error: "+error;
    }
}
