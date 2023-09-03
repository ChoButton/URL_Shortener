package com.chobutton.back.util;

import com.chobutton.back.exception.InvalidBase56CharacterException;
import org.springframework.stereotype.Component;

@Component
public class Base56Util {

    /*
     * 원 문자열 "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789"
     * 1씩 증가하는 PK값으로 shortenUrl주소를 인코딩 하기 때문에 예측하기 어렵게 하기 위하여 문자열을 랜덤하게 조합
     */
    static final char[] BASE56 = "2ZjDaHJXYkqh7mLz9bEF5iPxKlp8wrByoWcGvNCtUfA4gsVn6Mh3RSeQ".toCharArray();
    // 자주 사용되는 숫자 상수화
    private static final int BASE56_SIZE = 56;
    // Ecoding 메서드가 호출 될때마다 생성되기 때문에 상수화
    private static final String BASE56_STRING = new String(BASE56);

    /*
     * 등록된 originUrl의 id값 파라미터로 받아 인코딩 해주는 메서드
     * id값을 56으로 나눈 나머지값이 0이 될때까지 아래 로직이 수행됨.
     */
    public static String base56Encoding(int originUrlId) {
        final StringBuilder sb = new StringBuilder();
        do {
            int i = originUrlId % BASE56_SIZE;
            sb.append(BASE56[i]);
            originUrlId /= BASE56_SIZE;
        } while (originUrlId > 0);
        return sb.toString();
    }

    /*
     * 입력된 shortenUrl을 디코딩하여 id값 얻어내는 메서드
     * 입력된 shortenUrl에 인코딩에 포함되지 않은 문자가 입력될경우
     * 익셉션 발생
     */
    public static int base56Decoding(String shortenUrl) {
        int originUrlId = 0;
        int placeValue = 1;
        for (int i = 0; i < shortenUrl.length(); i++) {
            int charValue = BASE56_STRING.indexOf(shortenUrl.charAt(i));
            if (charValue == -1){
                throw new InvalidBase56CharacterException("등록되지 않은 URL입니다.");
            }
            originUrlId += charValue * placeValue;
            placeValue *= BASE56_SIZE;
        }
        return originUrlId;
    }

}
