package exception;

public class RegisteredIngredientException extends RuntimeException{
    public RegisteredIngredientException(String message) {
        super(message);
    }
}
