import { FILE_UTIL } from 'app/constants/common';
import useEmployee from 'app/hooks/use-employee';
import React from 'react';
import AttachmentPreview from '../attachment-preview/attachment-preview';
import Flex from '../flex/flex';
import SignDetailPopover from './sign-detail-popover';

interface ISign {
  signName: string;
  signId: string;
  employeeId: string;
  signAt?: string;
}

interface ITableProps {
  data: ISign[];
}
const { useGetListProfileByIds } = useEmployee

export default function SignTable({ data }: ITableProps) {
  const employeeIds = data?.map(sign => sign.employeeId);
  const { data: employees } = useGetListProfileByIds(employeeIds || []);
  return (
    <Flex gap={10} justify='start' flexWrap='wrap'>
      {employees && data
        ?.filter(sign => sign?.signName)
        ?.map((sign, index) => (
          <Flex key={index} className="sign-table " direction='column' gap={2} style={{ paddingLeft: '10px' }}>
            <SignDetailPopover
              id={`popover-sign${index}`}
              employee={employees?.data?.find(e => e?.id === sign?.employeeId)} signAt={sign?.signAt}>
              <AttachmentPreview name={sign?.signName} fileUrl={`${FILE_UTIL}/${sign?.signId}`} />
            </SignDetailPopover>
          </Flex>
        ))}
    </Flex>
  )
}
