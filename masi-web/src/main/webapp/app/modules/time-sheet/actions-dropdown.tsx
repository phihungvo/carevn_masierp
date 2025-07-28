import ButtonIcon from 'app/components/button-icon/button-icon';
import AuthGuard from 'app/components/guards/auth-guard';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { ITimeSheet } from 'app/shared/model/time-sheet.model';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';

interface IActionsDropdownProps {
    handleShowModalHistory?: (id: string) => void;
    record?: ITimeSheet;
}

const ActionsDropdown = (props: IActionsDropdownProps) => {
    const { handleShowModalHistory, record } = props;

    const onDetailClick = () => {
        if (record) {
            handleShowModalHistory(record.id);
        }
    };

    return (
      <UncontrolledDropdown>
        <DropdownToggle className="actions-dropdown-toggle">
          <img src="content/images/vuesax/linear/more.svg" alt="more" />
        </DropdownToggle>
        <DropdownMenu container="body" className="actions-dropdown">
          <AuthGuard permissionKey="TIME_SHEET.EDIT">
            <DropdownItem onClick={onDetailClick}>
              <ButtonIcon
                icon={
                  <img
                    className="pointer"
                    src="content/images/vuesax/linear/edit.svg"
                  />
                }
              >
                Cập nhật
              </ButtonIcon>
            </DropdownItem>
          </AuthGuard>
        </DropdownMenu>
      </UncontrolledDropdown>
    );
};

export default ActionsDropdown;