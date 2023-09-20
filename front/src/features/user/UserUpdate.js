import React, { useState } from "react";
import axios from "axios";
import 'bootstrap/dist/css/bootstrap.min.css';
import {Form, Button} from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import {ENDPOINTS} from "../../common/ApiEndpoints";
import {getToken, getUserIdFromToken} from "../../common/TokenService";
import jwt from "jsonwebtoken";
import TokenValidator from "../../common/TokenValidator";
import "./UserUpdate.css"
import {MessageModal} from "../../common/ModalSrvice";
import UserAccountDelete from "../../common/UserAccountDelete";

const UserUpdate = () => {
    const [originPassword, setOriginPassword] = useState("");
    const [password, setPassword] = useState("");
    const [checkPassword, setCheckPassword] = useState("");
    const [userId, setUserId] = useState(getUserIdFromToken());

    // 모달을 사용하기 위한 상태값
    const [showModal, setShowModal] = useState(false);
    const [message, setMessage] = useState("");

    const navigate = useNavigate();

    const isPasswordMatched = () =>
        originPassword !== '' &&
        password === checkPassword &&
        password !== '' &&
        checkPassword !== '';

    const updatePasswordSubmit = async (e) => {
        e.preventDefault();
        try {
            const userDTO = {
                id: userId,
                originPassword: originPassword,
                newPassword: password
            };
            const response = await axios.patch(ENDPOINTS.USER_UPDATE, userDTO, {
                headers: {
                        'Authorization': `Bearer ${getToken()}`
                    }
                });

            setMessage("비밀번호가 변경되었습니다.");
            setShowModal(true);
            navigate(`/userpage`);
        } catch (error) {
            console.error("비밀번호 변경에 실패했습니다.");
            setMessage("비밀번호 변경에 실패했습니다.");
            setShowModal(true);
        }
    }

    return (
        <div className="userUpdate">
            <TokenValidator />
            <h2 className="updateName">비밀번호 변경</h2>
            <br/>
            <br/>
            <Form onSubmit={updatePasswordSubmit}>
                <Form.Group className="mb-3" controlId="currentPassword">
                    <Form.Label>현재 비밀번호</Form.Label>
                    <Form.Control type="password"
                                  value={originPassword}
                                  onChange={(e) => setOriginPassword(e.target.value)} />
                </Form.Group>

                <Form.Group className="mb-3" controlId="inputPassword5">
                    <Form.Label>새로운 비밀번호</Form.Label>
                    <Form.Control type="password"
                                  maxLength={8}
                                  value={password}
                                  onChange={(e) => setPassword(e.target.value)} />
                    <Form.Text id="passwordHelpBlock" muted>
                        비밀번호는 최대 8자리까지 입력 가능합니다.
                    </Form.Text>
                </Form.Group>

                <Form.Group className="mb-3" controlId="confirmPassword5">
                    <Form.Label>비밀번호 확인</Form.Label>
                    <Form.Control type="password"
                                  maxLength={8} value={checkPassword}
                                  onChange={(e) => setCheckPassword(e.target.value)} />
                    {checkPassword && !isPasswordMatched() && (
                        <Form.Text style={{ color: 'red' }}>
                            처음 입력한 비밀번호와 일치하지 않습니다.
                        </Form.Text>
                    )}
                </Form.Group>
                <br/>
                <div className="passwordUpdateButtonContainer">
                    <Button className="passwordUpdateButton" variant="primary" type="submit" disabled={!isPasswordMatched()}>
                        비밀번호 변경
                    </Button>
                </div>
            </Form>
            <div className="userAccountDeleteButtonContainer">
                <br/>
                <br/>
                <UserAccountDelete id={userId} />
            </div>
            <MessageModal
                show={showModal}
                onHide={() => setShowModal(false)}
                message={message}
            />
        </div>
    );
}

export default UserUpdate;
