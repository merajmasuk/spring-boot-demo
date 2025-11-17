package com.example.springtutorial.dto;

import com.example.springtutorial.entities.Instance;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "instanceProjection", types = Instance.class)
public interface InstanceProjection {
    String getName();
    String getDescription();
}
