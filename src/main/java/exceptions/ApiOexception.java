package exceptions;

public class ApiOexception extends RuntimeException {
    private final int statusCode;

    public ApiOexception (int statusCode, String msg){
        super(msg); //see explanation below
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }


}
