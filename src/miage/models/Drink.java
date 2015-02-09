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
public class Drink{
    
    private Integer id;
    private String name;
    private Integer price;
    private Integer coffee;
    private Integer milk;
    private Integer chocolate;
    private Integer tea;

    public Drink(){
    }

    public Drink(Integer id, String name, Integer price, Integer coffee, Integer milk, Integer chocolate, Integer tea) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.coffee = coffee;
        this.milk = milk;
        this.chocolate = chocolate;
        this.tea = tea;
    }

    public Integer getId() {
        return id;
    }

    public Drink setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Drink setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getPrice() {
        return price;
    }

    public Drink setPrice(Integer price) {
        this.price = price;
        return this;
    }

    public Integer getCoffee() {
        return coffee;
    }

    public Drink setCoffee(Integer coffeeUnit) {
        this.coffee = coffeeUnit;
        return this;
    }

    public Integer getMilk() {
        return milk;
    }

    public Drink setMilk(Integer milkUnit) {
        this.milk = milkUnit;
        return this;
    }

    public Integer getChocolate() {
        return chocolate;
    }

    public Drink setChocolate(Integer chocolate) {
        this.chocolate = chocolate;
        return this;
    }

    public Integer getTea() {
        return tea;
    }

    public Drink setTea(Integer teaUnit) {
        this.tea = teaUnit;
        return this;
    }

    @Override
    public String toString() {
        return "Drink{" + "id=" + id + ", name=" + name + ", price=" + price + ", coffee=" + coffee + ", milk=" + milk + ", chocolate=" + chocolate + ", tea=" + tea + '}';
    }
    
    
}
