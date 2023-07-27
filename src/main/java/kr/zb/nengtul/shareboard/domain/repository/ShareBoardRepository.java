package kr.zb.nengtul.shareboard.domain.repository;

import java.util.List;
import kr.zb.nengtul.shareboard.domain.entity.ShareBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareBoardRepository extends JpaRepository<ShareBoard, Long> {
  List<ShareBoard> findByLatBetweenAndLonBetween(double minLat, double maxLat, double minLon, double maxLon);
  List<ShareBoard> findByLatBetweenAndLonBetweenAndClosed(double minLat, double maxLat, double minLon, double maxLon, boolean isClosed);

}
