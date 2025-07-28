import React, { useEffect } from 'react';

import Flex from 'app/components/flex/flex';
import TablePagination from 'app/components/table-v2/TablePagination';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import { PATH } from 'app/constants/path';
import { useDebounce } from 'app/hooks/use-debounce';
import useProductionCommand from 'app/hooks/use-production-command';
import useProductionStandard from 'app/hooks/use-production-standard';
import { MANUFACTURE_ORDER_TYPE } from 'app/shared/model/enumerations/production-command.model';
import {
  IProductionCommand,
  IProductionCommandParams,
} from 'app/shared/model/production-command.model';
import { useNavigate } from 'react-router';
import {
  IconCode,
  IconFish,
  IconFishHead,
  IconFlour,
  IconProtein,
  IconStandard,
  IconStatus,
  IconTotalMaterial,
} from './components/icons';
import { generateColumns } from './generate-columns';

const { useGetProductionCommandsQuery } = useProductionCommand;

interface IProductionDashboardByStandardTable {
  setSelectedRecord: (id: string) => void;
  searchText: string;
  filter: IProductionCommandParams;
  setFilter: React.Dispatch<React.SetStateAction<IProductionCommandParams>>;
  toggleCancel?: () => void;
}

const { useGetProductionStandards } = useProductionStandard;

export const ProductionDashboardByStandardTable = (
  props: IProductionDashboardByStandardTable,
) => {
  const { setSelectedRecord, searchText, filter, setFilter, toggleCancel } =
    props;

  const navigate = useNavigate();
  const name = useDebounce(searchText, 500);

  useEffect(() => {
    setFilter(prev => ({ ...prev, searchString: name, page: DEFAULT_PAGE }));
  }, [name]);

  const { data: standards } = useGetProductionStandards({
    size: DEFAULT_PAGE_SIZE_NAX,
  });
  const { data } = useGetProductionCommandsQuery({
    ...filter,
    searchString: name,
    typePage: MANUFACTURE_ORDER_TYPE.MANUFACTURE_ORDER_BY_STANDARD,
  });

  const handleUpdate = (id: string) => {
    navigate(PATH.PRODUCTION_PROCESS_UPDATE.replace(':id', id));
  };

  const handleDelete = (id: string) => {
    setSelectedRecord(id);
    toggleCancel();
  };

  const columns = generateColumns(
    handleUpdate,
    handleDelete,
    MANUFACTURE_ORDER_TYPE.MANUFACTURE_ORDER_BY_STANDARD,
    standards?.data?.map(x => ({ name: x.name, id: x.id })),
  );

  const handlePageChange = (page: number) => {
    setFilter({ ...filter, page: page });
  };

  const handlePageSizeChange = (pageSize: number) => {
    setFilter({ ...filter, page: DEFAULT_PAGE, size: pageSize });
  };

  const customHeaderRow = () => {
    return (
      <>
        <tr>
          <th rowSpan={2}>
            <Flex gap={8} align="center" style={{ height: '100%' }}>
              {IconCode()} Mã
            </Flex>
          </th>
          <th colSpan={3}>
            <Flex
              gap={8}
              align="center"
              justify="center"
              style={{ height: '100%' }}
            >
              Nguyên liệu
            </Flex>
          </th>
          <th colSpan={2}>
            <Flex
              gap={8}
              align="center"
              justify="center"
              style={{ height: '100%' }}
            >
              Thành phẩm
            </Flex>
          </th>
          <th rowSpan={2}>
            <Flex gap={8} align="center" style={{ height: '100%' }}>
              {IconStandard()} Định mức
            </Flex>
          </th>
          <th rowSpan={2}>
            <Flex gap={8} align="center" style={{ height: '100%' }}>
              {IconStatus()} Kiểm định
            </Flex>
          </th>
          <th rowSpan={2}>
            <Flex gap={8} align="center" style={{ height: '100%' }}>
              {IconStatus()} Trạng thái
            </Flex>
          </th>
          <th rowSpan={2}>
            <Flex gap={8} align="center" style={{ height: '100%' }}>
              Thao tác
            </Flex>
          </th>
        </tr>
        <tr>
          <th>
            <Flex gap={8} align="center">
              {IconFishHead()} Đầu cá (Kg)
            </Flex>
          </th>
          <th>
            <Flex gap={8} align="center">
              {IconFish()} Cá tươi (kg)
            </Flex>
          </th>
          <th>
            <Flex gap={8} align="center">
              {IconTotalMaterial()} Tổng (kg)
            </Flex>
          </th>
          <th>
            <Flex gap={8} align="center">
              {IconFlour()} Bột (kg)
            </Flex>
          </th>
          <th>
            <Flex gap={8} align="center">
              {IconProtein()} Đạm (%)
            </Flex>
          </th>
        </tr>
      </>
    );
  };

  return (
    <TablePagination<IProductionCommand>
      table_id="standards"
      columns={columns}
      data={data?.data || []}
      total_pages={data?.totalRecord || 0}
      itemsPerPage={filter?.size}
      handlePageClick={handlePageChange}
      handlePageSizeChange={handlePageSizeChange}
      custom_header_row={customHeaderRow}
    />
  );
};
