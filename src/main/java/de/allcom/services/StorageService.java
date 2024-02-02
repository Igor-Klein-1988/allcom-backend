package de.allcom.services;

import de.allcom.dto.storage.StorageCreateDto;
import de.allcom.dto.storage.StorageDto;
import de.allcom.exceptions.RestException;
import de.allcom.models.Area;
import de.allcom.models.Rack;
import de.allcom.models.Section;
import de.allcom.models.Shelf;
import de.allcom.models.Storage;
import de.allcom.repositories.AreaRepository;
import de.allcom.repositories.RackRepository;
import de.allcom.repositories.SectionRepository;
import de.allcom.repositories.ShelfRepository;
import de.allcom.repositories.StorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final StorageRepository storageRepository;
    private final AreaRepository areaRepository;
    private final RackRepository rackRepository;
    private final SectionRepository sectionRepository;
    private final ShelfRepository shelfRepository;

    @Transactional
    public Storage create(StorageCreateDto storageDto) {
        Storage newStorage = new Storage();
        return buildAndSaveStorage(newStorage, storageDto.getArea(), storageDto.getRack(), storageDto.getSection(),
                storageDto.getShelf());

    }

    public Storage update(StorageDto storageDto) {
        Storage storageForUpdate = storageRepository.findById(storageDto.getId())
                                                    .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                                                            "Storage with id: " + storageDto.getId() + " not found"));

        return buildAndSaveStorage(storageForUpdate, storageDto.getArea(), storageDto.getRack(),
                storageDto.getSection(), storageDto.getShelf());
    }

    private Storage buildAndSaveStorage(Storage storage, String areaName, Integer rackValue, Integer sectionValue,
            Integer shelfValue) {
        Area area = areaRepository.findByName(Area.Name.valueOf(areaName)).orElseGet(() -> {
            Area areaEntity = new Area();
            areaEntity.setName(Area.Name.valueOf(areaName));
            return areaRepository.save(areaEntity);
        });

        Rack rack = rackRepository.findByNumber(rackValue).orElseGet(() -> {
            Rack rackEntity = new Rack();
            rackEntity.setNumber(rackValue);
            return rackRepository.save(rackEntity);
        });

        Section section = sectionRepository.findByNumber(sectionValue).orElseGet(() -> {
            Section sectionEntity = new Section();
            sectionEntity.setNumber(sectionValue);
            return sectionRepository.save(sectionEntity);
        });

        Shelf shelf = shelfRepository.findByNumber(shelfValue).orElseGet(() -> {
            Shelf shelfEntity = new Shelf();
            shelfEntity.setNumber(shelfValue);
            return shelfRepository.save(shelfEntity);
        });

        storage.setArea(area);
        storage.setRack(rack);
        storage.setSection(section);
        storage.setShelf(shelf);

        return storageRepository.save(storage);
    }
}
