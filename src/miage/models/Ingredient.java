/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package miage.models;

/**
 *
 * @author maxime
 */
public class Ingredient {
    
    private Integer id;
    private String name;
    private Integer qte;

    public Ingredient() {
    }
    
    
    public Ingredient(Integer id, String name, Integer qte) {
        this.id = id;
        this.name = name;
        this.qte = qte;
    }

    public Integer getId() {
        return id;
    }

    public Ingredient setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Ingredient setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getQte() {
        return qte;
    }

    public Ingredient setQte(Integer qte) {
        this.qte = qte;
        return this;
    }

    @Override
    public String toString() {
        return "Ingredient{" + "id=" + id + ", name=" + name + ", qte=" + qte + '}';
    }
    
    
    
}
