import axios from "axios";

const { SERVICE_URL } = import.meta.env;

// local wannago api axios instance
function localAxios() {
  const instance = axios.create({
    baseURL: SERVICE_URL,
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });
  return instance;
}

export { localAxios };
