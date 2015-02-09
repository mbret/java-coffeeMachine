/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package miage.machine;

import miage.models.Drink;
import miage.models.Ingredient;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 *
 * @author maxime
 */
public class MainConsole extends Main{
    
    final static String designSeparator = "---------------------------\n"
                                        + "| %s\n"
                                        + "---------------------------";
    final static Logger LOGGER = Logger.getLogger(MainConsole.class.getName());
    
    
    
    public static void main(String[] args){
        try{
            bootstrap();
            logInfo(LOGGER, "Coffe machine started");
            run(args);
        }
        catch(Exception e){
            debug(e, LOGGER);
            e.printStackTrace();
            System.out.println("Due to severe problem the program must be stopped!");
        }
        finally{
            logInfo(LOGGER, "Coffee machine ended");
        }
    }
    
    
    
    
    /**
     * @param args the command line arguments
     * @throws CoffeeMachineException
     */
    public static void run(String[] args) throws Exception{
         
        // Useful variables
        boolean over = false, redo;
        Integer value, givenMoney, askedSugar, choice, listSize;
        Drink drink;
        ArrayList<Ingredient> ingredients;
        ArrayList<Drink> drinks;

        /**
         * MAIN LOOP
         */
        while(over == false){
            System.out.println(String.format(designSeparator, "Main menu"));
            System.out.println(   "0 : Buy drink\n"
                                + "1 : Remove a drink\n"
                                + "2 : Add a drink\n"
                                + "3 : Update a drink\n"
                                + "4 : Add a ingredient\n"
                                + "5 : Display drinks\n"
                                + "6 : Display stock\n"
                                + "9 : quit");        

            choice = getInputNumeric("Enter choice", false, 0, 9);

            // Do main choice
            switch(choice){

                /**
                 * Buy drink.
                 * Check if the drink exist then check if there are enough ingredient
                 * then wait for the correct price and then give the drink
                 */
                case 0 :
                    drinks = drinkDAO.fetchAll();
                    listSize = drinks.size();
                    if(listSize == 0){
                        System.out.println("No drinks available! Please add a drink before.");
                    }
                    else{
                        System.out.println(String.format(designSeparator, "Menu: Buy a drink"));
                        for (int i = 0; i < listSize; i++) {
                            System.out.println(String.format("%s : %s, %s€", i, drinks.get(i).getName(), drinks.get(i).getPrice())); // display each ingredient with its index in the list
                        }

                        // Get selected drink (range correspond to the list size) until it exist and it has enough ingredients
                        do{
                            redo = false;
                            choice = getInputNumeric("Which drink wish you buy?", false, 0, listSize-1); // get drink index
                            if( areEnoughIngredients( drinks.get(choice) ) == false){
                                System.out.println("There are not enough ingredient, please choose another drink");
                                redo = true;
                            }
                        }while( redo ); // while not enough ingredients

                        drink = drinks.get(choice);

                        // Get money mechanism
                        givenMoney = 0;
                        do{
                            givenMoney += getInputNumeric("Enter money"); // get price
                            System.out.println(String.format("%s€ provided", givenMoney));
                        }while(drink.getPrice() > givenMoney);

                        // Get number of sugar
                        do{
                            redo = false;
                            askedSugar = getInputNumeric("Enter sugar between 0 and 4", false, Integer.parseInt(properties.getProperty("minSugar")), Integer.parseInt(properties.getProperty("maxSugar"))); // get sugar
                            if( isEnoughSugar(askedSugar) == false ){
                                System.out.println("There is not enough sugar");
                                redo = true;
                            }
                        }while( redo ); // while not enough sugar

                        // Buy drink (get money back and reduce ingredient)
                        int moneyBack = givenMoney - drink.getPrice();
                        try{
                            drinkDAO.buyDrink(drink);
                            System.out.println(String.format("You received your %s and %s€ of money", drink.getName(), moneyBack));
                        }
                        catch(CoffeeMachineException e){
                            LOGGER.severe(e.toString());
                            System.out.println("Unable to buy the drink : " + e.getMessage());
                        }
                    }
                    break;

                // Delete drink
                case 1 :
                    drinks = drinkDAO.fetchAll();
                    listSize = drinks.size();
                    if(listSize == 0){
                        System.out.println("No drinks available! Please add a drink before.");
                    }
                    else{
                        System.out.println(String.format(designSeparator, "Menu: Delete a drink"));
                        for (int i = 0; i < listSize; i++) {
                            System.out.println(String.format("%s : %s", i, drinks.get(i).getName())); // display each ingredient with its index in the list
                        }

                        // Get selected drink (range correspond to the list size)
                        choice = getInputNumeric("Which drink wish you delete?", false, 0, (listSize-1)); // get drink index

                        try{
                            drinkDAO.delete(drinks.get(choice));
                            System.out.println("Drink removed!");
                        }
                        catch(CoffeeMachineException e){
                            LOGGER.severe(e.toString());
                            System.out.println("Unable to delete the drink : " + e.getMessage());
                        }
                    }
                    break;

                // Add drink
                case 2 :
                    if( drinkDAO.count() >= Integer.parseInt(properties.getProperty("nbDrinkMax")) ){
                        System.out.println("Unable to add a new drink, maximum reached");
                    }
                    else{
                        Drink newDrink = new Drink();
                        System.out.println(String.format(designSeparator, "Menu: Add a drink"));

                        // Get name + check if the name is taken
                        String name;
                        do{
                            redo = false;
                            name = getInputAlpha("Enter name:");
                            if( drinkDAO.exist(name) ){
                                System.out.println("A drink with this name already exist! Please choose an other name.");
                                redo = true;
                            }
                        }while( redo );

                        newDrink.setName( name )
                                .setPrice( getInputNumeric("Enter the price"))
                                .setMilk( getInputNumeric("Enter milk quantity:") )
                                .setTea( getInputNumeric("Enter tea quantity:") )
                                .setCoffee( getInputNumeric("Enter coffee quantity:") )
                                .setChocolate( getInputNumeric("Enter chocolate quantity:") );
                        try{
                            drinkDAO.create(newDrink);
                            System.out.println("Drink added!");
                        }
                        catch(CoffeeMachineException e){
                            LOGGER.severe(e.toString());
                            System.out.println("Unable to create the drink : " + e.getMessage());
                        }
                    }
                    break;

                /**
                 * Update a drink
                 * 
                 */
                case 3 :
                    drinks = drinkDAO.fetchAll();
                    listSize = drinks.size();
                    if(listSize == 0){
                        System.out.println("No drinks available! Please add a drink before.");
                    }
                    else{
                        System.out.println(String.format(designSeparator, "Menu: Update a drink"));
                        for (int i = 0; i < listSize; i++) {
                            System.out.println(String.format("%s : %s", i, drinks.get(i).getName())); // display each ingredient with its index in the list
                        }

                        // Get selected drink (range correspond to the list size)
                        choice = getInputNumeric("Which drink wish you update?", false, 0, listSize-1); // get drink index

                        // Get new values for the drink
                        drink = drinks.get(choice);
                        drink.setPrice(getInputNumeric("Enter new price"))
                             .setChocolate( getInputNumeric("Enter new chocolate") )
                             .setMilk( getInputNumeric("Enter new milk") )
                             .setTea( getInputNumeric("Enter new tea") )
                             .setCoffee( getInputNumeric("Enter new coffee"));

                        try{
                            drinkDAO.update(drink);
                            System.out.println("Drink updated!");
                        }
                        catch(CoffeeMachineException e){
                            System.out.println("Unable to update the drink : " + e.getMessage());
                        }
                    }
                    break;

                /**
                 * Add a ingredient
                 * Ask the ingredient and then ask the quantity
                 * - The ingredients are displayed
                 */
                case 4 :
                    ingredients = ingredientDAO.fetchAll(); // get all ingredients availables
                    listSize = ingredients.size();
                    System.out.println(String.format(designSeparator, "Menu: Add a ingredient"));
                    for (int i = 0; i < listSize; i++) {
                        System.out.println(String.format("%s : %s", i, ingredients.get(i).getName())); // display each ingredient with its index in the list
                    }

                    // Get selected ingredient (range correspond to the list size)
                    choice = getInputNumeric("Which ingredient wish you add?", false, 0, listSize-1);

                    // Get value to add
                    value = getInputNumeric("Enter quantity to add");

                    // Add ingredient new quantity inside the machine
                    try{
                        ingredientDAO.updateQte( ingredients.get(choice), value );
                        System.out.println("Stock updated!");
                    }
                    catch(CoffeeMachineException e){
                        System.out.println("Unable to update the ingredient : " + e.getMessage());
                    }
                    break;

                /**
                 * Display drinks
                 */
                case 5 :
                    drinks = drinkDAO.fetchAll();
                    listSize = drinks.size();
                    System.out.println(String.format(designSeparator, "Menu: Display drinks"));
                    for (int i = 0; i < listSize; i++) {
                        System.out.println( drinks.get(i) );
                    }
                    if(listSize == 0){
                        System.out.println("Empty!");
                    }
                    break;

                /**
                 * Display stock
                 * - get all ingredients and format the outputs
                 */
                case 6 :
                    ingredients = ingredientDAO.fetchAll(); // get all ingredients availables
                    listSize = ingredients.size();
                    System.out.println(String.format(designSeparator, "Menu: Display ingredients"));
                    for (int i = 0; i < listSize; i++) {
                        System.out.println( ingredients.get(i) );
                    }
                    if(listSize == 0){
                        System.out.println("Empty!");
                    }
                    break;

                case 9 :
                    over = true;
                    break;
            }
        }
    }
    
    
    /**
     * Get input choice
     * 
     * @param label
     * @param negativeAllowed
     * @param min
     * @param max
     * @return int value in any case
     */
    public static int getInputNumeric(String label, boolean negativeAllowed, Integer min, Integer max){
        Scanner in = new Scanner(System.in);
        String input;
        int choice = 0;
        boolean redo = true;
        do{
            if(label == null) System.out.println("Input [pattern:0-9]:"); // display default context
            else System.out.println(String.format("%s [pattern:0-9]:", label)); // display given context
            input = in.nextLine();
            try{
                choice = Integer.parseInt(input);
                if( negativeAllowed == false && choice < 0){ // negative
                    System.out.println("The input value cannot be negative, please try again!");
                }
                else if( min != null && choice < min){ // min
                    System.out.println(String.format("The minimum value is %s, please try again!", min));
                }
                else if( max != null && choice > max){ // min
                    System.out.println(String.format("The maximum value is %s, please try again!", max));
                }
                else{
                    redo = false;
                }
            }
            catch(NumberFormatException e){
                System.out.println("The input value seems to be not numeric, please try again!");
            }
        }while( redo );
        return choice;
    }
    public static Integer getInputNumeric(String label){
        return getInputNumeric(label, false, null, null);
    }
    public static int getInputNumeric(){
        return getInputNumeric(null);
    }
    
    /**
     * TODO: regex
     * @param label
     * @return 
     */
    public static String getInputAlpha(String label){
        Scanner in = new Scanner(System.in);
        String input;
        do{
            if(label == null) System.out.println("Input [pattern:a-Z]:");
            else System.out.println(String.format("%s [pattern:a-Z]:", label));
            input = in.nextLine();
        }while(input.equals("") || input == null);
        return input;
    }
    public static String getInputAlpha(){
        return getInputAlpha(null);
    }
    
}
