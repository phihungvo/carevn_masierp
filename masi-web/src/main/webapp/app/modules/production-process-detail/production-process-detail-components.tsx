import Button from 'app/components/button/button';
import Dropdown from 'app/components/dropdown/dropdown';
import FilterDateMulti from 'app/components/filter-date-multi/filter-date-multi';

import AuthGuard from 'app/components/guards/auth-guard';
import InputSearch from 'app/components/input/input-search';
import { DATE_FORMAT, DEFAULT_PAGE } from 'app/constants/common';
import { PATH } from 'app/constants/path';
import { CHECKLIST_TYPE } from 'app/shared/model/enumerations/production-process.model';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { IProductionProcessDetailParams, IProductionProcessItem } from 'app/shared/model/production-process.model';
import React, { useRef, useState } from 'react';
import { DateObject, DatePickerRef } from 'react-multi-date-picker';
import { useNavigate, useParams } from 'react-router';

export type Template = `${CHECKLIST_TYPE}`;
export interface ITemplateList {
  id: string;
  name: string;
  date: string;
  template: Template;
}

interface IProductionProcessDetailHeader {
  workItem: IProductionProcessItem;
  setFilter: React.Dispatch<React.SetStateAction<IProductionProcessDetailParams>>;
  setSearchText: (searchText: string) => void;
}

export const ProductionProcessDetailHeader = (props: IProductionProcessDetailHeader) => {
  const { workItem, setFilter, setSearchText } = props;

  const navigation = useNavigate();
  const { id, workItemId } = useParams();
  const datePickerFilterRef = useRef<DatePickerRef | null>(null);

  const [date, setDate] = useState<DateObject[]>([]);

  const closeFilterCalendar = () => {
    setDate([]);
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      startDate: '',
      endDate: '',
    }));
    datePickerFilterRef.current?.closeCalendar();
  };

  const filterTemplate = () => {
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      startDate: date[0]?.format(DATE_FORMAT.YEAR_DATE),
      endDate: date[1] ? date[1]?.format(DATE_FORMAT.YEAR_DATE) : date[0]?.format(DATE_FORMAT.YEAR_DATE),
    }));
    datePickerFilterRef.current?.closeCalendar();
  };

  const dropdownItems = [
    {
      label: 'Giám sát tiếp nhận nguyên liệu',
      onClick: () => {
        navigation(PATH.PRODUCTION_PROCESS_TEMPLATE_MATERIAL_RECEIPT.replace(':id', id).replace(':workItemId', workItemId));
      },
    },
    {
      label: 'Lựa và bổ sung phụ gia',
      onClick: () => {
        navigation(PATH.PRODUCTION_PROCESS_TEMPLATE_SELECTING_ADDING.replace(':id', id).replace(':workItemId', workItemId));
      },
    },
    {
      label: 'Giám sát hoạt động máy',
      onClick: () => {
        navigation(PATH.PRODUCTION_PROCESS_TEMPLATE_MACHINE_OPERATION.replace(':id', id).replace(':workItemId', workItemId));
      },
    },
    {
      label: 'Giám sát công đoạn hấp - sấy',
      onClick: () => {
        navigation(PATH.PRODUCTION_PROCESS_TEMPLATE_MONITORING_STEAMING.replace(':id', id).replace(':workItemId', workItemId));
      },
    },
    {
      label: 'Kiểm tra nam châm và lưới',
      onClick: () => {
        navigation(PATH.PRODUCTION_PROCESS_TEMPLATE_MAGNET_MESH.replace(':id', id).replace(':workItemId', workItemId));
      },
    },
    {
      label: 'Báo cáo trộn sản phẩm bột cá',
      onClick: () => {
        navigation(PATH.PRODUCTION_PROCESS_TEMPLATE_REPORT_MIXING.replace(':id', id).replace(':workItemId', workItemId));
      },
    },
  ];

  const [isOpen, setIsOpen] = useState(false);

  const toggle = () => setIsOpen(prev => !prev);

  return (
    <div className="card-header-container">
      <InputSearch className="card-header-extra" onChange={e => setSearchText(e.target.value)} />

      <div className="card-header-extra">
        <FilterDateMulti value={date} setSelectedDate={setDate} ref={datePickerFilterRef} onReset={closeFilterCalendar} onOk={filterTemplate} />
        <Button color="primary" onClick={() => navigation(-1)}>
          Quay lại
        </Button>
          <Dropdown label="Tạo biểu mẫu" items={dropdownItems} isOpen={isOpen} toggle={toggle} />
      </div>
    </div>
  );
};
