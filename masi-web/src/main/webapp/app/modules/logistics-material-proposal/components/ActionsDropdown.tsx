import React from 'react'
import { useNavigate } from 'react-router';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';

import ButtonIcon from 'app/components/button-icon/button-icon';
import { ILogisticsMaterialProposal } from "app/shared/model/logistics-material-proposal";
import { LOGISTICS_MATERIAL_RPOPOSAL_STATUS } from 'app/shared/model/enumerations/logistics-material-proposal';
import { PATH } from 'app/constants/path';

interface IActionsDropdownProps {
    record: ILogisticsMaterialProposal;
    setSelectedRecord: (id: string) => void;
    toggleUpdate: () => void;
    toggleDetail: () => void;
    togglePropose: () => void;
    toggleApprove: () => void;
    toggleDelete: () => void;
    toggleCancel: () => void;
}

export default function ActionsDropdown({ record, setSelectedRecord, toggleUpdate, toggleDetail, togglePropose, toggleApprove, toggleDelete, toggleCancel }: IActionsDropdownProps) {

    const navigate = useNavigate()

    const handleUpdate = () => {
        setSelectedRecord(record?.id)
        toggleUpdate()
    }

    const handleDetail = () => {
        setSelectedRecord(record?.id)
        toggleDetail()
    }

    const handleDelete = () => {
        setSelectedRecord(record?.id)
        toggleDelete()
    }

    const handleCancel = () => {
        setSelectedRecord(record?.id)
        toggleCancel()
    }

    const disablePropose = record?.status !== LOGISTICS_MATERIAL_RPOPOSAL_STATUS.NEW;

    const disableDelete = record?.status === LOGISTICS_MATERIAL_RPOPOSAL_STATUS.APPROVED;

    return (
        <UncontrolledDropdown className="actions-dropdown">
            <DropdownToggle className="actions-dropdown-toggle">
                <img src="content/images/vuesax/linear/more.svg" alt="more" />
            </DropdownToggle>

            <DropdownMenu>
                <DropdownItem onClick={() => handleDetail()}>
                    <ButtonIcon className="detail" icon={<img className="pointer" src="content/images/vuesax/linear/eye.svg" alt="detail" />}>
                        Chi tiết
                    </ButtonIcon>
                </DropdownItem>

                <DropdownItem onClick={() => handleUpdate()} >
                    <ButtonIcon className="update" icon={<img className="pointer" src="content/images/vuesax/linear/edit-active.svg" alt="update" />}>
                        Cập nhật
                    </ButtonIcon>
                </DropdownItem>

                <DropdownItem onClick={togglePropose} disabled={disablePropose}>
                    <ButtonIcon className="propose" icon={<img className="pointer" src="content/images/vuesax/linear/directbox-notif.svg" alt="propose" />} >
                        Yêu cầu xét duyệt
                    </ButtonIcon>
                </DropdownItem>

                <DropdownItem onClick={toggleApprove}>
                    <ButtonIcon
                        className="approve"
                        icon={<img className="pointer" src="content/images/vuesax/linear/receipt-search.svg" alt="approve" />}
                    >
                        Xét duyệt
                    </ButtonIcon>
                </DropdownItem>

                <DropdownItem onClick={handleCancel} >
                    <ButtonIcon className="cancel" icon={<img className="pointer" src="content/images/vuesax/linear/close.svg" alt="cancel" />}>
                        Huỷ
                    </ButtonIcon>
                </DropdownItem>


                <DropdownItem >
                    <ButtonIcon className="print" icon={<img className="pointer" src="content/images/vuesax/linear/receive-square.svg" />}>
                        In chứng từ
                    </ButtonIcon>
                </DropdownItem>

                <DropdownItem onClick={() => handleDelete()} disabled={disableDelete}>
                    <ButtonIcon className="delete" icon={<img className="pointer" src="content/images/vuesax/linear/trash.svg" alt="delete" />}>
                        Xoá
                    </ButtonIcon>
                </DropdownItem>

                <DropdownItem onClick={() => navigate(PATH.MATERIAL_PROPOSAL_CHANGE_LOGS.replace(':id', record?.id))}>
                    <ButtonIcon className="history" icon={<img className="pointer" src="content/images/vuesax/linear/rotate-left.svg" />}>
                        Lịch sử
                    </ButtonIcon>
                </DropdownItem>


            </DropdownMenu>
        </UncontrolledDropdown>
    )
}
