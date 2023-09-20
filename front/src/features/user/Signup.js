import React, { useState } from "react";
import axios from "axios";
import 'bootstrap/dist/css/bootstrap.min.css';
import {Form} from "react-bootstrap";
import {Button} from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import {ENDPOINTS} from "../../common/ApiEndpoints";
import { useLocation } from "react-router-dom";
import "./Signup.css"
import {MessageModal} from "../../common/ModalSrvice";

const Signup = () => {
    const location = useLocation();
    const userOriginUrl = location.state?.userOriginUrl || null;

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [checkPassword, setCheckPassword] = useState("");
    const [originUrl, setOriginUrl] = useState(userOriginUrl);

    // 모달을 사용하기 위한 상태값
    const [showModal, setShowModal] = useState(false);
    const [message, setMessage] = useState("");

    const navigate = useNavigate();

    // 비밀번호를 입력하고 비밀번호 확인과 일치하며, 비밀번호 및 비밀번호 확인 둘다 무언갈 작성해야 true반환
    // 버튼 활성화 기능을 위해 필요한 기능
    const isPasswordMatched = () =>
        password === checkPassword &&
        password !== '' &&
        checkPassword !== '' &&
        email !== '';

    const signupSubmit = async (e) => {
        e.preventDefault();
        try {
            // 회원가입을 위해 백서버로 전송할 userdata
            const userDTO = {
                id: null,
                email: email,
                password: password,
                originUrl: originUrl
            };

            // 백서버로 회원가입 정보 전송
            const response = await axios.post(ENDPOINTS.SIGNUP, userDTO);

            setMessage("회원가입이 완료되었습니다.");
            setShowModal(true);
            // 회원가입 완료후 메인페이지로 이동
            navigate("/");
        }catch (error){
            console.error("회원가입에 실패했습니다.");
            setMessage("회원가입에 실패했습니다.");
            setShowModal(true);
        }
    }

    return (
        <div className="signup">
            <h2 className="signupName">회원가입</h2>
            <br/>
            <br/>
            <Form onSubmit={signupSubmit}>
                <Form.Group className="mb-3" controlId="exampleFormControlInput1">
                    <Form.Label>이메일 주소</Form.Label>
                    <Form.Control type="email"
                                  placeholder="이메일을 입력해주세요"
                                  value={email}
                                  onChange={(e) => setEmail(e.target.value)}
                    />
                </Form.Group>

                <Form.Group className="mb-3" controlId="inputPassword5">
                    <Form.Label>비밀번호</Form.Label>
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
                <div className="signupButtonContainer">
                    <Button variant="primary"
                            type="submit"
                            disabled={!isPasswordMatched()}
                            className="signupButton">
                        회원가입
                    </Button>
                </div>
            </Form>
            <MessageModal
                show={showModal}
                onHide={() => setShowModal(false)}
                message={message}
            />
        </div>
    );
}

export default Signup;