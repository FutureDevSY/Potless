import { localAxios } from "@/util/http-commons";

const local = localAxios();

// areaId를 통해 구 이름 가져오기
const getAreaDetails = async (accessToken, areaId) => {
  try {
    const response = await local.get(`/damage/area/${areaId}`, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching area details:", error);
    return null;
  }
};

// 포트홀 구 별 통계 조회 (대전시의 모든 구 통계)
// ex) 동구 미완료, 완료
const getGuList = async (accessToken, success, fail) => {
  await local
    .get(`/damage/statistic`, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    })
    .then(success)
    .catch(fail);
};

// 포트홀 구에 속한 동에 대한 통계 조회 (구 1개, 동 여러개)
// ex) 대덕구
// 갈전동 미완료, 완료 / 대화동 미완료, 완료 ...
const getDongList = async (accessToken, areaId) => {
  return local.get(`/damage/statistic/${areaId}`, {
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  });
};

// 구 별로 상관없이 모든 동의 전체 통계 출력
const getTotalDongList = async (accessToken, success, fail) => {
  await local
    .get(`/damage/statistic/location`, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    })
    .then(success)
    .catch(fail);
};

// 동 이름 검색하면 해당 동의 통계 출력
const getDongDetail = async (accessToken, locationName, success, fail) => {
  await local
    .get(`/damage/statistic/location/${locationName}`, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    })
    .then(success)
    .catch(fail);
};

// 구 별로 지정한 월별 발생한 통계 출력
// START 만 입력시 단일 조회 START, END 입력시 START ~ END 조회
const getDongMonthly = async (accessToken, start, end) => {
  try {
    const response = await local.get(`/damage/damage/month/area`, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      params: { start, end },
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching monthly data:", error);
    return null;
  }
};

// 구 별로 지정한 날짜별 발생한 통계 출력
// START 만 입력시 단일 조회 START, END 입력시 START ~ END 조회
const getDongPerDate = async (accessToken, start, end, success, fail) => {
  await local
    .get("/damage/damage/date/area", {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      params: {
        start: start,
        end: end,
      },
    })
    .then(success)
    .catch(fail);
};
export {
  getAreaDetails,
  getGuList,
  getDongList,
  getTotalDongList,
  getDongDetail,
  getDongMonthly,
  getDongPerDate,
};
