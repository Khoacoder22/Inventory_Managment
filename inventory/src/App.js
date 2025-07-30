import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import {ProtectedRoute, AdminRoute} from './services/Guard';
import RegisterPage from './pages/RegisterPage';
import LoginPage from './pages/LoginPage';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/register" element={<RegisterPage/>}></Route>
        <Route path="/login" element={<LoginPage/>}></Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
