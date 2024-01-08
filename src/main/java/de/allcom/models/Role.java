package de.allcom.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public enum Role {

    ADMIN,
    STOREKEEPER,
    CLIENT

}
