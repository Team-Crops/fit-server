package org.crops.fitserver.domain.alarm.repository;

import java.util.List;
import org.crops.fitserver.domain.alarm.domain.Alarm;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

  @Query("select a from Alarm a "
      + "WHERE a.user.id = :userId "
      + "order by a.id DESC")
  Slice<Alarm> getAllByUserIdRecent(long userId, Pageable pageable);

  @Modifying
  @Query("update Alarm a set a.isRead = true where a.id in :idList")
  void updateReadBuIdList(List<Long> idList);
}
