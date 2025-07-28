import Modal from 'app/components/modal/modal';
import React from 'react';
import AllocationForm from '../component/allocation-form';
import { FORM } from 'app/shared/model/enumerations/form.model';

interface IAllocationUpdate {
  isOpen: boolean;
  toggle: () => void;
}

const AllocationUpdate = (props: IAllocationUpdate) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      okText="Cập nhật"
      className="allocation-update"
      titleHeader="Cập nhật khiếu nại"
      okSubmitForm={FORM.CALL_CENTER}
    >
      <AllocationForm type="update" />
    </Modal>
  );
};

export default AllocationUpdate;
