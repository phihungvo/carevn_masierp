import { typeRequestApproveEndpoints } from 'app/constants/endpoints';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import { ITypeRequestApproval, ITypeRequestApprovalParams } from 'app/shared/model/type-request-approval.model';
import axios from 'axios';

const getTypeRequestApproval = async (filter: ITypeRequestApprovalParams) => {
  try {
    const url = typeRequestApproveEndpoints.getTypeRequestApproves;

    const response = await axios.get<PaginationResponse<ITypeRequestApproval>>(url, {
      params: {
        page: filter?.page,
        size: filter?.size,
        'isDeleted.equals': filter?.['isDeleted.equals'],
        'pageName.equals': filter?.['pageName.equals'],
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


export default {
  getTypeRequestApproval
};
