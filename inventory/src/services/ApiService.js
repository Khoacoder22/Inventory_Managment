import axios from 'axios';
import CryptoJS from 'crypto-js';

export default class ApiService {
    //dia chi backend 
    static BASE_URL = "http://localhost:5050/api"
    static ENCRYPTION_KEY = "mysecretkey"

    //encrypt data
    static encrypt(data){
        return CryptoJS.AES.encrypt(data, this.ENCRYPTION_KEY).toString();
    }

    //decrypt
    static dencrypt(data){
        const bytes = CryptoJS.AES.decrypt(data, this.ENCRYPTION_KEY);
        return bytes.toString(CryptoJS.Utf8);
    }

    //save token
    static saveToken(token){
        const encryptedToken = this.encrypt(token);
        localStorage.setItem("token", encryptedToken)
    }

    //retreve the token
    static getToken(){
        const encryptedToken = localStorage.getItem("token");
        if(!encryptedToken){
            return null;
        }
        return this.dencrypt(encryptedToken);
    }

    static getRole(){
        const encryptedRole = localStorage.getItem("role");
        if(!encryptedRole) return null;
        return this.dencrypt(encryptedRole);
    }

    static clearAuth(){
        localStorage.removeItem("token");
        localStorage.removeItem("role");
    }

    // USERS

    //lay header 
    static getHeader(){
        const token = this.getToken();
        return{
            Authorization: `Bearer ${token}`,
            "Content-type": "application/json"
        }
    }

    // dang ky 
    static async registerUser(registerData){
        const response = await axios.post(`${this.BASE_URL}/auth/register`,registerData)
        return response.data;
    }

    // dang nhap
    static async loginUser(loginUser){
        const response = await axios.post(`${this.BASE_URL}/auth/login`,loginUser)
        return response.data;
    }

    // lay het user
    //ADMIN
    static async getAlluser(){
        const response = await axios.get(`${this.BASE_URL}/users/all`,{
            headers: this.getHeader()}
        );
        return response.data;
    }

    //ADMIN
    static async getLoggedInUsesInfo(){
        const response = await axios.get(`${this.BASE_URL}/users/current`,{
            headers: this.getHeader()
        })
    }

    static async getUserById(userId){
        const response = await axios.get(`${this.BASE_URL}/users/${userId}`)
    }

    static async updateUser(userId, userData){
        const response = await axios.put(`${this.BASE_URL}/users/${userId}`, userData, {
            headers: this.getHeader()
        });
        return response.data;
    }

    static async deleteUser(userId){
        const response = await axios.delete(`${this.BASE_URL}/users/delete/${userId}`, {
            headers: this.getHeader()
        });
        return response.data;
    }

    //PRODUCT 

    static async addProduct(formData){
        const response = await axios.post(`${this.BASE_URL}/products/add`, formData, {
            headers: {
                ...this.getHeader(),
                "Content-Type": "multipart/form-data"
            }
        })
        return response.data;
    }

    static async updateProduct(formData){
        
        const response = await axios.put(`${this.BASE_URL}/products/add`, formData,{
            headers:{
                ...this.getHeader(),
                "Content-Type" : "multipart/form-data"
            }
        });
        return response.data;
    }

    static async getAllproduct(){
        const response = await axios.get(`${this.BASE_URL}/products/all`,{
            headers: this.getHeader()
        });
        return response.data;
    }

    static async getByproductId(productId){
        const response = await axios.get(`${this.BASE_URL}/products/${productId}`, {
            headers: this.getHeader()
        });
        return response.data;
    }

    static async deleteProduct(productId){
        const response = await axios.delete(`${this.BASE_URL}/products/delete/${productId}`, {
            headers: this.getHeader()
        });
        return response.data;
    }

    static async searchProduct(searchValue){
        const response = await axios.get(`${this.BASE_URL}/products/search`, {
            params: {searchValue},
            headers: this.getHeader()
        });
        return response.data;
    }


}