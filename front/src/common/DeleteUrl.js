import axios from "axios";
import {ENDPOINTS} from "./ApiEndpoints";
import {getToken} from "./TokenService";
import React, {useState} from "react";
import {ConfirmDeleteModal, MessageModal} from "./ModalSrvice";

const DeleteUrl = ({id, originUrl, afterDelete}) => {
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [showModal, setShowModal] = useState(false);
    const [message, setMessage] = useState("");
    const deleteButton = () => {
        axios.delete(ENDPOINTS.DELETE_URL + id,
            {
                headers: {
                    'Authorization': 'Bearer ' + getToken()
                }
            })
            .then(() => {
                setMessage("URL을 삭제했습니다.");
                setShowModal(true);
            })
            .catch(error => {
                console.error("URL을 삭제하는데 실패했습니다.", error);
                setMessage("URL을 삭제하는데 실패했습니다.");
                setShowModal(true);
            });
    };

    const onAfterDelete = () => {
        if (afterDelete) {
            afterDelete();
        }
    };

    return(
        <div>
            <button
                type="button"
                className="btn btn-danger"
                onClick={() => setShowDeleteModal(true)}>
                삭제
            </button>

            {ConfirmDeleteModal({
                show: showDeleteModal,
                onHide: () => setShowDeleteModal(false),
                content: `등록하신 ${originUrl}을 정말로 삭제하시겠습니까?`,
                onConfirm: () => {
                    deleteButton();
                    setShowDeleteModal(false);
                }
            })}
            <MessageModal
                show={showModal}
                onHide={() => {
                    setShowModal(false)
                    onAfterDelete();
                }
            }
                message={message}
            />
        </div>
    );
};

export default DeleteUrl;