import CreatedBySelect from "app/components/CreatedBySelect/CreatedBySelect"
import FormDatePickerV2 from "app/components/formV2/form-date/form-date-picker"
import FormInputV2 from "app/components/formV2/form-input/form-input"
import FormWrap from "app/components/formV2/form-wrap/form-wrap"
import MoneyType from "app/components/money-type/MoneyType"
import PaymentMethod from "app/components/payment-method/PaymentMethod"
import { Typography } from "app/components/typography/typography"
import { PATH } from "app/constants/path"
import { BasicSelect } from "app/shared/model/arr-obj.model"
import { useEffect } from "react"
import { Controller, useFormContext } from "react-hook-form"
import { useParams } from "react-router"
import { Col, FormGroup, Input, Label, Row } from "reactstrap"
import { useIncomingInvoiceNextCode } from "../apis/api.hook"
import { IncomingInvoiceV2SchemaType } from "../validation/incoming.validate"
import Flex from "app/components/flex/flex"


const Summary = (props: any) => {
  const params = useParams()
  const id = params?.id
  const isEditMode = !!id
  const isImportForm = params?.type === PATH.IMPORT_FORM

  const { control, setValue, watch, formState } = useFormContext<IncomingInvoiceV2SchemaType>()
  const relativedFees = watch('relativedFees')
  const invoiceDate = watch('invoiceDate')
  const createdAt = watch('createdAt')
  const isInvoice = watch('isInvoice')

  // const { data: nextCode } = useIncomingInvoiceNextCode(!isEditMode)

  // useEffect(() => {
  //   if (!isEditMode && nextCode) {
  //     setValue('summary.invoiceNo', nextCode)
  //     setValue('relativedFees', [{
  //       id: 'default',
  //       invoiceId: nextCode,
  //     }])
  //   }
  // }, [nextCode])

  useEffect(() => {
    if (invoiceDate) {
      setValue('relativedFees.0.invoiceDate', invoiceDate)
    }
    if (createdAt) {
      setValue('relativedFees.0.createdAt', createdAt)
    }
  }, [invoiceDate, createdAt])

  return (
    <section>
      <Row>
        <Typography level={5} style={{ marginBottom: '12px' }}>
          Thông tin chung
        </Typography>
      </Row>
      <Row>
        <Col md={4}>
          <FormInputV2
            control={control}
            label="Mẫu số"
            name="summary.patternNo"
          />
        </Col>
        <Col md={4}>
          <FormInputV2
            control={control}
            label="Số seri"
            name="summary.series"
            placeholder="Mã - Tên đơn hàng"
          />
        </Col>
        <Col md={4}>
          <FormInputV2
            control={control}
            label="Số hóa đơn"
            name="summary.invoiceNo"
            setValue={setValue}
          />
        </Col>
      </Row>
      <Row>
        <Col md={4}>
          <FormDatePickerV2
            control={control}
            label="Ngày hóa đơn"
            name="invoiceDate"
            setValue={setValue}
            formState={formState}
          />
        </Col>
        <Col md={4}>
          <FormDatePickerV2
            control={control}
            label="Ngày phát sinh"
            name="orderCreatedAt"
            setValue={setValue}
            formState={formState}
          />
        </Col>
      </Row>
      <Row>
        <Col md={4}>
          <PaymentMethod<IncomingInvoiceV2SchemaType>
            isShowLabel
            showErrorMessage
            onSelectChange={value => {
              setValue('relativedFees.0.paymentMethodId', value?.id);
            }}
          />
        </Col>
        <Col md={4}>
          <MoneyType<IncomingInvoiceV2SchemaType>
            name="summary.currency.id"
            defaultCode={'VND'}
            onSelectChange={value => {
              setValue('summary.currency.code', value?.code);
              setValue('summary.currency.rate', value?.rate);
            }}
          />
        </Col>
        <Col md={4}>
          <FormInputV2
            control={control}
            label="Tỉ giá"
            name="summary.currency.rate"
            disabled
          />
        </Col>
      </Row>
      <Row>
        <Col md={4}>
          <Controller
            control={control}
            name="summary.employeeId"
            render={({ field, formState: { errors } }) => (
              <FormWrap
                label="Người lập"
                error={errors?.summary?.employeeId?.message}
              >
                <CreatedBySelect
                  onChange={(value: BasicSelect) => {
                    setValue('summary.employeeId', value?.value);
                  }}
                  selectedKey={field?.value}
                  isDisabled={true}
                />
              </FormWrap>
            )}
          />
        </Col>
        <Col md={4}>
          <FormInputV2
            label="Bộ phận"
            control={control}
            name="summary.departmentName"
            disabled
          />
        </Col>
        <Col md={4}>
          <Flex align="center" columnGap={10} style={{ height: '100%' }}>
            <Input
              type="checkbox"
              id="has-incoming-invoice"
              checked={isInvoice}
              onChange={e => {
                setValue('isInvoice', e.target.checked);
              }}
            />
            <label htmlFor="has-incoming-invoice">Đã có hóa đơn</label>
          </Flex>
        </Col>
      </Row>
      <Row>
        <Col md={12}>
          <FormInputV2
            control={control}
            label="Diễn giải"
            name="summary.content"
          />
        </Col>
      </Row>
    </section>
  );
}

export default Summary
