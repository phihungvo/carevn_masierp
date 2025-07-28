import ButtonAdd from "app/components/ButtonV2/ButtonAdd"
import ButtonDelete from "app/components/ButtonV2/ButtonDelete"
import Flex from "app/components/flex/flex"
import FormDatePickerV2 from "app/components/formV2/form-date/form-date-picker"
import FormWrap from "app/components/formV2/form-wrap/form-wrap"
import PaymentMethod from "app/components/payment-method/PaymentMethod"
import SelectV3 from "app/components/select/SelectV3"
import SupplierCodes from "app/components/suppliersCode/SuppliesCode"
import TableV2, { TableColumns } from "app/components/table-v2/Table"
import { Typography } from "app/components/typography/typography"
import { DATE_FORMAT } from "app/constants/common"
import useIncomingInvoice from "app/hooks/use-incoming-invoice"
import { BasicSelect, MapKeySelect } from "app/shared/model/arr-obj.model"
import { convertCurrency } from "app/shared/util/format"
import dayjs from "dayjs"
import { Controller, useFormContext } from "react-hook-form"
import { IIncomingInvoice } from "../types/list.type"
import { RelativedFees } from "../validation/incoming.validate"
import FormError from "app/components/form/form-error"

const {
  useIncomingInvoices
} = useIncomingInvoice

type Props = {}

const RelativedFees = (props: Props) => {
  const { control, watch, setValue, getValues, formState: { errors } } = useFormContext<RelativedFees>()
  const relativedFees = watch('relativedFees')

  const invoiceNoQuery: MapKeySelect = useIncomingInvoices(
  { page: 0, size: 2000000 },
  (res) => {
    let arr = []
    let obj = []
    res?.data?.data?.forEach(item => {
      arr.push({
        value: item.id,
        label: item.invoiceNo,
      })
      obj[item.id] = {
        ...item,
        label: item.invoiceNo
      }
    })
    return {
      obj,
      arr,
      totalRecord: res?.data?.totalRecord
    }
  })

  const columns: TableColumns<RelativedFees['relativedFees'][number]> = [
    {
      header: {
        render: 'Số hóa đơn'
      },
      body: {
        render: ({ data, index }) => {
          return (
            <>
              {data?.id === 'default' ? (
                <>{data?.invoiceId}</>
              ) : (
                <Controller
                  control={control}
                  name={`relativedFees.${index}.invoiceId`}
                  render={({ field }) => (
                    <FormWrap error={''}>
                      <SelectV3
                        {...field}
                        options={invoiceNoQuery?.data?.arr || []}
                        onChange={(value: BasicSelect) => {
                          field.onChange(value?.value);
                          const data: IIncomingInvoice =
                            invoiceNoQuery?.data?.obj?.[value?.value];
                          setValue(
                            `relativedFees.${index}.invoiceDate`,
                            data?.invoiceDate,
                          );
                          setValue(
                            `relativedFees.${index}.createdAt`,
                            data?.createdAt,
                          );
                          setValue(
                            `relativedFees.${index}.supplierId`,
                            data?.supplierId,
                          );
                          setValue(
                            `relativedFees.${index}.taxCode`,
                            data?.suppliers?.taxCode,
                          );
                          setValue(
                            `relativedFees.${index}.paymentMethodId`,
                            data?.paymentMethod,
                          );
                          setValue(
                            `relativedFees.${index}.vatPercent`,
                            data?.totalVat,
                          );
                          setValue(
                            `relativedFees.${index}.cost`,
                            data?.totalAmount,
                          );
                          setValue(
                            `relativedFees.${index}.totalAmountAfterVat`,
                            data?.totalAmountAfterVat,
                          );
                          setValue(
                            `relativedFees.${index}.grandTotal`,
                            data?.grandTotal,
                          );
                          setValue(
                            `relativedFees.${index}.debtDays`,
                            data?.debtDays,
                          );
                          setValue(`relativedFees.${index}.note`, data?.note);
                        }}
                        value={
                          field?.value && [
                            {
                              label:
                                invoiceNoQuery?.data?.obj?.[field?.value]
                                  ?.label,
                              value: field?.value,
                            },
                          ]
                        }
                      />
                    </FormWrap>
                  )}
                />
              )}
            </>
          );
        }
      }
    },
    {
      header: {
        render: 'Ngày hóa đơn'
      },
      body: {
        render: ({ data, index }) => dayjs(data?.invoiceDate).format(DATE_FORMAT.DATE)
      }
    },
    {
      header: {
        render: 'Ngày phát sinh'
      },
      body: {
        render: ({ data }) => dayjs(data?.createdAt).format(DATE_FORMAT.DATE)
      }
    },
    {
      header: {
        render: 'NCC'
      },
      body: {
        render: ({ data, index }) => {
          return (
            <SupplierCodes
              isShowLabel={false}
              name={`relativedFees.${index}.supplierId`}
              placeholder="Chọn"
              isDisabled
            />
          )
        }
      }
    },
    {
      header: {
        render: 'MST'
      },
      body: {
        render: ({ data }) => data?.taxCode
      }
    },
    {
      header: {
        render: 'Thanh toán'
      },
      body: {
        render: ({ index, data }) => {
          return (
            <PaymentMethod
              isShowLabel={false}
              name={`relativedFees.${index}.paymentMethodId`}
              placeholder="Chọn"
              getItemById={(data) => {
                setValue(`relativedFees.${index}.paymentMethodCode`, data?.code)
                setValue(`relativedFees.${index}.paymentMethodName`, data?.name)
              }}
              disabled={data?.id === 'default'}
            />
          )
        }
      }
    },
    {
      header: {
        render: '% VAT'
      },
      body: {
        render: ({ data }) => convertCurrency(data?.vatPercent)
      }
    },
    {
      header: {
        render: 'Giá trị'
      },
      body: {
        render: ({ data }) => convertCurrency(data?.cost)
      }
    },
    {
      header: {
        render: 'VAT'
      },
      body: {
        render: ({ data }) => convertCurrency(data?.vat)
      }
    },
    {
      header: {
        render: 'Thành tiền'
      },
      body: {
        render: ({ data }) => convertCurrency(data?.grandTotal)
      }
    },
    {
      header: {
        render: 'Ngày công nợ'
      },
      body: {
        render: ({ data }) => data?.debtDays
      }
    },
    {
      header: {
        render: 'Ghi chú'
      },
      body: {
        render: ({ data }) => data?.note
      }
    },
    {
      header: {
        render: ''
      },
      body: {
        render: ({ data, index }) => (
          <ButtonDelete onClick={onRemoveRow(index)} disabled={data?.id === 'default'} />
        )
      }
    },
  ]

  const onAddRow = () => {
    let relativedFees = getValues('relativedFees')
    relativedFees = relativedFees || []
    relativedFees.push({
      vatPercent: 0,
      cost: 0,
      vat: 0,
      totalAmountAfterVat: 0,
      grandTotal: 0,
      debtDays: 0,
    })
    setValue('relativedFees', relativedFees)
  }

  const onRemoveRow = (index: number) => () => {
    let relativedFees = getValues('relativedFees')
    relativedFees = relativedFees.filter((_, i) => i !== index)
    setValue('relativedFees', relativedFees)
  }

  return (
    <section>
      <Flex direction="column" rowGap={12}>
        <Flex align="center" columnGap={12}>
          <Typography level={5}>Phí liên quan</Typography>
          <ButtonAdd text="Thêm" onClick={onAddRow} />
        </Flex>
        <TableV2<any>
          table_id="relatived_fees"
          columns={columns}
          data={relativedFees}
        />
        {errors?.relativedFees?.root?.message && (
          <FormError message={errors?.relativedFees?.root?.message} />
        )}
      </Flex>
    </section>
  )
}

export default RelativedFees
