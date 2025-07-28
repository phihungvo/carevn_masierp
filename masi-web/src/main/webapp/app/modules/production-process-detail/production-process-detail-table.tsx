import ButtonIcon from 'app/components/button-icon/button-icon';
import Flex from 'app/components/flex/flex';
import Table from 'app/components/table/table';
import { ColumnsTypes } from 'app/components/table/table.d';
import { DATE_FORMAT } from 'app/constants/common';
import { IProductionProcessDetailParams, IProductionProcessItem } from 'app/shared/model/production-process.model';
import dayjs from 'dayjs';
import React from 'react';
import { useNavigate, useParams } from 'react-router';
import { ITemplateList, Template } from './production-process-detail-components';
import { PRODUCTION_PROCESS_STATUS } from 'app/shared/model/enumerations/production-process.model';
import { handleUpdate, handleViewDetail } from './utils';
import { templateNameMap } from './production-process-detail-mapping';
import AuthGuard from 'app/components/guards/auth-guard';
import { Action, PermissionResource } from 'app/shared/model/permission.model';

interface IProductionProcessDetailTable {
  status: PRODUCTION_PROCESS_STATUS;
  data: IProductionProcessItem;
  setSelectedRecord: (selectedRecord: { template: Template; id: string }) => void;
  toggle: () => void;
  filter: IProductionProcessDetailParams;
  setFilter: React.Dispatch<React.SetStateAction<IProductionProcessDetailParams>>;
}

export const ProductionProcessDetailTable = (props: IProductionProcessDetailTable) => {
  const { data, setSelectedRecord, toggle, filter, setFilter, status } = props;

  const { id, workItemId } = useParams();
  const navigate = useNavigate();

  const handleDelete = (templateId: string, template: Template) => {
    toggle();
    setSelectedRecord({ template, id: templateId });
  };

  const columns: ColumnsTypes<ITemplateList> = [
    {
      key: 'name',
      title: 'Tên biểu mẫu',
      dataIndex: 'name',
      render: (text, record) => (
        <p className="attachment-link" onClick={() => handleViewDetail(record.id, record.template, id, workItemId, navigate)}>
          {text}
        </p>
      ),
    },
    {
      key: 'date',
      title: 'Ngày tạo',
      dataIndex: 'date',
      render: text => (text ? dayjs(text).format(DATE_FORMAT.DATE) : ''),
    },

    {
      key: 'actions',
      title: '',
      render: (_, record) => (
        <Flex gap={16}>
            <ButtonIcon
              onClick={() => handleViewDetail(record.id, record.template, id, workItemId, navigate)}
              icon={<img className="pointer" src="content/images/vuesax/linear/eye.svg" alt="detail" />}
            />
          {status === PRODUCTION_PROCESS_STATUS.RUNNING && (
              <ButtonIcon
                onClick={() => handleUpdate(record.id, record.template, id, workItemId, navigate)}
                icon={<img className="pointer" src="content/images/vuesax/linear/edit-primary.svg" alt="edit" />}
              />
          )}
          {status === PRODUCTION_PROCESS_STATUS.RUNNING && (
              <ButtonIcon
                onClick={() => handleDelete(record.id, record.template)}
                icon={<img className="pointer" src="content/images/vuesax/linear/trash.svg" alt="delete" />}
              />
          )}
        </Flex>
      ),
    },
  ];

  let dataSource = [];

  dataSource = data?.checkLists?.map(checkList => ({
    name: templateNameMap(checkList.type),
    date: checkList.createdAt,
    id: checkList.id,
    template: checkList.type,
  }));

  const totalCount = data?.checkListsCount || 0;
  const { page, size } = filter;

  return (
    <Table<ITemplateList>
      columns={columns}
      dataSource={dataSource}
      pagination={{
        page,
        size,
        totalCount,
        onPageChange: (page, size) => setFilter(prev => ({ ...prev, page, size })),
      }}
    />
  );
};
