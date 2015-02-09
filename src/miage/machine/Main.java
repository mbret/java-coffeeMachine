
package miage.machine;

import miage.models.Drink;
import miage.models.DrinkDAO;
import miage.models.Ingredient;
import miage.models.IngredientDAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;
import miage.models.DAO;

/**
 *
 * @author maxime
 */
public abstract class Main{
    
    protected static IngredientDAO ingredientDAO;
    protected static DrinkDAO drinkDAO;
    public static Properties properties;
    public final static HashMap<String, String> errorMessages = new HashMap(){{
        put("errorDB", "Sorry but the database seems to be unavailable or an unexpected error appeared!");
    }};
            
    
    
    /**
     * Must be call at the start of the program
     * @throws Exception 
     */
    public static void bootstrap() throws Exception{
        // load properties + exit program if the loading throw an error
        try{
            properties = PropertiesManager.getProperties();
        }
        catch(Exception e){
            e.printStackTrace();
            System.exit(0);
        }
        // init DAO and DAOconenxion
        DAO.setConnection(Connexion.getInstance());
        ingredientDAO = new IngredientDAO();
        drinkDAO = new DrinkDAO(ingredientDAO);
    }
    
    
    
    /**
     * Check if there are enough ingredient according to the given drink
     * @param drink
     * @return 
     * @throws CoffeeMachineException
     */
    public static boolean areEnoughIngredients(Drink drink) throws CoffeeMachineException{
        boolean enough = true;
        ArrayList<Ingredient> ingredients = ingredientDAO.fetchAll();
        for (Ingredient ingredient : ingredients) {
            if(ingredient.getName().equals("coffee") && ingredient.getQte() < drink.getCoffee()){
                enough = false;
                break;
            }
            if(ingredient.getName().equals("tea") && ingredient.getQte() < drink.getTea()){
                enough = false;
                break;
            }
            if(ingredient.getName().equals("milk") && ingredient.getQte() < drink.getMilk()){
                enough = false;
                break;
            }
            if(ingredient.getName().equals("chocolate") && ingredient.getQte() < drink.getChocolate()){
                enough = false;
                break;
            }
        }
        return enough;
    }
    
    /**
     * Check if there are enough sugar according to the given quantity
     * @param askedQuatity
     * @return 
     */
    public static boolean isEnoughSugar(int askedQuatity) throws CoffeeMachineException{
        Ingredient sugar = ingredientDAO.find("sugar");
        return (askedQuatity <= sugar.getQte());
    }
    
    
    public static void debug(Exception e, Logger logger){
        if(properties.getProperty("environment").equals("testing") || properties.getProperty("environment").equals("development")){
            logger.severe(e.toString());
        }
        if(properties.getProperty("environment").equals("development")){
            e.printStackTrace();
        }
    }
    
    public static void logInfo(Logger logger, String message){
        if(properties.getProperty("environment").equals("testing") || properties.getProperty("environment").equals("development")){
            logger.info(message);
        }
    }
    
    
    
}
