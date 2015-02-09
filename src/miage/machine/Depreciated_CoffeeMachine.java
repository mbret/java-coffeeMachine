//
//package machine;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.logging.Logger;
//
///**
// * CoffeeMachine.
// * Main class coffee machine. Contain the main static function.
// * 
// * Description: 
// * 
// * @author maxime
// * @version 21-01-2014
// */
//public class Depreciated_CoffeeMachine {
//
//    private Integer coffee;
//    private Integer milk;
//    private Integer chocolate;
//    private Integer sugar;
//    private Integer tea;
//    
//    private ArrayList<Drink> drinkList;
//    
//    private final static Logger LOGGER = Logger.getLogger(Depreciated_CoffeeMachine.class.getName());
//    
//    public Depreciated_CoffeeMachine(){
//        this(0,0,0,0,new ArrayList<Drink>()); // init with 0 of each
//    }
//    
//    public Depreciated_CoffeeMachine(int coffee, int milk, int chocolate, int sugar, ArrayList<Drink> drinkList) {
//        this.coffee = coffee;
//        this.milk = milk;
//        this.chocolate = chocolate;
//        this.sugar = sugar;
//        this.drinkList = drinkList;
//    }
//    
//    /**
//     * Buy a drink.
//     * Reduce each ingredient and the given money
//     * 
//     * @param index
//     * @param givenMoney
//     * @return HashMap contain the drink and the money back {drink, moneyBack}
//     * @throws CoffeeMachineException 
//     */
//    public HashMap<String, Object> buyDrink(Drink drink, Integer givenMoney, Integer askedSugar) throws CoffeeMachineException{
//        LOGGER.info(String.format("Buying request {%s}{givenMoney:%s}",drink, givenMoney));
//        HashMap<String, Object> result = new HashMap();
//        Integer moneyBack;
//        // reduce ingredient
//        this.coffee -= drink.getCoffeeUnit();
//        this.chocolate -= drink.getChocolateUnit();
//        this.milk -= drink.getMilkUnit();
//        this.sugar -= askedSugar;
//        this.tea -= drink.getTeaUnit();
//        moneyBack = givenMoney - drink.getPrice(); // reduce price from given money
//        result.put("drink", drink);
//        result.put("moneyBack", moneyBack);
//        LOGGER.info(String.format("Drink buyed! {%s}{moneyBack:%s}",drink, moneyBack));
//        return result;
//    }
//    
//    public HashMap<String, Object> buyDrink(Integer index, Integer givenMoney, Integer askedSugar) throws CoffeeMachineException{
//        Drink drink = this.drinkList.get(index); // get drink or null
//        if(drink == null){
//            throw new CoffeeMachineException(("Drink doesn't exist"));
//        }
//        return this.buyDrink(drink, givenMoney, askedSugar);
//    }
//    
//    /**
//     * 
//     * @param name
//     * @return 
//     */
//    public Drink getDrinkByName(String name){
//        for (Drink drink : this.drinkList){
//            if (drink.getName().equals(name))
//                return drink; // skip useless loop
//        }
//        return null;
//    }
//    
//    public Drink getDrinkByIndex(int index){
//        Drink drink;
//        try{
//            drink = this.drinkList.get(index);
//        }
//        catch(IndexOutOfBoundsException e){
//            return null;
//        }
//        return drink;
//    }
//    
//    /**
//     * 
//     * @param name
//     * @return 
//     */
//    public boolean drinkExistByName(String name){
//        return (this.getDrinkByName(name) instanceof Drink);
//    }
//    
//    public boolean drinkExistByIndex(int index){
//        return (this.drinkList.get(index) instanceof Drink);
//    }
//    
//    /**
//     * Delete a drink 
//     * 
//     * @param drink
//     * @throws CoffeeMachineException 
//     */
//    public void addDrink(Drink drink) throws CoffeeMachineException{
//        LOGGER.info(String.format("Deleting request {%s}", drink.toString()));
//        if (this.drinkList.size() == this.nbDrinkMax){
//            throw new CoffeeMachineException("Maximum drink reached!");
//        }else if( this.getDrinkByName(drink.getName()) != null ){
//            throw new CoffeeMachineException("A drink with this name already exist!");
//        }
//        else{
//            this.drinkList.add(drink);
//        }
//        LOGGER.info(String.format("Drink {%s} added!", drink.toString()));
//    }
//    
//    /**
//     * Update a drink
//     * 
//     * @param name
//     * @param newValues HashMap containing the values to update (label) associated to their new values
//     *              'sugar'  => 3,
//     *              'coffee' => 4,
//     *              ...
//     * @return 
//     */
//    public boolean updateDrink(String name, HashMap newValues){
//        Drink drink = this.getDrinkByName(name);
//        drink.setPrice((Integer)newValues.get("price"));
//        drink.setChocolateUnit((Integer)newValues.get("chocolateUnit"));
//        drink.setCoffeeUnit((Integer)newValues.get("coffeeUnit"));
//        drink.setMilkUnit((Integer)newValues.get("milkUnit"));
//        drink.setTeaUnit( (Integer)newValues.get("milkUnit"));
//        return true;
//    }
//    
//    /**
//     * Delete drink
//     * 
//     * @param index
//     * @return 
//     */
//    public void deleteDrink(int index){
//        LOGGER.info(String.format("Deleting request {index:%s}", index));
//        Drink removedDrink = this.drinkList.remove(index);
//        LOGGER.info(String.format("{index:%s}{drink:%s} removed!", index, removedDrink));
//    }
//    
//    /**
//     * 
//     * @param name
//     * @param value
//     * @return
//     * @throws NoSuchMethodException
//     * @throws IllegalAccessException
//     * @throws IllegalArgumentException
//     * @throws InvocationTargetException 
//     */
//    public boolean addIngredient(String name, Integer value) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
//        Class objClass= this.getClass();
//        Method getMethod = objClass.getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
//        Method setMethod = objClass.getMethod("set" + name.substring(0, 1).toUpperCase() + name.substring(1), Object.class);
//        setMethod.invoke(this, (Integer)getMethod.invoke(this) + value);
//        return true;
//    }
//    
//    /**
//     * Check if there are enough ingredient for the given drink
//     * 
//     * @param drink
//     * @return 
//     */
//    public boolean enoughIngredient(Drink drink){
//        LOGGER.info(String.format("Checking ingredient request {drink:%s}",drink));
//        return drink.getCoffeeUnit() <= this.coffee && drink.getMilkUnit() <= this.milk && drink.getTeaUnit() <= this.tea && drink.getChocolateUnit() <= this.chocolate;
//    }
//    
//    public boolean enoughIngredient(int drinkIndex){
//        return this.enoughIngredient(this.drinkList.get(drinkIndex));
//    }
//    
//    
//    
//    /**
//     * Display drinks for user
//     * 
//     * The choice matchs with the index of drink in the list
//     */
//    public void displayDrinks(){
//        int choice = 0;
//        for (Drink drink : this.drinkList){
//            System.out.println(choice + " : " + drink.getName() + " (" +drink.getPrice()+ "€)");
//            choice++;
//        }
//    }
//    
//    public void displayDrinksByName(){
//        for (Drink drink : this.drinkList){
//            System.out.println(drink.getName() + " : " + drink.getName() + " (" +drink.getPrice()+ "€)");
//        }
//    }
//
//    public void displayIngredients(){
//        System.out.println(""
//                + "0 : coffee\n"
//                + "1 : milk\n"
//                + "2 : chocolate\n"
//                + "3 : sugar\n"
//                + "4 : tea");
//    }
//
//    public int getCoffee() {
//        return coffee;
//    }
//
//    public int getMilk() {
//        return milk;
//    }
//
//    public int getChocolate() {
//        return chocolate;
//    }
//
//    public int getSugar() {
//        return sugar;
//    }
//
//    public void setCoffee(Object coffee) {
//        this.coffee = (Integer)coffee;
//    }
//
//    public void setMilk(Object milk) {
//        this.milk = (Integer)milk;
//    }
//
//    public void setChocolate(Object chocolate) {
//        this.chocolate = (Integer)chocolate;
//    }
//
//    public void setSugar(Object sugar) {
//        this.sugar = (Integer)sugar;
//    }
//
//    public void setCoffee(Integer coffee) {
//        this.coffee = coffee;
//    }
//
//    public void setMilk(Integer milk) {
//        this.milk = milk;
//    }
//
//    public void setChocolate(Integer chocolate) {
//        this.chocolate = chocolate;
//    }
//
//    public void setSugar(Integer sugar) {
//        this.sugar = sugar;
//    }
//
//    public Integer getTea() {
//        return tea;
//    }
//
//    public void setTea(Integer tea) {
//        this.tea = tea;
//    }
//
//    public ArrayList<Drink> getDrinkList() {
//        return drinkList;
//    }
//    
//}
