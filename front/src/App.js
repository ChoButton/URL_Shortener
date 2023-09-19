import './App.css';
import { BrowserRouter, Route, Routes } from "react-router-dom";
import ShortenUrl from "./features/shortenURL/ShortenUrl";
import Signup from "./features/user/Signup";
import Login from "./features/user/Login";
import UserUpdate from "./features/user/UserUpdate";
import UrlListForUser from "./features/user/UrlListForUser";
import UrlListForAdmin from "./features/admin/UrlListForAdmin";
import PrivateRoute from "./common/PrivateRoute";
import TokenValidator from "./common/TokenValidator";

import 'bootstrap/dist/css/bootstrap.min.css';
import Logout from "./common/Logout";
import Header from "./features/Header";

function App() {
  return (
      <BrowserRouter>
          <Header />
          <div>
              <Routes>
                  <Route path="/" element={<ShortenUrl />} />
                  <Route path="/signup" element={<Signup />} />
                  <Route path="/login" element={<Login />} />
                  <Route path="/userUpdate" element={<PrivateRoute roles={['ROLE_USER']}><UserUpdate /></PrivateRoute>} />
                  <Route path="/userpage" element={<PrivateRoute roles={['ROLE_USER']}><UrlListForUser /></PrivateRoute>} />
                  <Route path="/adminpage" element={<PrivateRoute roles={['ROLE_ADMIN']}><UrlListForAdmin /></PrivateRoute>} />
              </Routes>
          </div>
      </BrowserRouter>
  )
}

export default App;
