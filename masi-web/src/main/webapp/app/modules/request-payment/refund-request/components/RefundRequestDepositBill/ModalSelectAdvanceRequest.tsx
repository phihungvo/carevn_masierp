import { zodResolver } from '@hookform/resolvers/zod';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import FormSelectV2 from 'app/components/formV2/form-select/form-select';
import Modal from 'app/components/modal/modal';
import { TableColumns } from 'app/components/table-v2/Table';
import TablePagination from 'app/components/table-v2/TablePagination';
import {
  DATE_FORMAT,
  DEFAULT_PAGE,
  DEFAULT_PAGE_SIZE,
} from 'app/constants/common';
import { useDebounce } from 'app/hooks/use-debounce';
import useEmployee from 'app/hooks/use-employee';
import usePaymentRequest from 'app/hooks/use-payment-request';
import {
  PAYMENT_REQUEST_STATUS,
  PAYMENT_REQUEST_TYPE,
} from 'app/shared/model/enumerations/payment-request';
import {
  IPaymentRequest,
  IPaymentRequestParams,
} from 'app/shared/model/payment-request.model';
import { convertCurrency } from 'app/shared/util/format';
import {
  RefundRequestSchema,
  RequestPaymentFilterDateSchema,
  requestPaymentFilterDateSchema,
} from 'app/validation/request-payment.validation';
import dayjs from 'dayjs';
import { useEffect, useState } from 'react';
import { useForm, useFormContext } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { useParams } from 'react-router';
import { Col, Input, Row } from 'reactstrap';

const { useGetPaymentRequests } = usePaymentRequest;
const { useGetEmployeesQuery } = useEmployee;

type Props = {
  isOpen: boolean;
  toggle: () => void;
  onIncomingChange: (data?: any) => void;
};

const ModalSelectAdvanceRequest = (props: Props) => {
  const { isOpen, toggle, onIncomingChange } = props;
  const { id } = useParams();

  const [searchPaymentRequestText, setSearchPaymentRequestText] =
    useState<string>(null);

  const search = useDebounce(searchPaymentRequestText, 500);

  const { watch } = useFormContext<RefundRequestSchema>();
  const [filter, setFilter] = useState<IPaymentRequestParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
  });

  const [selectedRowKeys, setSelectedRowKeys] = useState([]);

  const { data: requestPaymentAdvancement } = useGetPaymentRequests({
    'type.equals': PAYMENT_REQUEST_TYPE.ADVANCEMENT,
    'status.equals': PAYMENT_REQUEST_STATUS.APPROVED,
    ...filter,
  });
  const { data: employees } = useGetEmployeesQuery();
  const reimbursementsWatch = watch('reimbursements');

  const methods = useForm<RequestPaymentFilterDateSchema>({
    resolver: zodResolver(requestPaymentFilterDateSchema),
  });
  const { control, setValue: setValueFilter, watch: watchFilter } = methods;
  const createdStateWatch = watchFilter('createdDate');

  const [listTmp, setListTmp] = useState<IPaymentRequest[]>([]);

  const toggleRowKeys = (id: string) => () => {
    if (selectedRowKeys.includes(id)) {
      setSelectedRowKeys(prevState => prevState.filter(key => key !== id));
    } else {
      setSelectedRowKeys(prevState => [...prevState, id]);
    }
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
    const selectedIds = requestPaymentAdvancement.data?.map(x => x.id);
    setSelectedRowKeys(
      computeDifference(selectedIds, [...(selectedRowKeys ?? [])]),
    );
  };

  const handleOnOK = () => {
    const selectedRows = listTmp.filter(x => selectedRowKeys.includes(x.id));
    const selectedRowsExistId = selectedRows.map(x => {
      const exist = [...(reimbursementsWatch ?? [])].find(
        e => e.advanceId === x.id,
      );
      if (exist) return { ...x, headerId: exist.id };
      else return { ...x };
    });
    onIncomingChange(selectedRowsExistId);
    toggle();
  };

  const handlePageChange = (page: number) => {
    setFilter({ ...filter, page });
  };

  const handlePageSizeChange = (pageSize: number) => {
    setFilter({ ...filter, page: DEFAULT_PAGE, size: pageSize });
  };

  useEffect(() => {
    setFilter(prev => ({
      ...prev,
      search,
      'code.contains': search === '' ? undefined : search,
    }));
  }, [search]);

  useEffect(() => {
    if (reimbursementsWatch?.length && id) {
      const listIds = [...(reimbursementsWatch ?? [])].map(x => x.advanceId);
      setSelectedRowKeys(listIds);
    } else if (reimbursementsWatch?.length === 0) setSelectedRowKeys([]);
  }, [reimbursementsWatch]);

  useEffect(() => {
    const mergeAndRemoveDuplicates = (
      array1: IPaymentRequest[],
      array2: IPaymentRequest[],
    ): IPaymentRequest[] => {
      const uniqueItemsMap = new Map<string, IPaymentRequest>();
      array1.forEach(item => uniqueItemsMap.set(item.id, item));
      array2.forEach(item => uniqueItemsMap.set(item.id, item));
      return Array.from(uniqueItemsMap.values());
    };

    if (requestPaymentAdvancement?.data?.length) {
      const newArr = mergeAndRemoveDuplicates(
        listTmp,
        requestPaymentAdvancement?.data,
      );
      setListTmp([...newArr]);
    }
  }, [requestPaymentAdvancement]);

  const checkedAllByPage = larger => {
    const smaller = requestPaymentAdvancement?.data?.map(x => x.id);
    if ([...(smaller ?? [])]?.length === 0) return false;
    if ([...(larger ?? [])]?.length === 0) return false;
    return [...(smaller ?? [])].every(item => larger.includes(item));
  };

  useEffect(() => {
    if (!isOpen) {
      setSearchPaymentRequestText('');
      setValueFilter('createdDate', undefined);
      setValueFilter('employeeId', undefined);
      setFilter({
        ...filter,
        search: '',
        page: DEFAULT_PAGE,
        employeeId: undefined,
        'createdDate.equals': undefined,
      });
    }
  }, [isOpen]);

  const columns: TableColumns<IPaymentRequest> = [
    {
      header: {
        render: (
          <Input
            type="checkbox"
            onChange={() => toggleSelectAll()}
            checked={checkedAllByPage(selectedRowKeys)}
          />
        ),
      },
      body: {
        render: ({ data }) => (
          <Input
            type="checkbox"
            onClick={toggleRowKeys(data?.id)}
            checked={selectedRowKeys.includes(data?.id)}
          />
        ),
      },
    },
    {
      header: { render: 'Số ĐN' },
      body: { render: ({ data }) => data?.code },
    },
    {
      header: { render: 'Ngày ĐN' },
      body: {
        render: ({ data }) => dayjs(data?.paymentDate).format(DATE_FORMAT.DATE),
      },
    },
    {
      header: { render: 'Diễn giải' },
      body: { render: ({ data }) => data?.content },
    },
    {
      header: { render: 'Loại tiền' },
      body: { render: () => 'VND' },
    },
    {
      header: { render: 'Số tiền nguyên tệ' },
      body: {
        render: ({ data }) => convertCurrency(data?.totalAmount ?? 0, false),
      },
    },
    {
      header: { render: 'Số tiền' },
      body: {
        render: ({ data }) => convertCurrency(data?.totalAmount ?? 0, false),
      },
    },
  ];

  const onChangeFilter = (key: string, value: string | DateObject) => {
    setFilter(prev => ({ ...prev, [key]: value }));
  };

  useEffect(() => {
    if (createdStateWatch) {
      setFilter(prev => ({
        ...prev,
        'createdDate.equals': dayjs(createdStateWatch.toDate()).toISOString(),
      }));
    }
  }, [createdStateWatch]);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-create-group"
      okText="Đồng ý"
      titleHeader="Chọn đề nghị tạm ứng"
      style={{ width: '1234px' }}
      onOk={handleOnOK}
    >
      <Row>
        <Col md={4}>
          <FormInputV2
            name=""
            label="Mã phiếu"
            placeholder="Nhập mã phiếu"
            onChange={e => setSearchPaymentRequestText(e.target.value)}
          />
        </Col>

        <Col md={4}>
          <FormDatePickerV2
            control={control}
            name="createdDate"
            label="Ngày lập"
            placeholder="__/__/____"
            setValue={setValueFilter}
          />
        </Col>

        <Col md={4}>
          <FormSelect
            control={control}
            name="employeeId"
            label="Người lập"
            placeholder="Vui lòng chọn người lập"
            options={employees?.data?.map(x => ({
              value: x?.id,
              label: [
                x?.employeeProfile?.employeeCode,
                x?.employeeProfile?.fullName,
              ].join(' - '),
            }))}
            onChanges={e => onChangeFilter('employeeId', e)}
          />
        </Col>
      </Row>

      <TablePagination<any>
        table_id="select-htu"
        columns={columns}
        data={requestPaymentAdvancement?.data || []}
        total_pages={requestPaymentAdvancement?.totalRecord ?? 0}
        itemsPerPage={filter.size}
        handlePageClick={handlePageChange}
        handlePageSizeChange={handlePageSizeChange}
        isStickyLastRow
        custom_body_row={() => (
          <tr className="table-footer">
            <td
              className="text-right"
              colSpan={5}
              style={{ textAlign: 'center', fontWeight: '600' }}
            >
              Cộng tiền HĐ được chọn
            </td>
            <td
              className="text-right"
              colSpan={2}
              style={{ textAlign: 'left', fontWeight: '600' }}
            >
              {convertCurrency(
                requestPaymentAdvancement?.data.reduce(
                  (acc, item) => acc + (Number(item?.totalAmount ?? 0) || 0),
                  0,
                ),
                false,
              )}
            </td>
          </tr>
        )}
      />
    </Modal>
  );
};

export default ModalSelectAdvanceRequest;
