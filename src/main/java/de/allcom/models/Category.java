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
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

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
    @ToString.Exclude
    private List<Product> products;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "("
                + "id = " + id + ", "
                + "nameRu = " + nameRu + ", "
                + "nameDe = " + nameDe + ", "
                + "nameEn = " + nameEn + ", "
                + "parentId = " + parentId + ", "
                + "updateAt = " + updateAt + ", "
                + "createAt = " + createAt + ")";
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o)
                .getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Category category = (Category) o;
        return getId() != null && Objects.equals(getId(), category.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this)
                .getHibernateLazyInitializer()
                .getPersistentClass().hashCode() : getClass().hashCode();
    }
}
