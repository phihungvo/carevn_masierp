import AuthGuard from 'app/components/guards/auth-guard';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import React from 'react';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';
import ButtonIcon from 'app/components/button-icon/button-icon';
import { IRecruitment } from 'app/shared/model/recruitment.model';
import { RECRUITMENT_STATUS } from 'app/shared/model/enumerations/recruitment.model';
import { checkHighlightDeadline } from 'app/modules/recruitment/check-highlight-deadline';
import { useAppSelector } from 'app/config/store';

interface IActionsDropdownProps {
  toggleDetail: () => void;
  toggleUpdate: () => void;
  toggleApprove: () => void;
  toggleReject: () => void;
  toggleSchedule: () => void;
  toggleDelete: () => void;
  toggleRenew: () => void;
  toggleHistory: () => void;
  record: IRecruitment;
  setSelectedRecord: (id: string) => void;
  setRecord: React.Dispatch<React.SetStateAction<IRecruitment>>
}

const ActionsDropdown = (props: IActionsDropdownProps) => {
  const { toggleDetail, toggleUpdate, toggleApprove, toggleRenew, toggleSchedule, toggleHistory, toggleDelete, record, setSelectedRecord, setRecord } =
    props;

  const account = useAppSelector(state => state.authentication.account);

  const handleDetail = () => {
    setSelectedRecord(record.id);
    toggleDetail();
  };

  const handleUpdate = () => {
    setSelectedRecord(record.id);
    toggleUpdate();
  };

  const handleApprove = () => {
    setSelectedRecord(record.id);
    toggleApprove();
    setRecord(record);
  };

  const handleRenew = () => {
    setSelectedRecord(record.id);
    toggleRenew();
  };

  const handleSchedule = () => {
    setSelectedRecord(record.id);
    toggleSchedule();
  };

  const handleDelete = () => {
    setSelectedRecord(record.id);
    toggleDelete();
  };

  const handleHistory = () => {
    toggleHistory();
    setSelectedRecord(record.id);
  };

  const isAllowedProcess = record?.checkApprove;

  const disabledUpdate = record?.status !== RECRUITMENT_STATUS.WAITING_APPROVAL || record?.numberAdjourn !== 0;
  const disabledApprove = !isAllowedProcess || record?.status !== RECRUITMENT_STATUS.WAITING_APPROVAL && record?.status !== RECRUITMENT_STATUS.WAITING_RENEW;
  const disabledRenew =
    !checkHighlightDeadline(record?.deadline) && record?.numberAdjourn === 0 ||
    ((record?.status !== RECRUITMENT_STATUS.APPROVED && record?.numberAdjourn === 0) &&
      record?.status !== RECRUITMENT_STATUS.WAITING_INTERVIEW && record?.status !== RECRUITMENT_STATUS.WAITING_APPROVAL) ||
    !checkHighlightDeadline(record?.deadline);
  const disabledInterview =
    (record?.status !== RECRUITMENT_STATUS.APPROVED && record?.status !== RECRUITMENT_STATUS.WAITING_INTERVIEW) ||
    checkHighlightDeadline(record?.deadline);
  const disabledDelete = record?.status !== RECRUITMENT_STATUS.WAITING_APPROVAL;

  return (
    <UncontrolledDropdown>
      <DropdownToggle className="actions-dropdown-toggle">
        <img src="content/images/vuesax/linear/more.svg" alt="more" />
      </DropdownToggle>
      <DropdownMenu container="body" className="actions-dropdown">
          <AuthGuard permissionKey='RECRUITMENT.EDIT'>
            <DropdownItem onClick={handleUpdate} disabled={disabledUpdate}>
              <ButtonIcon className="update" icon={<img className="pointer" src="content/images/vuesax/linear/sms-edit.svg" alt="update" />}>
                Cập nhật
              </ButtonIcon>
            </DropdownItem>
  
            <DropdownItem onClick={handleApprove} disabled={disabledApprove}>
              <ButtonIcon
                className="approved"
                icon={<img className="pointer" src="content/images/vuesax/linear/receipt-search.svg" alt="approved" />}
              >
                Xét duyệt
              </ButtonIcon>
            </DropdownItem>
  
            <DropdownItem onClick={handleRenew} disabled={disabledRenew}>
              <ButtonIcon
                className="renew"
                icon={<img className="pointer" src="content/images/vuesax/linear/sms-tracking.svg" alt="renew" />}
              >
                Gia hạn
              </ButtonIcon>
            </DropdownItem>
  
            <DropdownItem onClick={handleSchedule} disabled={disabledInterview}>
              <ButtonIcon
                className="schedule"
                icon={<img className="pointer" src="content/images/vuesax/linear/stickynote.svg" alt="schedule" />}
              >
                Lên lịch phỏng vấn
              </ButtonIcon>
            </DropdownItem>
  
            <DropdownItem onClick={handleDelete} disabled={disabledDelete}>
              <ButtonIcon className="delete" icon={<img className="pointer" src="content/images/vuesax/linear/trash.svg" alt="delete" />}>
                Xoá tuyển dụng
              </ButtonIcon>
            </DropdownItem>
          </AuthGuard>

          <AuthGuard permissionKey='RECRUITMENT.VIEW'>
            <DropdownItem onClick={handleHistory}>
              <ButtonIcon className="history" icon={<img className="pointer" src="content/images/vuesax/linear/rotate-left.svg" />}>
                Lịch sử
              </ButtonIcon>
            </DropdownItem>
          </AuthGuard>
      </DropdownMenu>
    </UncontrolledDropdown>
  );
};

export default ActionsDropdown;
