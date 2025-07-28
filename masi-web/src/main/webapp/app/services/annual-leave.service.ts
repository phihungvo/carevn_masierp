import { annualLeaveEndpoints } from 'app/constants/endpoints';
import { IAnnualLeaveEmployee, IAnnualLeaveResponse } from 'app/shared/model/annual-leave.model';
import axios from 'axios';

const getAnnualLeaves = async () => {
  try {
    const url = annualLeaveEndpoints.getAnnualLeaves;
    const response = await axios.get<IAnnualLeaveResponse>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const patchAnnualLeave = async (data: IAnnualLeaveResponse) => {
  const url = annualLeaveEndpoints.patchAnnualLeave;
  return await axios.patch<IAnnualLeaveResponse>(url, data);
};

const getEmployeeAnnualLeaves = async (employeeId: string) => {
  try {
    const url = annualLeaveEndpoints.getEmployeeAnnualLeave(employeeId);
    const response = await axios.get<IAnnualLeaveEmployee>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

const getEmployeeSeniority = async (workspaceId: string, date: string) => {
  try {
    const url = annualLeaveEndpoints.getEmployeeSeniority(workspaceId, date);
    const response = await axios.get<{ seniority: number }>(url);

    return response;
  } catch (error) {
    console.error(error);
  }
};

export default {
  getAnnualLeaves,
  patchAnnualLeave,
  getEmployeeAnnualLeaves,
  getEmployeeSeniority,
};
