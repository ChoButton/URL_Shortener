import { Navigate, Route } from "react-router-dom";
import {getRolesFromToken} from "./TokenService";

const PrivateRoute = ({ children, roles }) => {
    const userRoles = getRolesFromToken();

    if(!userRoles){
        return <Navigate to="/" />;
    }

    // 사용자의 권한이 필요한 권한에 포함되어 있는지 확인
    if(roles && !roles.some(role => userRoles.includes(role))){
        // 사용자 권한에 필요한 권한이 없는경우
        // 추후 각종 에러 관련 페이지를 구현후 리턴해줘야함
        return <Navigate to="/" />;
    }
    return children;
}

export default PrivateRoute;