import React from 'react';
import { Link, useNavigate } from "react-router-dom";
import { isTokenValid, getToken } from "../common/TokenService";
import Logout from "../common/Logout";
import jwt from "jsonwebtoken";
import "./Header.css";

const Header = () => {
    const navigate = useNavigate();
    const token = getToken();

    // 권한 확인
    const hasAdminRole = () => {
        try {
            const decoded = jwt.decode(token);
            return decoded.roles && decoded.roles.includes("ROLE_ADMIN");
        } catch (error) {
            return false;
        }
    };

    const handleMyPageClick = () => {
        if (hasAdminRole()) {
            navigate("/adminpage");
        } else {
            navigate("/userpage");
        }
    };

    return (
        <div className="header">
            <div className="shortnee">
                <Link to="/">SHORTNEE</Link>
            </div>

            <div>
                {!isTokenValid() ? (
                    <>
                        <Link to="/login" className="btn btn-primary btn-lg">로그인</Link>
                        <Link to="/signup" className="btn btn-secondary btn-lg">회원가입</Link>
                    </>
                ) : (
                    <>
                        <button onClick={handleMyPageClick} className="btn btn-info btn-lg">
                            {hasAdminRole() ? "Admin Page" : "My Page"}
                        </button>
                        <Logout />
                    </>
                )}
            </div>
        </div>
    );
};

export default Header;
