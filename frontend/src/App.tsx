import './App.css'
import {BrowserRouter, Route, Routes} from "react-router-dom";
import ProtectedRoute from "./components/security/ProtectedRoute.tsx";
import LoginComponent from "./components/auth/login/LoginComponent.tsx";
import RegisterComponent from "./components/auth/register/RegisterComponent.tsx";

function App() {

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginComponent/>}/>
        <Route path="/register" element={<RegisterComponent/>}/>
        <Route path="*" element={
          <ProtectedRoute>
            <LoginComponent/>
          </ProtectedRoute>
        }>
        </Route>
      </Routes>
    </BrowserRouter>


  )
}

export default App
