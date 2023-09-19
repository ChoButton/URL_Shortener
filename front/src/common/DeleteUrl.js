import axios from "axios";
import {ENDPOINTS} from "./ApiEndpoints";
import {getToken} from "./TokenService";

const DeleteUrl = ({id, afterDelete}) => {
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
        <button type="button"
                className="btn btn-danger"
                onClick={deleteButton}>삭제</button>
    );
};

export default DeleteUrl;