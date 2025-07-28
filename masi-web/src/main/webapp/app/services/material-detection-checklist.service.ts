import { metalDetectionChecklistEndpoints } from 'app/constants/endpoints';
import { IMetalDetectionChecklist } from 'app/shared/model/production-process.model';
import axios from 'axios';

// Tạo biểu mẫu kiểm tra nam châm và lưới
const postMetalDetectionChecklist = async (data: IMetalDetectionChecklist) => {
  const url = metalDetectionChecklistEndpoints.postMetalDetectionChecklist;

  return await axios.post<IMetalDetectionChecklist>(url, data);
};

// Cập nhật biểu mẫu kiểm tra nam châm và lưới
const patchMetalDetectionChecklist = async (data: IMetalDetectionChecklist, id: string) => {
  const url = metalDetectionChecklistEndpoints.patchMetalDetectionChecklist(id);

  return await axios.patch<IMetalDetectionChecklist>(url, data);
};

// Xoá biểu mẫu kiểm tra nam châm và lưới
const deleteMetalDetectionChecklist = async (id: string) => {
  const url = metalDetectionChecklistEndpoints.deleteMetalDetectionChecklist(id);

  return await axios.delete(url);
};

// Lấy chi tiết biểu mẫu kiểm tra nam châm và lưới
const getMetalDetectionChecklistById = async (id: string) => {
  try {
    const url = metalDetectionChecklistEndpoints.getMetalDetectionChecklistById(id);
    const response = await axios.get<IMetalDetectionChecklist>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

export default {
  postMetalDetectionChecklist,
  patchMetalDetectionChecklist,
  deleteMetalDetectionChecklist,
  getMetalDetectionChecklistById,
};
