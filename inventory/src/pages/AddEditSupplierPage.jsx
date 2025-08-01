import React, { useEffect, useState } from 'react';
import Layout from '../components/Layout';
import ApiService from '../services/ApiService';
import { useNavigate, useParams } from 'react-router-dom';

const AddEditSupplierPage = () => {
  const { supplierId } = useParams();  // No need to pass an empty string in useParams
  const [name, setName] = useState("");  // Correct initialization with useState
  const [contactInfo, setContactInfo] = useState("");  // Correct initialization with useState
  const [address, setAddress] = useState("");  // Correct initialization with useState
  const [message, setMessage] = useState("");  // Correct initialization with useState
  const [isEditing, setIsEditing] = useState(false);  // Correct state name as "isEditing"
  const navigate = useNavigate();

  useEffect(() => {
    if (supplierId) {
      setIsEditing(true);

      const fetchSupplier = async () => {
        try {
          const supplierData = await ApiService.getSupplierById(supplierId);
          if (supplierData.status === 200) {
            setName(supplierData.supplier.name);
            setContactInfo(supplierData.supplier.contactInfo);
            setAddress(supplierData.supplier.address);
          }
        } catch (error) {
          showMessage(
            error.response?.data?.message ||
              "Error fetching supplier by ID: " + error
          );
        }
      };

      fetchSupplier();
    }
  }, [supplierId]);

  // Handle form submission for both adding and editing suppliers
  const handleSubmit = async (e) => {
    e.preventDefault();
    const supplierData = { name, contactInfo, address };

    try {
      if (isEditing) {
        await ApiService.updateSupplier(supplierId, supplierData);
        showMessage("Supplier edited successfully");
        navigate("/supplier");
      } else {
        await ApiService.addSupplier(supplierData);
        showMessage("Supplier added successfully");
        navigate("/supplier");
      }
    } catch (error) {
      showMessage(
        error.response?.data?.message ||
          "Error adding/editing supplier: " + error
      );
    }
  };

  // Method to display messages or errors
  const showMessage = (msg) => {
    setMessage(msg);
    setTimeout(() => {
      setMessage("");
    }, 4000);
  };

  return (
    <Layout>
      {message && <div className="message">{message}</div>}
      <div className="supplier-form-page">
        <h1>{isEditing ? "Edit Supplier" : "Add Supplier"}</h1>

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Supplier Name</label>
            <input
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
              type="text"
            />
          </div>

          <div className="form-group">
            <label>Contact Info</label>
            <input
              value={contactInfo}
              onChange={(e) => setContactInfo(e.target.value)}
              required
              type="text"
            />
          </div>

          <div className="form-group">
            <label>Address</label>
            <input
              value={address}
              onChange={(e) => setAddress(e.target.value)}
              required
              type="text"
            />
          </div>
          <button type="submit">
            {isEditing ? "Edit Supplier" : "Add Supplier"}
          </button>
        </form>
      </div>
    </Layout>
  );
};

export default AddEditSupplierPage;
