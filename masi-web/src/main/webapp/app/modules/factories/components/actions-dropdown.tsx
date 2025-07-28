import ButtonDropDown from 'app/components/ButtonV2/ButtonDropdown';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import AuthGuard from 'app/components/guards/auth-guard';
import Tooltip from 'app/components/tooltip/tooltip';
import { useAppSelector } from 'app/config/store';
import { isHasPermission } from 'app/constants/common';
import { PATH } from 'app/constants/path';
import { IFactoryLogistics } from 'app/shared/model/factory-logistics.model';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { useNavigate } from 'react-router';

const icon_path = 'content/images/vuesax/linear/';

interface IActionsDropdown {
  toggleDetail: () => void;
  toggleUpdate: () => void;
  toggleDelete: () => void;
  togglePropose: () => void;
  toggleApprove: () => void;
  toggleDispose: () => void;
  toggleActivate: () => void;
  record: IFactoryLogistics;
  setSelectedRecord: (id: string) => void;
}

const ActionsDropdown = (props: IActionsDropdown) => {
  const {
    toggleDetail,
    toggleUpdate,
    toggleDelete,
    toggleDispose,
    toggleActivate,
    record,
    setSelectedRecord,
  } = props;

  const navigate = useNavigate();

  const handleDetail = () => {
    setSelectedRecord(record.id);
    toggleDetail();
  };

  const handleUpdate = () => {
    navigate(PATH.FACTORIES_UPDATE.replace(':id', record.id));
  };

  const handleDelete = () => {
    setSelectedRecord(record.id);
    toggleDelete();
  };

  const handleDispose = () => {
    setSelectedRecord(record?.id);
    toggleDispose();
  };

  const handleActivate = () => {
    setSelectedRecord(record?.id);
    toggleActivate();
  };

  const authorities = useAppSelector(
    state => state.authentication.account.authorities,
  );

  const disabledDeactivate = !record?.isActive;
  const disabledActivate = record?.isActive;

  return (
    <ButtonDropDown
      items={[
        {
          children: (
              <Tooltip label={'Chi tiết'} target={`btn-detail`}>
                <ButtonV2
                  id="btn-detail"
                  variant="text"
                  left_section={
                    <img
                      src="content/images/vuesax/linear/eye.svg"
                      alt="detail"
                    />
                  }
                >
                  Chi tiết
                </ButtonV2>
              </Tooltip>
          ),
          onClick: () => handleUpdate(),
          hidden: !isHasPermission(authorities, 'LOGISTICS_FACTORIES.VIEW'),
        },
        {
          children: (
              <Tooltip label={'Cập nhật'} target={`btn-update`}>
                <ButtonV2
                  id="btn-update"
                  variant="text"
                  left_section={
                    <img
                      src="content/images/vuesax/linear/edit-active.svg"
                      alt="update"
                    />
                  }
                >
                  Cập nhật
                </ButtonV2>
              </Tooltip>
          ),
          onClick: () => handleUpdate(),
          hidden: !isHasPermission(authorities, 'LOGISTICS_FACTORIES.EDIT'),
        },
        {
          children: (
              <Tooltip label={'Vô hiệu'} target={`btn-deactivate`}>
                <ButtonV2
                  id="btn-deactivate"
                  variant="text"
                  left_section={
                    <img
                      src="content/images/vuesax/linear/slash.svg"
                      alt="deactivate"
                    />
                  }
                  disabled={disabledDeactivate}
                >
                  Vô hiệu
                </ButtonV2>
              </Tooltip>
          ),
          disable: disabledDeactivate,
          onClick: () => handleDispose(),
          hidden: !isHasPermission(authorities, 'LOGISTICS_FACTORIES.EDIT'),
        },
        {
          children: (
              <Tooltip label={'Kích hoạt'} target={`btn-active`}>
                <ButtonV2
                  className="activate"
                  id="btn-active"
                  variant="text"
                  left_section={
                    <img
                      src="content/images/vuesax/linear/profile-tick.svg"
                      alt="active"
                    />
                  }
                  disabled={disabledActivate}
                >
                  Kích hoạt
                </ButtonV2>
              </Tooltip>
          ),
          disable: disabledActivate,
          onClick: () => handleActivate(),
          hidden: !isHasPermission(authorities, 'LOGISTICS_FACTORIES.EDIT'),
        },
        {
          children: (
              <Tooltip label={'Xoá'} target={`btn-delete`}>
                <ButtonV2
                  id="btn-delete"
                  variant="text"
                  left_section={
                    <img
                      src="content/images/vuesax/linear/trash.svg"
                      alt="delete"
                    />
                  }
                >
                  Xoá
                </ButtonV2>
              </Tooltip>
          ),
          onClick: () => handleDelete(),
          hidden: !isHasPermission(authorities, 'LOGISTICS_FACTORIES.EDIT'),
        },
      ]}
    >
      <img src={icon_path + 'more-v2.svg'} alt="more" />
    </ButtonDropDown>
  );
};

export default ActionsDropdown;
