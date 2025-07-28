import ButtonIcon from 'app/components/button-icon/button-icon';
import ButtonDropDown from 'app/components/ButtonV2/ButtonDropdown';
import AuthGuard from 'app/components/guards/auth-guard';
import { ICustomer } from 'app/shared/model/customer.model';

interface IActionsDropdownProps {
  toggleActivate: () => void;
  toggleDelete: () => void;
  record: ICustomer;
  setSelectedRecord: (id: string) => void;
}

const ActionsDropdown = (props: IActionsDropdownProps) => {
  const { toggleActivate, toggleDelete, record, setSelectedRecord } = props;

  const handleActivate = () => {
    setSelectedRecord(record.id);
    toggleActivate();
  };

  const handleDelete = () => {
    setSelectedRecord(record.id);
    toggleDelete();
  };

  const icon_path = 'content/images/vuesax/linear/';

  return (
    <ButtonDropDown
      items={[
        {
          children: (
            <AuthGuard permissionKey="CUSTOMERS_DISPOSED.EDIT">
              <ButtonIcon
                className="activate"
                icon={
                  <img
                    className="pointer"
                    src="content/images/vuesax/linear/profile-tick.svg"
                    alt="activate"
                  />
                }
              >
                Kích hoạt
              </ButtonIcon>
            </AuthGuard>
          ),
          onClick: handleActivate,
        },
      ]}
    >
      <img src={icon_path + 'more-v2.svg'} alt="more" />
    </ButtonDropDown>
    // <UncontrolledDropdown className="actions-dropdown">
    //   <DropdownToggle className="actions-dropdown-toggle">
    //     <img src="content/images/vuesax/linear/more.svg" alt="more" />
    //   </DropdownToggle>
    //   <DropdownMenu>
    //       <DropdownItem onClick={handleActivate}>
    //       <AuthGuard permissionKey='CUSTOMERS_DISPOSED.EDIT'>
    //         <ButtonIcon
    //           className="activate"
    //           icon={<img className="pointer" src="content/images/vuesax/linear/profile-tick.svg" alt="activate" />}
    //         >
    //           Kích hoạt
    //         </ButtonIcon>
    //         </AuthGuard>
    //       </DropdownItem>
    //   </DropdownMenu>
    // </UncontrolledDropdown>
  );
};

export default ActionsDropdown;
