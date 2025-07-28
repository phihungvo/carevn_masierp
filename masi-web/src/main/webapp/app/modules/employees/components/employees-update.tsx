import Flex from 'app/components/flex/flex';
import { Typography } from 'app/components/typography/typography';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React, { useState } from 'react';
import { useNavigate } from 'react-router';
import { Button } from 'reactstrap';
import EmployeesForm from '../form/employees-form';
import { EmployeeUpdateSuccessModals } from '../modals/employee-update-success-modals';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';
import { IEmployeeParams } from 'app/shared/model/employee.model';
import { useSearchParams } from 'react-router-dom';
import { WORKSPACE_TYPE } from 'app/shared/model/enumerations/workspace.model';
import { EMPLOYEE_STATUS, PROFILE_STATES } from 'app/shared/model/enumerations/employee.model';
import { PATH } from 'app/constants/path';
import Card from 'app/components/card/card';

const { UPDATE_EMPLOYEE_PROFILES } = MUTATION_KEY;

const EmployeesUpdate = () => {
  const navigate = useNavigate();

  const [isOpen, setIsOpen] = useState(false);

  const toggle = () => {
    setIsOpen(prev => !prev);
  };

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

  const isUpdating = useIsMutating({
    mutationKey: [UPDATE_EMPLOYEE_PROFILES],
  });


  return (
    <div className='page_container'>
      <Typography level={4}>Danh sách NV</Typography>
      <Card className="card-template">
        <Flex direction="column">
          <Typography level={5}>Cập nhật hồ sơ nhân viên</Typography>

          <EmployeesForm toggle={toggle} />
        </Flex>

        <Flex gap={12} justify="end" className="btn-group-template">
          <Button outline className="btn-cancel-template" onClick={handleClick}>
            Huỷ
          </Button>
          <Button color="primary" type="submit" form={FORM.EMPLOYEES_CREATE} disabled={!!isUpdating}>
            Cập nhật
          </Button>
        </Flex>
      </Card>

      <EmployeeUpdateSuccessModals isOpen={isOpen} toggle={toggle} />
    </div>
  );
};

export default EmployeesUpdate;
