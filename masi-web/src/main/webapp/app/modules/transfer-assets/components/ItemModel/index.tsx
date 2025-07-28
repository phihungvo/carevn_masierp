import FormSelect from 'app/components/form/form-select';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import Modal from 'app/components/modal/modal';
import { TableColumns } from 'app/components/table-v2/Table';
import TablePagination from 'app/components/table-v2/TablePagination';
import {
  DATE_FORMAT,
  DEFAULT_PAGE,
  DEFAULT_PAGE_SIZE,
} from 'app/constants/common';
import useInventoriesStorage from 'app/hooks/use-inventories-storage';
import dayjs from 'dayjs';
import React, { useEffect, useState } from 'react';
import { set, useForm } from 'react-hook-form';
import { Col, Input, Row } from 'reactstrap';
import useItemCategory from 'app/hooks/use-items-category';
import useSupplier from 'app/hooks/use-supplier';
import useUom from 'app/hooks/use-uom';

const { useGetUoms } = useUom;
const { useGetItemsCategoryQuery } = useItemCategory;
const { useGetSuppliers } = useSupplier;
const { useGetDepreciationInventoriesStorageQuery } = useInventoriesStorage;

function ItemModel(props: any) {
  const { isOpen, toggle, handleOnOK, departmentId, selectedIds } = props;
  const methods = useForm();
  const { watch, setValue, control } = methods;
  const [selectedRowKeys, setSelectedRowKeys] = useState([]);
  const [listItemTmp, setListItemTmp] = useState([])
  const [filter, setFilter] = useState<any>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    'department.equals': departmentId,
    checkDepreciation:true
  });
  const { data: listItem } = useGetDepreciationInventoriesStorageQuery(filter);
  const { data: uom } = useGetUoms();
  const { data: itemCategory } = useGetItemsCategoryQuery();
  const { data: supplier } = useGetSuppliers();

  const search = watch('search')
  const supplierId = watch('supplierId')
  const itemCategoryId = watch('itemCategoryId')
  const uomId = watch('uomId')

  useEffect(() => {
    setValue('search', '')
    setValue('supplierId', '')
    setValue('itemCategoryId', '')
    setValue('uomId', '')
    setFilter({
      page: DEFAULT_PAGE,
      size: DEFAULT_PAGE_SIZE,
      'status.doesNotContain': 'LIQUIDATION',
      checkDepreciation:true,
      'department.contains': departmentId,
    })
  }, [isOpen])

  useEffect(() => {
    setSelectedRowKeys([...(selectedIds ?? [])])
  }, [selectedIds])


  useEffect(() => {
    setFilter({
      ...filter,
      'code.contains': search,
      'supplierId.equals': supplierId,
      'uomId.equals': uomId,
      'itemCategoryId.equals': itemCategoryId,
      'department.contains': departmentId,
    });
  }, [search, supplierId, itemCategoryId, uomId, departmentId])

  useEffect(() => {
    const listIds = listItemTmp.map(item => item.id);
    const listAdd = listItem?.data?.filter(s => listIds.indexOf(s.id) === -1);
    if (listAdd) {
      setListItemTmp([...listItemTmp, ...listAdd])
    }
  }, [listItem])


  const computeDifference = (current, next) => {
    const currentSet = new Set(current);
    const nextSet = new Set(next);

    // If `current` and `next` are identical, return an empty array
    if (
      current.length === next.length &&
      [...currentSet].every(item => nextSet.has(item))
    ) {
      return [];
    }

    // If `next` is a subset of `current`, return `current`
    if ([...nextSet].every(item => currentSet.has(item))) {
      return current;
    }

    // If `current` is a subset of `next`, return the difference (next - current)
    if ([...currentSet].every(item => nextSet.has(item))) {
      return next.filter(item => !currentSet.has(item));
    }

    // Otherwise, return the union of both arrays, removing duplicates
    return [...new Set([...current, ...next])];
  };

  const toggleSelectAll = () => {
    const selectedIds = listItem?.data?.map(x => x.id);
    setSelectedRowKeys(
      computeDifference(selectedIds, [...(selectedRowKeys ?? [])]),
    );
  };

  const checkedAllByPage = larger => {
    const smaller = listItem?.data?.map(s => s.id);
    if ([...(smaller ?? [])]?.length === 0) return false;
    if ([...(larger ?? [])]?.length === 0) return false;
    return [...(smaller ?? [])].every(item => larger.includes(item));
  };

  const toggleRowKeys = (id: string) => {
    if (selectedRowKeys.includes(id)) {
      setSelectedRowKeys(prevState => prevState.filter(key => key !== id));
    } else {
      setSelectedRowKeys(prevState => [...prevState, id]);
    }
  };

  const handlePageChange = (page: number) => {
    setFilter({ ...filter, page });
  };

  const handlePageSizeChange = (pageSize: number) => {
    setFilter({ ...filter, page: DEFAULT_PAGE, size: pageSize });
  };


  const columns: TableColumns<any> = [
    {
      header: {
        render: (
          <Input
            type="checkbox"
            onClick={() => toggleSelectAll()}
            checked={checkedAllByPage(selectedRowKeys)}
          />
        ),
      },
      body: {
        render: ({ data }) => (
          <Input
            type="checkbox"
            checked={selectedRowKeys.includes(data?.id)}
            onClick={() => toggleRowKeys(data?.id)}
          />
        ),
      },
    },
    {
      header: { render: 'Mã tài sản' },
      body: { render: ({ data }) => data?.code },
    },
    {
      header: { render: 'NCC' },
      body: {
        render: ({ data }) => data?.item?.supplier?.name
      },
    },
    {
      header: { render: 'Nhóm' },
      body: {
        render: ({ data }) => data?.item?.itemCategory?.name
      },
    },
    {
      header: { render: 'ĐVT' },
      body: {
        render: ({ data }) => data?.item?.uom?.name
      },
    },
    {
      header: { render: 'SL' },
      body: { render: ({ data }) => data?.quantity },
    },
    {
      header: { render: 'Diễn giải' },
      body: { render: ({ data }) => data?.notes },
    },
  ];

  const handleOnOKModel = () => {
    const listItemSelected = listItemTmp.filter(item => selectedRowKeys.indexOf(item.id) !== -1)
    setSelectedRowKeys([])
    handleOnOK(listItemSelected)
    toggle()
  }

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      titleHeader="Chọn tài sản"
      className="modal-default"
      onOk={handleOnOKModel}
      style={{ width: '1234px' }}
    >
      <Row style={{
        marginBottom: "1rem"
      }}>
        <Col >
          <FormInputV2
            control={control}
            id="search"
            name="search"
            label="Tìm kiếm"
            placeholder="Điền"
          />
        </Col>
        <Col >
          <FormSelect
            control={control}
            id={`supplierId`}
            name={`supplierId`}
            label={"NCC"}
            options={supplier?.data?.map(s => ({
              value: s?.id,
              label: `${s.code} - ${s?.name || ''}`,
            }))}
            placeholder="Vui lòng chọn"
          />
        </Col>
        <Col >
          <FormSelect
            control={control}
            id={`itemCategoryId`}
            name={`itemCategoryId`}
            label={"Nhóm"}
            options={itemCategory?.data?.map(s => ({
              value: s?.id,
              label: `${s.code} - ${s?.name}`,
            }))}
            placeholder="Vui lòng chọn"
          />
        </Col>
        <Col >
          <FormSelect
            control={control}
            id={`uomId`}
            name={`uomId`}
            label={"Đơn vị"}
            options={uom?.data?.map(s => ({
              value: s?.id,
              label: `${s?.name}`,
            }))}
            placeholder="Vui lòng chọn"
          />
        </Col>
      </Row>
      <TablePagination
        table_id="inventories-storage-resource_table"
        columns={columns}
        data={listItem?.data ?? []}
        total_pages={listItem?.totalRecord ?? 0}
        itemsPerPage={filter.size}
        handlePageClick={handlePageChange}
        handlePageSizeChange={handlePageSizeChange}
      />
    </Modal>
  );
}

export default ItemModel;
