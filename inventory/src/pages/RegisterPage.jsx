import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import ApiService from "../services/ApiService";

const RegisterPage = () => {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [message, setMessage] = useState("");
  const [submitting, setSubmitting] = useState(false);

  const navigate = useNavigate();

  const showMessage = (msg) => {
    setMessage(msg);
    setTimeout(() => setMessage(""), 4000);
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    if (submitting) return;
    try {
      setSubmitting(true);
      const registerData = { name, email, password, phoneNumber };
      await ApiService.registerUser(registerData);
      showMessage("Registration successfully");
      navigate("/login", { replace: true });
    } catch (error) {
      console.error(error);
      const msg =
        error?.response?.data?.message ||
        "Register failed. Please check your info and try again.";
      showMessage(msg);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="auth-container">
      <h2>Register</h2>

      {message && <p className="message">{message}</p>}

      <form onSubmit={handleRegister} noValidate>
        <input
          type="text"
          placeholder="Name"
          value={name}
          autoComplete="name"
          onChange={(e) => setName(e.target.value)}
          required
        />

        <input
          type="email"
          placeholder="Email"
          value={email}
          autoComplete="email"
          onChange={(e) => setEmail(e.target.value)}
          required
        />

        <input
          type="password"
          placeholder="Password"
          value={password}
          autoComplete="new-password"
          onChange={(e) => setPassword(e.target.value)}
          required
          minLength={6}
        />

        <input
          type="tel"
          placeholder="Phone number"
          value={phoneNumber}
          autoComplete="tel"
          onChange={(e) => setPhoneNumber(e.target.value)}
        />

        <button type="submit" disabled={submitting}>
          {submitting ? "Registering..." : "Register"}
        </button>
      </form>

      <p>
        Already have an account? <Link to="/login">Login</Link>
      </p>
    </div>
  );
};

export default RegisterPage;
