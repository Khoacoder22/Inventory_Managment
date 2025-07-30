import React, { use, useEffect, useState } from 'react'
import Layout from "../components/Layout"
import ApiService from '../services/ApiService'


const CategoryPage = () => {
  const [categories, setCategories] = useState([]);
  const [categoriesName, setCategoriesName] = useState("");
  const [message, setMessage] = useState("");
  const [isEditing, setIsEditing] = useState(false);
  const [editingCategoriesId, setEditingCategoriesId] = useState("");

  useEffect(() => {
    const getCategories = async () => {
        try{
            const data = await ApiService.getAllCategories();
            if(data.status === 200){
                setCategories(data.categories);
            }
        }

        catch(error){
            console.log(error);
        };
    };

  }, [])
  

  //get all
  useEffect(() => {
    const addCategories = async () =>{
        if(!categoriesName){
            showMessage("Category name can not be empty");
        }
        try {
            const data = await ApiService.createCategory({name: categoriesName});
            showMessage("category successfuly added");
            setCategoriesName("")
            window.location.reload();

            if(data.status === 200){
                setCategories(data.categories);
            }
        }
        catch(error){
            showMessage(error);
        }
    };
  })

  //edit category
  const editCategory = async() =>{
    try{
        await ApiService.updateCategory(editingCategoriesId, {name: categoriesName});
        showMessage("Category successfully update !")
        setIsEditing(false)
        setCategoriesName("")
        window.location.reload()
    }
    catch(error){
        showMessage(error);
    };
  }

  const handleEditCategory = (category) =>{
    setIsEditing(true)
    setEditingCategoriesId(category.id)
    setCategoriesName(category.name)
  };

  const handleDeleteCategory = async (categoriesId) => {
    if(window.confirm("Are you sure want to delete this ?")){
        try{
            await ApiService.deleteCategory(categoriesId);
            showMessage("Category successfully deleted");
            window.location.reload();
        } catch(error){
            error.response?.data?.message || "Error Loggin in a User" + error;
        }
    }
  }

  // show message
  const showMessage = (msg) =>{
    setMessage(msg);
    setTimeout(() => {
        setMessage("");
    }, 4000);
  };

   return (
    <Layout>
      {message && <div className="message">{message}</div>}
      <div className="category-page">
        <div className="category-header">
          <h1>Categories</h1>
          <div className="add-cat">
            <input
              value={categoryName}
              type="text"
              placeholder="Category Name"
              onChange={(e) => setCategoryName(e.target.value)}
            />

            {!isEditing ? (
              <button onClick={addCategory}>Add Category</button>
            ) : (
              <button onClick={editCategory}>Edit Cateogry</button>
            )}
          </div>
        </div>

        {categories && (
          <ul className="category-list">
            {categories.map((category) => (
              <li className="category-item" key={category.id}>
                <span>{category.name}</span>

                <div className="category-actions">
                  <button onClick={() => handleEditCategory(category)}>
                    Edit
                  </button>
                  <button onClick={() => handleDeleteCategory(category.id)}>
                    Edlete
                  </button>
                </div>
              </li>
            ))}
          </ul>
        )}
      </div>
    </Layout>
  );
};

export default CategoryPage