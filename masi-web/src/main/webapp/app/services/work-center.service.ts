import { workCenterEndpoints } from 'app/constants/endpoints';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import { IPostWorkCenterDto, IWorkCenter, IWorkCenterParams } from 'app/shared/model/work-center.model';
import axios from 'axios';

// Lấy danh sách cụm máy sản xuất
const getWorkCenters = async (filter?: IWorkCenterParams) => {
  try {
    const url = workCenterEndpoints.getWorkCenters;
    const response = await axios.get<PaginationResponse<IWorkCenter>>(url, {
      params: {
        page: filter?.page,
        size: filter?.size,
        search: filter?.search,
        ...(filter?.status?.length && { status: filter?.status }),
      },
      paramsSerializer: {
        indexes: null,
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

// Lấy chi tiết cụm máy sản xuất
const getWorkCenterById = async (id: string) => {
  try {
    const url = workCenterEndpoints.getWorkCenterById(id);
    const response = await axios.get<IWorkCenter>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

// Tạo cụm máy sản xuất
const postWorkCenter = async (data: IPostWorkCenterDto) => {
  const url = workCenterEndpoints.postWorkCenter;

  return await axios.post(url, data);
};

// Cập nhật cụm máy sản xuất
const patchWorkCenter = async (data: IPostWorkCenterDto, id: string) => {
  const url = workCenterEndpoints.patchWorkCenter(id);

  return await axios.patch(url, data);
};

// Xóa cụm máy sản xuất
const deleteWorkCenter = async (id: string) => {
  const url = workCenterEndpoints.deleteWorkCenter(id);

  return await axios.delete(url);
};

export default {
  getWorkCenters,
  getWorkCenterById,
  postWorkCenter,
  patchWorkCenter,
  deleteWorkCenter,
};
