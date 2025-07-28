import React from 'react';

import { IAuthority } from 'app/shared/model/authority.model';

export const generateColumns = (
  data: Map<string, {
    count: number;
    data: Record<string, IAuthority>;
  }>,
  setDataRow: React.Dispatch<React.SetStateAction<any[]>>,
  selectedRoot: Map<string, number>,
  setSelectedRoot: React.Dispatch<React.SetStateAction<Map<string, number>>>,
  selectedItem: {
    [permission: string]: {
      [action: string]: IAuthority;
    };
  },
  setSelectedItem: React.Dispatch<React.SetStateAction<{
    [permission: string]: {
      [action: string]: IAuthority;
    };
  }>>
) => {
  const handleOnchange = (record: IAuthority, index: number, root_count: number) => (e: React.ChangeEvent<HTMLInputElement>) => {
    let isChecked = e.target.checked;
    let resource = record?.resource;
    setSelectedItem(pre => {
      if (isChecked) {
        pre[resource] = pre[resource] || {};
        pre[resource][record?.action] = record;
        root_count++;
        root_count === data.get(resource)?.count && delete pre[resource];
      } else {
        delete pre[resource][record?.action]
        root_count--;
        !root_count && delete pre[resource];
      }
      return JSON.parse(JSON.stringify(pre));
    });
    setSelectedRoot(pre => {
      pre.set(record?.resource, root_count)
      !root_count && pre.delete(record?.resource)
      return new Map(pre)
    })
    if (root_count === data.get(resource)?.count) {
      setDataRow(pre => {
        pre[index].isChecked = isChecked;
        return [...pre]
      })
    }
  };

  // return columns;
};

