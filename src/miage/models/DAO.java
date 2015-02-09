package miage.models;



import java.sql.Connection;
import java.util.ArrayList;
import miage.machine.CoffeeMachineException;
import miage.machine.Connexion;

public abstract class DAO<T> {

	protected static Connection connect;

    /**
     * Permet de récupérer un objet via son ID
     * @param id
     * @return
     */
    public abstract T find(long id) throws CoffeeMachineException;

    /**
     * Permet de créer une entrée dans la base de données
     * par rapport à un objet
     * @param obj
     */
    public abstract T create(T obj) throws CoffeeMachineException;

    /**
     * Permet de mettre à jour les données d'une entrée dans la base 
     * @param obj
     */
    public abstract T update(T obj) throws CoffeeMachineException;

    /**
     * Permet la suppression d'une entrée de la base
     * @param obj
     */
    public abstract void delete(T obj) throws CoffeeMachineException;

    /**
     * 
     * @return 
     * @throws miage.machine.CoffeeMachineException
     */
    public abstract ArrayList<T> fetchAll() throws CoffeeMachineException;

    public static void setConnection(Connection connection) throws Exception{
        connect = Connexion.getInstance();
    }

}