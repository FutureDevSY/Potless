import { localAxios } from "@/util/http-commons";

const local = localAxios();

// 작업 정보 지시서 상세 조회
const getTaskDetail = async (accessToken, projectId, success, fail) => {
  await local
    .get(`/project/${projectId}`, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    })
    .then(success)
    .catch(fail);
};

// 작업 정보 지시서 삭제
const deleteTaskDetail = async (accessToken, projectId, success, fail) => {
  await local
    .delete(`/project/${projectId}`, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    })
    .then(success)
    .catch(fail);
};

// 작업 정보 지시서 생성
const postTaskCreate = async (accessToken, taskInfo, success, fail) => {
  await local
    .post(`/project`, taskInfo, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    })
    .then(success)
    .catch(fail);
};

// 작업 정보 지시서에 팀 할당
const postTeam = async (accessToken, assignTeam, success, fail) => {
  await local
    .post(`/team/project`, assignTeam, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    })
    .then(success)
    .catch(fail);
};

// 작업 정보 지시서에 포트홀 할당
const postPothole = async (accessToken, assignPothole, success, fail) => {
  await local
    .post(`/task`, assignPothole, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    })
    .then(success)
    .catch(fail);
};

const patchPothole = async (accessToken, assignPothole, success, fail) => {
  await local
    .patch(`/task`, assignPothole, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    })
    .then(success)
    .catch(fail);
};

// 길찾기 api
const postOptimal = async (accessToken, projectBody, success, fail) => {
  await local
    .post(`/path/optimal`, projectBody, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    })
    .then(success)
    .catch(fail);
};

export {
  getTaskDetail,
  postOptimal,
  deleteTaskDetail,
  postTeam,
  postPothole,
  patchPothole,
  postTaskCreate,
};
