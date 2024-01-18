package de.allcom.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column
    private Float weight;

    @Column
    private String color;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<PhotoProduct> photos;

    public String getCategoryName() {
        return category.getNameDe();
    }

    public List<String> getLinksOfPhotos() {
        return this.photos.stream().map(PhotoProduct::getLink).toList();
    }
}
