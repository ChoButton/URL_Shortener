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

// 토큰에서 userEmail을 얻어오는 기능
export const getUserEmailFromToken = () => {
    // 로컬 스토리지에서 토큰을 가져옴
    const token = localStorage.getItem('token');
    // 토큰이 없을경우 비회원이기 때문에 null을 리턴
    if(!token) return null;

    try {
        const decodedToken = jwt.decode(token);
        return decodedToken.sub;
    }catch (error){
        console.error("토큰 디코딩에 실패했습니다 : ", error);
        return null;
    }
}

// 백서버에서 리턴된 token을 클라이언트의 로컬스토리지에 저장하는 기능
export const setToken = (token) => {
    localStorage.setItem("token", token);
}

// 로컬스토리지에서 토큰을 얻어오는 기능
export const getToken = () => {
    return localStorage.getItem('token');
}

// 필요시 로컬스토리지에서 토큰을 삭제하는 기능
export const deleteToken = () => {
    localStorage.removeItem('token');
}

// 토큰에서 권한정보를 얻어오는 기능
export const getRolesFromToken = () => {
    const token = localStorage.getItem("token");

    if(!token) return null;

    try {
        const decodedToken = jwt.decode(token);
        return decodedToken.roles;
    }catch (error){
        console.error("토큰 디코딩에 실패했습니다 : ", error);
        return null;
    }
}

// 토큰 유효시간 만료 확인 기능
export const isTokenValid = () => {
    const token = localStorage.getItem("token");

    if (!token) return null;

    try {
        const decoded = jwt.decode(token);
        // 현재시간을 초 단위로 변환
        const currentTime = Date.now() / 1000;

        return decoded.exp > currentTime;
    } catch (e) {
        return false;
    }
}