package miage.models;

/**
CREATE TABLE drink
(
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,
    name VARCHAR(1024) NOT NULL UNIQUE,
    price INTEGER,
    coffee INTEGER NOT NULL,
    tea INTEGER NOT NULL,
    chocolate INTEGER NOT NULL,
    milk INTEGER NOT NULL
);
 */

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Logger;
import miage.machine.CoffeeMachineException;
import miage.machine.Main;

public class DrinkDAO extends DAO<Drink> {

    private final String tableName = "drink";
    private final static Logger LOGGER = Logger.getLogger(DrinkDAO.class.getName());
    private IngredientDAO ingredientDAO;
    
    public DrinkDAO( IngredientDAO ingredientDAO) {
        this.ingredientDAO = ingredientDAO;
    }
    
    
    /**
     * Ajoute une boisson
     * @param obj
     * @return 
     * @throws miage.machine.CoffeeMachineException
     */
    @Override
    public Drink create(Drink obj) throws CoffeeMachineException{
        try
        {
            String query = "insert into " + this.tableName + " (name, price, coffee, tea, chocolate, milk) values ('"
                    + obj.getName() + "'," 
                    + obj.getPrice()+ "," 
                    + obj.getCoffee()+ "," 
                    + obj.getTea()+ "," 
                    + obj.getChocolate()+ "," 
                    + obj.getMilk()+")";
//            System.out.println(query);
            Statement stmt = connect.createStatement();
            stmt.execute(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            obj.setId(rs.getInt(1));
            stmt.close();
            return obj;
        }
        catch (SQLException e)
        {
            Main.debug(e, LOGGER);
            throw new CoffeeMachineException(Main.errorMessages.get("errorDB"));
        }
        
    }

    @Override
    public Drink find(long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Update the drink values in database according to the given object's id
     * @param obj
     * @return
     * @throws CoffeeMachineException 
     */
    @Override
    public Drink update(Drink obj) throws CoffeeMachineException{
        Main.logInfo(LOGGER, String.format("Drink update db request (%s)", obj));
        try
        {
            Statement stmt = connect.createStatement();
            stmt.execute("UPDATE " + this.tableName + " "
                    + "SET price =  " + obj.getPrice() + ","
                    + "tea = " + obj.getTea() + ","
                    + "chocolate = " + obj.getChocolate() + ","
                    + "milk = " + obj.getMilk() + ","
                    + "coffee = " + obj.getCoffee() + ""
                    + "WHERE id = " + obj.getId());
            stmt.close();
            return obj;
        }
        catch (SQLException e)
        {
            Main.debug(e, LOGGER);
            throw new CoffeeMachineException(Main.errorMessages.get("errorDB"));
        }
    }

    @Override
    public void delete(Drink obj) throws CoffeeMachineException{
        Main.logInfo(LOGGER, String.format("Drink delete db request (%s)", obj));
        try
        {
            Statement stmt = connect.createStatement();
            stmt.execute("DELETE FROM " + this.tableName + " "
                    + "WHERE id = " + obj.getId());
            stmt.close();
        }
        catch (SQLException e)
        {
            Main.debug(e, LOGGER);
            throw new CoffeeMachineException(Main.errorMessages.get("errorDB"));
        }
    }

    @Override
    public ArrayList<Drink> fetchAll() throws CoffeeMachineException {
        Main.logInfo(LOGGER, "Fetch all drinks from database");
        ArrayList resultList = new ArrayList();
        try{
            ResultSet result = connect.createStatement().executeQuery("SELECT * FROM drink");
            while(result.next()) {
                resultList.add( new Drink(
                        result.getInt("id"), 
                        result.getString("name"), 
                        result.getInt("price"), 
                        result.getInt("coffee"), 
                        result.getInt("milk"),
                        result.getInt("chocolate"),
                        result.getInt("tea")));
            }
        }
        catch(SQLException e){
            Main.debug(e, LOGGER);
            throw new CoffeeMachineException(Main.errorMessages.get("errorDB"));
        }
        return resultList;
    }
    
    /**
     * Return tje number of drink inside the machine
     * @return 
     * @throws miage.machine.CoffeeMachineException
     */
    public int count() throws CoffeeMachineException{
        Main.logInfo(LOGGER, "Count all drinks from database");
        int nb = 0;
        try{
            ResultSet result = connect.createStatement().executeQuery("SELECT count(id) as nb FROM drink");
            while(result.next()) {
                nb = result.getInt("nb");
            }
            Main.logInfo(LOGGER, String.format("%s results found!", nb));
            return nb;
        }
        catch(SQLException e){
            Main.debug(e, LOGGER);
            throw new CoffeeMachineException(Main.errorMessages.get("errorDB"));
        }
    }
    
    /**
     * Check if a drink with this name already exist.
     * - case unsensitive
     * @param name
     * @return
     * @throws CoffeeMachineException 
     */
    public boolean exist(String name) throws CoffeeMachineException{
        Main.logInfo(LOGGER, String.format("Check if %s exist", name));
        int nb = 0;
        try{
            ResultSet result = connect.createStatement().executeQuery(String.format("SELECT count(id) as nb FROM drink WHERE UPPER(name) LIKE UPPER('%s') ", name));
            while(result.next()) {
                nb = result.getInt("nb");
            }
            Main.logInfo(LOGGER, String.format("%s results found!", nb));
            return nb > 0;
        }
        catch(SQLException e){
            Main.debug(e, LOGGER);
            throw new CoffeeMachineException(Main.errorMessages.get("errorDB"));
        }
    }
	
    /**
     * Buy a drink.
     * - reduce each ingredient according to the given drink
     * @param drink 
     * @throws miage.machine.CoffeeMachineException
     */
    public void buyDrink(Drink drink) throws CoffeeMachineException{
        this.ingredientDAO.reduceQuantity( "coffee" , drink.getCoffee());
        this.ingredientDAO.reduceQuantity( "milk", drink.getMilk() );
        this.ingredientDAO.reduceQuantity( "tea", drink.getTea() );
        this.ingredientDAO.reduceQuantity( "chocolate", drink.getChocolate() );
    }
	
	
}