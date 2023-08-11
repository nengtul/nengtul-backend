package kr.zb.nengtul.favorite.controller;

import java.util.List;
import java.util.Objects;
import kr.zb.nengtul.favorite.service.FavoriteService;
import kr.zb.nengtul.global.exception.CustomException;
import kr.zb.nengtul.global.exception.ErrorCode;
import kr.zb.nengtul.recipe.domain.dto.PushRecipeDto;
import kr.zb.nengtul.recipe.domain.entity.RecipeDocument;
import kr.zb.nengtul.recipe.service.RecipeService;
import kr.zb.nengtul.shareboard.domain.dto.PushShareBoardDto;
import kr.zb.nengtul.shareboard.domain.entity.ShareBoard;
import kr.zb.nengtul.shareboard.service.ShareBoardService;
import kr.zb.nengtul.user.domain.entity.User;
import kr.zb.nengtul.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class PushFavoriteController {

    private final UserService userService;
    private final FavoriteService favoriteService;
    private final ShareBoardService shareBoardService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RecipeService recipeService;

    @MessageMapping("/favorite/push/shareboards/{shareBoardId}")
    public void pushShareBoard(
            SimpMessageHeaderAccessor accessor,
            @DestinationVariable("shareBoardId") Long shareBoardId
    ) {

        String email = userService.getEmailByAccessor(accessor);
        User user = userService.findUserByEmail(email);

        ShareBoard shareBoard = shareBoardService.findById(shareBoardId);

        if (!Objects.equals(shareBoard.getUser().getId(), user.getId())) {
            throw new CustomException(ErrorCode.NOT_OWNER_OF_THE_POST);
        }

        List<Long> subscriberIds = favoriteService.getSubscriberIds(user);

        subscriberIds.forEach(
                subscriberId -> simpMessagingTemplate.convertAndSend(
                        "/sub/favorite/push/shareboard/users/" + subscriberId,
                        PushShareBoardDto.fromEntity(shareBoard)
                )
        );
    }

    @MessageMapping("/favorite/push/recipe/{recipeId}")
    public void pushRecipe(
            SimpMessageHeaderAccessor accessor,
            @DestinationVariable("recipeId") String recipeId
    ) {

        String email = userService.getEmailByAccessor(accessor);
        User user = userService.findUserByEmail(email);

        RecipeDocument recipe = recipeService.findById(recipeId);

        if (!Objects.equals(recipe.getUserId(), user.getId())) {
            throw new CustomException(ErrorCode.NOT_OWNER_OF_THE_POST);
        }

        List<Long> subscriberIds = favoriteService.getSubscriberIds(user);

        subscriberIds.forEach(
                subscriberId -> simpMessagingTemplate.convertAndSend(
                        "/sub/favorite/push/recipe/users/" + subscriberId,
                        PushRecipeDto.fromEntity(recipe, user)
                )
        );
    }
}
