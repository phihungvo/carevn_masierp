import { steamingProcessChecklistEndpoints } from 'app/constants/endpoints';
import { ISteamingProcessChecklist } from 'app/shared/model/production-process.model';
import axios from 'axios';

// Tạo biểu mẫu giám sát quy trình hấp sấy
const postSteamingProcessChecklist = async (data: ISteamingProcessChecklist) => {
  const url = steamingProcessChecklistEndpoints.postSteamingProcessChecklist;

  return await axios.post(url, data);
};

// Cập nhật biểu mẫu giám sát quy trình hấp sấy
const patchSteamingProcessChecklist = async (data: ISteamingProcessChecklist, id: string) => {
  const url = steamingProcessChecklistEndpoints.patchSteamingProcessChecklist(id);

  return await axios.patch(url, data);
};

// Xoá biểu mẫu giám sát quy trình hấp sấy
const deleteSteamingProcessChecklist = async (id: string) => {
  const url = steamingProcessChecklistEndpoints.patchSteamingProcessChecklist(id);

  return await axios.delete(url);
};

// Lấy chi tiết biểu mẫu giám sát quy trình hấp sấy theo
const getSteamingProcessChecklistById = async (id: string) => {
  try {
    const url = steamingProcessChecklistEndpoints.getSteamingProcessChecklistById(id);
    const response = await axios.get<ISteamingProcessChecklist>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

export default {
  postSteamingProcessChecklist,
  patchSteamingProcessChecklist,
  deleteSteamingProcessChecklist,
  getSteamingProcessChecklistById,
};
