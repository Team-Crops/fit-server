package org.crops.fitserver.domain.school.repository;

import java.util.List;
import org.crops.fitserver.domain.school.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
  List<School> findAllByNameStartsWith(String keyword);
}
