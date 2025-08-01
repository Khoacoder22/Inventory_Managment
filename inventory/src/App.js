import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import {ProtectedRoute, AdminRoute} from './services/Guard';
import RegisterPage from './pages/RegisterPage';
import LoginPage from './pages/LoginPage';
import CategoryPage from './pages/CategoryPage';
import SupplierPage from './pages/SupplierPage';
import AddEditSupplierPage from './pages/AddEditSupplierPage';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/register" element={<RegisterPage/>}></Route>
        <Route path="/login" element={<LoginPage/>}></Route>
        <Route path="/category" element={<CategoryPage/>}></Route>
        <Route path="/supplier" element={<SupplierPage></SupplierPage>}></Route>


        {/* Admin routes */}
        <Route path="/editsup/:supplierId" element={<AdminRoute element={<AddEditSupplierPage/>}/>}/>
        <Route path="/add-supplier" element={<AdminRoute element={<AddEditSupplierPage/>}/>}/>

      </Routes>
    </BrowserRouter>
  );
}

export default App;
