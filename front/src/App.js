import './App.css';
import { BrowserRouter, Route, Routes } from "react-router-dom";
import ShortenUrl from "./features/shortenURL/ShortenUrl";
import Signup from "./features/user/Signup";
import Login from "./features/user/Login";

function App() {
  return (
      <BrowserRouter>
          <div>
              <Routes>
                  <Route path="/" element={<ShortenUrl />} />
                  <Route path="/signup" element={<Signup />} />
                  <Route path="/login" element={<Login />} />
              </Routes>
          </div>
      </BrowserRouter>
  )
}

export default App;
