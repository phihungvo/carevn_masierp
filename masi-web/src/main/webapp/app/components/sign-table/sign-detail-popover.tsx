import { DATE_FORMAT } from 'app/constants/common';
import { IEmployeeProfiles } from 'app/shared/model/employee.model';
import dayjs from 'dayjs';
import React from 'react';
import { PopoverBody, PopoverHeader, UncontrolledPopover } from 'reactstrap';
import Flex from '../flex/flex';
import { Typography } from '../typography/typography';
interface ISignDetailProps {
  employee?: IEmployeeProfiles
  children?: React.ReactNode;
  id?: string;
  signAt?: string;
}

export default function SignDetailPopover({ employee, children, signAt, id }: ISignDetailProps) {
  id = id || "popover" + employee.employeeCode
  return (
    <>
      <div id={id}>{children}</div>
      <UncontrolledPopover placement="top" target={id} trigger="hover" >
        <PopoverHeader>Chi tiết xét duyệt</PopoverHeader>

        <PopoverBody >
          <Flex direction='column'>
            <div>
              Tên: {' '}
              <Typography level="text" className="fw-bolder">
                {employee?.fullName}
              </Typography>
            </div>
            <div>
              Chức vụ: {' '}
              <Typography level="text" className="fw-bolder">
                {employee?.role}
              </Typography>
            </div>
            <div>
              Ngày ký: {' '}
              <Typography level="text" className="fw-bolder">
                {dayjs(signAt).format(DATE_FORMAT.TIME_DATE)}
              </Typography>
            </div>
          </Flex>

        </PopoverBody>
      </UncontrolledPopover>
    </>
  )
}
