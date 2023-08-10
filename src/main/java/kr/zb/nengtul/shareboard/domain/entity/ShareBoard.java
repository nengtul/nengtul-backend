package kr.zb.nengtul.shareboard.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import kr.zb.nengtul.global.entity.BaseTimeEntity;
import kr.zb.nengtul.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Getter
@Setter
public class ShareBoard extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonManagedReference
  @ManyToOne
  private User user;

  private String title;
  private String content;
  private String place;

  private String shareImg;

  private Long price;

  private double lat;//위도

  private double lon;//경도

  @Column(columnDefinition = "boolean default false", nullable = false)
  private boolean closed; //거래 완료 여부
}



