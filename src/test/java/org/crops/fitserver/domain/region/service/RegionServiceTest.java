package org.crops.fitserver.domain.region.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.crops.fitserver.domain.region.domain.Region;
import org.crops.fitserver.domain.region.dto.request.CreateRegionRequest;
import org.crops.fitserver.domain.region.dto.request.UpdateRegionRequest;
import org.crops.fitserver.domain.region.repository.RegionRepository;
import org.crops.fitserver.global.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class RegionServiceTest {

  MockMvc mockMvc;

  @InjectMocks
  RegionServiceImpl target;

  @Mock
  RegionRepository regionRepository;

  private final Region region = Region.builder()
      .id(1L)
      .displayName("test")
      .build();

  @BeforeEach
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(target).build();
  }

  @Test
  public void get_region_list() {
    // given
    when(regionRepository.findAll()).thenReturn(List.of(region));

    // when
    var result =  target.getRegionList();

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getDisplayName()).isEqualTo("test");
  }

  @Test
  public void create_region() {
    // given
    when(regionRepository.findByDisplayName(any())).thenReturn(Optional.empty());
    when(regionRepository.save(any())).thenReturn(region);

    // when
    var result = target.createRegion(new CreateRegionRequest("test"));

    // then
    assertThat(result.getDisplayName()).isEqualTo("test");
  }

  @Test
  public void create_region_fail_exist_display_name() {
    // given
    when(regionRepository.findByDisplayName(any())).thenReturn(Optional.of(region));

    // then
    assertThatThrownBy(() -> target.createRegion(CreateRegionRequest.builder().displayName("test").build()))
        .isInstanceOf(BusinessException.class)
        ;
  }

  @Test
  public void update_region_failed_not_found() {
    // given
    when(regionRepository.findById(any())).thenReturn(Optional.empty());

    // when, then
    assertThatThrownBy(() -> target.updateRegion(1L, UpdateRegionRequest.builder().displayName("test").build()))
        .isInstanceOf(BusinessException.class)
        ;
  }

  @Test
  public void update_region_failed_duplicate_display_name() {
    // given
    var newDisplayName = "test";
    var region2 = Region.builder()
        .id(2L)
        .displayName("test1")
        .build();
    when(regionRepository.findById(any())).thenReturn(Optional.of(region2));
    when(regionRepository.findByDisplayName(any())).thenReturn(Optional.of(region));



    // when, then
    assertThatThrownBy(() -> target.updateRegion(region2.getId(), UpdateRegionRequest.builder().displayName(newDisplayName).build()))
        .isInstanceOf(BusinessException.class)
        ;
  }

  @Test
  public void update_region() {
    var newDisplayName = "test1";
    var newRegion = Region.builder()
        .id(region.getId())
        .displayName(newDisplayName)
        .build();
    // given
    when(regionRepository.findById(any())).thenReturn(Optional.of(region));
    when(regionRepository.findByDisplayName(any())).thenReturn(Optional.empty());
    when(regionRepository.save(any())).thenReturn(newRegion);

    // when
    var result = target.updateRegion(region.getId(), UpdateRegionRequest.builder().displayName(newDisplayName).build());

    // then
    assertThat(result.getDisplayName()).isEqualTo(newDisplayName);
  }

  @Test
  public void delete_region() {
    // given
    // when
    target.deleteRegion(1L);
    // then
  }
}
