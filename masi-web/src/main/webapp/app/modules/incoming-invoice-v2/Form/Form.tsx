import { zodResolver } from "@hookform/resolvers/zod"
import ButtonBack from "app/components/ButtonV2/ButtonBack"
import ButtonV2 from "app/components/ButtonV2/ButtonV2"
import CardV2 from "app/components/CardV2/CardV2"
import Flex from "app/components/flex/flex"
import { Typography } from "app/components/typography/typography"
import { PATH } from "app/constants/path"
import useAccountApp from "app/hooks/use-account-app"
import Attachments from "app/modules/request-payment/components/Attachments/Attachments"
import { ConvertedGoodsType, ImportConvertedGoodsType } from "app/validation/supplies-request.validation"
import { useEffect } from "react"
import { FormProvider, SubmitHandler, useForm } from "react-hook-form"
import { DateObject } from "react-multi-date-picker"
import { useParams } from "react-router"
import { Col, Form as FormStrap, Row } from "reactstrap"
import { useIncomingInvoiceCreate, useIncomingInvoiceDetail, useIncomingInvoiceUpdate } from "../apis/api.hook"
import { InvoiceType } from "../constants/status"
import { InvoiceSupplies } from "../types/api.type"
import { incomingInvoiceV2Schema, IncomingInvoiceV2SchemaType } from "../validation/incoming.validate"
import Detail from "./Detail"
import Goods from "./Goods"
import ImportGoods from "./ImportGoods"
import LinkContract from "./LinkContract/LinkContract"
import Provider from "./Provider"
import RelativedFees from "./RelativedFees"
import Summary from "./Summary"
import AuthGuard from "app/components/guards/auth-guard"
import { isHasPermission } from "app/constants/common"
import { useAppSelector } from "app/config/store"

const Form = () => {
  const type = "import";

  // State
  const params = useParams()
  const id = params?.id
  const isEditMode = !!id
  const isImportForm = params?.type === PATH.IMPORT_FORM

  const account = useAccountApp()

  const detailQuery = useIncomingInvoiceDetail(id)
  const createMutation = useIncomingInvoiceCreate(isImportForm)
  const updateMutation = useIncomingInvoiceUpdate(id, isImportForm)

  // Form
  const methods = useForm<IncomingInvoiceV2SchemaType>({
    resolver: zodResolver(incomingInvoiceV2Schema),
    defaultValues: {
      summary: {
        departmentName: account?.workspace?.normalizedName,
        employeeId: account?.employeeId,
      },
      invoiceType: isImportForm ? InvoiceType.IMPORT_INVOICE : InvoiceType.INVOICE,
      attachments: [],
      invoiceSupplies: [],
      totalInvoice: 0,
      supplierContractId: '',
      inventoryIds: [],
      createdAt: new DateObject(),
    }
  })

  var { control, handleSubmit, formState: { errors }, reset, getValues, setValue } = methods

  const onSubmit: SubmitHandler<IncomingInvoiceV2SchemaType> = (values) => {
    let {
      summary: { currency, ...summaryRest },
      attachments,
      invoiceType,
      provider,
      invoiceSupplies,
      inventoryIds,
      supplierContractId,
      isInvoice
    } = values;
    invoiceSupplies = invoiceSupplies.map((record: ImportConvertedGoodsType) => {
      let {
        itemDTO,
        uomDTO,
        vatDTO,
        note,
        totalItemAfterVat,
        price,
        quantity,
        totalItem,
      } = record;
      return {
        itemId: itemDTO?.id,
        uomId: uomDTO?.value,
        uomDTO,
        price,
        quantity,
        note,
        vat: vatDTO?.percent,
        vatId: vatDTO?.value,
        totalAmount: totalItem || 0,
        totalAmountAfterVat: totalItemAfterVat || 0,
        preImportFee: record?.feesBeforeImport,
        importTaxPercentage: record?.importPercent,
        importTaxAmount: record?.importTax,
        envFeePercentage: record?.envPercent,
        envFeeAmount: record?.envTax,
        postImportFee: record?.feesAfterImport,
      }
    });
    let body = {
      ...summaryRest,
      ...provider,
      currencyId: currency.id,
      currencyCode: currency.code,
      currencyRate: currency.rate,
      attachments,
      invoiceType,
      invoiceSupplies,
      inventoryIds,
      supplierContractId,
      orderCreatedAt: values?.orderCreatedAt?.toDate()?.toISOString(),
      invoiceDate: values?.invoiceDate?.toDate().toISOString() as any,
      createdAt: values?.createdAt?.toDate().toISOString() as any,
    }
    if (!isEditMode && isImportForm) {
      body['relatedCosts'] = values?.['relativedFees'].filter((_: any, index: number) => index !== 0)
    } else {
      body['relatedCosts'] = values?.['relativedFees']
    }
    body['isInvoice'] = isInvoice;
    if (!isEditMode) delete body?.['relativedFees']
    isEditMode ? updateMutation.mutate(body as any) : createMutation.mutate(body as any)
  }

  useEffect(() => {
    if (detailQuery?.data) {
      let data = detailQuery?.data?.data;

      let tmp = {} as IncomingInvoiceV2SchemaType & { tmp: ConvertedGoodsType };

      const { attachments, content, series, invoiceSupplies, relatedCosts, invoiceType, inventories, isInvoice } = data;

      tmp.isInvoice = isInvoice;
      tmp.orderCreatedAt = new DateObject(data.orderCreatedAt);

      tmp.summary = {
        patternNo: data?.patternNo,
        series,
        invoiceNo: data?.invoiceNo,
        paymentMethod: data?.paymentMethod,
        currency: data?.currency,
        employeeId: data?.employeeId,
        content,
        departmentName: account?.workspace?.normalizedName,
      };

      tmp.totalRoot = data?.['paymentRequest']?.['totalAmount'];

      tmp.summary.currency.rate = data?.currencyRate;

      tmp.invoiceDate = new DateObject(data?.invoiceDate)
      tmp.createdAt = new DateObject(data?.createdAt)

      tmp.provider = {
        supplierId: data?.supplierId,
        supplierTaxCode: data?.suppliers?.taxCode,
        debtDays: data?.debtDays,
        supplierAddress: data?.suppliers?.address,
        supplierName: data?.suppliers?.name,
        supplierPhone: data?.suppliers?.phone,
      };

      tmp.invoiceType = data?.invoiceType;

      tmp.attachments = attachments;
      tmp.invoiceSupplies = invoiceSupplies?.map((record: InvoiceSupplies) => {
        let { item: itemDTO } = record;
        return {
          itemDTO: {
            id: itemDTO?.id,
            code: itemDTO?.code,
            name: itemDTO?.name,
            label: itemDTO?.code + ' - ' + itemDTO?.name,
          },
          note: record?.note,
          price: record?.price,
          quantity: record?.quantity,
          totalItem: record?.total,
          vat: record?.total * record?.vat / 100,
          totalItemAfterVat: record.grandTotal,
          uomDTO: {
            label: '',
            value: itemDTO?.uom?.id,
          },
          vatDTO: {
            label: '',
            value: record?.vatId,
            percent: record?.vat,
          },
          feesBeforeImport: record?.['preImportFee'],
          importPercent: record?.['importTaxPercentage'],
          importTax: record?.['importTaxAmount'],
          envPercent: record?.['envFeePercentage'],
          envTax: record?.['envFeeAmount'],
          feesAfterImport: record?.['postImportFee'],
        } as ImportConvertedGoodsType
      });

      tmp.inventoryIds = []
      tmp.inventoryCodes = []
      inventories?.forEach((record) => {
        tmp.inventoryIds.push(record?.id);
        tmp.inventoryCodes.push(record?.code);
      });

      if (invoiceType === InvoiceType.IMPORT_INVOICE) {
        (tmp as any).relativedFees = relatedCosts.map((record) => {
          return {
            id: record?.id,
            invoiceId: record?.invoiceId,
            invoiceDate: record?.invoice?.invoiceDate,
            createdAt: record?.invoice?.createdAt,
            supplierId: record?.invoice?.supplierId,
            taxCode: (record?.invoice as any)?.suppliers?.taxCode,
            paymentMethodId: record?.invoice?.paymentMethod,
            paymentMethodCode: '',
            paymentMethodName: '',
            vatPercent: record?.invoice?.totalVat,
            cost: record?.invoice?.totalAmount,
            vat: record?.invoice?.totalAmountVat,
            totalAmountAfterVat: record?.invoice?.totalAmountAfterVat,
            grandTotal: record?.invoice?.grandTotal,
            debtDays: record?.invoice?.debtDays,
            note: record?.invoice?.content,
          }
        });
      }

      tmp.supplierContractId = data?.supplierContractId;
      tmp.supplierContractCode = data?.supplierContract?.contractCode;

      reset(tmp)
    }
  }, [detailQuery?.data]);

  return (
    <FormProvider {...methods}>
      <FormStrap>
        <CardV2
          header={
            <Flex justify="space-between" align="center">
              <Typography level={4}>
                {isEditMode
                  ? detailQuery?.data?.data?.invoiceNo
                  : isImportForm
                    ? 'Thêm hóa đơn nhập khẩu'
                    : 'Thêm hóa đơn'}
              </Typography>
              <Flex align="center" gap={10}>
                <ButtonBack />
                <AuthGuard permissionKey={isEditMode ? "INCOMING_INVOICE.EDIT" : 'INCOMING_INVOICE.CREATE'}>
                  <ButtonV2
                    variant="solid"
                    color="blue"
                    onClick={handleSubmit(onSubmit)}
                    isLoading={isEditMode ? updateMutation.isPending : createMutation.isPending}
                  >
                    Lưu
                  </ButtonV2>
                </AuthGuard>
              </Flex>
            </Flex>
          }
        >
          <Flex direction="column" rowGap={20}>
            <Summary />
            <Provider />
            <LinkContract />
            {isImportForm ? <ImportGoods /> : <Goods />}
            {isImportForm && <RelativedFees />}
            <Row>
              <Col md={isImportForm ? 4 : 6}>
                <Attachments />
              </Col>
              {isImportForm && (
                <Col md={8}>
                  <Detail />
                </Col>
              )}
            </Row>
          </Flex>
        </CardV2>
      </FormStrap>
      {/* <DevTool control={control} /> */}
    </FormProvider>
  );
}

export default Form
