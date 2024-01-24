package de.allcom.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String nameRu;

    @Column(length = 50)
    private String nameDe;

    @Column(length = 50)
    private String nameEn;

    @Column(nullable = false)
    private Long parentId;

    @Column(nullable = false)
    private LocalDateTime updateAt;

    @Column(nullable = false)
    private LocalDateTime createAt;

    @OneToMany(mappedBy = "category")
    private List<Product> products;
}
