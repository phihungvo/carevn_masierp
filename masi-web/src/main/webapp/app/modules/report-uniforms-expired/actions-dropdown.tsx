import ButtonIcon from 'app/components/button-icon/button-icon';
import AuthGuard from 'app/components/guards/auth-guard';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { IUniformExpiring } from 'app/shared/model/report.model';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';

interface IActionsDropdownProps {
    handleShowModalHistory?: (id: string) => void;
    record?: IUniformExpiring;
}

const ActionsDropdown = (props: IActionsDropdownProps) => {
    const { handleShowModalHistory, record } = props;

    const onDetailClick = () => {
        if (record) {
            handleShowModalHistory(record.employeeId);
        }
    };

    return (
        <UncontrolledDropdown>
            <DropdownToggle className="actions-dropdown-toggle">
                <img src="content/images/vuesax/linear/more.svg" alt="more" />
            </DropdownToggle>
            <DropdownMenu container="body" className="actions-dropdown">
                    <AuthGuard permissionKey='REPORT_UNIFORMS_EXPIRED.VIEW'>
                        <DropdownItem onClick={onDetailClick}>
                            <ButtonIcon icon={<img className="pointer" src="content/images/vuesax/linear/eye.svg" />}>
                                Lịch sử
                            </ButtonIcon>
                        </DropdownItem>
                    </AuthGuard>
            </DropdownMenu>
        </UncontrolledDropdown>
    );
};

export default ActionsDropdown;