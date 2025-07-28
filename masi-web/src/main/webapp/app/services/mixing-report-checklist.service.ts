import { mixingReportChecklistEndpoints } from 'app/constants/endpoints';
import { IMixingReportChecklist } from 'app/shared/model/production-process.model';
import axios from 'axios';

// Tạo biểu mẫu báo cáo trộn sản phẩm bột cá
const postReportMixingFishmealTemplate = async (data: IMixingReportChecklist) => {
  const url = mixingReportChecklistEndpoints.postMixingReportChecklist;

  return await axios.post(url, data);
};

// Cập nhật biểu mẫu báo cáo trộn sản phẩm bột cá
const patchReportMixingFishmealTemplate = async (data: IMixingReportChecklist, id: string) => {
  const url = mixingReportChecklistEndpoints.patchMixingReportChecklist(id);

  return await axios.patch(url, data);
};

// Xoá biểu mẫu báo cáo trộn sản phẩm bột cá
const deleteReportMixingFishmealTemplate = async (id: string) => {
  const url = mixingReportChecklistEndpoints.deleteMixingReportChecklist(id);

  return await axios.delete(url);
};

// Lấy chi tiết biểu mẫu báo cáo trộn sản phẩm bột cá
const getReportMixingFishmealTemplateById = async (id: string) => {
  try {
    const url = mixingReportChecklistEndpoints.getMixingReportChecklistById(id);
    const response = await axios.get<IMixingReportChecklist>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

export default {
  postReportMixingFishmealTemplate,
  patchReportMixingFishmealTemplate,
  deleteReportMixingFishmealTemplate,
  getReportMixingFishmealTemplateById,
};
