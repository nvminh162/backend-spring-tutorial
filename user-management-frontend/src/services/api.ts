import axios from "axios";

const BACKEND_URL = import.meta.env.VITE_BACKEND_URL;

export const getUsersAPI = () => {
  return axios.get(`${BACKEND_URL}/users`);
};

export const createUserAPI = (name: string, email: string) => {
  return axios.post(`${BACKEND_URL}/users`, { name, email });
};

export const updateUserAPI = (id: number, name: string, email: string) => {
  return axios.put(`${BACKEND_URL}/users/${id}`, { name, email });
};

export const deleteUserAPI = (id: number) => {
  return axios.delete(`${BACKEND_URL}/users/${id}`);
};
