import { Button, Col, Flex, Row, Typography } from 'antd';
import { TableColumns } from 'app/components/table-v2/Table';
import TablePagination from 'app/components/table-v2/TablePagination';
import WrapInputNumber from 'app/components/wrap-input-text/WrapInputNumber';
import WrapInputText from 'app/components/wrap-input-text/WrapInputText';
import WrapSelect from 'app/components/wrap-select/WrapSelect';
import { useDepriciationItemList } from 'app/modules/depreciation/apis/hook';
import { DepreciationItemSchema } from 'app/modules/depreciation/validations/depriciation-table.validation';
import { convertCurrency } from 'app/shared/util/format';
import { cloneDeep, mergeWith, reduce } from 'lodash';
import { useEffect, useState } from 'react';
import { useFormContext } from 'react-hook-form';
import { useParams } from 'react-router';
import { AllociationFormSchemaType } from '../validations/allociation-form.validation';
import useWorkspace from 'app/hooks/use-workspace';

const {
  useGetWorkspacesQuery
} = useWorkspace

type Props = {};

const DepreciationTable = (props: Props) => {
  const params = useParams()
  const isEditMode = !!params?.id

  const [hasId, setHasId] = useState<string>('')
  const [isAutoCalculated, setIsAutoCalculated] = useState(false)
  const { watch, setValue, reset, getValues, formState: { errors } } = useFormContext<AllociationFormSchemaType>();
  let assetObj = watch('itemAssetDepreciationDetails')

  const {
    depriciationItemApi,
    query,
    handleQuery,
    handleSearch,
    setQuery
  } = useDepriciationItemList(hasId)

  const workSpaceQuery = useGetWorkspacesQuery({}, (res) => reduce(res?.data?.data, (acc, item) => {
      acc[item?.id] = item?.name
      return acc
    }, {}))

  const columns: TableColumns<DepreciationItemSchema> = [
    {
      header: {
        render: 'Mã TS',
      },
      body: {
        render: ({ data, index }) => data?.code,
      },
    },
    {
      header: {
        render: 'Tên TS',
      },
      body: {
        render: ({ data, index }) => isEditMode ? data?.['inventoriesStorage']?.['item']?.['name'] : data?.['item']?.['name']
      },
    },
    {
      header: {
        render: 'TTCP',
      },
      body: {
        render: ({ data, index }) => workSpaceQuery?.data?.[data?.['department']]
      },
    },
    {
      header: {
        render: 'Phân bổ',
      },
      body: {
        render: ({ data, index }) => {
          let isCustomRecipe = !!assetObj?.[data?.id]?.isCustomRecipe
          return (
            <WrapInputText<any>
              key={`itemAssetDepreciationDetails.${data?.id}.amortizedCostInformation`}
              name={`itemAssetDepreciationDetails.${data?.id}.amortizedCostInformation`}
              disabled={!isCustomRecipe}
            />
          )
        },
      },
    },
    {
      header: {
        render: 'Khấu hao',
      },
      body: {
        render: ({ data, index }) => {
          let isCustomRecipe = !!assetObj?.[data?.id]?.isCustomRecipe
          return (
            <WrapInputNumber<any>
              key={`itemAssetDepreciationDetails.${data?.id}.amortizationAmount.${assetObj?.[data?.id]?.amortizationAmount}`}
              name={`itemAssetDepreciationDetails.${data?.id}.amortizationAmount`}
              disabled={!isCustomRecipe}
              max={data?.originalCost - data?.accumulatedAmortizationAmount}
              onCompletedChange={() => {
                if (assetObj?.[data?.id]?.isCalculated) {
                  setValue(`itemAssetDepreciationDetails.${data?.id}.isSubtract`, true)
                }
                setValue(`itemAssetDepreciationDetails.${data?.id}.isCalculated`, false)
                setValue(`itemAssetDepreciationDetails.${data?.id}.isResetCalculated`, false)
              }}
            />
          )
        },
      },
    },
    {
      header: {
        render: 'Tỉ lệ phân bổ',
      },
      body: {
        render: ({ data, index }) => {
          let isCustomRecipe = !!assetObj?.[data?.id]?.isCustomRecipe
          return (
            <WrapInputNumber<any>
              key={`itemAssetDepreciationDetails.${data?.id}.amortizationRate`}
              name={`itemAssetDepreciationDetails.${data?.id}.amortizationRate`}
              disabled={true}
            />
          )
        },
      },
    },
    {
      header: {
        render: 'Nguyên giá gốc',
      },
      body: {
        render: ({ data, index }) => convertCurrency(data?.originalCost),
      },
    },
    {
      header: {
        render: 'LK khấu hao',
      },
      body: {
        render: ({ data, index }) => {
          let isCustomRecipe = !!assetObj?.[data?.id]?.isCustomRecipe
          return (
            <WrapInputNumber<any>
              key={`itemAssetDepreciationDetails.${data?.id}.accumulatedAmortizationAmount`}
              name={`itemAssetDepreciationDetails.${data?.id}.accumulatedAmortizationAmount`}
              disabled={true}
            />
          )
        },
      },
    },
    {
      header: {
        render: 'Cách tính',
      },
      body: {
        render: ({ data, index }) => (
          <WrapSelect
            key={`itemAssetDepreciationDetailsTmp.${index}.recipe`}
            name={`itemAssetDepreciationDetailsTmp.${index}.recipe`}
            placeholder='Mặc định'
            data={{
              arr: [
                { label: 'Mặc định', value: 'default' },
                { label: 'Điều chỉnh', value: 'custom' },
              ],
              obj: {
                default: {
                  label: 'Mặc định',
                  value: 'default',
                },
                custom: {
                  label: 'Điều chỉnh',
                  value: 'custom',
                },
              },
            }}
            onSelectChange={value => {
              let item = assetObj?.[data?.id]
              if (!value || value?.value === 'default') {
                delete item.isCustomRecipe
                delete item.isCalculated
                let percent = (+item?.amortizationAmount * 100) / +item?.originalCost
                item.amortizationRate -= percent
                item.accumulatedAmortizationAmount -= item?.amortizationAmount
              } else {
                item.isCustomRecipe = true
              }
              setValue('itemAssetDepreciationDetails', assetObj)
            }}
          />
        ),
      },
    },
  ];

  const onCalculateDepreciation = () => {
    setIsAutoCalculated(true)
    for (let key in assetObj) {
      let item = assetObj?.[key]
      if (!item || !item?.isCustomRecipe || item?.isCalculated) continue;
      console.log('item', item);
      if (item?.isSubtract) {
        console.log('before', item?.['old']);
        let percent = (+item?.['old']?.amortizationAmount * 100) / +item?.originalCost
        item.amortizationRate -= percent
        item.accumulatedAmortizationAmount -= item?.['old']?.amortizationAmount;
        (item as any).old.isSubtract = true
        console.log('after', {
          amortizationRate: item.amortizationRate,
          accumulatedAmortizationAmount: item.accumulatedAmortizationAmount,
        });
      }
      let percent = (+item?.amortizationAmount * 100) / +item?.originalCost
      // backup old data
      item['old'] = {
        amortizationRate: percent,
        accumulatedAmortizationAmount: item.accumulatedAmortizationAmount + item.amortizationAmount,
        amortizationAmount: item.amortizationAmount,
      }
      item.amortizationRate += percent
      item.accumulatedAmortizationAmount += item?.amortizationAmount
      item.isCalculated = true
      item.isSubtract = true
      delete item.isResetCalculated
    }
    setValue('itemAssetDepreciationDetails', cloneDeep(assetObj))
  }

  const onResetDepreciation = () => {
    setIsAutoCalculated(false)
    setValue('isDeleteAll', true)
    setQuery(pre => ({ ...pre, checkDepreciation: true }))
    setHasId('')
    for (let key in assetObj) {
      let item = assetObj?.[key]
      if (!item) return;
      if (!item?.isResetCalculated) {
        item.amortizationRate = item?.['resourceData']?.['amortizationRate']
        item.accumulatedAmortizationAmount = item?.['resourceData']?.['accumulatedAmortizationAmount']
        item.amortizationAmount = item?.['resourceData']?.['amortizationAmount']
        item.isResetCalculated = true
        item.isCustomRecipe = false
        item.recipe = 'default'
        item.isSubtract = false
        delete item.isCalculated
      }
    }
    setValue('itemAssetDepreciationDetails', assetObj)
  }

  useEffect(() => {
    if (depriciationItemApi?.data?.obj) {
      setValue('itemAssetDepreciationDetails', mergeWith(assetObj, depriciationItemApi?.data?.obj, (objValue, srcValue) => {
        if (!objValue?.id) return srcValue
        return objValue
      }))
    }
  }, [depriciationItemApi?.data?.obj])

  useEffect(() => {
    if (params?.id) {
      setHasId(params?.id)
    }
  }, [])

  console.log('assetObj', assetObj);

  return (
    <Row gutter={[16, 12]}>
      <Col span={24}>
        <Flex gap={24}>
          <Typography.Text>Phân bổ công cụ dụng cụ</Typography.Text>
          <Flex gap={16}>
            <Button
              loading={false}
              ghost
              style={{
                color: '#365FBF',
                backgroundColor: '#F1F6FD',
              }}
              onClick={onCalculateDepreciation}
            >
              Tính khấu hao
            </Button>
            <Button
              loading={false}
              ghost
              style={{
                color: '#365FBF',
                backgroundColor: '#F1F6FD',
              }}
              onClick={onResetDepreciation}
            >
              Xóa khấu hao
            </Button>
          </Flex>
        </Flex>
      </Col>
      <Col span={24}>
        <TablePagination<any>
          table_id="depreciation"
          columns={columns}
          data={depriciationItemApi?.data?.data}
          total_pages={depriciationItemApi?.data?.totalRecord}
          itemsPerPage={query?.size}
          isLoading={depriciationItemApi?.isLoading}
          handlePageClick={handleQuery('page')}
          handlePageSizeChange={handleQuery('size')}
        />
      </Col>
    </Row>
  );
};

export default DepreciationTable;
