package org.crops.fitserver.domain.region.repository;

import java.util.Optional;
import org.crops.fitserver.domain.region.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {

   Optional<Region> findByDisplayName(String displayName );
}
