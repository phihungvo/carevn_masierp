import { authorityEndpoints } from 'app/constants/endpoints';
import { IAuthority, IAuthorityParams } from 'app/shared/model/authority.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import axios from 'axios';

const getAuthorities = async (filter?: IAuthorityParams) => {
  try {
    const url = authorityEndpoints.getAuthorities;

    const response = await axios.get<PaginationResponse<IAuthority>>(url, {
      params: filter,
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

export default {
  getAuthorities,
};
