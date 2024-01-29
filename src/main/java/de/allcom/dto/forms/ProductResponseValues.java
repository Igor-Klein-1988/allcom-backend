package de.allcom.dto.forms;

import de.allcom.dto.product.ProductDto;
import de.allcom.dto.product.StorageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductResponseValues {
    private ProductDto product;
    //    private AuctionDTO auction;
    private StorageDto storage;
}
