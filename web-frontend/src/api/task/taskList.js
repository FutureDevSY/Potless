import { localAxios } from "@/util/http-commons";

const local = localAxios();

// 작업 정보 지시서 전체 조회 (검색 쿼리 사용)
const getTaskList = async (accessToken, queryParams, success, fail) => {
  await local
    .get(`/project`, {
      params: {
        ...queryParams,
        size: 10,
      },
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    })
    .then(success)
    .catch(fail);
};

export { getTaskList };
