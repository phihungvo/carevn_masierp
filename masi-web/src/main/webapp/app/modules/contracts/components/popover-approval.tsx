import React from 'react';
import { PopoverBody, PopoverHeader, UncontrolledPopover } from 'reactstrap';

import Flex from 'app/components/flex/flex';

interface IPopoverApprovalProps {
  data: any;
  children?: React.ReactNode;
  id: string;
}

const PopoverApproval = (props: IPopoverApprovalProps) => {
  const { data, children, id = 'popover' } = props;

  return (
    <>
      <div id={id}>{children}</div>
      <UncontrolledPopover placement="bottom" target={id} trigger="hover">
        <PopoverHeader>Danh sách xét duyệt</PopoverHeader>
        <PopoverBody>
          <Flex direction="column" gap={12}>
            <div>
              <span className="fw-bold">Đã duyệt:</span>{' '}
              {data?.filter(item => item?.result)
                ?.map(r => (r?.employee?.lastName || '') + ' ' + (r?.employee?.firstName || ''))
                .join(', ')}
            </div>

            <div>
              <span className="fw-bold">Chưa duyệt:</span>{' '}
              {data?.filter(item => !item?.result)
                ?.map(r => (r?.employee?.lastName || '') + ' ' + (r?.employee?.firstName || ''))
                .join(', ')}
            </div>
          </Flex>
        </PopoverBody>
      </UncontrolledPopover>
    </>
  );
};

export default PopoverApproval;
