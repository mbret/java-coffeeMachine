
package miage.machine;

/**
 * A coffeeMachineException is a intern and a NON CRITICAL exception which does not need to stop the program.
 * The various methods can use this exception to push an error
 * Important: A CoffeeMachineException can be displayed to the user
 * @author maxime
 */
public class CoffeeMachineException extends Exception{
    public CoffeeMachineException(String message) {
        super(message);
    }
}
