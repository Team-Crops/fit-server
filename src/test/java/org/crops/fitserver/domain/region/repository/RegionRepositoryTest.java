package org.crops.fitserver.domain.region.repository;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.RequiredArgsConstructor;
import org.crops.fitserver.config.PersistenceConfig;
import org.crops.fitserver.config.QueryDslTestConfig;
import org.crops.fitserver.domain.region.domain.Region;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

@DataJpaTest
@Import({QueryDslTestConfig.class, PersistenceConfig.class})
@ActiveProfiles("test")
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RegionRepositoryTest {


  private final RegionRepository regionRepository;

  @Test
  public void 지역_저장_및_조회() {
    // given
    Region region = Region.builder()
        .displayName("서울")
        .build();

    // when
    regionRepository.save(region);
    var regions = regionRepository.findAll();

    // then
    assertThat(regions).hasSize(1);
  }

  @Test
  public void 지역_수정() {
    // given
    Region region = Region.builder()
        .displayName("서울")
        .build();
    regionRepository.save(region);
    var regions = regionRepository.findAll();
    assertThat(regions).hasSize(1);
    // when
    region.updateDisplayName("부산");
    regionRepository.save(region);

    var regions2 = regionRepository.findAll();
    // then
    assertThat(regions2).hasSize(1);
    assertThat(regions2.get(0).getDisplayName()).isEqualTo("부산");
  }

  @Test
  public void 지역_삭제() {
    // given
    Region region = Region.builder()
        .displayName("서울")
        .build();
    regionRepository.save(region);
    var regions = regionRepository.findAll();
    assertThat(regions).hasSize(1);
    // when
    regionRepository.delete(region);
    var regions2 = regionRepository.findAll();
    // then
    assertThat(regions2).hasSize(0);
  }

  @Test
  public void 지역_삭제_아무것도_삭제하지_못함(){
    // given
    Region region = Region.builder()
        .displayName("서울")
        .build();
    regionRepository.save(region);
    var regions = regionRepository.findAll();
    assertThat(regions).hasSize(1);
    // when
    regionRepository.deleteById(100L);
    var regions2 = regionRepository.findAll();
    // then
    assertThat(regions2).hasSize(1);
  }

}
