package com.m2z.tools.managementservice.generic;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@MappedSuperclass
public abstract class AbstractEntityUUID {

    @Id
    @Getter
    @Setter
    @GeneratedValue
    private UUID id;
}
