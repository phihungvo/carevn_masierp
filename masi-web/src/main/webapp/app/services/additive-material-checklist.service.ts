import { additiveMaterialChecklistEndpoints } from 'app/constants/endpoints';
import { IAdditiveMaterialChecklist } from 'app/shared/model/production-process.model';
import axios from 'axios';

// Tạo biểu mẫu giám sát nguyên liệu phụ gia
const postAdditiveMaterialChecklist = async (data: IAdditiveMaterialChecklist) => {
  const url = additiveMaterialChecklistEndpoints.postAdditiveMaterialChecklist;

  return await axios.post(url, data);
};

// Cập nhật biểu mẫu giám sát nguyên liệu phụ gia
const patchAdditiveMaterialChecklist = async (data: IAdditiveMaterialChecklist, id: string) => {
  const url = additiveMaterialChecklistEndpoints.patchAdditiveMaterialChecklist(id);

  return await axios.patch(url, data);
};

// Xoá biểu mẫu giám sát nguyên liệu phụ gia
const deleteAdditiveMaterialChecklist = async (id: string) => {
  const url = additiveMaterialChecklistEndpoints.patchAdditiveMaterialChecklist(id);

  return await axios.delete(url);
};

// Lấy chi tiết biểu mẫu giám sát nguyên liệu phụ gia theo
const getAdditiveMaterialChecklistById = async (id: string) => {
  try {
    const url = additiveMaterialChecklistEndpoints.getAdditiveMaterialChecklistById(id);
    const response = await axios.get<IAdditiveMaterialChecklist>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

export default {
  postAdditiveMaterialChecklist,
  patchAdditiveMaterialChecklist,
  deleteAdditiveMaterialChecklist,
  getAdditiveMaterialChecklistById,
};
