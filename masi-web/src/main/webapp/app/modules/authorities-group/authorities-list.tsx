import React, { useEffect, useMemo } from 'react';

import Card from 'app/components/card/card';
import Input from 'app/components/input/input';
import InputSearch from 'app/components/input/input-search';
import { useDebounce } from 'app/hooks/use-debounce';
import { IAuthorityParams } from 'app/shared/model/authority.model';

import TableV2, { TableColumns } from 'app/components/table-v2/Table';
import { action_arr } from 'app/shared/model/enumerations/authorities-group';
import './auth-list.scss';
import { action_label } from './authorities-mapping';
import { AuthoritiesListConverted } from './utils/format-data';

interface IAuthoritiesListProps {
  permissionList: AuthoritiesListConverted
  setPermissionList: React.Dispatch<React.SetStateAction<AuthoritiesListConverted>>
  searchText: string;
  setFilter: React.Dispatch<React.SetStateAction<IAuthorityParams>>;
  setSearchText: React.Dispatch<React.SetStateAction<string>>;
  permissionId: React.MutableRefObject<Set<string>>
}

const AuthoritiesList = (props: IAuthoritiesListProps) => {
  const {
    searchText,
    setFilter,
    setSearchText,
    permissionList,
    setPermissionList,
    permissionId
  } = props;

  const search = useDebounce(searchText, 500);
  useEffect(() => {
    setFilter(prev => ({ ...prev, search }));
  }, [search]);

  const onCheckedAllGroup = (e: React.ChangeEvent<HTMLInputElement>) => {
    let isChecked = e.target.checked;
    setPermissionList(pre => {
      let tmp: AuthoritiesListConverted = JSON.parse(JSON.stringify(pre));
      tmp.groups.forEach(group => {
        group.resources.forEach(resource => {
          for (let action_key in resource.actions) {
            let action = resource.actions[action_key];
            isChecked ? permissionId.current.add(action.id) : permissionId.current.delete(action.id);
            if (action.isChecked !== isChecked) {
              group[action_key].checked += isChecked ? 1 : -1;
            }
            const action_length = Object.keys(resource.actions).length;
            if (resource.total_checked_actions === action_length && !isChecked) {
              group.total_checked_resources -= 1;
            }
            if (action.isChecked !== isChecked) {
              resource.total_checked_actions += isChecked ? 1 : -1;
            }
            if (resource.total_checked_actions === action_length) {
              group.total_checked_resources += 1;
              tmp.total_checked_groups += 1;
            }
            action.isChecked = isChecked;
          }
        });
      });
      tmp.total_checked_groups = isChecked ? tmp.groups.length : 0;
      return tmp;
    });
  }

  const onCheckedOneGroup = (index: number) => (e: React.ChangeEvent<HTMLInputElement>) => {
    let isChecked = e.target.checked;
    setPermissionList(pre => {
      let tmp: AuthoritiesListConverted = JSON.parse(JSON.stringify(pre));
      tmp.groups[index].resources.forEach(resource => {
        for (let action_key in resource.actions) {
          let current_action = tmp.groups[index][action_key];
          let action = resource.actions[action_key];
          isChecked ? permissionId.current.add(action.id) : permissionId.current.delete(action.id);
          if (action.isChecked !== isChecked) {
            current_action.checked += isChecked ? 1 : -1;
          }
          const action_length = Object.keys(resource.actions).length;
          if (resource.total_checked_actions === action_length && !isChecked) {
            tmp.groups[index].total_checked_resources -= 1;
          }
          if (action.isChecked !== isChecked) {
            resource.total_checked_actions += isChecked ? 1 : -1;
          }
          if (resource.total_checked_actions === action_length) {
            tmp.groups[index].total_checked_resources += 1;
          }
          action.isChecked = isChecked;
        }
      });
      tmp.total_checked_groups += isChecked ? 1 : -1;
      return tmp
    });
  }

  const onCheckedOneSubRow = (payload: { index: number, parent_index: number }) => (e: React.ChangeEvent<HTMLInputElement>) => {
    let isChecked = e.target.checked;
    let { index, parent_index } = payload;
    setPermissionList(pre => {
      let tmp: AuthoritiesListConverted = JSON.parse(JSON.stringify(pre));
      let current_group = tmp.groups[parent_index];
      let current_resource = current_group.resources[index];
      for (let action_key in current_resource.actions) {
        let action = current_resource.actions[action_key];
        isChecked ? permissionId.current.add(action.id) : permissionId.current.delete(action.id);
        if (action.isChecked !== isChecked) {
          current_group[action_key].checked += isChecked ? 1 : -1;
        }

        const action_length = Object.keys(current_resource.actions).length;
        if (current_resource.total_checked_actions === action_length && !isChecked) {
          if (current_group.total_checked_resources === current_group.resources.length) {
            tmp.total_checked_groups -= 1
          }
          current_group.total_checked_resources -= 1;
        }
        if (action.isChecked !== isChecked) {
          current_resource.total_checked_actions += isChecked ? 1 : -1;
        }
        if (current_resource.total_checked_actions === action_length) {
          current_group.total_checked_resources += 1;
        }
        if (current_group.total_checked_resources === current_group.resources.length) {
          tmp.total_checked_groups += 1;
        }
        action.isChecked = isChecked;
      }
      return tmp;
    });
  }

  const onCheckedOneColumnAction =
    (payload: { action: string; index: number }) =>
    (e: React.ChangeEvent<HTMLInputElement>) => {
      let { action, index } = payload;
      let isChecked = e.target.checked;
      setPermissionList(pre => {
        let tmp: AuthoritiesListConverted = JSON.parse(JSON.stringify(pre));
        let current_group = tmp.groups[index];
        let current_action = current_group[action];
        current_action.checked = isChecked ? current_action.size : 0
        current_group.resources.forEach(resource => {
          if (!resource.actions[action]) return;

          let action_length = Object.keys(resource.actions).length;

          if (resource.total_checked_actions === action_length && !isChecked) {
            if (current_group.total_checked_resources === current_group.resources.length) {
              tmp.total_checked_groups -= 1;
            }
            current_group.total_checked_resources -= 1;
          }
          if (resource?.actions?.[action]?.isChecked !== isChecked) {
            resource.total_checked_actions += isChecked ? 1 : -1;
          }
          if (resource.total_checked_actions === action_length) {
            current_group.total_checked_resources += 1;
          }
          if (current_group.total_checked_resources === current_group.resources.length) {
            tmp.total_checked_groups += 1;
          }
          resource.actions[action].isChecked = isChecked;

          isChecked ? permissionId.current.add(resource.actions[action].id) : permissionId.current.delete(resource.actions[action].id);
        });
        return tmp;
      });
    };

  const onCheckedAction =
    (position: { group_index: number; resource_index: number; action: string; resource: string }) =>
    (e: React.ChangeEvent<HTMLInputElement>) => {
      let { resource_index, group_index, action, resource } = position;
      let isChecked = e.target.checked;
      setPermissionList(pre => {
        let tmp: AuthoritiesListConverted = JSON.parse(JSON.stringify(pre));
        let current_group = tmp.groups[group_index];
        let current_action = current_group[action];
        current_action.checked += isChecked ? 1 : -1;
        // row
        let current_resource = current_group.resources[resource_index];
        let action_length = Object.keys(current_resource.actions).length;
        if (current_resource.total_checked_actions === action_length && !isChecked) {
          if (current_group.total_checked_resources === current_group.resources.length) {
            tmp.total_checked_groups -= 1;
          }
          current_group.total_checked_resources -= 1;
        }
        if (current_resource.actions[action].isChecked !== isChecked) {
          current_resource.total_checked_actions += isChecked ? 1 : -1;
        }
        if (current_resource.total_checked_actions === action_length) {
          current_group.total_checked_resources += 1;
        }
        if (current_group.total_checked_resources === current_group.resources.length) {
          tmp.total_checked_groups += 1
        }
        current_resource.actions[action].isChecked = isChecked;
        return tmp;
      });
      let id = permissionList.groups[group_index].resources[resource_index].actions[action].id;
      e.target.checked ? permissionId.current.add(id) : permissionId.current.delete(id)
    };

    console.log('permissionList', permissionList);

  const columns = useMemo((): TableColumns<AuthoritiesListConverted['groups'][number]> => {
    return [
      {
        header: {
          render: (
            <Input
              type='checkbox'
              checked={permissionList?.total_checked_groups === permissionList?.groups?.length}
              onChange={onCheckedAllGroup}
              disabled={!permissionList?.groups}
            />
          )
        },
        body: {
          render: ({ index }) => (
            <Input
              type='checkbox'
              checked={permissionList?.groups?.[index]?.resources?.every(resource => resource?.total_checked_actions === Object.keys(resource.actions || {}).length)}
              onChange={onCheckedOneGroup(index)}
            />
          ),
          sub_render: ({ icon_path, data, index, parent_index }) => (
            <div className=''>
              <img src={icon_path} alt="table_icon" style={{ width: '18px', height: '18px', marginRight: '10px' }} />
              <Input
                type='checkbox'
                checked={permissionList?.groups?.[parent_index]?.resources?.[index]?.total_checked_actions === Object.keys(data?.actions || {}).length}
                onChange={onCheckedOneSubRow({ index, parent_index })}
              />
            </div>
          ),
          sub_key: 'resources'
        }
      },
      {
        header: {
          render: 'Nhóm quyền'
        },
        body: {
          render: ({ data }) => <>{data?.group_title}</>,
          sub_render: ({ data }) => <>{data?.resource_title}</>,
          sub_key: 'resources'
        }
      },
      ...action_arr?.map<TableColumns<AuthoritiesListConverted['groups'][number]>[number]>((action, action_index) => ({
        header: {
          render: action_label[action]
        },
        body: {
          td_class: 'text-center',
          render: ({ index }) => {
            return (
              <Input
                type='checkbox'
                hidden={!!!permissionList?.groups?.[index]?.[action]?.size}
                checked={permissionList?.groups?.[index]?.[action]?.checked === permissionList?.groups?.[index]?.[action]?.size}
                onChange={onCheckedOneColumnAction({ action, index })}
              />
            )
          },
          sub_render: ({ parent_index, index, data }) => {
            let resource = data?.actions?.[0]?.resource
            return (
              <Input
                type='checkbox'
                hidden={!!!permissionList?.groups?.[parent_index]?.resources?.[index]?.actions?.[action]}
                checked={permissionList?.groups?.[parent_index]?.resources?.[index]?.actions?.[action]?.isChecked}
                onChange={onCheckedAction({ group_index: parent_index, resource_index: index, action, resource })}
              />
            )
          },
          sub_key: 'resources'
        }
      }))
    ]
  }, [permissionList]);

  return (
    <>
      <Card
        header={
          <div className="card-header-container">
            <div className="card-header-extra">
              <InputSearch className="card-header-extra" onChange={e => setSearchText(e.target.value)} />
            </div>
            <div className="card-header-extra" />
          </div>
        }
      >
        <TableV2<AuthoritiesListConverted['groups'][number]>
          table_id='auth-list'
          columns={columns}
          data={permissionList?.groups}
          isSubRow
          sub_key='resources'
          className={{
            table: 'auth-table__group',
          }}
        />
      </Card>
    </>
  );
};

export default AuthoritiesList;
