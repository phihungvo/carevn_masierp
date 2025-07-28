import React from 'react';

import ButtonIcon from 'app/components/button-icon/button-icon';
import { IInterviewSchedule } from 'app/shared/model/recruitment.model';
import { RECRUITMENT_PROCESS } from 'app/shared/model/enumerations/recruitment.model';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';
import AuthGuard from 'app/components/guards/auth-guard';
import { Action, PermissionResource } from 'app/shared/model/permission.model';

interface IActionsDropdownCandidatesProps {
  toggleDetail: () => void;
  toggleUpdate: () => void;
  record: IInterviewSchedule;
  setSelectedRecord: (id: string) => void;
  toggleUpdateCandidates: () => void;
}

const ActionsDropdownCandidates = (props: IActionsDropdownCandidatesProps) => {
  const { toggleDetail, toggleUpdate, record, setSelectedRecord, toggleUpdateCandidates } = props;

  const handleDetail = () => {
    setSelectedRecord(record.id);
    toggleDetail();
  };

  const handleUpdate = () => {
    setSelectedRecord(record.id);
    toggleUpdate();
  };

  const handleUpdateCandidates = () => {
    setSelectedRecord(record.id);
    toggleUpdateCandidates();
  }

  const disabledUpdate = record?.process === RECRUITMENT_PROCESS.INTERVIEWED;
  const disableUpdateCandidates = record?.process === RECRUITMENT_PROCESS.INTERVIEWED;

  return (
    <UncontrolledDropdown>
      <DropdownToggle className="actions-dropdown-toggle">
        <img src="content/images/vuesax/linear/more.svg" alt="more" />
      </DropdownToggle>
      <DropdownMenu container="body" className="actions-dropdown">
          <AuthGuard permissionKey='RECRUITMENT_CANDIDATES.VIEW'>
            <DropdownItem onClick={handleDetail}>
              <ButtonIcon className="detail" icon={<img className="pointer" src="content/images/vuesax/linear/eye.svg" alt="detail" />}>
                Chi tiết
              </ButtonIcon>
            </DropdownItem>
          </AuthGuard>

          <AuthGuard permissionKey='RECRUITMENT_CANDIDATES.EDIT'>
            <DropdownItem onClick={handleUpdateCandidates} disabled={disableUpdateCandidates}>
              <ButtonIcon
                className="approve"
                icon={<img className="pointer" src="content/images/vuesax/linear/receipt-search.svg" alt="approve" />}
              >
                Cập nhật lịch phỏng vấn
              </ButtonIcon>
            </DropdownItem>
  
            <DropdownItem onClick={handleUpdate} disabled={disabledUpdate}>
              <ButtonIcon className="update" icon={<img className="pointer" src="content/images/vuesax/linear/sms-edit.svg" alt="update" />}>
                Cập nhật kết quả
              </ButtonIcon>
            </DropdownItem>
          </AuthGuard>
      </DropdownMenu>
    </UncontrolledDropdown>
  );
};

export default ActionsDropdownCandidates;
