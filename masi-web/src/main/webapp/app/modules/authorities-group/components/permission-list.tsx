import Card from 'app/components/card/card';
import AuthoritiesTable from 'app/modules/authorities/authorities-table';
import { IGroup } from 'app/shared/model/group.model';
import React from 'react';
import PermissionHeader from './permission-header';

interface IPermissionListProps {
  data: IGroup;
  selectedAuthorities: string[];
  setSelectedAuthorities: React.Dispatch<React.SetStateAction<string[]>>;
  toggleSuccess?: () => void;
}

const PermissionList = (props: IPermissionListProps) => {
  const { data, selectedAuthorities, setSelectedAuthorities, toggleSuccess } = props;

  return (
    <Card header={<PermissionHeader data={data} selectedAuthorities={selectedAuthorities} toggleSuccess={toggleSuccess} />}>
      <AuthoritiesTable
        rowSelection={{
          type: 'checkbox',
          onChange: (selectedRowKeys: string[], selectedRows) => {
            setSelectedAuthorities(selectedRowKeys);
          },
          selectedRowKeys: selectedAuthorities,
        }}
      />
    </Card>
  );
};

export default PermissionList;
