import { receiveMaterialChecklistEndpoints } from 'app/constants/endpoints';
import { IReceiveMaterialCheckList } from 'app/shared/model/production-process.model';
import axios from 'axios';

// Tạo mới biểu mẫu giám sát và tiếp nhận nguyên liệu
const postReceiveMaterialChecklist = async (data: IReceiveMaterialCheckList) => {
  const url = receiveMaterialChecklistEndpoints.postReceiveMaterialChecklist;

  return await axios.post(url, data);
};

// Cập nhật biểu mẫu giám sát và tiếp nhận nguyên liệu
const patchReceiveMaterialChecklist = async (data: IReceiveMaterialCheckList, id: string) => {
  const url = receiveMaterialChecklistEndpoints.patchReceiveMaterialChecklist(id);

  return await axios.patch(url, data);
};

// Xoá biểu mẫu giám sát và tiếp nhận nguyên liệu
const deleteReceiveMaterialChecklist = async (id: string) => {
  const url = receiveMaterialChecklistEndpoints.patchReceiveMaterialChecklist(id);

  return await axios.delete(url);
};

// Lấy chi tiết biểu mẫu giám sát và tiếp nhận nguyên liệu
const getReceiveMaterialChecklistById = async (id: string) => {
  try {
    const url = receiveMaterialChecklistEndpoints.getReceiveMaterialChecklistById(id);
    const response = await axios.get<IReceiveMaterialCheckList>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

export default {
  postReceiveMaterialChecklist,
  patchReceiveMaterialChecklist,
  deleteReceiveMaterialChecklist,
  getReceiveMaterialChecklistById,
};
