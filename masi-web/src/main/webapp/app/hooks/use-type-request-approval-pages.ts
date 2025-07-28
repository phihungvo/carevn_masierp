import { useQuery } from '@tanstack/react-query';
import { QUERY_KEY } from 'app/constants/query-key';
import typeRequestApprovalService from 'app/services/type-request-approval.service';
import { ITypeRequestApprovalParams } from 'app/shared/model/type-request-approval.model';

const { TYPE_REQUEST_APPROVAL_PAGE: TYPE_REQUEST_APPROVAL } = QUERY_KEY;


const useGetTypeRequestApproval = (filter?: ITypeRequestApprovalParams) => {
  return useQuery({
    queryKey: [TYPE_REQUEST_APPROVAL, filter?.page, filter?.size, filter?.['isDeleted.equals'], filter?.['pageName.equals']],
    queryFn: () => typeRequestApprovalService.getTypeRequestApproval(filter),
    select: data => data?.data,
  });
};



export default {
  useGetTypeRequestApproval,
};
