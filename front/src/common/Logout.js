import React from 'react';
import {deleteToken} from "./TokenService";
import {useNavigate} from "react-router-dom";

const Logout = () => {
    const navigate = useNavigate();
    const handleLogout = () => {
        alert("로그아웃 되었습니다.")
        deleteToken();
        navigate('/login');
    }

    return(
        <button type="button" className="btn btn-danger btn-lg" onClick={handleLogout}>
            로그아웃
        </button>
    );
}

export default Logout;