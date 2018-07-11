package com.tiger.arch.sample.vo;


public class Resource<T> {

   public final Status status;
   public final String message;
   public final T data;

    public Resource(Status status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }


    public static <T> Resource<T> onSuccess(T data) {
        return new Resource<>(Status.SUCCESS, "", data);
    }

    public static <T> Resource<T> onLoading(T data) {
        return new Resource<>(Status.LOADING, "", data);
    }

    public static <T> Resource<T> onError(String msg,T data) {
        return new Resource<>(Status.ERROR, msg, data);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj==null||getClass()!=obj.getClass()) {
            return false;
        }

        Resource<?> resource = (Resource<?>) obj;
        if (status != resource.status) {
            return false;
        }

        if(message !=null?!message.equals(resource.message): resource.message !=null) {
            return false;
        }
        return data != null ? data.equals(resource.data) : resource.data == null;
    }

    @Override
    public int hashCode() {
        int hashCode= status.hashCode();
        hashCode = hashCode * 31 + (message != null ? message.hashCode() : 0);
        hashCode = hashCode * 31 + (data != null ? data.hashCode() : 0);
        return hashCode;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
