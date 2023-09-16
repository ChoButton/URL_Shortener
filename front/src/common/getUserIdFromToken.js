import React from "react";
import jwt from 'jsonwebtoken';

const getUserIdFromToken = () => {
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

export default getUserIdFromToken;