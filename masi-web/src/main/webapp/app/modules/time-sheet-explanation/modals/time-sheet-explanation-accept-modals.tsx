import React from 'react';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { ITimeKeepingExplanation } from 'app/shared/model/time-keeping-explanation.model';
import useTimeKeepingExplanation from 'app/hooks/use-time-keeping-explanation';
import { TIME_KEEPING_EXPLANATION_REVIEW_STATUS } from 'app/shared/model/enumerations/time-keeping-explanation.model';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';

const { REVIEW_TIME_KEEPING_EXPLANATION } = MUTATION_KEY;
const { usePatchTimeKeepingExplanationReviewMutation } = useTimeKeepingExplanation;

// MODAL ACCEPT REQUEST
interface IModalAcceptRequest {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRows: ITimeKeepingExplanation[];
  setSelectedRowKeys: (selectedRowKeys: string[]) => void;
  setSelectedRows: React.Dispatch<React.SetStateAction<ITimeKeepingExplanation[]>>
}

export const AcceptRequestModal = (props: IModalAcceptRequest) => {
  const { isOpen, toggle, toggleSuccess, selectedRows, setSelectedRowKeys, setSelectedRows } = props;

  const { mutateAsync } = usePatchTimeKeepingExplanationReviewMutation();
  const isReviewingReq = useIsMutating({ mutationKey: [REVIEW_TIME_KEEPING_EXPLANATION] });

  const handleApproveReq = () => {
    const promises = selectedRows.map(row =>
      mutateAsync({
        data: {
          status: TIME_KEEPING_EXPLANATION_REVIEW_STATUS.APPROVED,
        },
        id: row.reviewDtos[0].id,
      }),
    );

    Promise.all(promises)
      .then(() => {
        toggleSuccess();
      })
      .then(() => {
        setSelectedRowKeys([]);
      })
      .finally(() => {
        toggle();
        setSelectedRows([]);
      });
  };

  return (
    <Modal
      disabledOk={!!isReviewingReq}
      isOpen={isOpen}
      toggle={toggle}
      className="modal-approve-explanation"
      onOk={handleApproveReq}
      cancel={false}
      titleHeader='Đồng ý đơn giải trình'
    >
      <Typography level={4}>Bạn đã đồng ý xét duyệt cho một hoặc nhiều giải trình chấm công. Vui lòng xác nhận</Typography>
    </Modal>
  );
};

// MODAL SUCCESS ACCEPT REQUEST
interface IModalSuccessAcceptRequest {
  isOpen: boolean;
  toggle: () => void;
}

export const SuccessAcceptRequestModal = (props: IModalSuccessAcceptRequest) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      titleHeader='Xét duyệt giải trình'
    >
      <Typography level={4}>Xét duyệt giải trình chấm công thành công</Typography>
    </Modal>
  );
};
