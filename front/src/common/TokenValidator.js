import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
import {deleteToken, isTokenValid} from "./TokenService";

const TokenValidator = () => {
    const navigate = useNavigate();

    useEffect(() => {
        // 로그아웃 플래그가 로컬스토리지에 저장되어 있다면 return
        if(localStorage.getItem('logoutFlag')){
            localStorage.removeItem('logoutFlag');
            return;
        }

        const tokenStatus = isTokenValid();

        // 토큰이 없는 경우(비회원) 아무런 동작도 하지 않음
        if(tokenStatus === null) return;

        if(!tokenStatus){
            alert("로그인 시간이 만료되었습니다. 다시 로그인해주세요.");
            deleteToken();
            navigate('/login');
        }
    }, [navigate]);
}

export default TokenValidator;