import axios from "axios";
import {ENDPOINTS} from "./ApiEndpoints";
import {getToken} from "./TokenService";
import {useState} from "react";
import {confirmDeleteModal} from "./ModalSrvice";

const DeleteUrl = ({id, originUrl, afterDelete}) => {
    const [showModal, setShowModal] = useState(false);
    const deleteButton = () => {
        axios.delete(ENDPOINTS.DELETE_URL + id,
            {
                headers: {
                    'Authorization': 'Bearer ' + getToken()
                }
            })
            .then(() => {
                if(afterDelete){
                    afterDelete();
                }
            })
            .catch(error => {
                console.error("URL을 삭제하는데 실패했습니다.", error);
            });
    };
    return(
        <>
            <button
                type="button"
                className="btn btn-danger"
                onClick={() => setShowModal(true)}>
                삭제
            </button>

            {confirmDeleteModal({
                show: showModal,
                onHide: () => setShowModal(false),
                content: `등록하신 ${originUrl}을 정말로 삭제하시겠습니까?`,
                onConfirm: () => {
                    deleteButton();
                    setShowModal(false);
                }
            })}
        </>
    );
};

export default DeleteUrl;