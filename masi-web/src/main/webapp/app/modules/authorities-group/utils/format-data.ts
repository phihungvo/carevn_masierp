import { IAuthority } from 'app/shared/model/authority.model';
import { actions_count } from 'app/shared/model/enumerations/authorities-group';
import { permission_group, permission_label } from '../authorities-mapping';

export type PermissionGroup = {
  group_title: string,
  total_checked_resources: number,
  resources: {
    resource_title: string,
    total_checked_actions: number,
    actions: {
      [action: string]: {
        id: string,
        action_title: string,
        isChecked: boolean
        resource: string
      }
    },
  }[],
  [action: string]: {
    size: number,
    checked: number
  } | any
}

export type AuthoritiesListConverted = {
  total_checked_groups: number,
  groups: PermissionGroup[]
}

export const formatAuthoritiesList = (data?: IAuthority[]) => {
  if (!data) return { groups: [], total_checked_groups: 0 }
  const converted_data: AuthoritiesListConverted = {
    total_checked_groups: 0,
    groups: []
  }
  const tmp: {
    [group_permission: string]: {
      index: number,
      resources: {
        [resource: string]: {
          index: number,
          actions: {
            [action: string]: IAuthority
          },
        }
      },
      [action: string]: {
        [resource: string]: boolean
      } | any
    }
  } = {}
  data.forEach((item) => {
    let { resource, action, description } = item

    if (!permission_label[resource]) return

    let group_title = permission_group[resource]

    if (!group_title) return;

    if (!tmp[group_title]) {
      converted_data.groups.push({
        group_title,
        total_checked_resources: 0,
        resources: []
      })

      tmp[group_title] = { index: converted_data.groups.length - 1, resources: {} }
    }

    let group_index = tmp[group_title].index

    if (!tmp[group_title].resources[resource]) {
      converted_data.groups[group_index].resources.push({
        resource_title: permission_label[resource],
        total_checked_actions: 0,
        actions: {}
      })

      tmp[group_title].resources[resource] = { index: converted_data.groups[group_index].resources.length - 1, actions: {} }
    }

    let resource_index = tmp[group_title].resources[resource]?.index

    if (!tmp[group_title].resources[resource].actions[action]) {
      tmp[group_title].resources[resource].actions[action] = item

      converted_data.groups[group_index].resources[resource_index].actions[action] = {
        id: item.id,
        action_title: description,
        isChecked: false,
        resource: item.resource
      }
    }

    if (!tmp[group_title][action]) {
      tmp[group_title][action] = {}
      converted_data.groups[group_index][action] = {
        size: 0,
        checked: 0
      }
    }

    if (!tmp[group_title][action][resource]) {
      tmp[group_title][action][resource] = true
      converted_data.groups[group_index][action].size++
      converted_data.groups[group_index][action][resource] = false
    }
  });

  return converted_data;
};

export const formatAuthoritiesListDetail = (data: IAuthority[] = []) => {
  const map = new Map<string, { count: number, data: Record<string, IAuthority> }>()
  const data_row = []
  data.forEach((item) => {
    const { resource, action } = item
    const isExist = map.has(resource)
    if (isExist) {
      const data = map.get(resource)
      data.count++
      data.data[action] = item
      map.set(resource, data)
    } else {
      data_row.push({ key: resource })
      map.set(resource, {
        count: 1,
        data: { [action]: item }
      })
    }
  });
  return map;
};
