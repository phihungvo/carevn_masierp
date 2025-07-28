import Button from 'app/components/button/button';
import useGroup from 'app/hooks/use-group';
import { IGroup } from 'app/shared/model/group.model';
import React from 'react';
import { useParams } from 'react-router';

const { usePatchGroup } = useGroup;

interface IPermissionHeaderProps {
  data: IGroup;
  selectedAuthorities: string[];
  toggleSuccess: () => void;
}

const PermissionHeader = (props: IPermissionHeaderProps) => {
  const { data, selectedAuthorities, toggleSuccess } = props;

  const { id } = useParams();

  const { mutate } = usePatchGroup(id);

  const handleGroupAuthorities = () => {
    mutate(
      {
        name: data?.name,
        description: data?.description,
        authorities: selectedAuthorities,
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
      <div className="card-header-extra" />

      <div className="card-header-extra">
        <Button color="primary" onClick={handleGroupAuthorities}>
          Lưu
        </Button>
      </div>
    </div>
  );
};

export default PermissionHeader;
