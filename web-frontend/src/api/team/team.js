import { localAxios } from "@/util/http-commons";

const local = localAxios();

// 관리자 담당 관할 team 리스트 조회
const getTeamList = async (accessToken, area, success, fail) => {
  await local
    .get(`/team/${area}`, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    })
    .then(success)
    .catch(fail);
};

// 새로운 팀 생성
const postAddTeam = async (accessToken, success, fail) => {
  await local
    .post(
      `/team`,
      {},
      {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      }
    )
    .then(success)
    .catch(fail);
};

// 지역별 작업자 조회
const getWokerList = async (accessToken, area, success, fail) => {
  await local
    .get(`/team/worker/${area}`, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    })
    .then(success)
    .catch(fail);
};

// // 팀에 팀원 추가
const postAddWorker = async (accessToken, workerInfo, success, fail) => {
  await local
    .post(`/team/worker`, workerInfo, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    })
    .then(success)
    .catch(fail);
};

// 팀에 팀원 삭제
const deleteWorker = async (accessToken, workerInfo, success, fail) => {
  await local
    .delete(`/team/worker`, workerInfo, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    })
    .then(success)
    .catch(fail);
};

export { getTeamList, postAddTeam, getWokerList, postAddWorker, deleteWorker };
