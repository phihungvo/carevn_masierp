import { companyEndpoints } from 'app/constants/endpoints';
import { ICallCenter } from 'app/shared/model/call-center.model';
import {
  ICompany,
  ICompanyParams,
  IPatchCompanyDto,
  IPostCompanyDto,
} from 'app/shared/model/company.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import axios from 'axios';

const getCompanies = async (filter?: ICompanyParams) => {
  try {
    const url = companyEndpoints.getCompanies;
    const response = await axios.get<PaginationResponse<ICompany>>(url, {
      params: { page: filter?.page, size: filter?.size },
    });

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getCompanyById = async (id: string) => {
  try {
    const url = companyEndpoints.getCompanyById(id);
    const response = await axios.get<ICompany>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const postCompany = async (data: IPostCompanyDto) => {
  const url = companyEndpoints.postCompany;
  return await axios.post<ICompany>(url, data);
};

const patchCompany = async (data: IPatchCompanyDto, id: string) => {
  const url = companyEndpoints.patchCompany(id);
  return await axios.patch<ICompany>(url, data);
};
const activeCompany = async (id: string) => {
  const url = companyEndpoints.activeCompany(id);
  return await axios.patch(url);
};

const inactiveCompany = async (id: string) => {
  const url = companyEndpoints.inactiveCompany(id);
  return await axios.patch(url);
};

export default {
  getCompanies,
  getCompanyById,
  postCompany,
  patchCompany,
  activeCompany,
  inactiveCompany,
};
