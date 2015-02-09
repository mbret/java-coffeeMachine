/**
CREATE TABLE ingredient
(
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,
    name VARCHAR(1024) NOT NULL UNIQUE,
    qte INTEGER
);
INSERT INTO ingredient (name, qte) values ('sugar', 0);
INSERT INTO ingredient (name, qte) values ('tea', 0);
INSERT INTO ingredient (name, qte) values ('milk', 0);
INSERT INTO ingredient (name, qte) values ('coffee', 0);
INSERT INTO ingredient (name, qte) values ('chocolate', 0);
 */

package miage.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Logger;
import miage.machine.CoffeeMachineException;
import miage.machine.Main;

/**
 *
 * @author maxime
 */
public class IngredientDAO extends DAO<Ingredient>{

    private final String tableName = "ingredient";
    private final static Logger LOGGER = Logger.getLogger(IngredientDAO.class.getName());
    
    @Override
    public Ingredient find(long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Return the requested ingredient by the name.
     * @param name
     * @return 
     * @throws miage.machine.CoffeeMachineException
     */
    public Ingredient find(String name) throws CoffeeMachineException {
        Main.logInfo(LOGGER, String.format("Find drink from db request (%s)", name));
        Ingredient ingredient = new Ingredient();
        try{
            ResultSet result = connect.createStatement().executeQuery("SELECT * FROM " + this.tableName + " WHERE UPPER(name) LIKE UPPER('" + name + "')");
            while(result.next()) {
                ingredient.setId(result.getInt("id"))
                          .setName(result.getString("name"))
                          .setQte(result.getInt("qte"));
            }
            return ingredient;
        }
        catch(SQLException e){
            Main.debug(e, LOGGER);
            throw new CoffeeMachineException(Main.errorMessages.get("errorDB"));
        }
    }

    @Override
    public Ingredient create(Ingredient obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Update the ingredient values in database according to the given object's id
     * @param currentIngredient
     * @param qteToAdd
     * @return 
     * @throws miage.machine.CoffeeMachineException
     */
    public void updateQte(Ingredient currentIngredient, int qteToAdd) throws CoffeeMachineException{
        Main.logInfo(LOGGER, String.format("Ingredient update db request (%s)", currentIngredient));
        try
        {
            Statement stmt = connect.createStatement();
            stmt.execute("UPDATE " + this.tableName + " SET qte =  " + (currentIngredient.getQte() + qteToAdd) + " WHERE id = " + currentIngredient.getId());
            stmt.close();
        }
        catch (SQLException e)
        {
            Main.debug(e, LOGGER);
            throw new CoffeeMachineException(Main.errorMessages.get("errorDB"));
        }
    }

    @Override
    public Ingredient update(Ingredient obj) throws CoffeeMachineException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    /**
     * Reduce the quantity of a ingredient.
     * @param name
     * @param quantity
     * @throws CoffeeMachineException 
     */
    public void reduceQuantity(String name, int quantity) throws CoffeeMachineException {
        Main.logInfo(LOGGER, String.format("Reduce ingredient quantity db request (%s, %s)", name, quantity));
        try
        {
            Statement stmt = connect.createStatement();
            stmt.execute("UPDATE " + this.tableName + " SET qte =  qte - " + quantity + " WHERE UPPER(name) = UPPER('" + name + "')");
            stmt.close();
        }
        catch (SQLException e)
        {
            Main.debug(e, LOGGER);
            throw new CoffeeMachineException(Main.errorMessages.get("errorDB"));
        }
    }
    
    @Override
    public void delete(Ingredient obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public ArrayList<Ingredient> fetchAll() throws CoffeeMachineException{
        Main.logInfo(LOGGER, "Fetch all ingredients from database");
        ArrayList resultList = new ArrayList();
        try{
            ResultSet result = connect.createStatement().executeQuery("SELECT * FROM ingredient");
            while(result.next()) {
                resultList.add( new Ingredient(result.getInt("id"), result.getString("name"), result.getInt("qte")));
            }
            return resultList;
        }
        catch(SQLException e){
            Main.debug(e, LOGGER);
            throw new CoffeeMachineException(Main.errorMessages.get("errorDB"));
        }
        
    }
    
}
