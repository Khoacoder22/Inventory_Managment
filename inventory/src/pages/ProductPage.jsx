import React, { useEffect, useState } from 'react'
import Layout from '../components/Layout'
import ApiService from '../services/ApiService'
import { useNavigate, useParams } from 'react-router-dom'
import PaginationCompnents from "../components/PaginationCompnents"

const ProductPage = () => {
  
    const [products, setProduct] = useState([]);
    const [message, setMessage] = useState("");
    
    const navigate = useNavigate();

    //pagination Set-up
    const [currentPage, setCurrentPage] = useState(1);
    const [TotalPage, setTotalPage] = useState(0);
    const itemsPerpage = 5;

    useEffect(() => {
        const getProduct = async() =>{
            const productData = await ApiService.getAllProducts();
            try{
            if(productData.status === 200){
                setTotalPage(Math.ceil(productData.products.length / itemsPerpage));
               
                setProduct (
                    productData.products.slice(
                        (currentPage - 1) * itemsPerpage,
                        currentPage * itemsPerpage
                    )
                );
            }
        } catch(error){
            showMessage(
                error.response?.data?.message || "Error Getting Products: " + error
            )
        }
        }
        getProduct();
    }, [currentPage])    

    //Delete
    const handleDeleteProduct = async (productId) => {
          if (window.confirm("Are you sure you want to delete this Product?")) {
      try {
        await ApiService.deleteProduct(productId);
        showMessage("Product sucessfully Deleted");
        window.location.reload(); //relode page
      } catch (error) {
        showMessage(
          error.response?.data?.message ||
            "Error Deleting in a product: " + error
        );
      }
    }
    };


    const showMessage = (msg) => {
        setMessage(msg);
        setTimeout(() =>  {
            setMessage("");
        }, 400);
    };
     return (
    <Layout>
      {message && <div className="message">{message}</div>}

      <div className="product-page">
        <div className="product-header">
          <h1>Products</h1>
          <button
            className="add-product-btn"
            onClick={() => navigate("/add-product")}
          >
            Add Product
          </button>
        </div>

        {products && (
          <div className="product-list">
            {products.map((product) => (
              <div key={product.id} className="product-item">
                <img
                  className="product-image"
                  src={product.imageUrl}
                  alt={product.name}
                />

                <div className="product-info">
                    <h3 className="name">{product.name}</h3>
                    <p className="sku">Sku: {product.sku}</p>
                    <p className="price">Price: {product.price}</p>
                    <p className="quantity">Quantity: {product.stockQuantity}</p>
                </div>

                <div className="product-actions">
                    <button className="edit-btn" onClick={()=> navigate(`/edit-product/${product.id}`)}>Edit</button>
                    <button  className="delete-btn" onClick={()=> handleDeleteProduct(product.id)}>Delete</button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      <PaginationCompnents
      currentPage={currentPage}
      totalPages={TotalPage}
      onPageChange={setCurrentPage}
      />
    </Layout>
  );
};


export default ProductPage