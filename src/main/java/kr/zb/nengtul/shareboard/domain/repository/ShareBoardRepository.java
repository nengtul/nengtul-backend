package kr.zb.nengtul.shareboard.domain.repository;

import java.util.List;
import java.util.Optional;
import kr.zb.nengtul.shareboard.domain.entity.ShareBoard;
import kr.zb.nengtul.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareBoardRepository extends JpaRepository<ShareBoard, Long> {

  List<ShareBoard> findByLatBetweenAndLonBetween(double minLat, double maxLat, double minLon,
      double maxLon);

  List<ShareBoard> findByLatBetweenAndLonBetweenAndClosed(double minLat, double maxLat,
      double minLon, double maxLon, boolean isClosed);

  List<ShareBoard> findAllByUser(User user);

  Optional<ShareBoard> findByIdAndUser(Long id, User user);

  int countByUserIdAndClosed(Long userId, boolean closed);

}
