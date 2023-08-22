package com.platzi.platzi.pizza.service;

import com.platzi.platzi.pizza.persistence.entity.PizzaEntity;
import com.platzi.platzi.pizza.persistence.repository.PizzaPagSortRepository;
import com.platzi.platzi.pizza.persistence.repository.PizzaRepository;
import com.platzi.platzi.pizza.service.Exception.EmailApiException;
import com.platzi.platzi.pizza.service.dto.UpdatePizzaPriceDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@Slf4j
public class PizzaService {
    private final PizzaRepository pizzaRepository;

    private final PizzaPagSortRepository pizzaPagSortRepository;

    @Autowired
    public PizzaService(PizzaRepository pizzaRepository, PizzaPagSortRepository pizzaPagSortRepository){
        this.pizzaRepository=pizzaRepository;
        this.pizzaPagSortRepository = pizzaPagSortRepository;
    }

    public Page<PizzaEntity> getAllPizzas(int page, int elements){
        Pageable pageRequest = PageRequest.of(page, elements);
        return this.pizzaPagSortRepository.findAll(pageRequest);
    }

    public Page<PizzaEntity> getAvailable(int page, int elements, String sortBy, String sortDirection){
        log.info("Vegan pizzas: {}", this.pizzaRepository.countByVeganTrue());
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageRequest = PageRequest.of(page, elements, sort);
        return this.pizzaPagSortRepository.findByAvailableTrue(pageRequest);
    }

    public PizzaEntity getPizzaByName(String name){
        return this.pizzaRepository.findFirstByAvailableTrueAndNameIgnoreCase(name).orElseThrow(() -> new RuntimeException("Pizza not found"));
    }

    public List<PizzaEntity> getPizzaWithIngredient(String description){
        return this.pizzaRepository.findAllByAvailableTrueAndDescriptionContainingIgnoreCase(description);
    }

    public List<PizzaEntity> getPizzaWithoutIngredient(String description){
        return this.pizzaRepository.findAllByAvailableTrueAndDescriptionNotContainingIgnoreCase(description);
    }

    public List<PizzaEntity> getCheapestPizza(double price){
        return this.pizzaRepository.findTop3ByAvailableTrueAndPriceLessThanEqualOrderByPriceAsc(price);
    }

    public PizzaEntity getPizza(int idPizza){
        return this.pizzaRepository.findById(idPizza)
                .orElseThrow(null);
    }

    public PizzaEntity savePizza(PizzaEntity pizza){
        return this.pizzaRepository.save(pizza);
    }

    public boolean exists(int idPizza){
        return this.pizzaRepository.existsById(idPizza);
    }

    public void deletePizza(int idPizza){
       this.pizzaRepository.deleteById(idPizza);
    }

    @Transactional(
            noRollbackFor = EmailApiException.class,
            propagation = Propagation.REQUIRED
    )
    public void updatePrice(UpdatePizzaPriceDto newPizzaPrice){
        this.pizzaRepository.updatePrice(newPizzaPrice);
//        this.sendEmail();
    }

    private void sendEmail(){
        throw new EmailApiException();
    }
}
