package kr.zb.nengtul.favorite.domain.repository;

import java.util.List;
import kr.zb.nengtul.favorite.domain.entity.Favorite;
import kr.zb.nengtul.user.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Page<Favorite> findAllByUserId(Long userId, Pageable pageable);

    Optional<Favorite> findByUserIdAndPublisherId(Long userId, Long publisherId);

    List<Favorite> findByPublisher(User publisher);
    int countByUserId(Long userId);
}
