import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useDocumentary from 'app/hooks/use-documentary';
import React from 'react';

const { usePatchDocumentaryReviewMutation } = useDocumentary;

interface IDocumentaryProposeModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string | null;
  setSelectedRecord: (value: string | null) => void;
}

const DocumentaryProposeModals = (props: IDocumentaryProposeModalsProps) => {
  const { isOpen, toggle, selectedRecord, toggleSuccess, setSelectedRecord } = props;

  const onOk = () => {
    toggle();
    toggleSuccess();
  };

  const { mutate, isPending } = usePatchDocumentaryReviewMutation(selectedRecord, onOk);

  const onSubmit = () => {
    mutate();
    setSelectedRecord(null);
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      okText="Xác nhận"
      disabledOk={isPending}
      onOk={onSubmit}
      titleHeader='Yêu cầu xét duyệt'
    >
      <Typography level={4}>Bạn muốn yêu cầu xét duyệt công văn này?</Typography>
    </Modal>
  );
};

export default DocumentaryProposeModals;
