import React, { useState } from "react";
import axios from "axios";
import {getUserIdFromToken} from "../../common/TokenService";
import {ENDPOINTS} from "../../common/ApiEndpoints";
import "./ShortenUrl.css";

const ShortenUrl = () => {
    const [originUrl, setOriginUrl] = useState("");

    const shortenUrlSubmit = async () => {
        try {
            // 백서버로 전송할 UrlDTO
            const urlDTO = {
                id: null,
                originUrl: originUrl,
                userId: getUserIdFromToken(),
                requestCount: null,
                shortenUrl: null
            };

            // 백서버로 전송
            const response = await axios.post(ENDPOINTS.CREATE_SHORTEN_URL, urlDTO);

            alert(response.data);
            setOriginUrl("");
        }catch (error){
            console.error("단축URL등록에 실패했습니다.", error);
        }
    }

    return (
        <div className="shotrenUrlContainer">
            <h1 className="shotrenUrlTopText">당신을 기억하는 가장 짧은 방법 <span className="shotrenUrlTopText-s">SHORTNEE</span></h1>
            <br/>
            <input type="text"
                   value={originUrl}
                   placeholder="단축하실 링크를 입력해주세요"
                   onChange={(e) => setOriginUrl(e.target.value)}
                   className="shotrenUrlInputUrlBox"
            />
            <button onClick={shortenUrlSubmit} className="shotrenUrlInputUrlButton">링크단축</button>
            <h2 className="shotrenUrlButttomText">
                <br/>
                긴 URL을 짧은URL로 단축시켜주는 서비스 입니다.<br/>
                회원가입후 서비스를 이용하실 경우 단축한 URL목록 및 접속 횟수를 볼수 있습니다.
            </h2>
        </div>
    );
}

export default ShortenUrl;