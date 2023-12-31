import React, { useState, useEffect } from 'react';
import axios from 'axios';
import {getToken, getUserEmailFromToken, getUserIdFromToken} from "../../common/TokenService";
import {ENDPOINTS} from "../../common/ApiEndpoints";
import DeleteUrl from "../../common/DeleteUrl";
import {Button} from "react-bootstrap";
import {useNavigate} from "react-router-dom";
import "./UrlListForUser.css";
import {MessageModal} from "../../common/ModalSrvice";

const UrlListForUser = () => {
    const [urlList, setUrlList] = useState([]);
    const [userId, setUserId] = useState(getUserIdFromToken());
    const [email, setEmail] = useState(getUserEmailFromToken());

    // 모달을 사용하기 위한 상태값
    const [showModal, setShowModal] = useState(false);
    const [message, setMessage] = useState("");

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
                console.error("등록된 URL이 없습니다.", error);
                setMessage("등록된 URL이 없습니다.");
                setShowModal(true);
            });
    };



    return (
        <div className="urlListForUser">
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
                                           originUrl={url.originUrl}
                                           afterDelete={loadUrls}
                                />
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
            <MessageModal
                show={showModal}
                onHide={() => setShowModal(false)}
                message={message}
            />
        </div>
    );
};

export default UrlListForUser;
