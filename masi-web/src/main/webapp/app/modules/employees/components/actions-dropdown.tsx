import ButtonIcon from 'app/components/button-icon/button-icon';
import AuthGuard from 'app/components/guards/auth-guard';
import { PATH } from 'app/constants/path';
import { useDownloadDocx } from 'app/hooks/use-download';
import useEmployee from 'app/hooks/use-employee';
import { IEmployeeParams, IEmployeeProfiles } from 'app/shared/model/employee.model';
import { EMPLOYEE_STATUS } from 'app/shared/model/enumerations/employee.model';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import React from 'react';
import { useNavigate } from 'react-router';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';
import { UserUpdateIcon } from './user-icons';
import Flex from 'app/components/flex/flex';

const { useGetEmployeeExportContractLazyQuery, usePatchEnableTimeKeepingDevice } = useEmployee;

interface IActionsDropdownProps {
  toggleDetail: () => void;
  toggleDeactivate: () => void;
  toggleActivate: () => void;
  toggleUpload: () => void;
  toggleConfirmLeave: () => void;
  record: IEmployeeProfiles;
  setSelectedRecord: (id: string) => void;
  filter: IEmployeeParams;
  toggleProvideAccount: () => void;
  toggleActivateTimkeepingDevicer: () => void;
}

const ActionsDropdown = (props: IActionsDropdownProps) => {
  const { toggleDetail, filter, toggleDeactivate, toggleActivate, toggleUpload, toggleConfirmLeave, record, setSelectedRecord, toggleProvideAccount, toggleActivateTimkeepingDevicer } = props;

  const navigate = useNavigate();

  const { trigger, data } = useGetEmployeeExportContractLazyQuery(record?.id);

  const { mutate } = usePatchEnableTimeKeepingDevice(record?.id, toggleActivateTimkeepingDevicer);

  const handleDetail = () => {
    setSelectedRecord(record?.id);
    toggleDetail();
  };

  const handleDeactivate = () => {
    setSelectedRecord(record?.id);
    toggleDeactivate();
  };

  const handleActivate = () => {
    setSelectedRecord(record?.id);
    toggleActivate();
  };

  const handleUpload = () => {
    setSelectedRecord(record?.id);
    toggleUpload();
  };

  const handleConfirmLeave = () => {
    setSelectedRecord(record?.id);
    toggleConfirmLeave();
  };
  const handleProvideAccount = () => {
    setSelectedRecord(record?.id);
    toggleProvideAccount();
  }

  const disabledDeactivate = !record?.isActive;
  const disabledActivate = record?.isActive;
  const disabledConfirmLeave = record?.status === EMPLOYEE_STATUS.RESIGNED;
  const disableActivateTimeClock = !!record?.pin;

  const disableUpload = record?.status === EMPLOYEE_STATUS.RESIGNED

  useDownloadDocx(data?.data, `HĐLĐ-${record?.fullName}`, 'docx');

  const handleClick = () => {
    const params = new URLSearchParams({
      page: filter.page?.toString() || '0',
      size: filter.size?.toString() || '10',
      search: filter.search || '',
      workspaceIds: filter.workspaceIds?.join(',') || '',
      workspaceTypes: filter.workspaceTypes || '',
      employeeStatuses: filter.employeeStatuses || '',
      profileStates: filter.profileStates?.join(',') || '',
    });

    const url = `${PATH.EMPLOYEES_UPDATE.replace(':id', record?.id)}?${params.toString()}`;

    navigate(url);
  };

  const handleActivateTimeClock = () => mutate()

  return (
    <UncontrolledDropdown>
      <DropdownToggle className="actions-dropdown-toggle">
        <img src="content/images/vuesax/linear/more.svg" alt="more" />
      </DropdownToggle>
      <DropdownMenu container="body" className="actions-dropdown">
        <AuthGuard permissionKey="EMPLOYEES.EDIT">
          <DropdownItem className='update' onClick={handleClick}>
            <Flex align="center" gap={10}>
              <img
                className="pointer"
                src="content/images/vuesax/linear/edit-primary.svg"
              />
              <p className="pointer">Cập nhật</p>
            </Flex>
          </DropdownItem>

          {!record?.account && (
            <DropdownItem className="provide-account" onClick={handleProvideAccount}>
              <Flex align="center" gap={10}>
                <UserUpdateIcon />
                <p className="pointer">Cấp tài khoản</p>
              </Flex>
            </DropdownItem>
          )}
          <DropdownItem
            className="upload-profile"
            onClick={handleUpload}
            disabled={disableUpload}
          >
            <Flex align="center" gap={10}>
              <img
                className="pointer"
                src="content/images/vuesax/linear/direct-inbox.svg"
              />
              <p className="pointer">Tải lên hồ sơ</p>
            </Flex>
          </DropdownItem>

          <DropdownItem
            className="confirm-leave"
            onClick={handleConfirmLeave}
            disabled={disabledConfirmLeave}
          >
            <Flex align="center" gap={10}>
              <img
                className="pointer"
                src="content/images/vuesax/linear/briefcase.svg"
              />
              <p className="pointer">Xác nhận đã nghỉ</p>
            </Flex>
          </DropdownItem>
        </AuthGuard>

        <AuthGuard permissionKey="EMPLOYEES.EXPORT">
          <DropdownItem className="print" onClick={trigger}>
            <Flex align="center" gap={10}>
              <img
                className="pointer"
                src="content/images/vuesax/linear/receive-square.svg"
              />
              <p className="pointer">In HĐLĐ</p>
            </Flex>
          </DropdownItem>
        </AuthGuard>

        <AuthGuard permissionKey="EMPLOYEES.EDIT">
          <DropdownItem
            className="activate"
            onClick={handleActivateTimeClock}
            disabled={disableActivateTimeClock}
          >
            <Flex align="center" gap={10}>
                <img
                  className="pointer"
                  src="content/images/vuesax/linear/profile-tick.svg"
                />
              <p className="pointer">Kích hoạt máy chấm công</p>
            </Flex>
          </DropdownItem>
        </AuthGuard>

        <AuthGuard permissionKey="EMPLOYEES.VIEW">
          <DropdownItem
            className='history'
            onClick={() =>
              navigate(PATH.EMPLOYEES_CHANGE_LOGS.replace(':id', record?.id))
            }
          >
            <Flex align="center" gap={10}>
                <img
                  className="pointer"
                  src="content/images/vuesax/linear/profile-tick.svg"
                />
              <p className="pointer">Lịch sử</p>
            </Flex>
          </DropdownItem>
        </AuthGuard>
      </DropdownMenu>
    </UncontrolledDropdown>
  );
};

export default ActionsDropdown;
