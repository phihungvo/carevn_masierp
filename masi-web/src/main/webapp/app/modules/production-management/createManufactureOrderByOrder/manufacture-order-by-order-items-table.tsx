import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import Flex from 'app/components/flex/flex';
import TableV2, { TableColumns } from 'app/components/table-v2/Table';
import Tooltip from 'app/components/tooltip/tooltip';
import { convertCurrency } from 'app/shared/util/format';
import { ManufactureOrderByOrderCreateSchema } from 'app/validation/manufacture-order.validation';
import { useFormContext } from 'react-hook-form';

const ManufactureOrderByOrderItemsTable = () => {
  const methods = useFormContext<ManufactureOrderByOrderCreateSchema>();
  const { watch } = methods;
  const itemsWatch = watch('items');

  const columns: TableColumns<any> = [
    {
      header: { render: 'STT' },
      body: { render: ({ data, index }) => index + 1 },
    },
    {
      header: { render: 'Mặt hàng' },
      body: { render: ({ data }) => data?.itemName },
    },
    {
      header: { render: 'Số lượng (Kg)' },
      body: { render: ({ data }) => convertCurrency(data?.quantity, false) },
    },
    {
      header: { render: 'Thông số' },
      body: {
        render: ({ data }) => {
          return (
            <Tooltip label={data?.parameter} target={`parameter-${data?.id}`}>
              <EllipsisParagraph
                text={data?.parameter}
                width={450}
                id={`parameter-${data?.id}`}
              />
            </Tooltip>
          );
        },
      },
    },
  ];

  return (
    <Flex direction="column" gap={8}>
      <div className="invoice_table_header">
        <div className="invoice_table_header_left">
          <span>Mặt hàng</span>
        </div>
      </div>

      <TableV2<any>
        table_id="items_table"
        columns={columns}
        data={[...(itemsWatch ?? [])]}
        className={{ table: 'at__table' }}
      />
    </Flex>
  );
};

export default ManufactureOrderByOrderItemsTable;
