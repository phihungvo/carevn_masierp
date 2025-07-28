import { useQueryClient } from "@tanstack/react-query";
import { useAppSelector } from "app/config/store";
import { QUERY_KEY } from "app/constants/query-key";
import { Account } from "app/shared/model/account.model";
import { AxiosResponse } from "axios";

const {
  USER_DEFAULT
} = QUERY_KEY;

type UserProfileDefault = Account & {
  label: string,
  employeeCode: string,
  fullName: string
}

const useAccountApp = () => {
  const account = useAppSelector(state => state?.authentication?.account)
  const queryClient = useQueryClient()
  const user = queryClient.getQueryData<AxiosResponse<UserProfileDefault>>([USER_DEFAULT])

  return {
    ...user?.data,
    label: user?.data?.employeeCode + ' - ' + user?.data?.fullName,
    employeeId: user?.data?.id,
    signatureId: account?.signatureId,
    signatureFileName: account?.signatureFileName,
  };
}

export default useAccountApp
