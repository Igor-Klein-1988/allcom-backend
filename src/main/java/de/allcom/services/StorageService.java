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

@Service
@RequiredArgsConstructor
public class StorageService {
    private StorageRepository storageRepository;
    private AreaRepository areaRepository;
    private RackRepository rackRepository;
    private SectionRepository sectionRepository;
    private ShelveRepository shelveRepository;

    public StorageDto savePlace(Character area, Integer rack, Integer section, Integer shelve) {

        Area newArea = new Area();
        if (areaRepository.findByName(area).isEmpty()) {
            newArea.setName(area);
            areaRepository.save(newArea);
        } else {
            newArea = areaRepository.findByName(area).get();
        }

        Rack newRack = new Rack();
        if (rackRepository.findByNumber(rack).getId() < 0) {
            newRack.setNumber(rack);
            rackRepository.save(newRack);
        } else {
            newRack = rackRepository.findByNumber(rack);
        }

        Section newSection = new Section();
        if (sectionRepository.findByNumber(section).getId() < 0) {
            newSection.setNumber(rack);
            sectionRepository.save(newSection);
        } else {
            newSection = sectionRepository.findByNumber(rack);
        }

        Shelve newShelve = new Shelve();
        if (shelveRepository.findByNumber(shelve).getId() < 0) {
            newShelve.setNumber(rack);
            shelveRepository.save(newShelve);
        } else {
            newShelve = shelveRepository.findByNumber(rack);
        }
        Storage newStorage = new Storage();
        newStorage.setArea(newArea);
        newStorage.setRack(newRack);
        newStorage.setSection(newSection);
        newStorage.setShelve(newShelve);

        Storage savedStorage = storageRepository.save(newStorage);
        return StorageDto.builder()
                .id(savedStorage.getId())
                .area(savedStorage.getArea().getName())
                .rack(savedStorage.getRack().getNumber())
                .section(savedStorage.getSection().getNumber())
                .shelve(savedStorage.getShelve().getNumber())
                .build();
    }
}
