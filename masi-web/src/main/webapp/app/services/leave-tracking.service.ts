import axios from 'axios';

import { timeKeepingResourseEndpoints } from 'app/constants/endpoints';

// Lấy danh sách đơn nghỉ phép
const getLeaveTracking = async (year: string, workspaceType: string) => {
  try {
    const url = timeKeepingResourseEndpoints.getLeaveTrackingExport;
    const response = await axios.get(url, {
      params: {
        year,
        workspaceType,
      },
      responseType: 'blob',
      headers: {
        Accept: 'application/octet-stream',
      },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

export default {
  getLeaveTracking,
};
