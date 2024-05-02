import { localAxios } from "@/util/http-commons";

const local = localAxios();

// 포트홀 구 별 통계 조회 (대전시의 모든 구 통계)
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
const getDongList = async (accessToken, areaId, success, fail) => {
  await local
    .get(`/damage/statistic/${areaId}`, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    })
    .then(success)
    .catch(fail);
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

export { getGuList, getDongList, getTotalDongList, getDongDetail };
