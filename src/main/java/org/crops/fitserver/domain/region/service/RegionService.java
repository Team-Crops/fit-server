package org.crops.fitserver.domain.region.service;

import java.util.List;
import org.crops.fitserver.domain.region.domain.Region;
import org.crops.fitserver.domain.region.dto.RegionDto;
import org.crops.fitserver.domain.region.dto.request.CreateRegionRequest;
import org.crops.fitserver.domain.region.dto.request.UpdateRegionRequest;

public interface RegionService {

  List<RegionDto> getRegionList();

  RegionDto createRegion(CreateRegionRequest request);

  RegionDto updateRegion(Long regionId, UpdateRegionRequest request);

  void deleteRegion(Long regionId);
}
