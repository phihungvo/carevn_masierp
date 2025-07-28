import Flex from 'app/components/flex/flex';
import { ORDER_STATUS } from 'app/shared/model/enumerations/order.model';
import { IOrder } from 'app/shared/model/order.model';
import React from 'react';
import { PopoverBody, PopoverHeader, UncontrolledPopover } from 'reactstrap';

interface IPopoverApprovalProps {
  data: IOrder['orderReviews'];
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
              {data
                .filter(item => item?.status === ORDER_STATUS.APPROVED)
                ?.map(r => (r?.employee?.lastName || '') + ' ' + (r?.employee?.firstName || ''))
                .join(', ')}
            </div>

            <div>
              <span className="fw-bold">Chưa duyệt:</span>{' '}
              {data
                .filter(item => item?.status !== ORDER_STATUS.APPROVED)
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
