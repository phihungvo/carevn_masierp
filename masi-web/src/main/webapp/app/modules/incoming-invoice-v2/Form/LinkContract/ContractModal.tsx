import DatePicker from "app/components/date-picker/date-picker"
import FormWrap from "app/components/formV2/form-wrap/form-wrap"
import InputDelay from "app/components/input-delay/InputDelay"
import Modal from "app/components/modal/modal"
import SupplierCodes from "app/components/suppliersCode/SuppliesCode"
import { TableColumns } from "app/components/table-v2/Table"
import TablePagination from "app/components/table-v2/TablePagination"
import { DATE_FORMAT } from "app/constants/common"
import useSearchQuery from "app/hooks/use-search-query"
import { useGetSupplierContracts } from "app/hooks/use-supplier-contract"
import { ISupplierContract } from "app/shared/model/supplier-contract.model"
import dayjs from "dayjs"
import { useFormContext } from "react-hook-form"
import { Col, Input, Row } from "reactstrap"
import { ImportFilter } from "../../types/Filter"
import { IncomingInvoiceV2SchemaType } from "../../validation/incoming.validate"

type Props = {
  openModal: boolean
  setOpenModal: React.Dispatch<React.SetStateAction<boolean>>
  toggle: () => void
}

const ContractModal = (props: Props) => {
  const { openModal, setOpenModal } = props

  const { query, handleQuery, setQuery } = useSearchQuery<any>({
    defaultValue: {
      query: {
        page: 0,
        size: 10,
        'contractDate.greaterThanOrEqual': null,
        'contractDate.lessThanOrEqual': null,
      },
    },
    howToResolveData: (key, value) => {
      switch (key) {
        case 'supplierId.equals':
          return value?.id
        default:
          return value;
      }
    }
  })

  const { data: suppliesContractList, isLoading } = useGetSupplierContracts({
    ...query,
    'contractDate.greaterThanOrEqual': query?.[
      'contractDate.greaterThanOrEqual'
    ]
      ? dayjs(query?.['contractDate.greaterThanOrEqual']?.toDate()).format(
          'YYYY-MM-DD',
        )
      : null,
    'contractDate.lessThanOrEqual': query?.['contractDate.lessThanOrEqual']
      ? dayjs(query?.['contractDate.lessThanOrEqual']?.toDate()).format(
          'YYYY-MM-DD',
        )
      : null,
  } as any);

  const { setValue, getValues, watch } = useFormContext<IncomingInvoiceV2SchemaType>()
  const supplierContractId = watch('supplierContractId')

  const onChecked = (data: ISupplierContract) => (e: React.ChangeEvent<HTMLInputElement>) => {
    let { id, contractCode } = data
    let isChecked = e.target.checked
    if (!isChecked) {
      id = ''
      contractCode = ''
    }
    setValue('supplierContractId', id)
    setValue('supplierContractCode', contractCode)
  }

  const toggle = () => setOpenModal(!openModal)

  const onReset = () => {
    toggle()
    setQuery(pre => {
      delete pre?.["code.contains"]
      delete pre?.["dateCreate.greaterThanOrEqual"]
      delete pre?.["dateCreate.lessThanOrEqual"]
      delete pre?.supplierId
      return {...pre}
    })
    setValue('supplierContractId', '')
    setValue('supplierContractCode', '')
  }

  const columns: TableColumns<ISupplierContract> = [
    {
      header: {
        render: (
          <Input
            type="checkbox"
            disabled={true}
          />
        ),
      },
      body: {
        render: ({ data }) => (
          <Input
            type="checkbox"
            checked={supplierContractId === data?.id}
            onChange={onChecked(data)}
          />
        ),
      },
    },
    {
      header: {
        render: 'Mã hợp đồng',
      },
      body: {
        render: ({ data }) => data?.contractCode
      },
    },
    {
      header: {
        render: 'Ngày PS',
      },
      body: {
        render: ({ data }) => dayjs(data?.contractDate).format(DATE_FORMAT.DATE)
      },
    },
    {
      header: {
        render: 'Ngày nhập',
      },
      body: {
        render: ({ data }) => dayjs(data?.createdAt).format(DATE_FORMAT.DATE)
      },
    },
  ];

  return (
    <Modal
      titleHeader={'Chọn hợp đồng'}
      okText="Đồng ý"
      cancelText="Hủy"
      onOk={toggle}
      onCancel={onReset}
      toggle={() => {
        toggle()
        setValue('supplierContractId', '')
        setValue('supplierContractCode', '')
      }}
      isOpen={openModal}
    >
      <Row>
        <Col md={3}>
          <SupplierCodes<ImportFilter>
            label="Nhà cung cấp"
            name="code.contains"
            placeholder="Chọn"
            autoSetValue={handleQuery('supplierId.equals')}
          />
        </Col>
        <Col md={3}>
          <FormWrap label="Mã hợp đồng">
            <InputDelay onCompletedChange={handleQuery('contractCode.contains')} />
          </FormWrap>
        </Col>
        <Col md={3}>
          <FormWrap label="Từ ngày">
            <DatePicker
              value={query?.['contractDate.greaterThanOrEqual']}
              onChange={handleQuery('contractDate.greaterThanOrEqual')}
              closeExportCalendar={() => {
                setQuery(pre => {
                  delete pre?.['contractDate.greaterThanOrEqual'];
                  return { ...pre };
                });
              }}
            />
          </FormWrap>
        </Col>
        <Col md={3}>
          <FormWrap label="Đến ngày">
            <DatePicker
              value={query?.['contractDate.lessThanOrEqual']}
              onChange={handleQuery('contractDate.lessThanOrEqual')}
              closeExportCalendar={() => {
                setQuery(pre => {
                  delete pre?.['contractDate.lessThanOrEqual'];
                  return { ...pre };
                });
              }}
            />
          </FormWrap>
        </Col>
      </Row>
      <TablePagination
        table_id="contract-popup"
        columns={columns}
        data={suppliesContractList?.data || []}
        total_pages={suppliesContractList?.totalRecord}
        handlePageClick={handleQuery('page')}
        handlePageSizeChange={handleQuery('size')}
        isLoading={isLoading}
      />
    </Modal>
  );
}

export default ContractModal
