import { ColumnsTypes } from 'app/components/table/table.d';
import { IWorkspace } from 'app/shared/model/workspace.model';
import { useMemo } from 'react';
import ActionsDropdown from './components/actions-dropdown';
import React from 'react';
import { WORKSPACE_TYPE } from 'app/shared/model/enumerations/workspace.model';
import Tooltip from 'app/components/tooltip/tooltip';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';

export const generateColumns = (
  toggleUpdate: () => void,
  toggleDelete: () => void,
  setSelectedRecord: (id: string) => void,
): ColumnsTypes<IWorkspace> => {
  const columns: ColumnsTypes<IWorkspace> = useMemo(() => {
    return [
      {
        title: 'Bộ phận',
        dataIndex: 'name',
        key: 'name',
        render: (text, record) => (
          <Tooltip label={text} target={`name-${record.id}`}>
            <EllipsisParagraph text={text} id={`name-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Diễn giải',
        dataIndex: 'description',
        key: 'description',
        render: (text, record) => (
          <Tooltip label={text} target={`description-${record.id}`}>
            <EllipsisParagraph text={text} id={`description-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Loại',
        dataIndex: 'workspaceType',
        key: 'workspaceType',
        render: (_, record) => (record?.workspaceType === WORKSPACE_TYPE.OFFICE ? 'Văn phòng' : 'Nhà máy'),
      },
      {
        title: 'Thao tác',
        key: 'action',
        width: 106,
        render: (_, record) => (
          <ActionsDropdown toggleDelete={toggleDelete} toggleUpdate={toggleUpdate} record={record} setSelectedRecord={setSelectedRecord} />
        ),
      },
    ];
  }, []);

  return columns;
};
