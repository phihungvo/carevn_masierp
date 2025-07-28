import Flex from "app/components/flex/flex"
import FormError from "app/components/form/form-error"
import TableV2, { TableColumns } from "app/components/table-v2/Table"
import WrapDate from "app/components/wrap-date/WrapDate"
import WrapCheckbox from "app/components/wrap-input-checkbox/WrapCheckbox"
import WrapInputNumber from "app/components/wrap-input-text/WrapInputNumber"
import Order from "app/components/wrap-select/Order"
import SupplierContract from "app/components/wrap-select/SupplierContract"
import WrapTextArea from "app/components/wrap-text-area/WrapTextArea"
import useOrders from "app/hooks/use-orders"
import { IContract } from "app/shared/model/contract.model"
import { IOrder } from "app/shared/model/order.model"
import { convertCurrency } from "app/shared/util/format"
import { clone } from "lodash"
import { useEffect } from "react"
import { useFormContext } from "react-hook-form"
import { Col, Input, Row } from "reactstrap"
import { useGetItemList } from "../apis/api"
import useDeliverySearchParams, { purchase, sell } from "../useDeliverySearchParams"
import { DeliveryScheduleSchema } from "../validations/delivery-schduler.validate"

const {
  useGetOrderByIdQuery
} = useOrders

const convertedToIsoString = (date: string) => {
  if (date === 'undefined') return undefined
  const [day, month, year] = date?.split('/')
  return new Date(+year, +month - 1, +day).toISOString();
}

export const ScheduleModal = () => {
  const {
    searchParams, isEdit, isPurchase, 
    isCreate, isSell, isOpenModal, onToggleModal
  } = useDeliverySearchParams()

  const {
    control, watch, setValue, formState: { errors }, reset
  } = useFormContext<DeliveryScheduleSchema>()
  const deliveryDetail = watch('body.deliveryDetail')
  const orderId = watch('body.orderId')
  const contractId = watch('body.contractId')
  const scheduleId = watch('body.id')
  const modeWatch = watch('body.mode')

  const itemList = useGetItemList(isCreate ? isSell ? orderId : contractId : undefined)

  const onCheckedAll = (e: React.ChangeEvent<HTMLInputElement>) => {
    let isChecked = e.target.checked
    let arrs = clone(deliveryDetail)
    setValue('body.deliveryDetail', arrs.map(item => ({ ...item, isChecked })))
  }

  const columns: TableColumns<any> = [
    {
      header: {
        render: (
          <Input 
            type="checkbox"
            disabled={isEdit}
            checked={deliveryDetail?.every(item => item.isChecked)} 
            onChange={onCheckedAll}
          />
        )
      },
      body: {
        render: ({ index, data }) => (
          <WrapCheckbox<DeliveryScheduleSchema>
            name={`body.deliveryDetail.${index}.isChecked`}
            disabled={isEdit}
          />
        )
      }
    },
    {
      header: {
        render: 'Hàng hóa'
      },
      body: {
        render: ({ data }) => data?.name
      }
    },
    {
      header: {
        render: 'SL hợp đồng'
      },
      body: {
        render: ({ data }) => convertCurrency(data?.contractQuantity)
      }
    },
    {
      header: {
        render: 'SL dự kiến'
      },
      body: {
        render: ({ index, data }) => (
          <Flex direction="column">
            <WrapInputNumber<DeliveryScheduleSchema>
              name={`body.deliveryDetail.${index}.deliveryQuantity`}
              max={data?.contractQuantity}
            />
            {(errors?.body as any)?.deliveryDetail?.[index]?.root && (
              <FormError message={(errors?.body as any)?.deliveryDetail?.[index]?.root?.message} />
            )}
          </Flex>
        )
      }
    },
  ]

  useEffect(() => {
    if (itemList?.data) {
      setValue('body.deliveryDetail', itemList?.data)
    }
  }, [itemList?.data])

  useEffect(() => {
    if (isEdit && !scheduleId) onToggleModal()()
    if (isCreate && !modeWatch) {
      setValue('body.mode', isPurchase ? purchase : sell)
    }
  }, [])

  return (
    <>
      {isCreate && isSell && (
        <Order<DeliveryScheduleSchema>
          name="body.orderId"
          label="Mã hợp đồng"
          onSelectChange={(value: IOrder) => {
            setValue('body.orderId', value?.id);
            setValue('body.contractId', value?.contractId);
          }}
        />
      )}
      {isCreate && isPurchase && (
        <SupplierContract<DeliveryScheduleSchema>
          label="Mã hợp đồng"
          name="body.contractId"
          onSelectChange={(value: IContract) => {
            setValue('body.contractId', value?.id);
          }}
        />
      )}
      <Row>
        <Col md={isEdit ? 6 : 12}>
          <WrapDate<DeliveryScheduleSchema>
            label="Ngày giao dự kiến"
            name="body.deliveryDate"
          />
        </Col>
        {isEdit && (
          <Col md={6}>
            <WrapInputNumber<DeliveryScheduleSchema>
              label="Số lượng dự kiến"
              name="body.quantity"
            />
          </Col>
        )}
      </Row>
      {isEdit && (
        <Row>
          <Col md={6}>
            <WrapDate<DeliveryScheduleSchema>
              label="Ngày giao thực tế"
              name="body.actualDeliveryDate"
            />
          </Col>
          <Col md={6}>
            <WrapInputNumber<DeliveryScheduleSchema>
              label="Số lượng thực tế"
              name="body.actualQuantity"
            />
          </Col>
        </Row>
      )}
      <Row>
        <WrapTextArea<DeliveryScheduleSchema>
          name="body.deliveryLocation"
          label="Địa chỉ"
          rows={2}
        />
      </Row>
      <Row>
        <WrapTextArea<DeliveryScheduleSchema>
          name="body.note"
          label="Ghi chú"
          rows={2}
        />
      </Row>
      {isCreate && (
        <TableV2<any>
          table_id="delivery-schedule-table-modal"
          columns={columns}
          data={deliveryDetail}
          isLoading={itemList?.isLoading}
        />
      )}
      {(errors?.body as any)?.deliveryDetail?.message && (
        <FormError message={(errors?.body as any)?.deliveryDetail?.message} />
      )}
      {(errors?.body as any)?.deliveryDetail?.root && (
        <FormError message={(errors?.body as any)?.deliveryDetail?.root?.message} />
      )}
    </>
  );
}
