package com.chobutton.back.exception;

public class InvalidBase56CharacterException extends RuntimeException{

    // 생성자에 익셉션 사유를 적을수 있도록 메세지를 적음
    public InvalidBase56CharacterException(String message){
        super(message);
    }
}
