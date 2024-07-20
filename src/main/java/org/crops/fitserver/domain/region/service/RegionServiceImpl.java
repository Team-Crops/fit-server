package org.crops.fitserver.domain.region.service;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.crops.fitserver.domain.region.domain.Region;
import org.crops.fitserver.domain.region.dto.RegionDto;
import org.crops.fitserver.domain.region.dto.request.CreateRegionRequest;
import org.crops.fitserver.domain.region.dto.request.UpdateRegionRequest;
import org.crops.fitserver.domain.region.repository.RegionRepository;
import org.crops.fitserver.global.exception.BusinessException;
import org.crops.fitserver.global.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

  private final RegionRepository regionRepository;

  @Override
  public List<RegionDto> getRegionList() {
    return regionRepository.findAll().stream().map(RegionDto::from).toList();
  }

  @Override
  @Transactional
  public RegionDto createRegion(CreateRegionRequest request) {
    if (regionRepository.existsByDisplayName(request.displayName())) {
      throw new BusinessException(ErrorCode.DUPLICATED_RESOURCE_EXCEPTION);
    }

    var region = Region.builder()
        .displayName(request.displayName())
        .build();

    return RegionDto.from(regionRepository.save(region));
  }

  @Override
  @Transactional
  public RegionDto updateRegion(Long regionId, UpdateRegionRequest updateRegionRequest) {
    var region = regionRepository.findById(regionId)
        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION));
    if (region.getDisplayName().equals(updateRegionRequest.displayName())) {
      return RegionDto.from(region);
    }

    if (regionRepository.existsByDisplayName(updateRegionRequest.displayName())) {
      throw new BusinessException(ErrorCode.DUPLICATED_RESOURCE_EXCEPTION);
    }

    var newDisplayName = updateRegionRequest.displayName();
    region.updateDisplayName(newDisplayName);
    return RegionDto.from(regionRepository.save(region));
  }

  @Override
  public void deleteRegion(Long regionId) {
    regionRepository.deleteById(regionId);
  }
}
