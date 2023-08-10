package kr.zb.nengtul.notice.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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
public class Notice extends BaseTimeEntity {
  @JsonIgnore
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonManagedReference
  @ManyToOne
  private User user;

  private String title;

  @Lob
  @Column(name = "content", columnDefinition = "BLOB")
  private String content;

  private String noticeImg;

  @Column(columnDefinition = "bigint default 0", nullable = false)
  private Long viewCount;

  public void setTitle(String title) {
    this.title = title;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setNoticeImg(String noticeImg) {
    this.noticeImg = noticeImg;
  }

  public void setViewCount(Long viewCount) {
    this.viewCount = viewCount;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
