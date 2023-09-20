import React, { useState, useEffect } from 'react';
import axios from 'axios';
import {getToken} from "../../common/TokenService";
import {ENDPOINTS} from "../../common/ApiEndpoints";
import DeleteUrl from "../../common/DeleteUrl";
import TokenValidator from "../../common/TokenValidator";
import "./UrlListForAdmin.css"
import {MessageModal} from "../../common/ModalSrvice";

const UrlListForUser = () => {
    const [urlList, setUrlList] = useState([]);
    const [editingId, setEditingId] = useState(null);
    const [editedOriginUrl, setEditedOriginUrl] = useState("");
    const [searchEmail, setSearchEmail] = useState("");

    // 모달을 사용하기 위한 상태값
    const [showModal, setShowModal] = useState(false);
    const [message, setMessage] = useState("");

    useEffect(() => {
        loadUrls();
    },[]);

    const loadUrls = () => {
        // 사용자의 URL 리스트를 불러옵니다.
        axios.get(ENDPOINTS.URL_LIST_FOR_ADMIN,{
            headers: {
                'Authorization': 'Bearer ' + getToken()
            }
        })
            .then(response => {
                setUrlList(response.data);
            })
            .catch(error => {
                console.error("URL 리스트를 가져오는데 실패했습니다.", error);
                setMessage("URL 리스트를 가져오는데 실패했습니다.");
                setShowModal(true);
            });
    };

    const handleEdit = (id, originUrl) => {
        setEditingId(id);
        setEditedOriginUrl(originUrl);
    }

    const handleUpdate = (id, userId) => {
        const urlDTO = {
            id: id,
            originUrl: editedOriginUrl,
            userId: userId,
            requestCount: null
        };

        axios.patch(ENDPOINTS.URL_UPDATE, urlDTO, {
            headers: {
                'Authorization': 'Bearer ' + getToken()
            }
        })
            .then(response => {
                setMessage("URL이 수정되었습니다.");
                setShowModal(true);
                loadUrls();
                setEditingId(null);
            })
            .catch(error => {
                console.error("URL 업데이트 실패", error);
                setMessage("URL을 업데이트 하는데 실패했습니다.");
                setShowModal(true);
            });
    }

    const searchUrlsByEmail = (email) => {
        // 이메일을 기반으로 URL 리스트를 검색하는 함수
        axios.get(ENDPOINTS.URL_LIST_BY_EMAIL_FOR_ADMIN + searchEmail, {
            headers: {
                'Authorization': 'Bearer ' + getToken()
            }
        })
            .then(response => {
                setUrlList(response.data);
            })
            .catch(error => {
                console.error("이메일로 URL 리스트를 가져오는데 실패했습니다.", error);
                setMessage("이메일로 URL 리스트를 가져오는데 실패했습니다.");
                setShowModal(true);
            });
    };


    return (
        <div className="urlListForAdmin">
            <TokenValidator />
            <div className="searchBar">
                <button className="btn btn-secondary" type="button" onClick={loadUrls}>전체 리스트 보기</button>
                <div className="emailInput">
                    <input
                        className="emailInputBox"
                        type="text"
                        placeholder="이메일 검색"
                        value={searchEmail}
                        onChange={e => setSearchEmail(e.target.value)}
                    />
                    <button className="emailInputButton"
                            type="button"
                            onClick={() => searchEmail ? searchUrlsByEmail(searchEmail) : loadUrls()}>검색</button>
                </div>
            </div>
            <table className="table">
                <thead>
                <tr className="table-primary">
                    <th>원래 URL</th>
                    <th>단축 URL</th>
                    <th>접속 횟수</th>
                    <th>수정</th>
                    <th>삭제</th>
                </tr>
                </thead>
                <tbody>
                {urlList.map(url => (
                    <tr key={url.id}>
                        <td className="wrapText">
                            {editingId === url.id ? (
                                <input
                                    value={editedOriginUrl}
                                    onChange={e => setEditedOriginUrl(e.target.value)}
                                />
                            ) : (
                                <>{url.originUrl}</>
                            )}
                        </td>
                        <td className="wrapText">{url.shortenUrl}</td>
                        <td>{url.requestCount}</td>
                        <td>
                            {editingId === url.id ? (
                                <button type="button" className="btn btn-info" onClick={() => handleUpdate(url.id, url.userId)}>수정하기</button>
                            ) : (
                                <button type="button" className="btn btn-warning" onClick={() => handleEdit(url.id, url.originUrl)}>수정</button>
                            )}
                        </td>
                        <td>
                            <DeleteUrl id={url.id}
                                       originUrl={url.originUrl}
                                       afterDelete={loadUrls} />
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            <MessageModal
                show={showModal}
                onHide={() => setShowModal(false)}
                message={message}
            />
        </div>
    );
};

export default UrlListForUser;
