package com.example.springtutorial.handlers;

import com.example.springtutorial.entities.Instance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RepositoryEventHandler
public class InstanceEventHandler {

    @HandleAfterCreate
    public void handleAfterCreate(Instance instance) {
        log.info("Instance {} created via Repository Rest Controller", instance.getId());
    }

    @HandleAfterSave
    public void handleAfterSave(Instance instance) {
        log.info("Instance {} saved via Repository Rest Controller", instance.getId());
    }
}
