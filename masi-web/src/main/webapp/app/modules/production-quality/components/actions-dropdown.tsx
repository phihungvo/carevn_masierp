import { useNavigate } from 'react-router';

import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import Flex from 'app/components/flex/flex';
import AuthGuard from 'app/components/guards/auth-guard';
import { useAppSelector } from 'app/config/store';
import { ICON_PATH } from 'app/constants/common';
import { PATH } from 'app/constants/path';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { IQualityCheckSample } from 'app/shared/model/production-quality-control.model';

interface IActionsDropdownProps {
  handleDelete: (id: string) => void;
  record: IQualityCheckSample;
}

const ActionsDropdown = (props: IActionsDropdownProps) => {
  const { handleDelete, record } = props;

  const navigate = useNavigate();
  const account = useAppSelector(state => state.authentication.account);

  const handleUpdate = () => {
    navigate(PATH.PRODUCTION_QUALITY_UPDATE.replace(':id', record.id));
  };

  return (
    <Flex gap={16}>
        <AuthGuard permissionKey='PRODUCTION_QUALITY.EDIT'>
          <ButtonV2 variant="text" onClick={() => handleUpdate()}>
            <img src={ICON_PATH + 'edit-3.svg'} alt="edit" />
          </ButtonV2>
        </AuthGuard>
    </Flex>
  );
};

export default ActionsDropdown;
