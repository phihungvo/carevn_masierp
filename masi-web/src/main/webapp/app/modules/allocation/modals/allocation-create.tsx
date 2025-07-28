import Modal from 'app/components/modal/modal'
import React from 'react'
import AllocationForm from 'app/modules/allocation/component/allocation-form';
import { FORM } from 'app/shared/model/enumerations/form.model';

interface IAllocationCreate {
    isOpen: boolean;
    toggle: () => void;
    toggleSuccess: () => void;
}

const AllocationCreate = (props: IAllocationCreate) => {
    const { isOpen, toggle, toggleSuccess } = props;
    return (
        <Modal
            isOpen={isOpen}
            toggle={toggle}
            titleHeader='Tạo mới khiếu nại'
            okText='Tạo mới'
            className='allocation-create'
            okSubmitForm={FORM.ALLOCATION}
        >
            <AllocationForm type='create' />
        </Modal>
    )
}

export default AllocationCreate
