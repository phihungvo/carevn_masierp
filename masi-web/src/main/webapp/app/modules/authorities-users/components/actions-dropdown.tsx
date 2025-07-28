import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';

import ButtonIcon from 'app/components/button-icon/button-icon';
import Flex from 'app/components/flex/flex';
import AuthGuard from 'app/components/guards/auth-guard';
import useAccount from 'app/hooks/use-account';
import useModalRedux from 'app/hooks/use-modal-redux';
import { IEmployeeProfiles } from 'app/shared/model/employee.model';
import { iconPath } from 'app/shared/util/format';

const {
  useToggleStatusMutation,
} = useAccount

interface IActionsDropdown {
  record: IEmployeeProfiles;
  setSelectedRecord: (id: string) => void;
  toggleDetail: () => void;
  toggleUpdateUsers: () => void;
  selectedRow: any,
  setSelectedRow: React.Dispatch<any>
  toggleUpdateAccount: () => void
  toggleSettingCompanies: () => void
}

const ActionsDropdown = (props: IActionsDropdown) => {
  const {
    record,
    setSelectedRecord,
    toggleDetail,
    toggleUpdateUsers,
    selectedRow,
    setSelectedRow,
    toggleUpdateAccount,
    toggleSettingCompanies
  } = props;

  const { handleToggleModal, closeModal } = useModalRedux()

  const toggleStatusMutation = useToggleStatusMutation(record?.id)

  const handleUpdate = () => {
    setSelectedRecord(record.id);
    toggleUpdateUsers();
  };

  const onToggleUpdateAccount = () => {
    setSelectedRow(record);
    toggleUpdateAccount()
  }

  const onToggleUpdateStatusAccount = () => {
    handleToggleModal({
      isOpen: true,
      title: 'Cập nhật trạng thái tài khoản',
      content: (
        <Flex align='center'>
          <p>Bạn có chắc chắn muốn {!record?.['isActivated'] ? 'kích hoạt' : 'vô hiệu'} tài khoản này không?</p>
        </Flex>
      ),
      okText: 'Xác nhận',
      onOK: () => toggleStatusMutation.mutate(),
      cancelText: 'Hủy',
      onCancel: closeModal,
    })
  }

  const onToggleUpdateCompany = () => {
    setSelectedRow(record);
    toggleSettingCompanies()
  }

  return (
    <UncontrolledDropdown>
      <DropdownToggle className="actions-dropdown-toggle">
        <img src="content/images/vuesax/linear/more.svg" alt="more" />
      </DropdownToggle>
      <DropdownMenu container="body" className="actions-dropdown">
          <AuthGuard permissionKey='PERMISSIONS_USERS.EDIT'>
            <DropdownItem
              onClick={onToggleUpdateAccount}
            >
              <ButtonIcon className="authorities-btn" icon={<img className="pointer" src="content/images/vuesax/linear/role.svg" alt="update" />}>
                Cập nhật tài khoản
              </ButtonIcon>
            </DropdownItem>
          </AuthGuard>
          <AuthGuard permissionKey='PERMISSIONS_USERS.EDIT'>
            <DropdownItem
              onClick={onToggleUpdateStatusAccount}
            >
              <ButtonIcon 
                className="authorities-btn"
                icon={<img 
                  className="pointer" 
                  src={!record?.['isActivated'] ? "content/images/vuesax/linear/check.svg" : "content/images/vuesax/linear/uncheck.svg"}
                  alt="update" />}
              >
                {!record?.['isActivated'] ? 'Kích hoạt' : 'Vô hiệu'}
              </ButtonIcon>
            </DropdownItem>
          </AuthGuard>
          <AuthGuard permissionKey='PERMISSIONS_USERS.EDIT'>
            <DropdownItem
              onClick={handleUpdate}
            >
              <ButtonIcon className="authorities-btn" icon={<img className="pointer" src="content/images/vuesax/linear/permission.svg" alt="update" />}>
                Cập nhật quyền
              </ButtonIcon>
            </DropdownItem>
          </AuthGuard>
          <AuthGuard permissionKey='PERMISSIONS_USERS.EDIT'>
            <DropdownItem
              onClick={onToggleUpdateCompany}
            >
              <ButtonIcon className="authorities-btn" icon={<img className="pointer" src="content/images/vuesax/linear/setting_company.svg" alt="update" />}>
                Cài đặt công ty
              </ButtonIcon>
            </DropdownItem>
          </AuthGuard>
      </DropdownMenu>
    </UncontrolledDropdown>
  );
};

export default ActionsDropdown;
