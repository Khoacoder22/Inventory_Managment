import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import {ProtectedRoute, AdminRoute} from './services/Guard';
import RegisterPage from './pages/RegisterPage';
import LoginPage from './pages/LoginPage';
import CategoryPage from './pages/CategoryPage';
import SupplierPage from './pages/SupplierPage';
import AddEditSupplierPage from './pages/AddEditSupplierPage';
import ProductPage from './pages/ProductPage';
import AddEditProductPage from './pages/AddEditProductPage';
import PurchasePage from './pages/PurchasePage';
import TransactionsPage from './pages/TransactionPage';
import TransactionDetailsPage from './pages/TransactionDetailPage';
import DashboardPage from './pages/DashboardPage';
import SellPage from './pages/SellPage';


function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/register" element={<RegisterPage/>}></Route>
        <Route path="/login" element={<LoginPage/>}></Route>
        <Route path="/category" element={<CategoryPage/>}></Route>
        <Route path="/supplier" element={<SupplierPage></SupplierPage>}></Route>
        <Route path="/products" element={<ProductPage></ProductPage>}></Route>

        {/* Admin routes */}
        <Route path="/editsup/:supplierId" element={<AdminRoute element={<AddEditSupplierPage/>}/>}/>
        <Route path="/add-supplier" element={<AdminRoute element={<AddEditSupplierPage/>}/>}/>
        <Route path="/edit-product/:productId" element={<AdminRoute element={<AddEditProductPage/>}/>}/>
        <Route path="/add-product" element={<AdminRoute element={<AddEditProductPage/>}/>}/>

        {/* both */}
        <Route path="/purchase" element={<ProtectedRoute element={<PurchasePage/>}/>}/>
        <Route path="/transaction" element={<ProtectedRoute element={<TransactionsPage/>}/>}/>
        <Route path="/transaction/:transactionId" element={<ProtectedRoute element={<TransactionDetailsPage/>}/>}/>
        <Route path="/dashboard" element={<ProtectedRoute element={<DashboardPage/>}/>}/>
        <Route path="/sell" element={<ProtectedRoute element={<SellPage/>}/>}/>


      </Routes>
    </BrowserRouter>
  );
}

export default App;
