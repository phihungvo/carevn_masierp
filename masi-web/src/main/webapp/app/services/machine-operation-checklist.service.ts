import { machineOperationChecklistEndpoints } from 'app/constants/endpoints';
import { IMachineOperationChecklist } from 'app/shared/model/production-process.model';
import axios from 'axios';

// Tạo biểu mẫu giám sát hoạt động máy
const postMachineOperationChecklist = async (data: IMachineOperationChecklist) => {
  const url = machineOperationChecklistEndpoints.postMachineOperationChecklist;

  return await axios.post<IMachineOperationChecklist>(url, data);
};

// Cập nhật biểu mẫu giám sát hoạt động máy
const patchMachineOperationChecklist = async (data: IMachineOperationChecklist, id: string) => {
  const url = machineOperationChecklistEndpoints.patchMachineOperationChecklist(id);

  return await axios.patch<IMachineOperationChecklist>(url, data);
};

// Xoá biểu mẫu giám sát hoạt động máy
const deleteMachineOperationChecklist = async (id: string) => {
  const url = machineOperationChecklistEndpoints.deleteMachineOperationChecklist(id);

  return await axios.delete(url);
};

// Lấy chi tiết biểu mẫu giám sát hoạt động máy
const getMachineOperationChecklistById = async (id: string) => {
  try {
    const url = machineOperationChecklistEndpoints.getMachineOperationChecklistById(id);
    const response = await axios.get<IMachineOperationChecklist>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

export default {
  postMachineOperationChecklist,
  patchMachineOperationChecklist,
  deleteMachineOperationChecklist,
  getMachineOperationChecklistById,
};
