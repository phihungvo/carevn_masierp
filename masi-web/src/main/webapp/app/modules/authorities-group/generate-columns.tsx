import { ColumnsTypes } from 'app/components/table/table.d';
import { useMemo } from 'react';
import React from 'react';
import ActionsDropdown from './components/actions-dropdown';
import { IGroup } from 'app/shared/model/group.model';
import Tooltip from 'app/components/tooltip/tooltip';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import { useAppSelector } from 'app/config/store';
import { isHasPermission } from 'app/constants/common';

export const generateColumns = (
  toggleUpdate: () => void,
  toggleDelete: () => void,
  toggleDetail: () => void,
  setSelectedRecord: (id: string) => void,
): ColumnsTypes<IGroup> => {
  // const navigate = useNavigate();
  const authorities = useAppSelector(
    state => state.authentication.account.authorities,
  );
  const handleDetail = (id: string) => {
    // navigate(PATH.AUTHORITIES_GROUPS_DETAIL.replace(':id', id));
    setSelectedRecord(id);
    toggleDetail();
  };

  const columns: ColumnsTypes<IGroup> = useMemo(() => {
    return [
      {
        title: 'Tên nhóm quyền',
        key: 'name',
        dataIndex: 'name',
        render: (text, record) => (
          <Tooltip label={text} target={`name-${record.id}`}>
            <p className="attachment-link" onClick={() => {
              if (!isHasPermission(authorities, 'PERMISSIONS_GROUPS.CREATE')) return
              handleDetail(record?.id)
            }}>
              <EllipsisParagraph text={text} width={300} id={`name-${record.id}`} />
            </p>
          </Tooltip>
        )
      },
      {
        title: 'Mô tả',
        key: 'description',
        dataIndex: 'description',
        render: (text, record) => (
          <Tooltip label={text} target={`description-${record.id}`}>
            <EllipsisParagraph text={text} width={400} id={`description-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Thao tác',
        key: 'action',
        dataIndex: 'action',
        width: 106,
        render: (_, record) => (
          <ActionsDropdown
            toggleUpdate={toggleUpdate}
            toggleDelete={toggleDelete}
            toggleDetail={toggleDetail}
            record={record}
            setSelectedRecord={setSelectedRecord}
          />
        ),
      },
    ];
  }, []);

  return columns;
};
