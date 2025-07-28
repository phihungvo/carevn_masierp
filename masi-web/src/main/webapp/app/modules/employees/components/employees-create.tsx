import { Button } from 'reactstrap';
import React, { useState } from 'react';
import { useNavigate } from 'react-router';

import Flex from 'app/components/flex/flex';
import Card from 'app/components/card/card';
import EmployeesForm from '../form/employees-form';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { Typography } from 'app/components/typography/typography';
import { EmployeeCreateSuccessModals } from '../modals/employee-create-success-modals';

const { CREATE_EMPLOYEE_PROFILES } = MUTATION_KEY;

const EmployeesCreate = () => {
  const navigate = useNavigate();

  const [isOpen, setIsOpen] = useState(false);

  const toggle = () => {
    setIsOpen(prev => !prev);
  };

  const isCreating = useIsMutating({
    mutationKey: [CREATE_EMPLOYEE_PROFILES],
  });

  return (
    <div className='page_container'>
      <Typography level={4}>Danh sách NV</Typography>
      <Card className="card-template">
        <Flex direction="column" >
          <Typography level={5}>Tạo mới hồ sơ nhân viên</Typography>

          <EmployeesForm toggle={toggle} />
        </Flex>

        <Flex gap={12} justify="end" className="btn-group-template">
          <Button outline className="btn-cancel-template" onClick={() => navigate(-1)}>
            Huỷ
          </Button>
          <Button color="primary" type="submit" form={FORM.EMPLOYEES_CREATE} disabled={!!isCreating}>
            Tạo mới
          </Button>
        </Flex>
      </Card>

      <EmployeeCreateSuccessModals isOpen={isOpen} toggle={toggle} />
    </div>
  );
};

export default EmployeesCreate;
