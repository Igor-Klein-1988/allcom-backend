package de.allcom.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Check;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "products")
public class Product {
    public enum StateName {
        Draft("Draft"),
        InStock("InStock"),
        Sold("Sold");

        private final String value;

        StateName(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 3, max = 50)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column
    private Float weight;

    @Column
    @Size(min = 3, max = 10)
    private String color;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @OneToOne
    @JoinColumn(name = "storage_id", referencedColumnName = "id")
    private Storage storage;

    @Column
    @Enumerated(EnumType.STRING)
    @Check(constraints = "state IN ('Draft', 'InStock', 'Sold')")
    private StateName state;

    @Column
    private LocalDateTime updateAt;

    @Column
    private LocalDateTime createAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ProductImage> images;
}
