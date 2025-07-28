import { zodResolver } from '@hookform/resolvers/zod';
import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import Button from 'app/components/button/button';
import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import FormDatePicker from 'app/components/form/form-date-picker';
import FormInput from 'app/components/form/form-input';
import InputFile from 'app/components/input/input-file';
import useEmployee from 'app/hooks/use-employee';
import { CONTRACT_STATUS, CONTRACT_TYPE } from 'app/shared/model/enumerations/contract.model';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { ContractFormSchema, contractSchema } from 'app/validation/contract.validation';
import React, { useEffect, useRef, useState } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import contractsMapping from '../contracts-mapping';
import useContracts from 'app/hooks/use-contracts';
import { useAppSelector } from 'app/config/store';
import { handleValidatePaste, handleValidDecimal } from 'app/shared/util/handle-valid-decimal';
import { DEFAULT_DECIMAL_REGEX, DEFAULT_PAGE_SIZE_NAX, FILE_UTIL } from 'app/constants/common';
import { DateObject } from 'react-multi-date-picker';
import FormSelect from 'app/components/form/form-select';
import useCustomers from 'app/hooks/use-customers';
import useFile from 'app/hooks/use-file';
import { IFIle } from 'app/shared/model/file.model';
import AdditivesForm from './additives-form';
import { v4 } from 'uuid';
import { UNIT } from 'app/shared/model/enumerations/unit.model';
import { useCalculateContractTotal } from 'app/hooks/use-calculate-contract-total';
import { IContract } from 'app/shared/model/contract.model';
import FormNumberic from 'app/components/form/form-numberic';
import useQuotations from 'app/hooks/use-quotations';
import Card from 'app/components/card/card';

const { contractTypeTextMapping, contractStatusTextMapping } = contractsMapping;
const { useGetEmployeesQuery } = useEmployee;
const { useGetEnabledCustomers } = useCustomers;
const { usePostContract, useGetContractById, usePatchContract, useGetContractMaterials } = useContracts;
const { usePostFiles } = useFile;
const { useGetQuotations } = useQuotations;

interface IContractsFormProps {
  type: 'create' | 'update';
  toggle?: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string;
  setSelectedRecord?: (id: string) => void;
  contractStatus?: string;
}

const ContractsForm = (props: IContractsFormProps) => {
  const { type, toggle, toggleSuccess, selectedRecord, setSelectedRecord, contractStatus } = props;
  const account = useAppSelector(state => state.authentication.account);

  const fileInputRef = useRef<HTMLInputElement>(null);

  const [fileList, setFileList] = useState<IFIle[] | null>([]);

  const { control, setValue, handleSubmit, watch, formState } = useForm<ContractFormSchema>({
    resolver: zodResolver(contractSchema),
    defaultValues: {
      // contractOwner: account?.id,
      status: CONTRACT_STATUS.DRAFT,
      monetaryUnit: UNIT.VND,
      exchangeRate: '1',
      additives: [
        {
          idMaterial: '',
          price: '',
          quantity: '',
          proteinParameters: '',
        },
      ],
    },
  });

  const watchExchangeRate = watch('exchangeRate');
  const additives = watch('additives');
  const quotationId = watch('quotationId');

  useCalculateContractTotal<ContractFormSchema>(watchExchangeRate, additives, 'contractTotal', setValue);

  const { data, isLoading } = useGetEmployeesQuery();
  const { data: customers, isLoading: cusLoading } = useGetEnabledCustomers({ size: DEFAULT_PAGE_SIZE_NAX });
  const { data: contract } = useGetContractById(selectedRecord);
  // const { data: contractProducts, isLoading: loadingProducts } = useGetContractProducts();
  const { data: materialContracts, isLoading: loadingMaterial } = useGetContractMaterials();
  const { data: quotations, isLoading: loadingQuotation } = useGetQuotations();
  const { mutate: uploadFile, isPending: loadingUpload } = usePostFiles(setFileList);
  const { mutate: create } = usePostContract(toggle, toggleSuccess);
  const { mutate: update } = usePatchContract(selectedRecord, toggle, toggleSuccess);

  const onSubmit: SubmitHandler<ContractFormSchema> = async values => {
    const submitValues: IContract = {
      contractName: values?.contractName,
      contractType: values?.contractType,
      contractValidFrom: values?.contractValidFrom?.toDate()?.toISOString(),
      contractValidTo: values?.contractValidTo?.toDate()?.toISOString(),
      contractTotal: Number(values?.contractTotal),
      contractOwner: values?.contractOwner,
      // proteinPercent: values?.proteinPercent,
      status: values?.status,
      contractFile: {
        contractFileNew: fileList?.map(file => file?.id),
      },

      customerId: values?.customerId,
      // deliveryTermFrom: values?.deliveryTerm?.[0]?.toDate()?.toISOString(),
      // deliveryTermTo: values?.deliveryTerm?.[1]
      //   ? values?.deliveryTerm?.[1]?.toDate()?.toISOString()
      //   : values?.deliveryTerm?.[0]?.toDate()?.toISOString(),
      // payTerm: values?.payTerm,
      // payCondition: values?.payCondition,
      // deliveryLocation: values?.deliveryLocation,
      contractMaterialDTOS: values?.additives?.map(additive => ({
        idMaterial: materialContracts?.data?.find(item => item?.id === additive?.idMaterial) ? additive?.idMaterial : v4(),
        price: Number(additive?.price),
        quantity: Number(additive?.quantity),
        proteinParameters: additive?.proteinParameters,
        nameMaterialNew: materialContracts?.data?.find(item => item?.id === additive?.idMaterial) ? '' : additive?.idMaterial,
        unit: additive?.unit,
      })),
      // contractProductDTOS: values?.products?.map((product: BaseOption) => ({
      //   idProduct: product?.value?.toString(),
      //   productName: product?.label,
      // })),
      monetaryUnit: values?.monetaryUnit,
      exchangeRate: Number(values?.exchangeRate),
      quotationId: values?.quotationId
    };

    if (type === 'update') {
      update(submitValues);
      setFileList([]);
      setSelectedRecord(null);
      return;
    }

    create(submitValues);
    setFileList([]);
  };

  useEffect(() => {
    setFileList([]);
    if (contract) {
      setValue('contractName', contract?.contractName);
      setValue('contractType', contract?.contractType);
      setValue('contractTotal', contract?.contractTotal?.toString());
      setValue('contractOwner', contract?.contractOwner);
      setValue('customerId', contract?.customer?.id || '');
      // setValue('proteinPercent', contract?.proteinPercent);
      setValue('status', contract?.status);
      // setValue('deliveryTerm', [
      //   new DateObject(contract?.deliveryTermFrom).add(7, 'hours'),
      //   new DateObject(contract?.deliveryTermTo).add(7, 'hours'),
      // ]);
      // setValue('payTerm', contract?.payTerm || '');
      // setValue('payCondition', contract?.payCondition || '');
      // setValue('deliveryLocation', contract?.deliveryLocation || '');
      if (contract?.fileAttachments?.length) {
        setFileList([...contract?.fileAttachments]);
      }
      setValue(
        'additives',
        contract?.contractMaterialDTOS?.map(additive => ({
          idMaterial: additive?.idMaterial,
          price: additive?.price?.toString(),
          quantity: additive?.quantity?.toString(),
          proteinParameters: additive?.proteinParameters,
          unit: additive?.unit,
        })),
      );
      // setValue('products', contract?.contractProductDTOS?.map(product => ({ label: product?.productName, value: product?.idProduct })));
      setValue('monetaryUnit', contract?.monetaryUnit);
      setValue('exchangeRate', contract?.exchangeRate?.toString());
      setValue('contractValidFrom', new DateObject(contract?.contractValidFrom).add(7, 'hours'));
      setValue('contractValidTo', new DateObject(contract?.contractValidTo).add(7, 'hours'));
      setValue('quotationId', contract?.quotationId)
    }
  }, [contract]);

  useEffect(() => {
    if (quotationId) {
      const quotation = quotations?.data?.find(q => q?.id === quotationId);
      if (quotation) {
        setValue(
          'additives',
          quotation?.quotationDetails?.map(detail => ({
            idMaterial: detail?.materialId,
            price: detail?.price,
            quantity: detail?.weight,
            proteinParameters: detail?.note,
            unit: 'Kg',
          })),
        );
      }
    }
  }, [quotationId]);

  useEffect(() => {
    if (account?.id) setValue('contractOwner', account?.id)
  }, [account?.id])

  return (
    <Form id={FORM.CUSTOMER} onSubmit={handleSubmit(onSubmit)}>
      <Card header='Thông tin chung' className='card-body-padding' classNameHeader='card-header-bold'>
        <Row>
          <Col md={6}>
            <FormInput control={control} label="Số HĐ" name="contractName" disabled={contractStatus === CONTRACT_STATUS?.APPROVED} />
          </Col>

          <Col md={6}>
            <FormSelect
              control={control}
              id="customerId"
              name="customerId"
              placeholder="Chọn công ty"
              label="Công ty"
              options={customers?.data?.map(c => ({
                label: c?.companyName,
                value: c?.id,
              }))}
              isLoading={cusLoading}
              disabled={contractStatus === CONTRACT_STATUS?.APPROVED}
            />
          </Col>
        </Row>
      </Card>

      <div className="divider" />

      <Card header='Thông tin chi tiết' className='card-body-padding' classNameHeader='card-header-bold'>
        <Row>
          <Col md={6}>
            <FormInput control={control} label="Loại hợp đồng" name="contractType" type="select">
              <option selected disabled>
                Chọn loại hợp đồng
              </option>
              <option value={CONTRACT_TYPE.NEW}>{contractTypeTextMapping(CONTRACT_TYPE.NEW)}</option>
              <option value={CONTRACT_TYPE.PRICE_UP}>{contractTypeTextMapping(CONTRACT_TYPE.PRICE_UP)}</option>
              <option value={CONTRACT_TYPE.PRICE_DOWN}>{contractTypeTextMapping(CONTRACT_TYPE.PRICE_DOWN)}</option>
              <option value={CONTRACT_TYPE.EXTEND}>{contractTypeTextMapping(CONTRACT_TYPE.EXTEND)}</option>
              <option value={CONTRACT_TYPE.ADDITIONAL_CONTRACT_INDEX}>
                {contractTypeTextMapping(CONTRACT_TYPE.ADDITIONAL_CONTRACT_INDEX)}
              </option>
            </FormInput>
          </Col>

          <Col md={6}>
            <FormSelect
              control={control}
              id="contractOwner"
              name="contractOwner"
              placeholder="Chọn người phụ trách"
              label="Người phụ trách"
              options={data?.data?.map(e => ({
                label: `${(e?.lastName || '') + ' ' + (e?.firstName || '')}`,
                value: e?.id,
              }))}
              isLoading={isLoading}
              disabled={contractStatus === CONTRACT_STATUS?.APPROVED}
            />
          </Col>

          <Col md={6}>
            <FormDatePicker setValue={setValue} control={control} label="Thời hạn HĐ từ" name="contractValidFrom" formState={formState} disabled={contractStatus === CONTRACT_STATUS?.APPROVED} />
          </Col>
          <Col md={6}>
            <FormDatePicker setValue={setValue} control={control} label="Thời hạn HĐ đến" name="contractValidTo" formState={formState} disabled={contractStatus === CONTRACT_STATUS?.APPROVED} />
          </Col>
          <Col md={6}>
            <FormInput control={control} label="Tình trạng hợp đồng" name="status" type="select" disabled={contractStatus === CONTRACT_STATUS?.APPROVED}>
              <option selected disabled>
                Chọn tình trạng hợp đồng
              </option>
              <option value={CONTRACT_STATUS.DRAFT}>{contractStatusTextMapping(CONTRACT_STATUS.DRAFT)}</option>
              <option value={CONTRACT_STATUS.WAITING_APPROVAL}>{contractStatusTextMapping(CONTRACT_STATUS.WAITING_APPROVAL)}</option>
              <option value={CONTRACT_STATUS.APPROVED}>{contractStatusTextMapping(CONTRACT_STATUS.APPROVED)}</option>
              <option value={CONTRACT_STATUS.FINISHED}>{contractStatusTextMapping(CONTRACT_STATUS.FINISHED)}</option>
              <option value={CONTRACT_STATUS.WAITING_LIQUIDATION}>{contractStatusTextMapping(CONTRACT_STATUS.WAITING_LIQUIDATION)}</option>
              <option value={CONTRACT_STATUS.LIQUIDATED}>{contractStatusTextMapping(CONTRACT_STATUS.LIQUIDATED)}</option>
              {/* {type === 'update' && <option value={CONTRACT_STATUS.DELETED}>{contractStatusTextMapping(CONTRACT_STATUS.DELETED)}</option>} */}
              {/* <option value={CONTRACT_STATUS.CANCELLED}>{contractStatusTextMapping(CONTRACT_STATUS.CANCELLED)}</option> */}
            </FormInput>
          </Col>

          <Col md={6}>
            <FormSelect
              control={control}
              id="quotationId"
              name="quotationId"
              placeholder="Chọn bảng báo giá"
              label="Bảng báo giá"
              options={quotations?.data?.map(q => ({
                label: q?.name,
                value: q?.id,
              }))}
              isLoading={loadingQuotation}
            />
          </Col>

          <Col md={6}>
            <FormNumberic control={control} name="contractTotal" id="contractTotal" label="Giá trị hợp đồng" disabled />
          </Col>
          {/* <Col md={6}> */}
          {/*   <FormInput control={control} label="Thông số đạm" name="proteinPercent" /> */}
          {/* </Col> */}
          {/* <Col md={6}>
          <FormDatePicker range setValue={setValue} control={control} formState={formState} label="Thời hạn giao hàng" name="deliveryTerm"/>
        </Col>
        <Col md={6}>
          <FormInput control={control} label="Thời hạn thanh toán" name="payTerm"/>
        </Col>
        <Col md={6}>
          <FormInput control={control} label="Điều kiện thanh toán" name="payCondition" type="select">
            <option selected disabled>
              Chọn điều kiện thanh toán
            </option>
            {
              Object.values(EPayCondition).map(condition => {
                return (
                  <option key={condition} value={condition}>{condition}</option>
                )
              })
            }
          </FormInput>
        </Col>
        <Col md={6}>
          <FormInput control={control} label="Địa điểm nhận hàng" name="deliveryLocation"/>
        </Col> */}

          <Col md={6}>
            <FormInput
              control={control}
              label="Đơn vị tiền tệ"
              id="unit"
              name="monetaryUnit"
              type="select"
              onChange={e => e.target.value === UNIT.VND && setValue('exchangeRate', '1')}
              disabled={contractStatus === CONTRACT_STATUS?.APPROVED}
            >
              <option selected disabled>
                Chọn đơn vị tiền tệ
              </option>
              <option value={UNIT.VND}>VND</option>
              <option value={UNIT.USD}>USD</option>
              <option value={UNIT.OTHER}>Khác</option>
            </FormInput>
          </Col>

          <Col md={6}>
            {watch('monetaryUnit') !== UNIT.VND && (
              <FormInput
                control={control}
                label="Tỉ giá"
                name="exchangeRate"
                onChange={e => handleValidDecimal<ContractFormSchema>(e.target.value, 'exchangeRate', DEFAULT_DECIMAL_REGEX, setValue)}
                onPaste={e => handleValidatePaste(e, DEFAULT_DECIMAL_REGEX)}
                disabled={contractStatus === CONTRACT_STATUS?.APPROVED}
              />
            )}
          </Col>

          {/* <Col md={6}>
          <FormSelectMulti
            control={control}
            id="products"
            name="products"
            placeholder="Chọn sản phẩm"
            label="Sản phẩm"
            options={contractProducts?.data?.map(c => ({
              label: c?.productName,
              value: c?.id,
            }))}
            isLoading={loadingProducts}
          />
        </Col> */}
        </Row>
      </Card>

      <div className="divider" />

      <Card header='Danh sách nguyên liệu' className='card-body-padding ' classNameHeader='card-header-bold'>
        <AdditivesForm control={control} setValue={setValue} data={materialContracts?.data} isLoading={loadingMaterial} watch={watch} disabled={contractStatus === CONTRACT_STATUS?.APPROVED} />
      </Card>

      <div className="divider" />

      <Card header='Tải tệp đính kèm' className='card-body-padding' classNameHeader='card-header-bold'>
        <Button
          key={new Date().getMilliseconds()}
          type="button"
          color="primary"
          onClick={() => fileInputRef.current?.click()}
          className="btn-upload"
          loading={loadingUpload}
        >
          <Flex align="center" gap={8}>
            <img src="content/images/vuesax/linear/paperclip.svg" alt="attach" />
            Đính kèm
            <InputFile onFileChange={file => uploadFile(file)} name="fileAttachment" hidden ref={fileInputRef} />
          </Flex>
        </Button>
        {!!fileList?.length && (
          <>
            <div className="divider" />
            <Flex gap={8} flexWrap="wrap">
              {fileList?.map((file, index) => (
                <React.Fragment key={file?.id}>
                  <AttachmentPreview
                    name={file?.name}
                    onClose={() => setFileList(prev => prev.filter(curFile => curFile?.id !== file?.id))}
                    fileUrl={`${FILE_UTIL}/${file?.id}`}
                  />
                </React.Fragment>
              ))}
            </Flex>
          </>
        )}
      </Card>
    </Form>
  );
};

export default ContractsForm;
