import { zodResolver } from '@hookform/resolvers/zod';
import { Card, Col, Divider, Flex, Row, Tabs, Typography } from 'antd';
import ButtonPrint from 'app/components/ButtonV2/ButtonPrint';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import WrapInputNumber from 'app/components/wrap-input-text/WrapInputNumber';
import GroupCode from 'app/components/wrap-select/GroupCode';
import SmallGroup from 'app/components/wrap-select/SmallGroup';
import WrapSelect from 'app/components/wrap-select/WrapSelect';
import { PATH } from 'app/constants/path';
import useAccountApp from 'app/hooks/use-account-app';
import useGoTo from 'app/hooks/use-go-to';
import dayjs from 'dayjs';
import { useEffect, useMemo } from 'react';
import { FormProvider, SubmitHandler, useForm } from 'react-hook-form';
import { useParams } from 'react-router';
import WrapInputText from '../../components/wrap-input-text/WrapInputText';
import { useAssetCreate, useAssetDetail, useAssetUpdate } from './apis/api.hook';
import Arise from './component/tabs/arise';
import { GeneralInformation } from './component/tabs/general-information';
import IncludedAccessories from './component/tabs/included-accessories';
import Move from './component/tabs/move-table';
import { OtherInformation } from './component/tabs/other-information';
import RelativedDocument from './component/tabs/related-documents';
import { convertDataToUpdate } from './utils/convert-data.utils';
import {
  AssetForm,
  assetSchema,
  AssetSchemaType,
} from './validations/index.validation';
import AuthGuard from 'app/components/guards/auth-guard';
import WorkSpace from 'app/components/wrap-select/Workspace';

function CreateAsset() {
  const { goTo } = useGoTo();
  const params = useParams();
  const id = params?.id;
  const isEditMode = !!id;

  const methods = useForm<AssetSchemaType>({
    resolver: zodResolver(assetSchema),
  });
  const {
    handleSubmit,
    getValues,
    formState: { errors },
    reset,
    watch
  } = methods;
  const propertyCode = watch('basisInformation.propertyCode')

  const { employeeId } = useAccountApp()
  const detail = useAssetDetail(id);
  const createMutation = useAssetCreate()
  const updateMutation = useAssetUpdate(id)

  const tabs = useMemo(() => {
    return [
      {
        label: 'Thông tin chung',
        key: 'GeneralInformation',
        children: <GeneralInformation />,
      },
      {
        label: 'Phụ kiện đi kèm',
        key: 'IncludedAccessories',
        children: <IncludedAccessories />,
      },
      {
        label: 'Chứng từ liên quan',
        key: 'RelatedDocuments',
        children: <RelativedDocument />,
      },
      {
        label: 'Phát sinh',
        key: 'Arise',
        children: <Arise />,
      },
      {
        label: 'Di chuyển',
        key: 'MoveTable',
        children: <Move />,
      },
      {
        label: 'Thông tin khác',
        key: 'OtherInformation',
        children: <OtherInformation />,
      },
    ];
  }, []);

  const onSubmit: SubmitHandler<any> = (values) => {
    let convertedValues = convertDataToUpdate(values);
    isEditMode ? updateMutation.mutate(convertedValues) : createMutation.mutate(convertedValues);
  };

  useEffect(() => {
    if (detail?.data) {
      let body = {
        basisInformation: detail?.data?.basisInformation,
        generalInformation: detail?.data?.generalInformation,
        includeAccessoriesSchema: detail?.data?.includeAccessoriesSchema,
        relativedDocument: detail?.data?.relativedDocument,
        move: detail?.data?.move,
        otherInformation: {
          ...detail?.data?.otherInformation,
          updatedBy: detail?.data?.otherInformation?.updatedBy,
        },
      } as AssetSchemaType;
      reset(body);
    }
  }, [detail?.data]);

  return (
    <div className="page_container">
      <FormProvider {...methods}>
        <Flex justify="space-between" align="center">
          <Typography.Text>{propertyCode}</Typography.Text>
          <Flex gap={10}>
            <ButtonV2 onClick={goTo(PATH.ASSET)}>Đóng</ButtonV2>
            <AuthGuard permissionKey={isEditMode ? 'ASSET.EDIT' : 'ASSET.CREATE'}>
              <ButtonV2 onClick={handleSubmit(onSubmit)}>Lưu</ButtonV2>
            </AuthGuard>
            <AuthGuard permissionKey="ASSET.EXPORT">
              <ButtonPrint />
            </AuthGuard>
          </Flex>
        </Flex>
        <Divider />
        <Flex vertical gap={24}>
          <Row gutter={[16, 16]}>
            <Col span={24}>
              <Typography.Text>Thông tin căn bản</Typography.Text>
            </Col>
            <Col span={24}>
              <Row gutter={[20, 12]}>
                <Col span={6}>
                  <WrapInputText<AssetForm>
                    name="basisInformation.propertyCode"
                    label="Mã tài sản"
                    disabled
                  />
                </Col>
                <Col span={6}>
                  <WrapInputText<AssetForm>
                    name="basisInformation.codeFormWarehouse"
                    label="Mã từ kho"
                    disabled
                  />
                </Col>
                <Col span={12}>
                  <WrapInputText<AssetForm>
                    name="basisInformation.note"
                    label="Diễn giải"
                  />
                </Col>
                <Col span={6}>
                  <GroupCode<AssetForm>
                    label="Mã nhóm"
                    name="basisInformation.groupCode"
                  />
                </Col>
                <Col span={6}>
                  <SmallGroup<AssetForm>
                    label="Tiểu nhóm"
                    name="basisInformation.smallGroup"
                  />
                </Col>
                <Col span={12}>
                  <WrapInputText<AssetForm>
                    name="basisInformation.reason"
                    label="Lý do nhập"
                  />
                </Col>
                <Col span={6}>
                  <WrapInputNumber<AssetForm>
                    label="Nguyên giá"
                    name="basisInformation.originalPrice"
                    disabled={
                      getValues('generalInformation.usingDate') &&
                      !dayjs(getValues('generalInformation.usingDate')).isAfter(
                        dayjs(),
                      )
                    }
                  />
                </Col>
                <Col span={6}>
                  <WrapInputNumber<AssetForm>
                    label="Giá trị còn lại"
                    name="basisInformation.remainingValue"
                    disabled
                  />
                </Col>
                <Col span={6}>
                  <WrapInputNumber<AssetForm>
                    label="Số lượng còn lại"
                    name="basisInformation.remainingQuantity"
                    disabled
                  />
                </Col>
                <Col span={6}>
                  <WorkSpace<AssetForm>
                    label="Đơn vị quản lý"
                    name="basisInformation.managementUnit"
                  />
                </Col>
              </Row>
            </Col>
          </Row>
        </Flex>
        <Divider />
        <Card>
          <Tabs defaultActiveKey="GeneralInformation" items={tabs} />
        </Card>
      </FormProvider>
    </div>
  );
}

export default CreateAsset;
