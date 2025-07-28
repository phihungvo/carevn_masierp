import { zodResolver } from '@hookform/resolvers/zod';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import Modal from 'app/components/modal/modal';
import { TableColumns } from 'app/components/table-v2/Table';
import TablePagination from 'app/components/table-v2/TablePagination';
import {
  DATE_FORMAT,
  DEFAULT_PAGE,
  DEFAULT_PAGE_SIZE,
} from 'app/constants/common';
import useCurrencies from 'app/hooks/use-currencies';
import { useDebounce } from 'app/hooks/use-debounce';
import useIncomingInvoice from 'app/hooks/use-incoming-invoice';
import {
  IIncomingInvoice,
  IIncomingInvoiceParams,
} from 'app/shared/model/incoming-invoice.model';
import { convertCurrency } from 'app/shared/util/format';
import {
  RequestPaymentFilterDateSchema,
  requestPaymentFilterDateSchema,
} from 'app/validation/request-payment.validation';
import dayjs from 'dayjs';
import React, { useEffect, useState } from 'react';
import { useForm, useFormContext } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { useParams } from 'react-router';
import { Col, Input, Row } from 'reactstrap';

const { useGetCurrencies } = useCurrencies;
const { useIncomingInvoices } = useIncomingInvoice;

interface IIncomingInvoiceModals {
  isOpen: boolean;
  toggle: () => void;
  setListInComingInvoice: React.Dispatch<
    React.SetStateAction<IIncomingInvoice[]>
  >;
}

function IncomingInvoiceModals(props: IIncomingInvoiceModals) {
  const { isOpen, toggle, setListInComingInvoice } = props;
  const { id } = useParams();

  const [selectedRowKeys, setSelectedRowKeys] = useState<string[]>([]);
  const [searchText, setSearchText] = useState<string>(null);
  const [searchSupplierText, setSearchSupplierText] = useState<string>(null);
  const [filter, setFilter] = useState<IIncomingInvoiceParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    sort: 'created_at,desc',
  });

  const [listInvoiceTmp, setListInvoiceTmp] = useState<IIncomingInvoice[]>([]);

  const search = useDebounce(searchText, 500);
  const searchSupplier = useDebounce(searchSupplierText, 500);

  const { data: currencies } = useGetCurrencies();
  const { data: incomingInvoices } = useIncomingInvoices({
    ...filter,
  });

  const methods = useFormContext();
  const { watch, setValue } = methods;

  const supplierIdWatch = watch('supplierId');
  const paymentDetailsWatch = watch('paymentDetails');
  const supplierCodeNameWatch = watch('supplierCodeName');

  const methodFilters = useForm<RequestPaymentFilterDateSchema>({
    resolver: zodResolver(requestPaymentFilterDateSchema),
  });

  const {
    control: controlFilter,
    formState: formSateFilter,
    setValue: setValueFilter,
    watch: watchFilter,
  } = methodFilters;

  const toggleRowKeys = (id: string, data: IIncomingInvoice) => {
    if (data?.supplierId !== supplierIdWatch && supplierIdWatch) {
      setSelectedRowKeys([]);
    }

    if (selectedRowKeys.includes(id)) {
      setSelectedRowKeys(prevState => prevState.filter(key => key !== id));
    } else {
      setSelectedRowKeys(prevState => [...prevState, id]);
    }

    setValue('supplierId', data?.supplierId);
    setValue('supplierCodeName', `${data?.suppliers?.name}`);
    setSearchSupplierText(`${data?.suppliers?.name}`);
  };

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
    const selectedIds = incomingInvoices.data?.map(x => x.id);
    setSelectedRowKeys(
      computeDifference(selectedIds, [...(selectedRowKeys ?? [])]),
    );
  };

  const handleOnOK = () => {
    const selectedRows = listInvoiceTmp?.filter(x =>
      selectedRowKeys.includes(x.id),
    );
    const selectedRowsExistId = selectedRows.map(x => {
      const exist = paymentDetailsWatch.find(e => e.invoiceId === x.id);
      if (exist) return { ...x, headerId: exist.id };
      else return { ...x };
    });

    setListInComingInvoice(selectedRowsExistId);
    toggle();
  };

  useEffect(() => {
    if (paymentDetailsWatch?.length && id) {
      const listIds = paymentDetailsWatch.map(x => x.invoiceId);
      setSelectedRowKeys(listIds);
    } else if (paymentDetailsWatch?.length === 0) setSelectedRowKeys([]);
  }, [paymentDetailsWatch]);

  useEffect(() => {
    setSearchSupplierText(supplierCodeNameWatch ?? '');
  }, [supplierCodeNameWatch]);

  const checkedAllByPage = larger => {
    const smaller = incomingInvoices?.data?.map(x => x.id);
    if ([...(smaller ?? [])]?.length === 0) return false;
    if ([...(larger ?? [])]?.length === 0) return false;
    return [...(smaller ?? [])].every(item => larger.includes(item));
  };

  useEffect(() => {
    if (!isOpen) {
      setSearchText('');
      setSearchSupplierText('');
      setValue('supplierCodeName', undefined);
      setValue('currencyId', undefined);
      setValue('invoiceNo', undefined);
      setValueFilter('createdDateArr', []);
      setFilter({
        ...filter,
        page: DEFAULT_PAGE,
        currencyId: undefined,
        startDate: undefined,
        endDate: undefined,
      });
    }
  }, [isOpen]);

  const columns: TableColumns<any> = [
    {
      header: {
        render: (
          <Input
            type="checkbox"
            onChange={() => toggleSelectAll()}
            checked={checkedAllByPage(selectedRowKeys)}
            disabled={Boolean(!supplierIdWatch)}
          />
        ),
      },
      body: {
        render: ({ data }) => (
          <Input
            type="checkbox"
            checked={selectedRowKeys.includes(data?.id)}
            onChange={() => toggleRowKeys(data?.id, data)}
          />
        ),
      },
    },
    {
      header: { render: 'Số HĐ' },
      body: { render: ({ data }) => data?.invoiceNo },
    },
    {
      header: { render: 'Ngày PS' },
      body: {
        render: ({ data }) => dayjs(data?.createdAt).format(DATE_FORMAT.DATE),
      },
    },
    {
      header: { render: 'Diễn giải' },
      body: { render: ({ data }) => data?.content },
    },
    {
      header: { render: 'Loại tiền' },
      body: { render: ({ data }) => data?.currencyCode },
    },
    {
      header: { render: 'Số tiền nguyên tệ' },
      body: {
        render: ({ data }) =>
          convertCurrency(
            Number(data?.grandTotal ?? 0) * Number(data?.currencyRate ?? 1),
          ),
      },
    },
    {
      header: { render: 'Số tiền' },
      body: { render: ({ data }) => convertCurrency(data?.grandTotal, false) },
    },
  ];

  const handlePageChange = (page: number) => {
    setFilter({ ...filter, page });
  };

  const handlePageSizeChange = (pageSize: number) => {
    setFilter({ ...filter, page: DEFAULT_PAGE, size: pageSize });
  };

  const onChangeFilter = (key: string, value: string | DateObject) => {
    setFilter(prev => ({ ...prev, [key]: value }));
  };

  const onChangeFilterDate = () => {
    const createdDateArr = watchFilter('createdDateArr');
    if (createdDateArr?.length === 2) {
      const startValid = dayjs(createdDateArr[0]?.toDate()).isValid();
      const endValid = dayjs(createdDateArr[1]?.toDate()).isValid();
      if (startValid && endValid)
        setFilter(prev => ({
          ...prev,
          startDate:
            createdDateArr[0] && dayjs(createdDateArr[0]?.toDate()).isValid()
              ? dayjs(createdDateArr[0].toDate()).format(DATE_FORMAT.YEAR_DATE)
              : null,
          endDate:
            createdDateArr[1] && dayjs(createdDateArr[1]?.toDate()).isValid()
              ? dayjs(createdDateArr[1].toDate()).format(DATE_FORMAT.YEAR_DATE)
              : null,
        }));
    }
  };

  useEffect(() => {
    setFilter(prev => ({ ...prev, search }));
  }, [search]);

  useEffect(() => {
    setFilter(prev => ({ ...prev, searchSupplier }));
  }, [searchSupplier]);

  useEffect(() => {
    const mergeAndRemoveDuplicates = (
      array1: IIncomingInvoice[],
      array2: IIncomingInvoice[],
    ): IIncomingInvoice[] => {
      const uniqueItemsMap = new Map<string, IIncomingInvoice>();
      array1.forEach(item => uniqueItemsMap.set(item.id, item));
      array2.forEach(item => uniqueItemsMap.set(item.id, item));
      return Array.from(uniqueItemsMap.values());
    };

    if (incomingInvoices?.data?.length) {
      const newArr = mergeAndRemoveDuplicates(
        listInvoiceTmp,
        incomingInvoices?.data,
      );
      setListInvoiceTmp([...newArr]);
    }
  }, [incomingInvoices]);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      titleHeader="Nhập hóa đơn"
      className="modal-default"
      onOk={handleOnOK}
      style={{ width: '1234px' }}
    >
      <Row>
        <Col md={3}>
          <FormInputV2
            id="supplierCodeName"
            name="supplierCodeName"
            label="Nhà cung cấp"
            placeholder="Nhập Mã - Tên NCC"
            onChange={e => setSearchSupplierText(e.target.value)}
          />
        </Col>
        <Col span={1}>
          <FormSelect
            id="currencyId"
            name="currencyId"
            placeholder="Chọn"
            label="Tiền"
            options={currencies?.data?.map(e => ({
              label: e?.name,
              value: e?.id,
            }))}
            onChanges={e => onChangeFilter('currencyId', e)}
          />
        </Col>
        <Col span={2}>
          <FormInputV2
            id="invoiceNo"
            name="invoiceNo"
            label="Số HĐ"
            placeholder="Nhập"
            onChange={e => setSearchText(e.target.value)}
          />
        </Col>
        <Col span={3}>
          <FormDatePickerV2
            control={controlFilter}
            formState={formSateFilter}
            name="createdDateArr"
            label="Từ ngày - đến ngày"
            placeholder="Chọn ngày"
            setValue={setValue}
            onChange={e => {
              if (e) onChangeFilterDate();
              else setValueFilter('createdDateArr', []);
            }}
            range
          />
        </Col>
      </Row>

      <TablePagination
        table_id="invoice_table"
        columns={columns}
        data={[...(incomingInvoices?.data ?? [])]}
        total_pages={incomingInvoices?.totalRecord ?? 0}
        itemsPerPage={filter.size}
        handlePageClick={handlePageChange}
        handlePageSizeChange={handlePageSizeChange}
      />
    </Modal>
  );
}

export default IncomingInvoiceModals;
