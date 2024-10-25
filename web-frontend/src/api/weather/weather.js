import axios from "axios";

const { VITE_WEATHER_API_KEY } = import.meta.env;

// 관리자 담당 관할 team 리스트 조회
const getWeatherInfo = async (lat, lon, success, fail) => {
  await axios
    .get(
      `https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&appid=${VITE_WEATHER_API_KEY}&units=metric&lang=kr`
    )
    .then(success)
    .catch(fail);
};

export { getWeatherInfo };
