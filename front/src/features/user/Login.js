import React, {useState} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import {ENDPOINTS} from "../../common/ApiEndpoints";
import {setToken} from "../../common/TokenService";
import {Button, Form} from "react-bootstrap";

const Login = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

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
                navigate("/");
            }else {
                throw new Error("토큰이 없습니다.");
            }
        }catch (error){
            console.error("로그인 실패", error.message);
        }
    }

    return (
        <div>
            <h2>로그인</h2>
            <Form onSubmit={loginSubmit}>
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

                <Button variant="primary" type="submit">
                     로그인
                </Button>
            </Form>
        </div>
    );
}

export default Login;