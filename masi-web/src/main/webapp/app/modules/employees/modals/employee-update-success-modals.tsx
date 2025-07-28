import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import { IEmployeeParams } from 'app/shared/model/employee.model';
import { EMPLOYEE_STATUS, PROFILE_STATES } from 'app/shared/model/enumerations/employee.model';
import { WORKSPACE_TYPE } from 'app/shared/model/enumerations/workspace.model';
import React from 'react';
import { useNavigate } from 'react-router';
import { useSearchParams } from 'react-router-dom';

interface IModalsTemplateUpdateSuccess {
  isOpen: boolean;
  toggle: () => void;
}

export const EmployeeUpdateSuccessModals = (props: IModalsTemplateUpdateSuccess) => {
  const { isOpen, toggle } = props;

  const navigate = useNavigate();

  const [searchParams] = useSearchParams();

  const searchParamsFilter: IEmployeeParams = {
    page: parseInt(searchParams.get('page') || '0'),
    size: parseInt(searchParams.get('size') || '10'),
    search: searchParams.get('search') || '',
    workspaceIds: searchParams.get('workspaceIds')?.split(',') || [],
    workspaceTypes: searchParams.get('workspaceTypes') as WORKSPACE_TYPE || undefined,
    employeeStatuses: searchParams.get('employeeStatuses') as EMPLOYEE_STATUS || undefined,
    profileStates: searchParams.get('profileStates')?.split(',') as PROFILE_STATES[] || [],
  };

  const handleClick = () => {
    const params = new URLSearchParams();

    if (searchParamsFilter.page?.toString()) params.append('page', searchParamsFilter.page.toString());
    if (searchParamsFilter.size?.toString()) params.append('size', searchParamsFilter.size.toString());
    if (searchParamsFilter.search) params.append('search', searchParamsFilter.search);
    if (searchParamsFilter.workspaceIds.length > 0) params.append('workspaceIds', searchParamsFilter.workspaceIds.join(','));
    if (searchParamsFilter.workspaceTypes) params.append('workspaceTypes', searchParamsFilter.workspaceTypes);
    if (searchParamsFilter.employeeStatuses) params.append('employeeStatuses', searchParamsFilter.employeeStatuses);
    if (searchParamsFilter.profileStates.length > 0) params.append('profileStates', searchParamsFilter.profileStates.join(','));

    const url = `${PATH.EMPLOYEES}?${params.toString()}`;

    navigate(url);
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      okText="Đồng ý"
      onOk={handleClick}
      titleHeader='Cập nhật hồ sơ nhân viên thành công'
    >
      <Typography level={4}>Bạn đã cập nhật hồ sơ nhân viên thành công</Typography>
    </Modal>
  );
};
