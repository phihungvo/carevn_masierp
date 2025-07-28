import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import accountService from 'app/services/account.service';
import employeeService from 'app/services/employee.service';
import useModalRedux from './use-modal-redux';
const { ACCOUNT_STATUSES, EMPLOYEE_PROFILES } = QUERY_KEY;

const useCreateEmployeeAccount = () => {
  const { handleToggleSuccessModal, handleToggleFailModalWithoutOkText } = useModalRedux()

  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: accountService.createAccount,
    onSuccess: (data) => {
      queryClient.invalidateQueries({
        queryKey: [QUERY_KEY.ACCOUNT_STATUSES],
      });
      queryClient.invalidateQueries({
        queryKey: [QUERY_KEY.ACCOUNT_BY_ID,data?.data?.id],
      });
      employeeService.syncAccountStatus();
      handleToggleSuccessModal({
        content: 'Tạo tài khoản thành công',
      }, false)
    },
    onError: () => {
      handleToggleFailModalWithoutOkText({
        content: 'Tạo tài khoản thất bại',
      })
    }
  });
};

const useGetAccountsStatuses = (accountIds: string[] = []) => {
  return useQuery({
    queryKey: [ACCOUNT_STATUSES, accountIds],
    queryFn: () => accountService.getAccountsStatuses(accountIds),
    enabled: !!accountIds,
  });
};

const useGetAccountById = (id: string) => {
  return useQuery({
    queryKey: [QUERY_KEY.ACCOUNT_BY_ID, id],
    queryFn: () => accountService.getAccountById(id),
    enabled: !!id,
  });
};

const useToggleActivate = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: string) => accountService.toggleActivate(id),
    onSuccess(data) {
      queryClient.invalidateQueries({
        queryKey: [QUERY_KEY.ACCOUNT_BY_ID, data?.data?.id],
      });
    },
    mutationKey: [MUTATION_KEY.TOGGLE_ACTIVATE_ACCOUNT],
  });
}

const useToggleStatusMutation = (id: string) => {
  const queryClient = useQueryClient();
  const { handleToggleFailModalWithoutOkText, handleToggleSuccessModal } = useModalRedux()
  return useMutation({
    mutationFn: accountService.toggleActivateV2(id),
    onSuccess(data) {
      queryClient.invalidateQueries({
        queryKey: [EMPLOYEE_PROFILES],
      });
      handleToggleSuccessModal({
        content: 'Cập nhật trạng thái tài khoản thành công',
      }, false)
    },
    onError: () => {
      handleToggleFailModalWithoutOkText({
        content: 'Cập nhật trạng thái tài khoản thất bại'
      })
    }
  });
}

const useSetCompanyMutation = () => {
  const queryClient = useQueryClient();
  const { handleToggleFailModalWithoutOkText, handleToggleSuccessModal } = useModalRedux()
  return useMutation({
    mutationFn: accountService.settingCompany,
    onSuccess(data) {
      queryClient.invalidateQueries({
        queryKey: [EMPLOYEE_PROFILES],
      });
      handleToggleSuccessModal({
        content: 'Cài đặt công ty thành công',
      }, false)
    },
    onError: () => {
      handleToggleFailModalWithoutOkText({
        content: 'Cài đặt công ty thất bại'
      })
    }
  });
}

const useSetCompanyUserMutation = () => {
  const queryClient = useQueryClient();
  const { handleToggleFailModalWithoutOkText, handleToggleSuccessModal } = useModalRedux()
  return useMutation({
    mutationFn: ({id, data} : { id: string, data: any }) => accountService.settingCompanyUser(id, data),
    onSuccess(data) {
      queryClient.invalidateQueries({
        queryKey: [EMPLOYEE_PROFILES],
      });
      handleToggleSuccessModal({
        content: 'Cài đặt công ty thành công',
      }, false)
    },
    onError: () => {
      handleToggleFailModalWithoutOkText({
        content: 'Cài đặt công ty thất bại'
      })
    }
  });
}

const useUpdateAccountMutation = (id: string, username: string) => {
  const queryClient = useQueryClient();
  const { handleToggleSuccessModal } = useModalRedux()

  return useMutation({
    mutationFn: accountService.updateAccount(id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [EMPLOYEE_PROFILES],
      });
      handleToggleSuccessModal({
        content: 'Cập nhật tài khoản thành công',
      }, false)
    },
  })
}

const useGetAccountByUsername =  () => {
  return useMutation({
    mutationFn: (userName: string) => accountService.getAccountByUsername(userName),
  });
}

const usePatchChangeAccountCompany = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [QUERY_KEY.ACCOUNT_BY_ID],
    mutationFn: (id: string) =>
      accountService.changeAccountCompany({ companyId: id }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [EMPLOYEE_PROFILES] });
      onOk && onOk();
    },
  });
};


export default {
  useCreateEmployeeAccount,
  useGetAccountsStatuses,
  useGetAccountById,
  useToggleActivate,
  useGetAccountByUsername,
  useToggleStatusMutation,
  useUpdateAccountMutation,
  useSetCompanyMutation,
  useSetCompanyUserMutation,
  usePatchChangeAccountCompany
};
