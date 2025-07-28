import React, { Dispatch, SetStateAction } from 'react';
import Tabs from '@uiw/react-tabs';

import Card from 'app/components/card/card';
import { ProductionProcessTable } from './production-process-table';
import { ProductionProcessHeader } from './production-process-components';
import { IProductionProcess } from 'app/shared/model/production-process.model';
import { IProductionCommandWithProcessParams } from 'app/shared/model/production-command.model';
import { PRODUCTION_COMMAND_TYPE } from 'app/shared/model/enumerations/production-command.model';
import { useSearchParams } from 'react-router-dom';
import { DateObject } from 'react-multi-date-picker';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { PRODUCTION_PROCESS_STATUS } from 'app/shared/model/enumerations/production-process.model';

interface IProductionProcessBodyProps {
  toggleModalUpdate: () => void;
  toggleModalDelete: () => void;
  toggleModalStart: () => void;
  toggleModalStop: () => void;
  toggleModalCreate: () => void;
  toggleModalComplete: () => void;
  toggleModalFilter: () => void;
  toggleNoticesUpdate: () => void;
  setSelectedRecord: (id: string) => void;
  setSearchText: (searchText: string) => void;
  searchText: string;
  selectedRowKeys: string[];
  selectedRows: IProductionProcess[];
  filter: IProductionCommandWithProcessParams;
  setSelectedRowKeys: (selectedRowKeys: string[]) => void;
  setSelectedRows: (selectedRows: IProductionProcess[]) => void;
  setFilter: React.Dispatch<React.SetStateAction<IProductionCommandWithProcessParams>>;
  setSelectedDate?: Dispatch<SetStateAction<DateObject[]>>
  setStatus: Dispatch<SetStateAction<PRODUCTION_PROCESS_STATUS[]>>
}

function ProductionProcessBody({
  toggleModalUpdate,
  toggleModalDelete,
  toggleModalStart,
  toggleModalStop,
  toggleModalCreate,
  toggleModalComplete,
  toggleModalFilter,
  toggleNoticesUpdate,
  setSelectedRecord,
  setSearchText,
  searchText,
  selectedRowKeys,
  selectedRows,
  filter,
  setSelectedRowKeys,
  setSelectedRows,
  setFilter,
  setSelectedDate,
  setStatus
}: IProductionProcessBodyProps) {
  const [searchParams, setSearchParams] = useSearchParams();

  const handleTabClick = (key: string) => {
    setSelectedDate([])
    setSearchParams({ type: key })

    setStatus([])

    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      size: DEFAULT_PAGE_SIZE,
      fromDate: '',
      toDate: '',
      statuses: [],
      searchString: '',
    }))
  };

  const type = searchParams.get('type');

  return (
    <Tabs type="card" activeKey={!type || type === PRODUCTION_COMMAND_TYPE.MANUFACTURE_ORDER_BY_STANDARD ? PRODUCTION_COMMAND_TYPE.MANUFACTURE_ORDER_BY_STANDARD : PRODUCTION_COMMAND_TYPE.MANUFACTURE_ORDER_BY_ORDER} onTabClick={item => handleTabClick(item)}>
      <Tabs.Pane label="Định mức" key={PRODUCTION_COMMAND_TYPE.MANUFACTURE_ORDER_BY_STANDARD}>
        <Card
          header={
            <ProductionProcessHeader
              toggleModalCreate={toggleModalCreate}
              toggleModalDelete={toggleModalDelete}
              toggleModalFilter={toggleModalFilter}
              selectedRows={selectedRows}
              setSearchText={setSearchText}
            />
          }
        >
          <ProductionProcessTable
            toggleModalUpdate={toggleModalUpdate}
            toggleModalDelete={toggleModalDelete}
            toggleModalStart={toggleModalStart}
            toggleModalStop={toggleModalStop}
            toggleModalComplete={toggleModalComplete}
            toggleNoticesUpdate={toggleNoticesUpdate}
            setSelectedRecord={setSelectedRecord}
            selectedRowKeys={selectedRowKeys}
            setSelectedRowKeys={setSelectedRowKeys}
            selectedRows={selectedRows}
            setSelectedRows={setSelectedRows}
            filter={filter}
            setFilter={setFilter}
            searchText={searchText}
            manufactureOrderType={PRODUCTION_COMMAND_TYPE.MANUFACTURE_ORDER_BY_STANDARD}
          />
        </Card>
      </Tabs.Pane>
      <Tabs.Pane label="Đơn hàng" key={PRODUCTION_COMMAND_TYPE.MANUFACTURE_ORDER_BY_ORDER}>
        <Card
          header={
            <ProductionProcessHeader
              toggleModalCreate={toggleModalCreate}
              toggleModalDelete={toggleModalDelete}
              toggleModalFilter={toggleModalFilter}
              selectedRows={selectedRows}
              setSearchText={setSearchText}
            />
          }
        >
          <ProductionProcessTable
            toggleModalUpdate={toggleModalUpdate}
            toggleModalDelete={toggleModalDelete}
            toggleModalStart={toggleModalStart}
            toggleModalStop={toggleModalStop}
            toggleModalComplete={toggleModalComplete}
            toggleNoticesUpdate={toggleNoticesUpdate}
            setSelectedRecord={setSelectedRecord}
            selectedRowKeys={selectedRowKeys}
            setSelectedRowKeys={setSelectedRowKeys}
            selectedRows={selectedRows}
            setSelectedRows={setSelectedRows}
            filter={filter}
            setFilter={setFilter}
            searchText={searchText}
            manufactureOrderType={PRODUCTION_COMMAND_TYPE.MANUFACTURE_ORDER_BY_ORDER}
          />
        </Card>
      </Tabs.Pane>
    </Tabs>
  );
}

export default ProductionProcessBody;
