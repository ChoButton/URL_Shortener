import React, { useState, useEffect } from 'react';
import axios from 'axios';
import {getToken, getUserEmailFromToken, getUserIdFromToken} from "../../common/TokenService";
import {ENDPOINTS} from "../../common/ApiEndpoints";
import DeleteUrl from "../../common/DeleteUrl";
import TokenValidator from "../../common/TokenValidator";
import {Button} from "react-bootstrap";
import {useNavigate} from "react-router-dom";
import "./UrlListForUser.css";

const UrlListForUser = () => {
    const [urlList, setUrlList] = useState([]);
    const [userId, setUserId] = useState(getUserIdFromToken());
    const [email, setEmail] = useState(getUserEmailFromToken());

    const navigate = useNavigate();

    useEffect(() => {
        loadUrls();
    },[]);

    const loadUrls = () => {
        // 사용자의 URL 리스트를 불러옵니다.
        axios.get(ENDPOINTS.URL_LIST_FOR_USER + userId, {
            headers: {
                'Authorization': 'Bearer ' + getToken()
            }
        })
            .then(response => {
                setUrlList(response.data);
            })
            .catch(error => {
                console.error("URL 리스트를 가져오는데 실패했습니다.", error);
            });
    };



    return (
        <div className="urlListForUser">
            <TokenValidator />
            <div>
                <h2>
                    {email}님이 등록하신 URL목록입니다.
                    <div className="userUpdateButtonContainer">
                        <br/>
                        <Button onClick={() => {navigate("/userUpdate")}}
                                className="userUpdateButton">
                            회원정보수정
                        </Button>
                    </div>
                </h2>
            </div>
            <br/>
            <div>
                <table className="table">
                    <thead>
                    <tr className="table-primary">
                        <th>원래 URL</th>
                        <th>단축 URL</th>
                        <th>접속 횟수</th>
                        <th>삭제</th>
                    </tr>
                    </thead>
                    <tbody>
                    {urlList.map(url => (
                        <tr key={url.id}>
                            <td className="wrapText">{url.originUrl}</td>
                            <td className="wrapText">{url.shortenUrl}</td>
                            <td>{url.requestCount}</td>
                            <td>
                                <DeleteUrl id={url.id}
                                           afterDelete={loadUrls}
                                />
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default UrlListForUser;
