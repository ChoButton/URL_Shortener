import React, {useState} from "react";
import axios from "axios";
import {ENDPOINTS} from "./ApiEndpoints";
import {deleteToken, getToken} from "./TokenService";
import {ConfirmUserAccountDeleteModal, MessageModal} from "./ModalSrvice";
import {useNavigate} from "react-router-dom";

const UserAccountDelete = ({ id }) => {
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [showModal, setShowModal] = useState(false);
    const [message, setMessage] = useState("");

    const navigate = useNavigate();

    const deleteButton = () => {
        axios.delete(ENDPOINTS.USER_DELETE + id,
            {
                headers: {
                    'Authorization': 'Bearer ' + getToken()
                }
            })
            .then(() => {
                setMessage("정상적으로 탈퇴처리 되었습니다.");
                setShowModal(true);
            })
            .catch(error => {
                console.error("회원탈퇴 실패", error);
                setMessage("회원탈퇴중 오류가 발생하였습니다.");
                setShowModal(true);
            });
    };

    const onAfterDelete = () => {
        deleteToken();
        navigate("/")
    };

    return(
        <div>
            <button
                type="button"
                className="btn btn-danger"
                onClick={() => setShowDeleteModal(true)}>
                회원탈퇴
            </button>

            {ConfirmUserAccountDeleteModal({
                show: showDeleteModal,
                onHide: () => setShowDeleteModal(false),
                content: `정말로 회원탈퇴 하시겠습니까?`,
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

export default UserAccountDelete;