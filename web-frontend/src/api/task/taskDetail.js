import { localAxios } from "@/util/http-commons";

const local = localAxios();

const getTaskDetail = async (accessToken, project_id, success, fail) => {
  await local
    .get(`/project/${project_id}`, {
      headers: {
        Authorization: accessToken,
      },
    })
    .then(success)
    .catch(fail);
};

const postOptimal = async (accessToken, success, fail) => {
  await local
    .get(`/project/optimal`, {
      headers: {
        Authorization: accessToken,
      },
    })
    .then(success)
    .catch(fail);
};
export { getTaskDetail, postOptimal };
