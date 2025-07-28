import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import TableV2, { TableColumns } from 'app/components/table-v2/Table';
import WrapInputNumber from 'app/components/wrap-input-text/WrapInputNumber';
import Item from 'app/components/wrap-select/Item';
import Status from 'app/components/wrap-select/Status';
import Unit from 'app/components/wrap-select/Unit';
import WareHouse from 'app/components/wrap-select/Warehouse';
import { ICON_PATH } from 'app/constants/common';
import { IItem } from 'app/shared/model/item.model';
import { convertCurrency } from 'app/shared/util/format';
import { clone, sumBy } from 'lodash';
import { useFormContext } from 'react-hook-form';
import WrapDate from '../../../../components/wrap-date/WrapDate';
import WrapInputText from '../../../../components/wrap-input-text/WrapInputText';
import {
  IncludeAccessoriesSchemaType
} from '../../validations/include-accessories.validation';
import Flex from 'app/components/flex/flex';

type Props = {};

const IncludedAccessories = (props: Props) => {
  const { watch, setValue, reset, getValues } =
    useFormContext<IncludeAccessoriesSchemaType>();
  let includedAccessories = watch('includeAccessoriesSchema');

  const onAddRow = () => {
    includedAccessories = includedAccessories || [];
    includedAccessories.push({
      quantity: 1,
      price: 0,
      addedDate: new Date().toISOString(),
    });
    setValue('includeAccessoriesSchema', clone(includedAccessories));
  };

  const onDeleteRow = (index: number) => () => {
    includedAccessories = includedAccessories.filter((_, i) => i !== index);
    setValue('includeAccessoriesSchema', clone(includedAccessories));
  };

  const columns: TableColumns<IncludeAccessoriesSchemaType> = [
    {
      header: {
        th_style: {
          width: 200,
        },
        render: 'Mã đi kèm',
      },
      body: {
        render: ({ data, index }) => (
          <WrapInputText<IncludeAccessoriesSchemaType>
            name={`includeAccessoriesSchema.${index}.code`}
            disabled
          />
        ),
      },
    },
    {
      header: {
        th_style: {
          width: 250,
        },
        render: 'Tên phụ kiện',
      },
      body: {
        render: ({ data, index }) => (
          <Item<IncludeAccessoriesSchemaType>
            name={`includeAccessoriesSchema.${index}.name`}
            onSelectChange={(data: IItem) => {
              let { code, uomId } = data;
              let item = getValues(`includeAccessoriesSchema.${index}`);
              item.code = code;
              item.unitId = uomId;
              setValue(`includeAccessoriesSchema.${index}`, clone(item));
            }}
          />
        ),
      },
    },
    {
      header: {
        th_style: {
          width: 250,
        },
        render: 'Đơn vị tính',
      },
      body: {
        render: ({ data, index }) => (
          <Unit<IncludeAccessoriesSchemaType>
            name={`includeAccessoriesSchema.${index}.unitId`}
            isDisabled
          />
        ),
      },
    },
    {
      header: {
        th_style: {
          width: 200,
        },
        render: 'Số lượng',
      },
      body: {
        render: ({ data, index }) => (
          <WrapInputNumber<IncludeAccessoriesSchemaType>
            name={`includeAccessoriesSchema.${index}.quantity`}
          />
        ),
      },
    },
    {
      header: {
        th_style: {
          width: 200,
        },
        render: 'Giá trị',
      },
      body: {
        render: ({ data, index }) => (
          <WrapInputNumber<IncludeAccessoriesSchemaType>
            name={`includeAccessoriesSchema.${index}.price`}
          />
        ),
      },
    },
    {
      header: {
        th_style: {
          width: 250,
        },
        render: 'Trạng thái',
      },
      body: {
        render: ({ data, index }) => (
          <Status<IncludeAccessoriesSchemaType>
            name={`includeAccessoriesSchema.${index}.statusId`}
          />
        ),
      },
    },
    {
      header: {
        th_style: {
          width: 200,
        },
        render: 'Ngày hỏng',
      },
      body: {
        render: ({ data, index }) => (
          <WrapDate<IncludeAccessoriesSchemaType>
            name={`includeAccessoriesSchema.${index}.brokenDate`}
          />
        ),
      },
    },
    {
      header: {
        th_style: {
          width: 200,
        },
        render: 'Ngày thêm',
      },
      body: {
        render: ({ data, index }) => (
          <WrapDate<IncludeAccessoriesSchemaType>
            name={`includeAccessoriesSchema.${index}.addedDate`}
          />
        ),
      },
    },
    {
      header: {
        th_style: {
          width: 250,
        },
        render: 'Lưu kho',
      },
      body: {
        render: ({ data, index }) => (
          <WareHouse<IncludeAccessoriesSchemaType>
            name={`includeAccessoriesSchema.${index}.save`}
          />
        ),
      },
    },
    {
      header: {
        th_style: {
          width: 250,
        },
        render: 'Ghi chú',
      },
      body: {
        render: ({ data, index }) => (
          <WrapInputText<IncludeAccessoriesSchemaType>
            name={`includeAccessoriesSchema.${index}.note`}
          />
        ),
      },
    },
    {
      header: {
        th_style: {
          width: 100,
        },
        render: '',
      },
      body: {
        render: ({ data, index }) => (
          <ButtonDelete onClick={onDeleteRow(index)} />
        ),
      },
    },
  ];

  return (
    <TableV2<any>
      table_id="included-accessories"
      columns={columns}
      data={includedAccessories || []}
      tableFixedLayout={true}
      custom_body_row={() => {
        return (
          <tr>
            <td colSpan={3} />
            <td>Tổng</td>
            <td>{convertCurrency(sumBy(includedAccessories, (item) => item.price * item.quantity))}</td>
            <td colSpan={7}>
              <Flex justify='end'>
                <ButtonV2 
                  onClick={onAddRow}
                  variant='text'
                  left_section={(
                    <img src={ICON_PATH + 'asset_add.svg'} alt="add" />
                  )}
                />
              </Flex>
            </td>
          </tr>
        );
      }}
    />
  );
};

export default IncludedAccessories;
