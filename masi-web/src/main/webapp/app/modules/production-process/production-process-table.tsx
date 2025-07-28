import dayjs from 'dayjs';
import React, { useEffect } from 'react';
import { Link, useNavigate, useSearchParams } from 'react-router-dom';

import Input from 'app/components/input/input';
import Badge from 'app/components/badge/badge';
import ActionsDropdown from './actions-dropdown';
import TableUIW from 'app/components/table/table-uiw';
import productionProcessMapping from './production-process-mapping';
import useProductionCommand from 'app/hooks/use-production-command';
import { PATH } from 'app/constants/path';
import { useDebounce } from 'app/hooks/use-debounce';
import { DATE_FORMAT, DEFAULT_PAGE } from 'app/constants/common';
import { IProductionProcess } from 'app/shared/model/production-process.model';
import { IProductionCommandWithProcessParams } from 'app/shared/model/production-command.model';
import { PRODUCTION_COMMAND_TYPE } from 'app/shared/model/enumerations/production-command.model';
import Tooltip from 'app/components/tooltip/tooltip';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';

const { useGetProductionCommandsWithProcessQuery } = useProductionCommand;
const { mapProductionProcessStatusColor, mapProductionProcessStatusText, mapProductionProcessType, mapProductionProcessNamePath } =
  productionProcessMapping;

interface IProductionProcessTable {
  toggleModalUpdate: () => void;
  toggleModalDelete: () => void;
  toggleModalStart: () => void;
  toggleModalStop: () => void;
  toggleModalComplete: () => void;
  toggleNoticesUpdate: () => void;
  setSelectedRecord: (id: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (selectedRowKeys: string[]) => void;
  selectedRows: IProductionProcess[];
  setSelectedRows: (selectedRows: IProductionProcess[]) => void;
  filter: IProductionCommandWithProcessParams;
  setFilter: React.Dispatch<React.SetStateAction<IProductionCommandWithProcessParams>>;
  searchText: string;
  manufactureOrderType: PRODUCTION_COMMAND_TYPE;
}

export const ProductionProcessTable = (props: IProductionProcessTable) => {
  const {
    toggleModalUpdate,
    toggleModalDelete,
    toggleModalStart,
    toggleModalStop,
    toggleModalComplete,
    toggleNoticesUpdate,
    setSelectedRecord,
    selectedRowKeys,
    setSelectedRowKeys,
    selectedRows,
    setSelectedRows,
    filter,
    setFilter,
    searchText,
    manufactureOrderType,
  } = props;

  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const type = searchParams.get('type');

  const searchString = useDebounce(searchText, 500);

  const isCheck = !type || type === PRODUCTION_COMMAND_TYPE?.MANUFACTURE_ORDER_BY_STANDARD ? PRODUCTION_COMMAND_TYPE?.MANUFACTURE_ORDER_BY_STANDARD : PRODUCTION_COMMAND_TYPE?.MANUFACTURE_ORDER_BY_ORDER

  useEffect(() => {
    setFilter(prev => ({ ...prev, searchString, page: DEFAULT_PAGE }));
  }, [searchString]);

  const { data } = useGetProductionCommandsWithProcessQuery({ ...filter, manufactureOrderType });

  const handleViewDetail = (id: string, workItemId: string, checklistType: string) =>
    navigate(
      PATH.PRODUCTION_PROCESS_DETAIL.replace(':id', id).replace(':workItemId', workItemId) +
      `/${mapProductionProcessNamePath(checklistType)}/detail`,
    );

  const dataSource: any = data?.data?.reduce((acc, item) => {
    acc.push({
      id: item.id,
      title: item?.name,
      date: item?.fromDate,
      status: item?.status,
      isChild: false,
      children: item?.workOrders?.map((wo, index) => ({
        id: wo?.id,
        title: mapProductionProcessType(wo?.checklistType),
        date: wo?.fromDate,
        status: wo?.status,
        workItemId: wo?.workItemId,
        isChild: true,
        checklistType: wo?.checklistType,
        checklistOrder: wo?.checklistOrder,
        parent: item?.workOrders,
        checklist: wo?.checklist,
        lastUpdated: wo?.lastUpdated,
      })),
    });

    return acc;
  }, []);

  const listChildrenKeys = dataSource
    ?.reduce((acc, item) => {
      item?.children && acc.push(item?.children.map(child => child?.id));

      return acc;
    }, [])
    .flat();
  const listChildren = dataSource
    ?.reduce((acc, item) => {
      item?.children && acc.push(item?.children);

      return acc;
    }, [])
    .flat();

  const handleSelectRow = (e: React.ChangeEvent<HTMLInputElement>, data: any) => {
    if (e.target.checked) {
      setSelectedRowKeys([...selectedRowKeys, e.target.value]);
      setSelectedRows([...selectedRows, data]);
    } else {
      setSelectedRowKeys(selectedRowKeys.filter(key => key !== e.target.value));
      setSelectedRows(selectedRows.filter(row => row.id !== e.target.value));
    }
  };

  const handleSelectAll = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.checked) {
      setSelectedRowKeys(listChildrenKeys);
      setSelectedRows(listChildren);
    } else {
      setSelectedRowKeys([]);
      setSelectedRows([]);
    }
  };

  const columns = [
    {
      key: 'checkbox',
      title: (
        // <Input
        //   type="checkbox"
        //   onChange={handleSelectAll}
        //   checked={selectedRowKeys?.length !== 0 && listChildrenKeys?.length === selectedRowKeys?.length}
        // />
        <div />
      ),
      width: 50,
      render: (text, key, rowData) => (
        <>
          {rowData?.isChild ? (
            // <Input
            //   type="checkbox"
            //   checked={selectedRowKeys.includes(rowData?.id)}
            //   value={rowData?.id}
            //   onChange={e => handleSelectRow(e, rowData)}
            // />
            <div />
          ) : (
            <>{''}</>
          )}
        </>
      ),
    },
    {
      key: 'stt',
      title: 'STT',
      render(text, key, rowData, rowNumber, columnNumber) {
        return (
          <span {...(rowData?.isChild && { style: { marginLeft: 32 } })}>{`${rowNumber + 1 + filter.page * filter.size >= 10 ? '' : '0'}${rowNumber + 1 + filter.page * filter.size
            }`}</span>
        );
      },
    },
    {
      key: 'title',
      title: 'Lệnh sản xuất',
      render(text, key, rowData) {
        return (
          <>
            {rowData?.isChild ? (

              <Tooltip label={text} target={`title-child-${rowData.id}`}>
                <span
                  className="attachment-link cursor-pointer"
                  onClick={() => handleViewDetail(rowData?.id, rowData?.workItemId, rowData?.checklistType)}
                >
                  <EllipsisParagraph text={
                    text
                  } width={260} id={`title-child-${rowData.id}`} />
                </span>
              </Tooltip>
            ) : (
              <Tooltip label={rowData?.title} target={`title-parent-${rowData.id}`}>
                <Link to={`${PATH.PRODUCTION_COMMAND}?type=${isCheck}`} target="_blank" className="attachment-link">
                  <EllipsisParagraph text={
                    text
                  } id={`title-parent-${rowData.id}`} />
                </Link>
              </Tooltip>
            )}
          </>
        );
      },
    },
    {
      key: 'date',
      title: 'Ngày sản xuất',
      render: (text, key, rowData) => (text && rowData?.isChild ? <>{dayjs(text).format(DATE_FORMAT.DATE)}</> : <>{''}</>),
    },
    {
      key: 'status',
      title: 'Trạng thái',
      render: (text, key, rowData) =>
        rowData?.isChild ? <Badge color={mapProductionProcessStatusColor(text)}>{mapProductionProcessStatusText(text)}</Badge> : <>{''}</>,
    },
    {
      key: 'actions',
      title: '',
      render: (_, key, rowData, rowNumber) =>
        rowData?.isChild ? (
          <ActionsDropdown
            rowNumber={rowNumber}
            record={rowData}
            id={rowData?.id}
            status={rowData?.status}
            workItemId={rowData?.workItemId}
            toggleUpdateReq={toggleModalUpdate}
            toggleModalStart={toggleModalStart}
            toggleModalStop={toggleModalStop}
            toggleModalComplete={toggleModalComplete}
            toggleModalDelete={toggleModalDelete}
            toggleNoticesUpdate={toggleNoticesUpdate}
            setSelectedRecord={setSelectedRecord}
          />
        ) : (
          <>{''}</>
        ),
    },
  ];

  const totalCount = data?.totalRecord || 0;
  const { page, size } = filter;

  return (
    <TableUIW
      rowKey="id"
      columns={columns}
      dataSource={dataSource}
      pagination={{
        page,
        size,
        totalCount,

        onPageChange: (page, size) => setFilter(prev => ({ ...prev, page, size })),
      }}
      scroll={{
        y: 'calc(100vh - 324px)',
      }}
    />
  );
};
