package com.platzi.platzi.pizza.web.controller;

import com.platzi.platzi.pizza.persistence.entity.PizzaEntity;
import com.platzi.platzi.pizza.service.PizzaService;
import com.platzi.platzi.pizza.service.dto.UpdatePizzaPriceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pizza")
public class PizzaController {

    private final PizzaService pizzaService;

    @Autowired
    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @GetMapping
    public ResponseEntity<Page<PizzaEntity>> getAllPizzas(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "8") int elements){
        return ResponseEntity.ok(this.pizzaService.getAllPizzas(page, elements));
    }

    @GetMapping("/{idPizza}")
    public ResponseEntity<PizzaEntity> getPizzaById(@PathVariable int idPizza){
        return ResponseEntity.ok(this.pizzaService.getPizza(idPizza));
    }

    @GetMapping("/available")
    public ResponseEntity<Page<PizzaEntity>> getAvailablePizzas(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "8") int elements,
                                                                @RequestParam(defaultValue = "price") String sortBy,
                                                                @RequestParam(defaultValue = "ASC") String sortDirection){
        return ResponseEntity.ok(this.pizzaService.getAvailable(page, elements, sortBy, sortDirection));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<PizzaEntity> getAvailableByName(@PathVariable String name){
        return ResponseEntity.ok(this.pizzaService.getPizzaByName(name));
    }

    @GetMapping("/with/{ingredient}")
    public ResponseEntity<List<PizzaEntity>> getAvailableByDescription(@PathVariable String ingredient){
        return ResponseEntity.ok(this.pizzaService.getPizzaWithIngredient(ingredient));
    }

    @GetMapping("/without/{ingredient}")
    public ResponseEntity<List<PizzaEntity>> getAvailableByDescriptionWithoutIngredient(@PathVariable String ingredient){
        return ResponseEntity.ok(this.pizzaService.getPizzaWithoutIngredient(ingredient));
    }

    @GetMapping("/cheapest/{price}")
    public ResponseEntity<List<PizzaEntity>> getCheapestPizzasUnderPrice(@PathVariable double price){
        return ResponseEntity.ok(this.pizzaService.getCheapestPizza(price));
    }

    @PostMapping
    public ResponseEntity<PizzaEntity> createPizza(@RequestBody PizzaEntity pizza){
        if (pizza.getIdPizza() == null || !this.pizzaService.exists(pizza.getIdPizza())){
            return ResponseEntity.ok(this.pizzaService.savePizza(pizza));
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping
    public ResponseEntity<PizzaEntity> updatePizza(@RequestBody PizzaEntity pizza){
        if (pizza.getIdPizza() != null && this.pizzaService.exists(pizza.getIdPizza())){
            return ResponseEntity.ok(this.pizzaService.savePizza(pizza));
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/price")
    public ResponseEntity<Void> updatePizzaPrice(@RequestBody UpdatePizzaPriceDto pizza){
        if (this.pizzaService.exists(pizza.getPizzaId())){
            this.pizzaService.updatePrice(pizza);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{idPizza}")
    public ResponseEntity<Void> deletePizza(@PathVariable int idPizza){
        if (this.pizzaService.exists(idPizza)){
            this.pizzaService.deletePizza(idPizza);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
