package com.platzi.platzi.pizza.persistence.audit;

import com.platzi.platzi.pizza.persistence.entity.PizzaEntity;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PreRemove;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.SerializationUtils;

@Slf4j
public class AuditPizzaListener {

    private PizzaEntity currentValue;

    @PostLoad
    public void postLoad(PizzaEntity pizzaEntity){
        log.info("Post LOAD");
        this.currentValue = SerializationUtils.clone(pizzaEntity);
    }

    @PostPersist
    @PostUpdate
    public void onPostPersist(PizzaEntity pizzaEntity){
        log.info("POST PERSIST OR UPDATE");
        log.info("OLD VALUE: {}", this.currentValue);
        log.info("NEW VALUE: {}", pizzaEntity);
    }

    @PreRemove
    public void onPreDelete(PizzaEntity pizzaEntity){
        log.info(pizzaEntity.toString());
    }


}
