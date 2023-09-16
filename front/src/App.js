import './App.css';
import { BrowserRouter, Route, Routes } from "react-router-dom";
import ShortenUrl from "./features/shortenURL/ShortenUrl";

function App() {
  return (
      <BrowserRouter>
          <div>
              <Routes>
                  <Route path="/" element={<ShortenUrl />} />
              </Routes>
          </div>
      </BrowserRouter>
  )
}

export default App;
