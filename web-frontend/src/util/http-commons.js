import axios from "axios";

const { VITE_SERVICE_URL } = import.meta.env;

function localAxios() {
  const instance = axios.create({
    baseURL: VITE_SERVICE_URL,
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });
  return instance;
}

export { localAxios };
