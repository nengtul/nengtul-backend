package kr.zb.nengtul.recipe.domain.constants;

import lombok.Getter;

@Getter
public enum RecipeCategory {

    SIDE_DISH("밑반찬"),
    MAIN_SIDE_DISH("메인 반찬"),
    KOREAN_SOUP("국/탕"),
    STEW("찌개"),
    DESSERT("디저트"),
    NOODLES_DUMPLINGS("면/만두"),
    RICE_PORRIDGE_RICE_CAKE("밥/죽/떡"),
    FUSION("퓨전"),
    KIMCHI_SALTED_FISH_SAUCES("김치/젓갈/장류"),
    SEASONING_SAUCE_JAM("양념/소스/잼"),
    SALAD("샐러드"),
    SOUP("스프"),
    BREAD("빵"),
    SNACKS("과자"),
    TEA_DRINK("차/음료/술"),
    WESTERN_FOOD("양식"),
    ETC("기타"),
    NONE("없음")
    ;

    final String korean;

    RecipeCategory(String korean) {
        this.korean = korean;
    }
}
