import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ApiService from '../services/ApiService';
import Layout from '../components/Layout';

const SupplierPage = () => {
    const [suppliers, setSuppliers] = useState([]);
    const [message, setMessage] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        const getSuppliers = async () => {
            try {
                const responsedata = await ApiService.getAllSuppliers();
                console.log("API response:", responsedata);

                if (responsedata.status === 200) {
                    setSuppliers(responsedata.suppliers || []);
                    showMessage("Get all suppliers successfully");
                }
            } catch (error) {
                showMessage(error.response?.data?.message || "Error getting suppliers: " + error);
            }
        };
        getSuppliers();
    }, []);

    const showMessage = (msg) => {
        setMessage(msg);
        setTimeout(() => setMessage(""), 4000);
    };

    // Delete supplier
    const handleDeleteSupplier = async (supplierId) => {
        try {
            if (window.confirm("Are you sure?")) {
                await ApiService.deleteSupplier(supplierId);
                setSuppliers(prev => prev.filter(s => s.id !== supplierId));
                showMessage("Supplier deleted successfully");
            } 
        } catch (error) {
            showMessage(error.response?.data?.message || "Error deleting supplier: " + error);
        }
    };

    // Navigate to edit supplier
    const handleEditSupplier = (supplierId) => {
        navigate(`/editsup/${supplierId}`);
    };

    return (
        <Layout>
            {message && <div className="message">{message}</div>}

            <div className="supplier-page">
                <div className="supplier-header">
                    <h1>Suppliers</h1>
                    <div className="add-sup">
                        <button onClick={() => navigate("/add-supplier")}>
                            Add Supplier
                        </button>
                    </div>
                </div>
            </div>

            {suppliers.length > 0 ? (
                <ul className="supplier-list">
                    {suppliers.map((supplier) => (
                        <li className="supplier-item" key={supplier.id}>
                            <span>{supplier.name}</span>
                            <div className="supplier-actions">
                                <button onClick={() => handleEditSupplier(supplier.id)}>Edit</button>
                                <button onClick={() => handleDeleteSupplier(supplier.id)}>Delete</button>
                            </div>
                        </li>
                    ))}
                </ul>
            ) : (
                <p>No suppliers found.</p>
            )}
        </Layout>
    );
};

export default SupplierPage;
