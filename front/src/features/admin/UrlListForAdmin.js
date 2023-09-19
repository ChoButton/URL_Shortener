import React, { useState, useEffect } from 'react';
import axios from 'axios';
import {getToken, getUserIdFromToken} from "../../common/TokenService";
import {ENDPOINTS} from "../../common/ApiEndpoints";
import DeleteUrl from "../../common/DeleteUrl";
import TokenValidator from "../../common/TokenValidator";

const UrlListForUser = () => {
    const [urlList, setUrlList] = useState([]);
    const [editingId, setEditingId] = useState(null);
    const [editedOriginUrl, setEditedOriginUrl] = useState("");

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
                loadUrls();
                setEditingId(null);
            })
            .catch(error => {
                console.error("URL 업데이트 실패", error);
            });
    }


    return (
        <div>
            <TokenValidator />
            <table>
                <thead>
                <tr>
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
                        <td>
                            {editingId === url.id ? (
                                <input
                                    value={editedOriginUrl}
                                    onChange={e => setEditedOriginUrl(e.target.value)}
                                />
                            ) : (
                                <>{url.originUrl}</>
                            )}
                        </td>
                        <td>{url.shortenUrl}</td>
                        <td>{url.requestCount}</td>
                        <td>
                            {editingId === url.id ? (
                                <button type="button" className="btn btn-info" onClick={() => handleUpdate(url.id, url.userId)}>수정하기</button>
                            ) : (
                                <button type="button" className="btn btn-warning" onClick={() => handleEdit(url.id, url.originUrl)}>수정</button>
                            )}
                        </td>
                        <td>
                            <DeleteUrl id={url.id} afterDelete={loadUrls} />
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default UrlListForUser;
