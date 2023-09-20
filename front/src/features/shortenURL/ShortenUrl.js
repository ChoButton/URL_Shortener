import React, { useState } from "react";
import axios from "axios";
import { getUserIdFromToken } from "../../common/TokenService";
import { ENDPOINTS } from "../../common/ApiEndpoints";
import "./ShortenUrl.css";
import {useNavigate} from "react-router-dom";

const ShortenUrl = () => {
    const [originUrl, setOriginUrl] = useState("");
    const [shortenedUrl, setShortenedUrl] = useState("");

    const navigate = useNavigate();

    const shortenUrlSubmit = async () => {
        try {
            const urlDTO = {
                id: null,
                originUrl: originUrl,
                userId: getUserIdFromToken(),
                requestCount: null,
                shortenUrl: null
            };

            const response = await axios.post(ENDPOINTS.CREATE_SHORTEN_URL, urlDTO);
            setShortenedUrl(response.data);
        } catch (error) {
            console.error("단축URL등록에 실패했습니다.", error);
        }
    }

    const resetComponent = () => {
        setOriginUrl("");
        setShortenedUrl("");
    }

    const isNonMember = () => {
        return !localStorage.getItem('token');
    }

    const handleSignUp = () => {
        navigate("/signup", { state: { userOriginUrl: originUrl } });
    }

    return (
        <div className="shotrenUrl">
            <h1 className="shotrenUrlTopText">당신을 기억하는 가장 짧은 방법 <span className="shotrenUrlTopText-s">SHORTNEE</span></h1>
            <h2 className="shotrenUrlBottomText"> 긴 URL을 짧은URL로 단축시켜주는 서비스 입니다. </h2>
            <br />
            {shortenedUrl ? (
                <div className="shortenedUrlResult">
                    단축된 URL: <a href={shortenedUrl} target="_blank" rel="noopener noreferrer">{shortenedUrl}</a>
                </div>
            ) : (
                <>
                    <input type="text"
                           value={originUrl}
                           placeholder="단축하실 링크를 입력해주세요"
                           onChange={(e) => setOriginUrl(e.target.value)}
                           className="shotrenUrlInputBox"
                    />
                    <button onClick={shortenUrlSubmit} className="shotrenUrlInputButton">링크단축</button>
                    <h2 className="shotrenUrlBottomText">
                        <br />
                        회원가입후 서비스를 이용하실 경우 단축한 URL목록 및 접속 횟수를 볼수 있습니다.
                    </h2>
                </>
            )}
            <div className="addUrlAftersignupButton">
                <br/>
                <p className="urlWarning">비회원으로 등록하신 URL은 다시 조회가 불가능하니 꼭 단축된URL을 복사하셔야 합니다.</p>
                <h3>아래 버튼을 통해서 회원가입 하실경우 지금 등록하신 URL이 등록됩니다.</h3>
                {isNonMember() && shortenedUrl && (
                    <button onClick={handleSignUp} className="btn btn-success" id="addUrlAfterSignupButton">회원가입</button>
                )}
            </div>
            <div className="addUrlButton">
                <br/>
                {shortenedUrl && (
                    <button onClick={resetComponent} className="btn btn-warning" id="addUrlButton">URL추가 등록</button>
                )}
            </div>
        </div>
    );
}

export default ShortenUrl;
