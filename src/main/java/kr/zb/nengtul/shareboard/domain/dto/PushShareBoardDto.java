package kr.zb.nengtul.shareboard.domain.dto;

import kr.zb.nengtul.shareboard.domain.entity.ShareBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PushShareBoardDto {

    private Long shareBoardId;
    private String title;
    private String publisherNickname;

    public static PushShareBoardDto fromEntity(ShareBoard shareBoard) {
        return PushShareBoardDto.builder()
                .shareBoardId(shareBoard.getId())
                .title(shareBoard.getTitle())
                .publisherNickname(shareBoard.getUser().getNickname())
                .build();
    }
}
