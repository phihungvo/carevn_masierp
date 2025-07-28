import { Col, Flex, Row, Typography } from 'antd';
import ButtonAdd from 'app/components/ButtonV2/ButtonAdd';
import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import TableV2, { TableColumns } from 'app/components/table-v2/Table';
import WrapInputNumber from 'app/components/wrap-input-text/WrapInputNumber';
import WrapInputText from 'app/components/wrap-input-text/WrapInputText';
import Properties from 'app/components/wrap-select/Properties';
import Provider from 'app/components/wrap-select/provider';
import { DepriciationItem } from 'app/modules/depreciation/types/depriciation-item';
import { cloneDeep } from 'lodash';
import { useFormContext } from 'react-hook-form';
import { useParams } from 'react-router';
import { PropertyListSchemaType } from '../validations/property-list.validation';

type Props = {};

const filter = {
  'status.doesNotContain': 'LIQUIDATION',
  page: 0,
  size: 2000000,
  checkDepreciation: true
}

const PropertyListTable = (props: Props) => {
  const params = useParams();
  const isEditMode = !!params?.id;
  
  const { watch, setValue, getValues } = useFormContext<PropertyListSchemaType>();
  let propertyList = watch('propertyList');

  const onAdd = () => {
    propertyList = propertyList || [];
    propertyList.push({});
    setValue('propertyList', cloneDeep(propertyList));
  };

  const onRemove = (index: number) => () => {
    let filtered = propertyList.filter((_: any, i: number) => i !== index);
    setValue('propertyList', cloneDeep(filtered));
  };

  const columns: TableColumns<any> = [
    {
      header: {
        render: 'TS/CCDC',
      },
      body: {
        render: ({ data, index }) => {
          return (
            <Properties<PropertyListSchemaType> 
            name={`propertyList.${index}.id`}
            keyValidate={propertyList?.[index]?.id ? 'properties-detail-list' : 'property-list'}
            filter={propertyList?.[index]?.id ? { size: 2000000, page: 0 } : filter}
            onSelectChange={(value: DepriciationItem) => {
              let tmp = getValues(`propertyList.${index}`);
              tmp = {
                ...tmp,
                ...value,
                content: value?.notes,
                originalPrice: value?.price,
                depreciation: value?.depreciationValue,
                remainingValue: value?.remainingPrice,
                providerId: value?.item?.supplierId,
                codeVtcc: value?.item?.code,
                inventoriesStorageId: value?.itemInfo?.inventoryStorageId,
              }
              setValue(`propertyList.${index}`, cloneDeep(tmp));
            }}
          />
          )
        }
      },
    },
    {
      header: {
        render: 'Diễn giải',
      },
      body: {
        render: ({ data, index }) => (
          <WrapInputText<PropertyListSchemaType>
            name={`propertyList.${index}.content`}
            disabled
          />
        ),
      },
    },
    {
      header: {
        render: 'TTCP',
      },
      body: {
        render: ({ data, index }) => (
          <WrapInputText<PropertyListSchemaType>
            name={`propertyList.${index}.ttcp`}
          />
        ),
      },
    },
    {
      header: {
        render: 'Mã VTCC',
      },
      body: {
        render: ({ data, index }) => (
          <WrapInputText<PropertyListSchemaType>
            name={`propertyList.${index}.codeVtcc`}
            disabled
          />
        ),
      },
    },
    {
      header: {
        render: 'SL',
      },
      body: {
        render: ({ data, index }) => (
          <WrapInputNumber<PropertyListSchemaType>
            name={`propertyList.${index}.quantity`}
            disabled
          />
        ),
      },
    },
    {
      header: {
        render: 'Nguyên giá',
      },
      body: {
        render: ({ data, index }) => (
          <WrapInputNumber<PropertyListSchemaType>
            name={`propertyList.${index}.originalPrice`}
            disabled
          />
        ),
      },
    },
    {
      header: {
        render: 'Khấu hao',
      },
      body: {
        render: ({ data, index }) => (
          <WrapInputNumber<PropertyListSchemaType>
            name={`propertyList.${index}.depreciation`}
            disabled
          />
        ),
      },
    },
    {
      header: {
        render: 'Giá trị còn lại',
      },
      body: {
        render: ({ data, index }) => (
          <WrapInputNumber<PropertyListSchemaType>
            name={`propertyList.${index}.remainingValue`}
            disabled
          />
        ),
      },
    },
    {
      header: {
        render: 'TK thanh lý/giảm',
      },
      body: {
        render: ({ data, index }) => (
          <WrapInputText<PropertyListSchemaType>
            name={`propertyList.${index}.tkThanhLyGiam`}
          />
        ),
      },
    },
    {
      header: {
        render: 'NCC',
      },
      body: {
        render: ({ data, index }) => (
          <Provider<PropertyListSchemaType>
            name={`propertyList.${index}.providerId`}
            isDisabled
          />
        ),
      },
    },
    {
      header: {
        render: 'Ghi chú',
      },
      body: {
        render: ({ data, index }) => (
          <WrapInputText<PropertyListSchemaType>
            name={`propertyList.${index}.note`}
          />
        ),
      },
    },
    {
      header: {
        render: '',
      },
      body: {
        render: ({ data, index }) => <ButtonDelete onClick={onRemove(index)} />,
      },
    },
  ];

  return (
    <Row gutter={[16, 12]}>
      <Col span={24}>
        <Flex gap={24}>
          <Typography.Text>Danh sách tài sản</Typography.Text>
          <ButtonAdd text="Thêm" onClick={onAdd} />
        </Flex>
      </Col>
      <Col span={24}>
        <TableV2<any>
          table_id="human-resource-table"
          columns={columns}
          data={propertyList}
        />
      </Col>
    </Row>
  );
};

export default PropertyListTable;
