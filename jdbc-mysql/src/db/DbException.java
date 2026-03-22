package db;

public class DbException extends RuntimeException{
    DbException(String message){
        super(message);
    }
}
