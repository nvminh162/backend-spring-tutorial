import axios from "axios";

const BACKEND_URL = import.meta.env.VITE_BACKEND_URL;

export const getUsersAPI = () => {
  return axios.get(`${BACKEND_URL}/users`);
};

export const createUserAPI = (name: string, email: string) => {
  return axios.post(`${BACKEND_URL}/users`, { name, email });
};
