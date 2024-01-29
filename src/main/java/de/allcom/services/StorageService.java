package de.allcom.services;

import de.allcom.dto.storage.StorageDto;
import de.allcom.models.Area;
import de.allcom.models.Rack;
import de.allcom.models.Section;
import de.allcom.models.Shelve;
import de.allcom.models.Storage;
import de.allcom.repositories.AreaRepository;
import de.allcom.repositories.RackRepository;
import de.allcom.repositories.SectionRepository;
import de.allcom.repositories.ShelveRepository;
import de.allcom.repositories.StorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final StorageRepository storageRepository;
    private final AreaRepository areaRepository;
    private final RackRepository rackRepository;
    private final SectionRepository sectionRepository;
    private final ShelveRepository shelveRepository;

    @Transactional
    public StorageDto savePlace(String area, Integer rackNumber, Integer sectionNumber, Integer shelveNumber) {

        Area newArea = areaRepository.findByName(Area.Name.valueOf(area))
                .orElseGet(() -> {
                    Area areaEntity = new Area();
                    areaEntity.setName(Area.Name.valueOf(area));
                    return areaRepository.save(areaEntity);
                });

        Rack newRack = rackRepository.findByNumber(rackNumber)
                .orElseGet(() -> {
                    Rack rackEntity = new Rack();
                    rackEntity.setNumber(rackNumber);
                    return rackRepository.save(rackEntity);
                });

        Section newSection = sectionRepository.findByNumber(sectionNumber)
                .orElseGet(() -> {
                    Section sectionEntity = new Section();
                    sectionEntity.setNumber(sectionNumber);
                    return sectionRepository.save(sectionEntity);
                });

        Shelve newShelve = shelveRepository.findByNumber(shelveNumber)
                .orElseGet(() -> {
                    Shelve shelveEntity = new Shelve();
                    shelveEntity.setNumber(shelveNumber);
                    return shelveRepository.save(shelveEntity);
                });

        Storage newStorage = new Storage();
        newStorage.setArea(newArea);
        newStorage.setRack(newRack);
        newStorage.setSection(newSection);
        newStorage.setShelve(newShelve);

        Storage savedStorage = storageRepository.save(newStorage);

        return StorageDto.builder()
                .id(savedStorage.getId())
                .area(savedStorage.getArea().getName().name())
                .rack(savedStorage.getRack().getNumber())
                .section(savedStorage.getSection().getNumber())
                .shelve(savedStorage.getShelve().getNumber())
                .build();
    }
}
