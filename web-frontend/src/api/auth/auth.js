import { localAxios } from "../../util/http-commons";
const local = localAxios();

// 로그인
const login = async (loginData, success, fail) => {
  await local.post(`/member/login-web`, loginData).then(success).catch(fail);
};

// 로그아웃
const logout = async (accessToken, success, fail) => {
  await local
    .post(
      `/member/logout-web`,
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

export { login, logout };
