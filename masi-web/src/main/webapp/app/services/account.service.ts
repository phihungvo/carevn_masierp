import { accountEndpoints } from 'app/constants/endpoints';
import { IAccount } from 'app/shared/model/account.model';
import axios from 'axios';

const createAccount = (data: IAccount) => {
  const url = accountEndpoints.createAccount;
  return axios.post<IAccount>(url, data);
};

const getAccountsStatuses = async (data: string[]) => {
  try {
    const url = accountEndpoints.getAccountsStatuses;
    const response = await axios.post(url, data);

    return response.data as IAccount[];
  } catch (error) {
    console.error(error);
  }
};

const getAccountById = async (id: string) => {
  try {
    const url = accountEndpoints.getAccountById(id);
    const response = await axios.get<IAccount>(url);

    return response.data;
  } catch (error) {
    console.error(error);
  }
};

const toggleActivate = async (id: string) => {
  try {
    const url = accountEndpoints.toggleActiveAccount(id);
    const response = await axios.post(url);

    return response.data;
  } catch (error) {
    console.error(error);
  }
};

const toggleActivateV2 = (id: string) => () => {
  const url = accountEndpoints.toggleActiveAccount(id);
  return axios.delete(url);
};

const updateAccount = (id: string) => (data: any) => {
  const url = accountEndpoints.updateAccount(id);
  return axios.patch(url, data);
};

const checkExistUserName = (id: string, username: string) => () => {
  const url = accountEndpoints.checkAccountExist(id);
  return axios.get(url, { params: { username } });
};

const settingCompany = (data: any) => {
  const url = accountEndpoints.setCompany;
  return axios.patch(url, data);
};

const settingCompanyUser = (id: string, data: any) => {
  const url = accountEndpoints.setCompanyUser(id);
  return axios.patch(url, data);
};

const getAccountByUsername = async (username: string) => {
  try {
    const url = accountEndpoints.getAccountByUsername(username);
    const response = await axios.get<IAccount>(url);

    return response.data;
  } catch (error) {
    console.error(error);
  }
};

const changeAccountCompany = (data: { companyId: string }) => {
  const url = accountEndpoints.changeAccountCompany;
  return axios.patch(url, data);
};

export default {
  createAccount,
  getAccountsStatuses,
  getAccountById,
  toggleActivate,
  getAccountByUsername,
  toggleActivateV2,
  updateAccount,
  settingCompany,
  settingCompanyUser,
  checkExistUserName,
  changeAccountCompany,
};
