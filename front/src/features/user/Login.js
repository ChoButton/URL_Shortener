import React, {useState} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import {ENDPOINTS} from "../../common/ApiEndpoints";
import {setToken} from "../../common/TokenService";
import {Button, Form} from "react-bootstrap";
import "./Login.css"
import {MessageModal} from "../../common/ModalSrvice";

const Login = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    // 모달을 사용하기 위한 상태값
    const [showModal, setShowModal] = useState(false);
    const [message, setMessage] = useState("");

    const navigate = useNavigate();

    const loginSubmit = async (e) => {
        e.preventDefault();

        try {
            // 로그인을 위해 백서버로 전송할 data
            const userDTO = {
                email: email,
                password: password
            };

            // 백서버로 로그인 정보 전송
            const response = await axios.post(ENDPOINTS.LOGIN, userDTO);
            if(response.data){
                setToken(response.data);
                setMessage("로그인 성공");
                setShowModal(true);
            }else {
                throw new Error("토큰이 없습니다.");
            }
        }catch (error){
            console.error("로그인 실패", error.message);
            setMessage("로그인 실패");
            setShowModal(true);
        }
    }

    const navigateToLogin = () => {
        navigate('/');
    }

    return (
        <div className="login">
                <h2 className="loginName">로그인</h2>
            <br/>
            <br/>
            <Form onSubmit={loginSubmit}>
                <Form.Group className="mb-3" controlId="exampleFormControlInput1">
                    <Form.Label>이메일 주소</Form.Label>
                    <Form.Control type="email"
                                  placeholder="이메일을 입력해주세요"
                                  value={email}
                                  onChange={(e) => setEmail(e.target.value)}
                    />
                </Form.Group>
                <br/>
                <Form.Group className="mb-3" controlId="inputPassword5">
                    <Form.Label>비밀번호</Form.Label>
                    <Form.Control type="password"
                                  minLength={8}
                                  maxLength={12}
                                  value={password}
                                  onChange={(e) => setPassword(e.target.value)}
                                  placeholder="비밀번호를 입력해주세요" />
                    <Form.Text id="passwordHelpBlock" muted>
                        비밀번호는 최초 8자리 최대 12자리까지 입력 가능합니다.
                    </Form.Text>
                </Form.Group>
                <br/>
                <div className="loginButtonContainer">
                    <Button variant="btn" type="submit" className="loginButton">
                         로그인
                    </Button>
                </div>
            </Form>
            <MessageModal
                show={showModal}
                onHide={() => {
                    setShowModal(false);
                    navigateToLogin();
                }}
                message={message}
            />
        </div>
    );
}

export default Login;