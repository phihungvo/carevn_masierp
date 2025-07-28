import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import annualLeaveService from 'app/services/annual-leave.service';

const { ANNUAL_LEAVES, ANNUAL_LEAVES_EMPLOYEE, EMPLOYEE_SENIORITY } = QUERY_KEY;
const { PATCH_ANNUAL_LEAVE } = MUTATION_KEY;

const useAnnualLeaves = () => {
  return useQuery({
    queryKey: [ANNUAL_LEAVES],
    queryFn: annualLeaveService.getAnnualLeaves,
    select: data => data.data,
  });
};

const usePatchAnnualLeave = (toggle?: () => void, toggleSuccess?: () => void) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationKey: [PATCH_ANNUAL_LEAVE],
    mutationFn: annualLeaveService.patchAnnualLeave,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [ANNUAL_LEAVES] });
      toggle && toggle();
      toggleSuccess && toggleSuccess;
    },
  });
};

const useAnnualLeavesEmployee = (employeeId: string) => {
  return useQuery({
    queryKey: [ANNUAL_LEAVES_EMPLOYEE, employeeId],
    queryFn: () => annualLeaveService.getEmployeeAnnualLeaves(employeeId),
    select: data => data.data,
    enabled: !!employeeId,
  });
};

const useEmployeeSeniority = (workspaceId: string, date: string) => {
  return useQuery({
    queryKey: [EMPLOYEE_SENIORITY, workspaceId, date],
    queryFn: () => annualLeaveService.getEmployeeSeniority(workspaceId, date),
    select: data => data.data,
    enabled: !!workspaceId && !!date,
  });
};

export default {
  useAnnualLeaves,
  usePatchAnnualLeave,
  useAnnualLeavesEmployee,
  useEmployeeSeniority,
};
