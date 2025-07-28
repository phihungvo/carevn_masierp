import DatePicker from "app/components/date-picker/date-picker"
import FormWrap from "app/components/formV2/form-wrap/form-wrap"
import InputDelay from "app/components/input-delay/InputDelay"
import Modal from "app/components/modal/modal"
import SupplierCodes from "app/components/suppliersCode/SuppliesCode"
import { TableColumns } from "app/components/table-v2/Table"
import TablePagination from "app/components/table-v2/TablePagination"
import { DATE_FORMAT } from "app/constants/common"
import { useInventories } from "app/hooks/use-inventories"
import useSearchQuery from "app/hooks/use-search-query"
import { IInventoriesStorage } from "app/shared/model/inventories-storage.model"
import dayjs from "dayjs"
import { cloneDeep } from "lodash"
import { useFormContext } from "react-hook-form"
import { Col, Input, Row } from "reactstrap"
import { ImportFilter } from "../../types/Filter"
import { IncomingInvoiceV2SchemaType } from "../../validation/incoming.validate"

type Props = {
  openModal: boolean
  setOpenModal: React.Dispatch<React.SetStateAction<boolean>>
  toggle: () => void
  checkedObj: Record<string, boolean>
  setCheckedObj: React.Dispatch<React.SetStateAction<Record<string, boolean>>>
}

const InventoryModal = (props: Props) => {
  const { openModal, setOpenModal, toggle, checkedObj, setCheckedObj } = props

  const { query, handleQuery, setQuery } = useSearchQuery<Partial<ImportFilter>>({
    defaultValue: {
      query: {
        page: 0,
        size: 10,
        'createdAt.greaterThanOrEqual': null,
        'createdAt.lessThanOrEqual': null,
        'invoiceId.specified': false,
      },
    },
    howToResolveData: (key, value) => {
      switch (key) {
        case 'customerId.equals':
          return value?.id
        default:
          return value;
      }
    }
  })

  const { data: inventorieList, isLoading } = useInventories({
    ...query,
    'createdAt.greaterThanOrEqual': query?.['createdAt.greaterThanOrEqual']
      ? dayjs(query?.['createdAt.greaterThanOrEqual']?.toDate()).format(
          'YYYY-MM-DD',
        )
      : null,
    'createdAt.lessThanOrEqual': query?.['createdAt.lessThanOrEqual']
      ? dayjs(query?.['createdAt.lessThanOrEqual']?.toDate()).format(
          'YYYY-MM-DD',
        )
      : null,
  });

  const { setValue, getValues, watch } = useFormContext<IncomingInvoiceV2SchemaType>()
  const inventoryIds = watch('inventoryIds')

  const onChecked = (data: IInventoriesStorage) => (e: React.ChangeEvent<HTMLInputElement>) => {
    let { id, code } = data
    let isChecked = e.target.checked
    let tmp = cloneDeep(getValues('inventoryIds'))
    let codes = cloneDeep(getValues('inventoryCodes'))
    tmp = tmp || []
    codes = codes || []
    let obj = { ...checkedObj }
    if (isChecked) {
      codes.push(code)
      tmp.push(id)
      obj[id] = true
    } else {
      tmp = tmp.filter((item: string) => item !== id)
      codes = codes.filter((item: string) => item !== code)
      delete obj[id]
    }
    setCheckedObj(obj)
    setValue('inventoryIds', tmp)
    setValue('inventoryCodes', codes)
  }

  const onCheckedAll = (e: React.ChangeEvent<HTMLInputElement>) => {
    let isChecked = e.target.checked
    let tmp = []
    let codes = []
    let obj = {}
    if (isChecked) {
      inventorieList?.data?.forEach(item => {
        codes.push(item?.code)
        tmp.push(item.id)
        obj[item.id] = true
      })
    }
    setValue('inventoryIds', tmp)
    setValue('inventoryCodes', codes)
    setCheckedObj(obj)
  }

  const onReset = () => {
    toggle()
    setQuery(pre => {
      delete pre?.["code.contains"]
      delete pre?.["dateCreate.greaterThanOrEqual"]
      delete pre?.["dateCreate.lessThanOrEqual"]
      delete pre?.['supplierId.equals']
      return {...pre}
    })
    setValue('inventoryIds', [])
    setValue('inventoryCodes', [])
  }

  const columns: TableColumns<IInventoriesStorage> = [
    {
      header: {
        render: (
          <Input
            type="checkbox"
            checked={inventoryIds?.length === inventorieList?.data?.length}
            onChange={onCheckedAll}
          />
        ),
      },
      body: {
        render: ({ data }) => (
          <Input
            type="checkbox"
            checked={checkedObj?.[data?.id]}
            onChange={onChecked(data)}
          />
        ),
      },
    },
    {
      header: {
        render: 'Số phiếu',
      },
      body: {
        render: ({ data }) => data?.code
      },
    },
    {
      header: {
        render: 'Ngày PS',
      },
      body: {
        render: ({ data }) => dayjs(data?.createdAt).format(DATE_FORMAT.DATE)
      },
    },
    {
      header: {
        render: 'Ngày nhập',
      },
      body: {
        render: ({ data }) => data?.dateCreate
      },
    },
    {
      header: {
        render: 'NCC',
      },
      body: {
        render: ({ data }) => data?.customer?.name
      },
    },
    {
      header: {
        render: 'Diễn giải',
      },
      body: {
        render: ({ data }) => data?.note
      },
    },
  ];

  return (
    <Modal
      titleHeader='Chọn phiếu nhập kho'
      okText="Đồng ý"
      cancelText="Hủy"
      onOk={toggle}
      onCancel={onReset}
      toggle={() => {
        toggle()
        setValue('inventoryIds', [])
        setValue('inventoryCodes', [])
      }}
      isOpen={openModal}
    >
      <Row>
        <Col md={3}>
          <SupplierCodes<ImportFilter>
            label="Nhà cung cấp"
            name="code.contains"
            placeholder="Chọn"
            autoSetValue={handleQuery('customerId.equals')}
          />
        </Col>
        <Col md={3}>
          <FormWrap label="Số phiếu">
            <InputDelay onCompletedChange={handleQuery('search')} />
          </FormWrap>
        </Col>
        <Col md={3}>
          <FormWrap label="Từ ngày">
            <DatePicker
              value={query?.['start']}
              onChange={handleQuery('createdAt.greaterThanOrEqual')}
              closeExportCalendar={() => {
                setQuery(pre => {
                  delete pre?.['start'];
                  return { ...pre };
                });
              }}
            />
          </FormWrap>
        </Col>
        <Col md={3}>
          <FormWrap label="Đến ngày">
            <DatePicker
              value={query?.['end']}
              onChange={handleQuery('createdAt.lessThanOrEqual')}
              closeExportCalendar={() => {
                setQuery(pre => {
                  delete pre?.['end'];
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
        data={inventorieList?.data || []}
        total_pages={inventorieList?.totalRecord}
        itemsPerPage={query.size}
        handlePageClick={handleQuery('page')}
        handlePageSizeChange={handleQuery('size')}
        isLoading={isLoading}
      />
    </Modal>
  );
}

export default InventoryModal
