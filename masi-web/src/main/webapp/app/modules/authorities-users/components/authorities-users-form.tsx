import { useForm } from 'react-hook-form';
import React, { useEffect, useState } from 'react'

import Form from 'app/components/form/form';
import Card from 'app/components/card/card';
import useAdminUser from 'app/hooks/use-admin-users'
import InputSearch from 'app/components/input/input-search';
import AuthoritiesGroupTable from 'app/modules/authorities-users/authorities-group-table'
import { IGroupParams } from 'app/shared/model/group.model';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { useDebounce } from 'app/hooks/use-debounce';

const { usePostAdminUserGroups, useGetAdminUserGroups } = useAdminUser;

interface IAuthoritiesUsersForm {
  selectedRecord: string;
  toggleSuccess: () => void;
  toggleUpdateUsers: () => void;
}

function AuthoritiesUsersForm({ selectedRecord, toggleSuccess, toggleUpdateUsers }: IAuthoritiesUsersForm) {

  const [searchText, setSearchText] = useState<string>('')

  const searchDebounce = useDebounce(searchText, 500);

  const [selectedRowKeys, setSelectedRowKeys] = useState<string[]>([]);

  const [filter, setFilter] = useState<IGroupParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
  });

  usePostAdminUserGroups

  const { mutate } = usePostAdminUserGroups();
  const { data } = useGetAdminUserGroups(selectedRecord)

  useEffect(() => {
    if (data) {
      setSelectedRowKeys(prev => [...prev, ...data?.map(item => item?.id)])
    }
  }, [data])

  const { handleSubmit } = useForm();
  useEffect(() => {
    setFilter(prev => ({ ...prev, search: searchDebounce }))
  }, [searchDebounce])

  const onSubmit = () => {
    mutate(
      {
        groupIds: selectedRowKeys,
        userId: selectedRecord,
      },
      {
        onSuccess: () => {
          toggleUpdateUsers();
          toggleSuccess();
        },
      },
    );
  }

  return (
    <Form id={FORM.UPDATE_ROLE} onSubmit={handleSubmit(onSubmit)}>

      <Card
        header={
          <div className="card-header-container">
            <div className="card-header-extra">
              <InputSearch
                className="card-header-extra"
                onChange={e => {
                  setSearchText(e.target.value);
                }}
              />
            </div>
            <div className="card-header-extra" />
          </div>
        }
      >
        <AuthoritiesGroupTable filter={filter} setFilter={setFilter} selectedRowKeys={selectedRowKeys} setSelectedRowKeys={setSelectedRowKeys} />
      </Card>
    </Form>

  )
}

export default AuthoritiesUsersForm