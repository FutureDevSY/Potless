import { localAxios } from "@/util/http-commons";

const local = localAxios();

const getTaskList = async (
  accessToken,
  managerId,
  start,
  end,
  status,
  word,
  area,
  page,
  success,
  fail
) => {
  await local
    .get(
      `/project?managerId=${managerId}&start=${start}&end=${end}&status=${status}&word=${word}&area=${area}&page=${page}&size=10`,
      {
        headers: {
          Authorization: accessToken,
        },
      }
    )
    .then(success)
    .catch(fail);
};

const postTaskCreate = async (
  accessToken,
  managerId,
  teamId,
  areaId,
  title,
  projectDate,
  damageNums,
  success,
  fail
) => {
  await local
    .post(
      `/project`,
      managerId,
      teamId,
      areaId,
      title,
      projectDate,
      damageNums,
      {
        headers: {
          Authorization: accessToken,
        },
      }
    )
    .then(success)
    .catch(fail);
};
export { getTaskList, postTaskCreate };
