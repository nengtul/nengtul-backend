package kr.zb.nengtul.shareboard.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.zb.nengtul.global.entity.BaseTimeEntity;
import kr.zb.nengtul.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class ShareBoard extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonManagedReference
  @ManyToOne
  private User user;

  private String title;

  private String shareImg;

  private String content;

  private Long price;

  private double lat;//위도

  private double lon;//경도

  @Column(columnDefinition = "bigint default 0", nullable = false)
  private Long views;

}
