import React from 'react';
import { useNavigate } from 'react-router';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';

import AuthGuard from 'app/components/guards/auth-guard';
import ButtonIcon from 'app/components/button-icon/button-icon';
import productionProcessMapping from './production-process-mapping';
import { PATH } from 'app/constants/path';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { PRODUCTION_PROCESS_STATUS } from 'app/shared/model/enumerations/production-process.model';
import { checkDisabledStartBtn } from './utils/handle-disable-button-start';

const { mapProductionProcessNamePath } = productionProcessMapping;

interface IActionsDropdownProps {
  record: any;
  id: string;
  status: PRODUCTION_PROCESS_STATUS;
  workItemId: string;
  toggleUpdateReq: () => void;
  toggleModalStart: () => void;
  toggleModalStop: () => void;
  toggleModalComplete: () => void;
  toggleModalDelete: () => void;
  toggleNoticesUpdate?: () => void;
  setSelectedRecord: (id: string) => void;
  rowNumber?: number;
}

const ActionsDropdown = (props: IActionsDropdownProps) => {
  const {
    record,
    id,
    toggleUpdateReq,
    toggleModalStart,
    toggleModalStop,
    toggleModalComplete,
    toggleModalDelete,
    toggleNoticesUpdate,
    setSelectedRecord,
    status,
    workItemId,
    rowNumber,
  } = props;

  const navigate = useNavigate();

  const handleViewDetail = () => {
    navigate(
      PATH.PRODUCTION_PROCESS_DETAIL.replace(':id', id).replace(':workItemId', workItemId) +
      `/${mapProductionProcessNamePath(record?.checklistType)}/detail`,
    );
  };

  const handleUpdate = () => {
    navigate(
      PATH.PRODUCTION_PROCESS_DETAIL.replace(':id', id).replace(':workItemId', workItemId) +
      `/${mapProductionProcessNamePath(record?.checklistType)}/update`,
    );
    setSelectedRecord(id);
  };

  // const handleDelete = () => {
  //   toggleModalDelete();
  //   setSelectedRecord(id);
  // };

  const handleStart = () => {
    toggleModalStart();
    setSelectedRecord(id);
  };

  const handleStop = () => {
    toggleModalStop();
    setSelectedRecord(id);
  };

  const handleComplete = () => {
    toggleModalComplete();
    setSelectedRecord(id);
  };

  const disabledUpdate = status === PRODUCTION_PROCESS_STATUS.COMPLETED;
  // const disabledDelete = status === PRODUCTION_PROCESS_STATUS.COMPLETED;
  // let disabledStart = handleDisableButtonStart(record?.parent, record);
  const disabledStart = checkDisabledStartBtn(record?.parent, rowNumber);
  const disabledStop = status !== PRODUCTION_PROCESS_STATUS.RUNNING;
  const disabledComplete = status !== PRODUCTION_PROCESS_STATUS.RUNNING;

  return (
    <UncontrolledDropdown >
      <DropdownToggle className="actions-dropdown-toggle">
        <img src="content/images/vuesax/linear/more.svg" alt="more" />
      </DropdownToggle>
      <DropdownMenu container="body" className="actions-dropdown">
          <DropdownItem onClick={handleViewDetail}>
            <ButtonIcon icon={<img className="pointer" src="content/images/vuesax/linear/eye.svg" alt="detail" />}>Chi tiết</ButtonIcon>
          </DropdownItem>

          <DropdownItem onClick={handleUpdate} disabled={disabledUpdate}>
            <ButtonIcon
              className="update"
              icon={<img className="pointer" src="content/images/vuesax/linear/edit-primary.svg" alt="edit" />}
            >
              Cập nhật
            </ButtonIcon>
          </DropdownItem>

          <DropdownItem onClick={handleStart} disabled={disabledStart}>
            <ButtonIcon className="start" icon={<img className="pointer" src="content/images/vuesax/linear/play.svg" alt="start" />}>
              Bắt đầu
            </ButtonIcon>
          </DropdownItem>

          <DropdownItem onClick={handleStop} disabled={disabledStop}>
            <ButtonIcon className="stop" icon={<img className="pointer" src="content/images/vuesax/linear/record.svg" alt="stop" />}>
              Ngừng
            </ButtonIcon>
          </DropdownItem>

          <DropdownItem onClick={handleComplete} disabled={disabledComplete}>
            <ButtonIcon
              className="success"
              icon={<img className="pointer" src="content/images/vuesax/linear/tick-circle.svg" alt="success" />}
            >
              Hoàn thành
            </ButtonIcon>
          </DropdownItem>
      </DropdownMenu>
    </UncontrolledDropdown>
  );
};

export default ActionsDropdown;
