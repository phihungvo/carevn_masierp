import Flex from 'app/components/flex/flex';
import { LEAVE_REGIME_STATUS } from 'app/shared/model/enumerations/leave-regime.model';
import { ILeaveRegime } from 'app/shared/model/leave-regime.model';
import React from 'react';
import { PopoverBody, PopoverHeader, UncontrolledPopover } from 'reactstrap';

interface IPopoverApprovalProps {
  data: ILeaveRegime['processLeaveRegimeRequests'];
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
              <span className="fw-bold">Chưa duyệt:</span>{' '}
              {data
                .filter(item => item?.status === LEAVE_REGIME_STATUS.WAITING_APPROVAL)
                ?.map(r => (r?.approver?.lastName || '') + ' ' + (r?.approver?.firstName || ''))
                .join(', ')}
            </div>
          </Flex>
        </PopoverBody>
      </UncontrolledPopover>
    </>
  );
};

export default PopoverApproval;
