import React from "react";
import jwt from 'jsonwebtoken';

// token에서 userId를 얻어내기 위한 기능
export const getUserIdFromToken = () => {
    // 로컬 스토리지에서 토큰을 가져옴
    const token = localStorage.getItem('token');
    // 토큰이 없을경우 비회원이기 때문에 null을 리턴
    if(!token) return null;

    try {
        const decodedToken = jwt.decode(token);
        return decodedToken.id;
    }catch (error){
        console.error("토큰 디코딩에 실패했습니다 : ", error);
        return null;
    }
}

// 백서버에서 리턴된 token을 클라이언트의 로컬스토리지에 저장하는 기능
export const setToken = (token) => {
    localStorage.setItem("authToken", token);
}
