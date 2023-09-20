import React, {useState} from 'react';
import {deleteToken} from "./TokenService";
import {useNavigate} from "react-router-dom";
import {MessageModal} from "./ModalSrvice";

const Logout = () => {
    // 모달을 사용하기 위한 상태값
    const [showModal, setShowModal] = useState(false);
    const [message, setMessage] = useState("");

    const navigate = useNavigate();
    const handleLogout = () => {
        deleteToken();
        setMessage("로그아웃 되었습니다.");
        setShowModal(true);
    }

    const navigateToLogin = () => {
        navigate('/login');
    }

    return(
        <>
            <button type="button" className="btn btn-danger btn-lg" onClick={handleLogout}>
                로그아웃
            </button>
            <MessageModal
                show={showModal}
                onHide={() => {
                    setShowModal(false);
                    navigateToLogin();
                }}
                message={message}
            />
        </>
    );
}

export default Logout;