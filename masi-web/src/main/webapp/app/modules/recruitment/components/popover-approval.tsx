import Flex from 'app/components/flex/flex';
import { IRecruitmentReviewRequest } from 'app/shared/model/recruitment.model';
import React from 'react';
import { PopoverBody, PopoverHeader, UncontrolledPopover } from 'reactstrap';

interface IPopoverApprovalProps {
  data: IRecruitmentReviewRequest[];
  id: string;
  children?: React.ReactNode;
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
                .filter(item => !!!!item?.approvalSignFile)
                ?.map(r => r?.employeeName || '')
                .join(', ')}
            </div>

            <div>
              <span className="fw-bold">Chưa duyệt:</span>{' '}
              {data
                .filter(item => !!!item?.approvalSignFile)
                ?.map(r => r?.employeeName || '')
                .join(', ')}
            </div>
          </Flex>
        </PopoverBody>
      </UncontrolledPopover>
    </>
  );
};

export default PopoverApproval;
