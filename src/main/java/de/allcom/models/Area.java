package de.allcom.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;

@Getter
@Setter
@Entity
@Table(name = "storage_areas")
public class Area {
    public enum AreaName {
        L('L'),
        R('R');

        private final char value;

        AreaName(char value) {
            this.value = value;
        }

        public char getValue() {
            return value;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1)
    @Enumerated(EnumType.STRING)
    @Check(constraints = "name IN ('L', 'R')")
    private AreaName name;

    @OneToMany(mappedBy = "area")
    private List<Storage> storages;
}
