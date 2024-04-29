import { localAxios } from "@/util/http-commons";

const local = localAxios();

// 새로운 팀 생성
const postAddTeam = async (accessToken, success, fail) => {
  await local
    .post(`/project/team`, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    })
    .then(success)
    .catch(fail);
};

// // 팀에 팀원 추가
const postChangeTeam = async (accessToken, success, fail) => {
  await local
    .post(`/project/team`, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    })
    .then(success)
    .catch(fail);
};

export { postAddTeam };
