import React, { useState, useEffect } from 'react';
import axios from 'axios';
import {getToken, getUserIdFromToken} from "../../common/TokenService";
import {ENDPOINTS} from "../../common/ApiEndpoints";
import DeleteUrl from "../../common/DeleteUrl";
import TokenValidator from "../../common/TokenValidator";

const UrlListForUser = () => {
    const [urlList, setUrlList] = useState([]);
    const [userId, setUserId] = useState(getUserIdFromToken());

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
        <div>
            <TokenValidator />
            <table>
                <thead>
                <tr>
                    <th>원래 URL</th>
                    <th>단축 URL</th>
                    <th>접속 횟수</th>
                    <th>삭제</th>
                </tr>
                </thead>
                <tbody>
                {urlList.map(url => (
                    <tr key={url.id}>
                        <td>{url.originUrl}</td>
                        <td>{url.shortenUrl}</td>
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
    );
};

export default UrlListForUser;
