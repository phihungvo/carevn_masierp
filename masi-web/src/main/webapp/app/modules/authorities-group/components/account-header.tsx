import Button from 'app/components/button/button';
import AuthGuard from 'app/components/guards/auth-guard';
import InputSearch from 'app/components/input/input-search';
import useGroup from 'app/hooks/use-group';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import React from 'react';
import { useParams } from 'react-router';

const { usePostGroupUsers } = useGroup;

interface IAccountHeaderProps {
  setSearchText: React.Dispatch<React.SetStateAction<string>>;
  selectedAccounts: string[];
  toggleSuccess?: () => void;
}

const AccountHeader = (props: IAccountHeaderProps) => {
  const { setSearchText, selectedAccounts, toggleSuccess } = props;

  const { id } = useParams();

  const { mutate } = usePostGroupUsers();

  const handleSaveUserToGroup = () => {
    mutate(
      {
        groupId: id,
        userIds: selectedAccounts,
      },
      {
        onSuccess: () => {
          toggleSuccess();
        },
      },
    );
  };

  return (
    <div className="card-header-container">
      <InputSearch
        className="card-header-extra"
        onChange={e => {
          setSearchText(e.target.value);
        }}
      />

      <div className="card-header-extra">
          <Button color="primary" onClick={handleSaveUserToGroup}>
            Lưu
          </Button>
      </div>
    </div>
  );
};

export default AccountHeader;
