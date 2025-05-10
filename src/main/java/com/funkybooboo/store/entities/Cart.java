package com.funkybooboo.store.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter 
@Setter
@NoArgsConstructor
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(
            name = "id",
            updatable = false,
            nullable = false,
            columnDefinition = "CHAR(36)"
    )
    private UUID id;

    @Column(
            name = "date_created",
            insertable = false,
            updatable = false
    )
    private LocalDate dateCreated;

    @OneToMany(mappedBy = "cart")
    private Set<CartItem> cartItems = new LinkedHashSet<>();
}
