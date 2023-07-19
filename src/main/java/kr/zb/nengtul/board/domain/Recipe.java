package kr.zb.nengtul.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.zb.nengtul.global.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@Entity
@ToString
public class Recipe extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String intro;
    private String ingredient;
    @Column(columnDefinition = "TEXT")

    private String cookingStep;
    @Column(columnDefinition = "TEXT")
    private String imageUrl;
    private String cookingTime;
    private String serving;
    @Column(columnDefinition = "VARCHAR(50) DEFAULT '-'")
    private String category;
    private String videoUrl;
    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long viewCount;


}
