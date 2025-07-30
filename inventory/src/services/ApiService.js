import axios from "axios";
import CryptoJS from "crypto-js";

export default class ApiService {
  // ===== Config =====
  static BASE_URL = "http://localhost:5050/api";
  static ENCRYPTION_KEY = "mysecretkey"; 

  // ===== Crypto helpers =====
  static encrypt(data) {
    if (data == null) return null;
    return CryptoJS.AES.encrypt(String(data), this.ENCRYPTION_KEY).toString();
  }

  static decrypt(data) {
    if (!data) return null;
    const bytes = CryptoJS.AES.decrypt(data, this.ENCRYPTION_KEY);
    return bytes.toString(CryptoJS.enc.Utf8);
  }

  // ===== Auth storage =====
  static saveToken(token) {
    localStorage.setItem("token", this.encrypt(token));
  }

  static getToken() {
    const encryptedToken = localStorage.getItem("token");
    return this.decrypt(encryptedToken);
  }

  static saveRole(role) {
    localStorage.setItem("role", this.encrypt(role));
  }

  static getRole() {
    const encryptedRole = localStorage.getItem("role");
    return this.decrypt(encryptedRole);
  }

  static clearAuth() {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
  }

  // ===== Headers =====
  static getHeader(extra = {}) {
    const token = this.getToken();
    const headers = { "Content-Type": "application/json", ...extra };
    if (token) headers.Authorization = `Bearer ${token}`;
    return headers;
  }

  // ===== USERS =====
  static async registerUser(registerData) {
    const res = await axios.post(`${this.BASE_URL}/auth/register`, registerData);
    return res.data;
  }

  static async loginUser(loginData) {
    const res = await axios.post(`${this.BASE_URL}/auth/login`, loginData);
    return res.data;
  }

  static async getAllUsers() {
    const res = await axios.get(`${this.BASE_URL}/users/all`, {
      headers: this.getHeader(),
    });
    return res.data;
  }

  static async getLoggedInUserInfo() {
    const res = await axios.get(`${this.BASE_URL}/users/current`, {
      headers: this.getHeader(),
    });
    return res.data;
  }

  static async getUserById(userId) {
    const res = await axios.get(`${this.BASE_URL}/users/${userId}`, {
      headers: this.getHeader(),
    });
    return res.data;
  }

  static async updateUser(userId, userData) {
    const res = await axios.put(`${this.BASE_URL}/users/${userId}`, userData, {
      headers: this.getHeader(),
    });
    return res.data;
  }

  static async deleteUser(userId) {
    const res = await axios.delete(`${this.BASE_URL}/users/delete/${userId}`, {
      headers: this.getHeader(),
    });
    return res.data;
  }

  // ===== PRODUCTS =====
  static async addProduct(formData) {
    const res = await axios.post(`${this.BASE_URL}/products/add`, formData, {
      headers: this.getHeader({ "Content-Type": "multipart/form-data" }),
    });
    return res.data;
  }

  static async updateProduct(formData) {
    const res = await axios.put(`${this.BASE_URL}/products/update`, formData, {
      headers: this.getHeader({ "Content-Type": "multipart/form-data" }),
    });
    return res.data;
  }

  static async getAllProducts() {
    const res = await axios.get(`${this.BASE_URL}/products/all`, {
      headers: this.getHeader(),
    });
    return res.data;
  }

  static async getProductById(productId) {
    const res = await axios.get(`${this.BASE_URL}/products/${productId}`, {
      headers: this.getHeader(),
    });
    return res.data;
  }

  static async deleteProduct(productId) {
    const res = await axios.delete(
      `${this.BASE_URL}/products/delete/${productId}`,
      { headers: this.getHeader() }
    );
    return res.data;
  }

  static async searchProduct(searchValue) {
    const res = await axios.get(`${this.BASE_URL}/products/search`, {
      params: { searchValue },
      headers: this.getHeader(),
    });
    return res.data;
  }

  // ===== CATEGORIES =====
  static async createCategory(category) {
    const res = await axios.post(`${this.BASE_URL}/categories/add`, category, {
      headers: this.getHeader(),
    });
    return res.data;
  }

  static async getAllCategories() {
    const res = await axios.get(`${this.BASE_URL}/categories/getAll`, {
      headers: this.getHeader(),
    });
    return res.data;
  }

  static async getCategoryById(categoryId) {
    const res = await axios.get(`${this.BASE_URL}/categories/${categoryId}`, {
      headers: this.getHeader(),
    });
    return res.data;
  }

  static async deleteCategory(categoryId) {
    const res = await axios.delete(`${this.BASE_URL}/categories/${categoryId}`, {
      headers: this.getHeader(),
    });
    return res.data;
  }

  static async updateCategory(categoryId, categoryData) {
    const res = await axios.put(
      `${this.BASE_URL}/categories/${categoryId}`,
      categoryData,
      { headers: this.getHeader() }
    );
    return res.data;
  }

  // ===== SUPPLIERS =====
  // Lưu ý: Tùy backend. Thường: POST /suppliers (không truyền id)
  static async addSupplier(supplierData) {
    const res = await axios.post(`${this.BASE_URL}/suppliers`, supplierData, {
      headers: this.getHeader(),
    });
    return res.data;
  }

  static async deleteSupplier(supplierId) {
    const res = await axios.delete(
      `${this.BASE_URL}/suppliers/delete/${supplierId}`,
      { headers: this.getHeader() }
    );
    return res.data;
  }

  static async getAllSuppliers() {
    const res = await axios.get(`${this.BASE_URL}/suppliers/all`, {
      headers: this.getHeader(),
    });
    return res.data;
  }

  static async getSupplierById(supplierId) {
    const res = await axios.get(`${this.BASE_URL}/suppliers/${supplierId}`, {
      headers: this.getHeader(),
    });
    return res.data;
  }

  static async updateSupplier(supplierId, supplierData) {
    const res = await axios.put(
      `${this.BASE_URL}/suppliers/${supplierId}`,
      supplierData,
      { headers: this.getHeader() }
    );
    return res.data;
  }

  // ===== TRANSACTIONS =====
  static async addTransaction(transactionData) {
    const res = await axios.post(
      `${this.BASE_URL}/transactions/add`,
      transactionData,
      { headers: this.getHeader() }
    );
    return res.data;
  }

  static async updateTransaction(transactionId, status) {
    const res = await axios.put(
      `${this.BASE_URL}/transactions/${transactionId}`,
      status,
      { headers: this.getHeader() }
    );
    return res.data;
  }

  static async deleteTransaction(transactionId) {
    const res = await axios.delete(
      `${this.BASE_URL}/transactions/delete/${transactionId}`,
      { headers: this.getHeader() }
    );
    return res.data;
  }

  static async getAllTransactions(filter) {
    const res = await axios.get(`${this.BASE_URL}/transactions/all`, {
      headers: this.getHeader(),
      params: { filter },
    });
    return res.data;
  }

  static async getTransactionById(transactionId) {
    const res = await axios.get(`${this.BASE_URL}/transactions/${transactionId}`, {
      headers: this.getHeader(),
    });
    return res.data;
  }

  static async purchaseProduct(body) {
    const res = await axios.post(
      `${this.BASE_URL}/transactions/purchase`,
      body,
      { headers: this.getHeader() }
    );
    return res.data;
  }

  static async returnToSupplier(body) {
    const res = await axios.post(`${this.BASE_URL}/transactions/return`, body, {
      headers: this.getHeader(),
    });
    return res.data;
  }

  static async getTransactionByMonthAndYear(month, year) {
    const res = await axios.get(`${this.BASE_URL}/transactions/byTime`, {
      headers: this.getHeader(),
      params: { month, year },
    });
    return res.data;
  }

  // ===== Auth helpers =====
  static logout() {
    this.clearAuth();
  }

  static isAuthenticated() {
    return !!this.getToken();
  }

  static isAdmin() {
    const role = this.getRole();
    return role === "ADMIN";
  }
}
